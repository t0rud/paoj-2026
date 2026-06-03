package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.config.DatabaseConfiguration;
import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.model.Cititor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CititorService {
    private static CititorService instance;

    private CititorService() {
    }

    public static CititorService getInstance() {
        if (instance == null) {
            instance = new CititorService();
        }
        return instance;
    }

    public void inregistreazaCititor(Cititor cititor) {
        String sql = "INSERT INTO cititori (cnp, nume) VALUES (?, ?)";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cititor.getCnp());
            statement.setString(2, cititor.getNume());
            statement.executeUpdate();
            System.out.println("Cititor inregistrat in baza de date: " + cititor.getNume());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaCititor(String cnp) {
        String deleteImprumuturiSql = "DELETE FROM imprumuturi WHERE cititor_cnp = ?";
        String deleteCititorSql = "DELETE FROM cititori WHERE cnp = ?";

        try (Connection connection = DatabaseConfiguration.getConnection()) {

            try (PreparedStatement statementImprumuturi = connection.prepareStatement(deleteImprumuturiSql)) {
                statementImprumuturi.setString(1, cnp);
                statementImprumuturi.executeUpdate();
            }

            try (PreparedStatement statementCititor = connection.prepareStatement(deleteCititorSql)) {
                statementCititor.setString(1, cnp);
                int rowsDeleted = statementCititor.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Cititorul si istoricul sau au fost eliminate cu succes.");
                } else {
                    System.out.println("Cititorul cu acest CNP nu a fost gasit.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listeazaTotiCititorii() {
        String sql = "SELECT cnp, nume FROM cititori ORDER BY nume ASC";
        try (Connection connection = DatabaseConfiguration.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("--- Lista Cititori ---");
            while (resultSet.next()) {
                String cnp = resultSet.getString("cnp");
                String nume = resultSet.getString("nume");
                System.out.println("Cititor{nume='" + nume + "', cnp='" + cnp + "'}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void imprumutaCarte(String cnp, String isbnValoare) {
        try {
            CarteService.getInstance().preiaCartePentruImprumut(isbnValoare);

            String sql = "INSERT INTO imprumuturi (cititor_cnp, carte_isbn, data_imprumut) VALUES (?, ?, CURRENT_DATE)";
            try (Connection connection = DatabaseConfiguration.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, cnp);
                statement.setString(2, isbnValoare);
                statement.executeUpdate();

                CarteService.getInstance().setDisponibilitate(isbnValoare, false);
                System.out.println("Cartea cu ISBN " + isbnValoare + " a fost imprumutata cititorului cu CNP " + cnp);
            }
        } catch (CarteNedisponibilaException | SQLException e) {
            System.out.println("Eroare la imprumut: " + e.getMessage());
        }
    }

    public void returneazaCarte(String cnp, String isbnValoare) {
        String checkSql = "SELECT id FROM imprumuturi WHERE cititor_cnp = ? AND carte_isbn = ? AND data_returnare IS NULL LIMIT 1";
        String updateSql = "UPDATE imprumuturi SET data_returnare = CURRENT_DATE WHERE id = ?";

        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, cnp);
            checkStmt.setString(2, isbnValoare);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int idImprumut = rs.getInt("id");
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, idImprumut);
                    updateStmt.executeUpdate();
                }
                CarteService.getInstance().setDisponibilitate(isbnValoare, true);
                System.out.println("Cartea a fost returnata cu succes.");
            } else {
                System.out.println("Nu exista un imprumut activ pentru acest cititor si aceasta carte.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void afiseazaIstoric(String cnp) {
        String sql = "SELECT c.titlu, i.data_imprumut, i.data_returnare FROM imprumuturi i JOIN carti c ON i.carte_isbn = c.isbn WHERE i.cititor_cnp = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            ResultSet rs = statement.executeQuery();

            System.out.println("--- Istoric Imprumuturi pt CNP " + cnp + " ---");
            boolean areIstoric = false;
            while (rs.next()) {
                areIstoric = true;
                String titlu = rs.getString("titlu");
                java.sql.Date dataImprumut = rs.getDate("data_imprumut");
                java.sql.Date dataReturnare = rs.getDate("data_returnare");
                String status = (dataReturnare != null) ? dataReturnare.toString() : "NERETURNAT";
                System.out.println("Carte: " + titlu + " | Imprumutat: " + dataImprumut + " | Returnat: " + status);
            }
            if (!areIstoric) {
                System.out.println("Acest cititor nu are istoric de imprumuturi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}