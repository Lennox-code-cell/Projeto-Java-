package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Emprestimo no banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Emprestimo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    public void salvar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (id_exemplar, id_leitor, data_emprestimo, data_prevista, data_devolucao, situacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdExemplar());
            stmt.setInt(2, emprestimo.getIdLeitor());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataPrevista()));
            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(5, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, emprestimo.getSituacao());
            stmt.executeUpdate();
        }
    }

    public void registrarEmprestimo(Emprestimo emprestimo) throws SQLException {
        String inserirEmprestimo = "INSERT INTO emprestimo (id_exemplar, id_leitor, data_emprestimo, data_prevista, data_devolucao, situacao) VALUES (?, ?, ?, ?, ?, ?)";
        String atualizarExemplar = "UPDATE exemplar SET situacao = 'emprestado' WHERE id_exemplar = ? AND situacao = 'disponivel'";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtEmp = conn.prepareStatement(inserirEmprestimo)) {
                stmtEmp.setInt(1, emprestimo.getIdExemplar());
                stmtEmp.setInt(2, emprestimo.getIdLeitor());
                stmtEmp.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
                stmtEmp.setDate(4, Date.valueOf(emprestimo.getDataPrevista()));
                if (emprestimo.getDataDevolucao() != null) {
                    stmtEmp.setDate(5, Date.valueOf(emprestimo.getDataDevolucao()));
                } else {
                    stmtEmp.setNull(5, Types.DATE);
                }
                stmtEmp.setString(6, emprestimo.getSituacao());
                stmtEmp.executeUpdate();
            }

            try (PreparedStatement stmtEx = conn.prepareStatement(atualizarExemplar)) {
                stmtEx.setInt(1, emprestimo.getIdExemplar());
                int linhas = stmtEx.executeUpdate();
                if (linhas == 0) {
                    throw new SQLException("Exemplar indisponível para empréstimo.");
                }
            }

            conn.commit();
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo ORDER BY data_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setIdEmprestimo(rs.getInt("id_emprestimo"));
                emprestimo.setIdExemplar(rs.getInt("id_exemplar"));
                emprestimo.setIdLeitor(rs.getInt("id_leitor"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                emprestimo.setDataPrevista(rs.getDate("data_prevista").toLocalDate());
                Date dataDevolucao = rs.getDate("data_devolucao");
                emprestimo.setDataDevolucao(dataDevolucao != null ? dataDevolucao.toLocalDate() : null);
                emprestimo.setSituacao(rs.getString("situacao"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }

    public List<Object[]> listarAtivos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo, e.data_emprestimo, e.data_prevista, e.situacao " +
                "FROM emprestimo e JOIN leitor l ON l.id_leitor = e.id_leitor JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar " +
                "JOIN livro li ON li.id_livro = ex.id_livro WHERE e.situacao = 'ativo' ORDER BY e.data_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_emprestimo"),
                        rs.getString("nome"),
                        rs.getString("titulo"),
                        rs.getString("codigo"),
                        rs.getDate("data_emprestimo").toLocalDate(),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getString("situacao")
                });
            }
        }
        return lista;
    }

    public List<Object[]> relatorioPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo, e.data_emprestimo, e.data_prevista, e.data_devolucao, e.situacao " +
                "FROM emprestimo e JOIN leitor l ON l.id_leitor = e.id_leitor JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar " +
                "JOIN livro li ON li.id_livro = ex.id_livro WHERE e.data_emprestimo BETWEEN ? AND ? ORDER BY e.data_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));
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

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET id_exemplar = ?, id_leitor = ?, data_emprestimo = ?, data_prevista = ?, data_devolucao = ?, situacao = ? WHERE id_emprestimo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdExemplar());
            stmt.setInt(2, emprestimo.getIdLeitor());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataPrevista()));
            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(5, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, emprestimo.getSituacao());
            stmt.setInt(7, emprestimo.getIdEmprestimo());
            stmt.executeUpdate();
        }
    }

    public void registrarDevolucao(int idEmprestimo, LocalDate dataDevolucao) throws SQLException {
        String atualizarEmprestimo = "UPDATE emprestimo SET data_devolucao = ?, situacao = 'devolvido' WHERE id_emprestimo = ?";
        String buscarExemplar = "SELECT id_exemplar FROM emprestimo WHERE id_emprestimo = ?";
        String atualizarExemplar = "UPDATE exemplar SET situacao = 'disponivel' WHERE id_exemplar = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int idExemplar = -1;
            try (PreparedStatement stmtBusca = conn.prepareStatement(buscarExemplar)) {
                stmtBusca.setInt(1, idEmprestimo);
                try (ResultSet rs = stmtBusca.executeQuery()) {
                    if (rs.next()) {
                        idExemplar = rs.getInt("id_exemplar");
                    }
                }
            }

            if (idExemplar == -1) {
                throw new SQLException("Empréstimo não encontrado.");
            }

            try (PreparedStatement stmtEmp = conn.prepareStatement(atualizarEmprestimo)) {
                stmtEmp.setDate(1, Date.valueOf(dataDevolucao));
                stmtEmp.setInt(2, idEmprestimo);
                stmtEmp.executeUpdate();
            }

            try (PreparedStatement stmtEx = conn.prepareStatement(atualizarExemplar)) {
                stmtEx.setInt(1, idExemplar);
                stmtEx.executeUpdate();
            }

            conn.commit();
        }
    }
}
