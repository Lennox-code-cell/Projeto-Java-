package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Exemplar no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Exemplar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO {
    public void salvar(Exemplar exemplar) throws SQLException {
        String sql = "INSERT INTO exemplar (id_livro, codigo, situacao) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exemplar.getIdLivro());
            stmt.setString(2, exemplar.getCodigo());
            stmt.setString(3, exemplar.getSituacao() == null || exemplar.getSituacao().isBlank() ? "disponivel" : exemplar.getSituacao());
            stmt.executeUpdate();
        }
    }

    public List<Exemplar> listarTodos() throws SQLException {
        List<Exemplar> exemplares = new ArrayList<>();
        String sql = "SELECT * FROM exemplar ORDER BY codigo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Exemplar exemplar = new Exemplar();
                exemplar.setIdExemplar(rs.getInt("id_exemplar"));
                exemplar.setIdLivro(rs.getInt("id_livro"));
                exemplar.setCodigo(rs.getString("codigo"));
                exemplar.setSituacao(rs.getString("situacao"));
                exemplares.add(exemplar);
            }
        }
        return exemplares;
    }

    public List<Exemplar> listarDisponiveis() throws SQLException {
        List<Exemplar> exemplares = new ArrayList<>();
        String sql = "SELECT * FROM exemplar WHERE situacao = 'disponivel' ORDER BY codigo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Exemplar exemplar = new Exemplar();
                exemplar.setIdExemplar(rs.getInt("id_exemplar"));
                exemplar.setIdLivro(rs.getInt("id_livro"));
                exemplar.setCodigo(rs.getString("codigo"));
                exemplar.setSituacao(rs.getString("situacao"));
                exemplares.add(exemplar);
            }
        }
        return exemplares;
    }

    public boolean exemplarDisponivel(int idExemplar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM exemplar WHERE id_exemplar = ? AND situacao = 'disponivel'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idExemplar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void atualizar(Exemplar exemplar) throws SQLException {
        String sql = "UPDATE exemplar SET id_livro = ?, codigo = ?, situacao = ? WHERE id_exemplar = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exemplar.getIdLivro());
            stmt.setString(2, exemplar.getCodigo());
            stmt.setString(3, exemplar.getSituacao() == null || exemplar.getSituacao().isBlank() ? "disponivel" : exemplar.getSituacao());
            stmt.setInt(4, exemplar.getIdExemplar());
            stmt.executeUpdate();
        }
    }

    public void atualizarSituacao(int idExemplar, String situacao) throws SQLException {
        String sql = "UPDATE exemplar SET situacao = ? WHERE id_exemplar = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, situacao);
            stmt.setInt(2, idExemplar);
            stmt.executeUpdate();
        }
    }

    public void excluir(int idExemplar) throws SQLException {
        String sql = "DELETE FROM exemplar WHERE id_exemplar = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idExemplar);
            stmt.executeUpdate();
        }
    }
}
