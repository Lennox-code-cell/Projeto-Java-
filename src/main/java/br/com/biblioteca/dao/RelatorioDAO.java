package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Relatorio no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {
    public List<Object[]> gerarRelatorioPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo, e.data_emprestimo, e.data_prevista, e.data_devolucao, e.situacao " +
                "FROM emprestimo e JOIN leitor l ON l.id_leitor = e.id_leitor JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar " +
                "JOIN livro li ON li.id_livro = ex.id_livro WHERE e.data_emprestimo BETWEEN ? AND ? ORDER BY e.data_emprestimo DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                            rs.getInt("id_emprestimo"),
                            rs.getString("nome"),
                            rs.getString("titulo"),
                            rs.getString("codigo"),
                            rs.getDate("data_emprestimo").toLocalDate(),
                            rs.getDate("data_prevista").toLocalDate(),
                            rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null,
                            rs.getString("situacao")
                    });
                }
            }
        }
        return lista;
    }
}
