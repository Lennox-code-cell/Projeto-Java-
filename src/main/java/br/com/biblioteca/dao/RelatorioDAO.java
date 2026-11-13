
package br.com.biblioteca.dao;

// Responsável por acessar e consultar os dados necessários
// para gerar relatórios de empréstimos.

// Classe responsável pela conexão com o banco de dados.
import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.EmprestimoRelatorio;
import br.com.biblioteca.model.SituacaoEmprestimo;

// Importações para executar consultas SQL.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Importação para trabalhar com datas sem horário.
import java.time.LocalDate;

// Importações para armazenar os resultados em uma lista.
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public List<EmprestimoRelatorio> gerarRelatorioPorPeriodo(
            LocalDate inicio,
            LocalDate fim,
            String situacao
    ) throws SQLException {
        List<EmprestimoRelatorio> lista = new ArrayList<>();
        String sql = """
                SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo,
                       e.data_emprestimo, e.data_prevista, e.data_devolucao
                FROM emprestimo e
                JOIN leitor l ON l.id_leitor = e.id_leitor
                JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar
                JOIN livro li ON li.id_livro = ex.id_livro
                WHERE e.data_emprestimo BETWEEN ? AND ?
                ORDER BY e.data_emprestimo DESC, e.id_emprestimo DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fim));
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                LocalDate hoje = LocalDate.now();
                while (rs.next()) {
                    java.sql.Date dataDevolucao = rs.getDate("data_devolucao");
                    LocalDate dataPrevista = rs.getDate("data_prevista").toLocalDate();
                    SituacaoEmprestimo estado = SituacaoEmprestimo.calcular(
                            dataPrevista,
                            dataDevolucao == null ? null : dataDevolucao.toLocalDate(),
                            hoje
                    );
                    if (!"todos".equals(situacao) && !situacao.equals(estado.valorBanco())) {
                        continue;
                    }

                    lista.add(new EmprestimoRelatorio(
                            rs.getInt("id_emprestimo"),
                            rs.getString("nome"),
                            rs.getString("titulo"),
                            rs.getString("codigo"),
                            rs.getDate("data_emprestimo").toLocalDate(),
                            dataPrevista,
                            dataDevolucao == null ? null : dataDevolucao.toLocalDate(),
                            estado.valorBanco()
                    ));
                }
            }
        }
        return lista;
    }
}
