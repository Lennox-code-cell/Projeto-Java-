package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Autor no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    public void salvar(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nome, nacionalidade) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.executeUpdate();
        }
    }

    public List<Autor> listarTodos() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setIdAutor(rs.getInt("id_autor"));
                autor.setNome(rs.getString("nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                autores.add(autor);
            }
        }
        return autores;
    }

    public void atualizar(Autor autor) throws SQLException {
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ? WHERE id_autor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setInt(3, autor.getIdAutor());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idAutor) throws SQLException {
        String sql = "DELETE FROM autor WHERE id_autor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAutor);
            stmt.executeUpdate();
        }
    }
}
