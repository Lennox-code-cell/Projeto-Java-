package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Leitor no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Leitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeitorDAO {
    public void salvar(Leitor leitor) throws SQLException {
        String sql = "INSERT INTO leitor (nome, cpf, telefone, email, endereco, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leitor.getNome());
            stmt.setString(2, leitor.getCpf());
            stmt.setString(3, leitor.getTelefone());
            stmt.setString(4, leitor.getEmail());
            stmt.setString(5, leitor.getEndereco());
            stmt.setBoolean(6, leitor.isAtivo());
            stmt.executeUpdate();
        }
    }

    public List<Leitor> listarTodos() throws SQLException {
        List<Leitor> leitores = new ArrayList<>();
        String sql = "SELECT * FROM leitor ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Leitor leitor = new Leitor();
                leitor.setIdLeitor(rs.getInt("id_leitor"));
                leitor.setNome(rs.getString("nome"));
                leitor.setCpf(rs.getString("cpf"));
                leitor.setTelefone(rs.getString("telefone"));
                leitor.setEmail(rs.getString("email"));
                leitor.setEndereco(rs.getString("endereco"));
                leitor.setAtivo(rs.getBoolean("ativo"));
                leitores.add(leitor);
            }
        }
        return leitores;
    }

    public void atualizar(Leitor leitor) throws SQLException {
        String sql = "UPDATE leitor SET nome = ?, cpf = ?, telefone = ?, email = ?, endereco = ?, ativo = ? WHERE id_leitor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leitor.getNome());
            stmt.setString(2, leitor.getCpf());
            stmt.setString(3, leitor.getTelefone());
            stmt.setString(4, leitor.getEmail());
            stmt.setString(5, leitor.getEndereco());
            stmt.setBoolean(6, leitor.isAtivo());
            stmt.setInt(7, leitor.getIdLeitor());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idLeitor) throws SQLException {
        String sql = "DELETE FROM leitor WHERE id_leitor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLeitor);
            stmt.executeUpdate();
        }
    }
}
