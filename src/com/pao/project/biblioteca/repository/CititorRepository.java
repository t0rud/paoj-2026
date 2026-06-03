package com.pao.project.biblioteca.repository;

import com.pao.project.biblioteca.model.Cititor;
import com.pao.project.biblioteca.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CititorRepository implements Repository<Cititor, String> {
    private static CititorRepository instance;

    private CititorRepository() {}

    public static CititorRepository getInstance() {
        if (instance == null) {
            instance = new CititorRepository();
        }
        return instance;
    }

    @Override
    public void save(Cititor cititor) {
        String sql = "INSERT INTO cititori (cnp, nume) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cititor.getCnp());
            statement.setString(2, cititor.getNume());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String cnp) {
        String deleteImprumuturiSql = "DELETE FROM imprumuturi WHERE cititor_cnp = ?";
        String deleteCititorSql = "DELETE FROM cititori WHERE cnp = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement statementImprumuturi = connection.prepareStatement(deleteImprumuturiSql)) {
                statementImprumuturi.setString(1, cnp);
                statementImprumuturi.executeUpdate();
            }
            try (PreparedStatement statementCititor = connection.prepareStatement(deleteCititorSql)) {
                statementCititor.setString(1, cnp);
                statementCititor.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cititor> findAll() {
        List<Cititor> cititori = new ArrayList<>();
        String sql = "SELECT cnp, nume FROM cititori ORDER BY nume ASC";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                cititori.add(new Cititor(resultSet.getString("cnp"), resultSet.getString("nume")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cititori;
    }

    public void adaugaImprumut(String cnp, String isbnValoare) {
        String sql = "INSERT INTO imprumuturi (cititor_cnp, carte_isbn, data_imprumut) VALUES (?, ?, CURRENT_DATE)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            statement.setString(2, isbnValoare);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean returneazaImprumut(String cnp, String isbnValoare) {
        String checkSql = "SELECT id FROM imprumuturi WHERE cititor_cnp = ? AND carte_isbn = ? AND data_returnare IS NULL LIMIT 1";
        String updateSql = "UPDATE imprumuturi SET data_returnare = CURRENT_DATE WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, cnp);
            checkStmt.setString(2, isbnValoare);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int idImprumut = rs.getInt("id");
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, idImprumut);
                    updateStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getIstoric(String cnp) {
        List<String> istoric = new ArrayList<>();
        String sql = "SELECT c.titlu, i.data_imprumut, i.data_returnare FROM imprumuturi i JOIN carti c ON i.carte_isbn = c.isbn WHERE i.cititor_cnp = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String titlu = rs.getString("titlu");
                Date dataImprumut = rs.getDate("data_imprumut");
                Date dataReturnare = rs.getDate("data_returnare");
                String status = (dataReturnare != null) ? dataReturnare.toString() : "NERETURNAT";
                istoric.add("Carte: " + titlu + " | Imprumutat: " + dataImprumut + " | Returnat: " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return istoric;
    }
}