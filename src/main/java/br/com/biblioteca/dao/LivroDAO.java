package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Livro no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    public void salvar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, isbn, ano_publicacao, editora, id_autor, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getEditora());
            stmt.setInt(5, livro.getIdAutor());
            stmt.setInt(6, livro.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro ORDER BY titulo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setIdLivro(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setEditora(rs.getString("editora"));
                livro.setIdAutor(rs.getInt("id_autor"));
                livro.setIdCategoria(rs.getInt("id_categoria"));
                livros.add(livro);
            }
        }
        return livros;
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, isbn = ?, ano_publicacao = ?, editora = ?, id_autor = ?, id_categoria = ? WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getEditora());
            stmt.setInt(5, livro.getIdAutor());
            stmt.setInt(6, livro.getIdCategoria());
            stmt.setInt(7, livro.getIdLivro());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }
}
