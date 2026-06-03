package com.pao.project.biblioteca.repository;

import com.pao.project.biblioteca.model.Autor;
import com.pao.project.biblioteca.model.Carte;
import com.pao.project.biblioteca.model.ISBN;
import com.pao.project.biblioteca.model.Sectiune;
import com.pao.project.biblioteca.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteRepository implements Repository<Carte, String> {
    private static CarteRepository instance;

    private CarteRepository() {}

    public static CarteRepository getInstance() {
        if (instance == null) {
            instance = new CarteRepository();
        }
        return instance;
    }

    private int getOrInsertAutor(String nume, String prenume) throws SQLException {
        String checkSql = "SELECT id FROM autori WHERE nume = ? AND prenume = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, nume);
            checkStmt.setString(2, prenume);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insertSql = "INSERT INTO autori (nume, prenume) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
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

    @Override
    public void save(Carte carte) {
        try {
            int autorId = getOrInsertAutor(carte.getAutor().getNume(), carte.getAutor().getPrenume());
            String sql = "INSERT INTO carti (isbn, titlu, autor_id, sectiune, disponibila) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, carte.getIsbn().getValoare());
                statement.setString(2, carte.getTitlu());
                statement.setInt(3, autorId);
                statement.setString(4, carte.getSectiune().name());
                statement.setBoolean(5, carte.isDisponibila());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String isbn) {
        String sql = "DELETE FROM carti WHERE isbn = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Carte> findAll() {
        return new ArrayList<>();
    }

    public List<Carte> findBySectiune(Sectiune sectiune) {
        List<Carte> carti = new ArrayList<>();
        String sql = "SELECT c.isbn, c.titlu, c.disponibila, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE c.sectiune = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sectiune.name());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Autor autor = new Autor(rs.getString("nume"), rs.getString("prenume"));
                Carte carte = new Carte(new ISBN(rs.getString("isbn")), rs.getString("titlu"), autor, sectiune);
                carte.setDisponibila(rs.getBoolean("disponibila"));
                carti.add(carte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carti;
    }

    public List<Carte> findByAutor(String numeAutor) {
        List<Carte> carti = new ArrayList<>();
        String sql = "SELECT c.isbn, c.titlu, c.disponibila, c.sectiune, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE LOWER(a.nume) LIKE ? OR LOWER(a.prenume) LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchParam = "%" + numeAutor.toLowerCase() + "%";
            statement.setString(1, searchParam);
            statement.setString(2, searchParam);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Autor autor = new Autor(rs.getString("nume"), rs.getString("prenume"));
                Carte carte = new Carte(new ISBN(rs.getString("isbn")), rs.getString("titlu"), autor, Sectiune.valueOf(rs.getString("sectiune")));
                carte.setDisponibila(rs.getBoolean("disponibila"));
                carti.add(carte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carti;
    }

    public Carte findById(String isbnValoare) {
        String sql = "SELECT c.titlu, c.sectiune, c.disponibila, a.nume, a.prenume FROM carti c JOIN autori a ON c.autor_id = a.id WHERE c.isbn = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbnValoare);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Autor autor = new Autor(rs.getString("nume"), rs.getString("prenume"));
                Sectiune sectiune = Sectiune.valueOf(rs.getString("sectiune"));
                Carte carte = new Carte(new ISBN(isbnValoare), rs.getString("titlu"), autor, sectiune);
                carte.setDisponibila(rs.getBoolean("disponibila"));
                return carte;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDisponibilitate(String isbnValoare, boolean status) {
        String sql = "UPDATE carti SET disponibila = ? WHERE isbn = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, status);
            statement.setString(2, isbnValoare);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}