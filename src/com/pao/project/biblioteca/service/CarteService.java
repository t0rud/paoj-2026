package com.pao.project.biblioteca.service;

import com.pao.project.biblioteca.config.DatabaseConfiguration;
import com.pao.project.biblioteca.exception.CarteNedisponibilaException;
import com.pao.project.biblioteca.model.Autor;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.ISBN;
import com.pao.project.biblioteca.model.Sectiune;

import java.sql.*;

public class CarteService {
    private static CarteService instance;

    private CarteService() {
    }

    public static CarteService getInstance() {
        if (instance == null) {
            instance = new CarteService();
        }
        return instance;
    }

    private int getOrInsertAutor(String nume, String prenume) throws SQLException {
        String checkSql = "SELECT id FROM autori WHERE nume = ? AND prenume = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, nume);
            checkStmt.setString(2, prenume);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insertSql = "INSERT INTO autori (nume, prenume) VALUES (?, ?)";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, nume);
            insertStmt.setString(2, prenume);
            insertStmt.executeUpdate();
            ResultSet keys = insertStmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        throw new SQLException("Eroare la procesarea autorului.");
    }

    public void adaugaCarte(Carte carte) {
        try {
            int autorId = getOrInsertAutor(carte.getAutor().getNume(), carte.getAutor().getPrenume());
            String sql = "INSERT INTO carti (isbn, titlu, autor_id, sectiune, disponibila) VALUES (?, ?, ?, ?, ?)";

            try (Connection connection = DatabaseConfiguration.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, carte.getIsbn().getValoare());
                statement.setString(2, carte.getTitlu());
                statement.setInt(3, autorId);
                statement.setString(4, carte.getSectiune().name());
                statement.setBoolean(5, carte.isDisponibila());
                statement.executeUpdate();
                System.out.println("Carte adaugata cu succes: " + carte.getTitlu());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listeazaCartiDinSectiune(Sectiune sectiune) {
        String sql = "SELECT c.isbn, c.titlu, c.disponibila, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE c.sectiune = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sectiune.name());
            ResultSet rs = statement.executeQuery();
            System.out.println("--- Carti in sectiunea " + sectiune + " ---");
            while (rs.next()) {
                System.out.println(rs.getString("titlu") + " de " + rs.getString("prenume") + " " + rs.getString("nume") + " (ISBN: " + rs.getString("isbn") + ") - Disponibila: " + rs.getBoolean("disponibila"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cautaCartiDupaAutor(String numeAutor) {
        String sql = "SELECT c.isbn, c.titlu, c.disponibila, c.sectiune, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE LOWER(a.nume) LIKE ? OR LOWER(a.prenume) LIKE ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchParam = "%" + numeAutor.toLowerCase() + "%";
            statement.setString(1, searchParam);
            statement.setString(2, searchParam);
            ResultSet rs = statement.executeQuery();
            System.out.println("--- Carti gasite pentru autorul " + numeAutor + " ---");
            while (rs.next()) {
                System.out.println(rs.getString("titlu") + " de " + rs.getString("prenume") + " " + rs.getString("nume"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void verificaDisponibilitate(ISBN isbn) {
        String sql = "SELECT titlu, disponibila FROM carti WHERE isbn = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn.getValoare());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("Cartea '" + rs.getString("titlu") + "' disponibila: " + rs.getBoolean("disponibila"));
            } else {
                System.out.println("Cartea nu exista in inventar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Carte preiaCartePentruImprumut(String isbnValoare) throws CarteNedisponibilaException {
        String sql = "SELECT c.titlu, c.sectiune, c.disponibila, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE c.isbn = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbnValoare);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                boolean disponibila = rs.getBoolean("disponibila");
                if (!disponibila) {
                    throw new CarteNedisponibilaException("Cartea cu ISBN " + isbnValoare + " nu este disponibila.");
                }
                Autor autor = new Autor(rs.getString("nume"), rs.getString("prenume"));
                Sectiune sectiune = Sectiune.valueOf(rs.getString("sectiune"));
                Carte carte = new Carte(new ISBN(isbnValoare), rs.getString("titlu"), autor, sectiune);
                carte.setDisponibila(true);
                return carte;
            } else {
                throw new CarteNedisponibilaException("Cartea cu ISBN " + isbnValoare + " nu exista.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CarteNedisponibilaException("Eroare la accesarea bazei de date.");
        }
    }

    public void setDisponibilitate(String isbnValoare, boolean status) {
        String sql = "UPDATE carti SET disponibila = ? WHERE isbn = ?";
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, status);
            statement.setString(2, isbnValoare);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}