package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Categoria no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public void salvar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.executeUpdate();
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                categoria.setDescricao(rs.getString("descricao"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idCategoria) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.executeUpdate();
        }
    }
}
