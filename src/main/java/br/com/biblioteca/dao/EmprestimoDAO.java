
package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Empréstimo
 * no banco de dados.
 */

// Importa a classe responsável por estabelecer a conexão com o MySQL.
import br.com.biblioteca.connection.DatabaseConnection;

// Importa o modelo que representa um empréstimo.
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.EmprestimoRelatorio;

// Importa as classes necessárias para executar comandos SQL.
import java.sql.*;

// Importa LocalDate para trabalhar com datas sem horário.
import java.time.LocalDate;

// Importa ArrayList para armazenar os empréstimos encontrados.
import java.util.ArrayList;

// Importa List para trabalhar com listas de empréstimos.
import java.util.List;

public class EmprestimoDAO {

    /**
     * Salva um empréstimo no banco de dados.
     *
     * @param emprestimo Objeto contendo os dados do empréstimo.
     * @throws SQLException Caso ocorra um erro na operação SQL.
     */
    public void salvar(Emprestimo emprestimo) throws SQLException {
        registrarEmprestimo(emprestimo);
    }

    /**
     * Registra um empréstimo e atualiza a situação do exemplar.
     *
     * @param emprestimo Objeto contendo os dados do empréstimo.
     * @throws SQLException Caso ocorra um erro na operação SQL.
     */
    public void registrarEmprestimo(Emprestimo emprestimo) throws SQLException {
        validarEmprestimo(emprestimo);
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                sincronizarSituacoes(conn);
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT ativo FROM leitor WHERE id_leitor = ? FOR UPDATE")) {
                    stmt.setInt(1, emprestimo.getIdLeitor());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next() || !rs.getBoolean("ativo")) {
                            throw new SQLException("O leitor não existe ou está inativo.");
                        }
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE exemplar SET situacao = 'emprestado' "
                                + "WHERE id_exemplar = ? AND situacao = 'disponivel' "
                                + "AND NOT EXISTS (SELECT 1 FROM emprestimo "
                                + "WHERE emprestimo.id_exemplar = exemplar.id_exemplar "
                                + "AND emprestimo.data_devolucao IS NULL)")) {
                    stmt.setInt(1, emprestimo.getIdExemplar());
                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Exemplar indisponível para empréstimo.");
                    }
                }

                String sql = "INSERT INTO emprestimo "
                        + "(id_exemplar, id_leitor, data_emprestimo, data_prevista, data_devolucao, situacao) "
                        + "VALUES (?, ?, ?, ?, NULL, 'ativo')";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, emprestimo.getIdExemplar());
                    stmt.setInt(2, emprestimo.getIdLeitor());
                    stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
                    stmt.setDate(4, Date.valueOf(emprestimo.getDataPrevista()));
                    stmt.executeUpdate();
                }
                sincronizarSituacoes(conn);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    /**
     * Consulta todos os empréstimos cadastrados.
     *
     * @return Lista contendo todos os empréstimos.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Emprestimo> listarTodos() throws SQLException {
        sincronizarSituacoes();

        // Cria uma lista vazia para armazenar os empréstimos.
        List<Emprestimo> emprestimos = new ArrayList<>();

        // Consulta todos os empréstimos, ordenando
        // pelos mais recentes primeiro.
        String sql = "SELECT * FROM emprestimo ORDER BY data_emprestimo DESC";

        // Abre a conexão, prepara e executa a consulta.
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                // Percorre todos os registros encontrados.
                while (rs.next()) {

                    // Cria um objeto Emprestimo para representar cada registro.
                    Emprestimo emprestimo = new Emprestimo();

                    // Recupera o ID do empréstimo.
                    emprestimo.setIdEmprestimo(rs.getInt("id_emprestimo"));

                    // Recupera o ID do exemplar emprestado.
                    emprestimo.setIdExemplar(rs.getInt("id_exemplar"));

                    // Recupera o ID do leitor responsável.
                    emprestimo.setIdLeitor(rs.getInt("id_leitor"));

                    // Recupera a data do empréstimo e converte para LocalDate.
                    emprestimo.setDataEmprestimo(
                            rs.getDate("data_emprestimo").toLocalDate()
                    );

                    // Recupera a data prevista para devolução.
                    emprestimo.setDataPrevista(
                            rs.getDate("data_prevista").toLocalDate()
                    );

                    // Recupera a data de devolução.
                    Date dataDevolucao = rs.getDate("data_devolucao");

                    // Se a data existir, converte para LocalDate.
                    // Caso contrário, mantém o valor como null.
                    emprestimo.setDataDevolucao(
                            dataDevolucao != null ? dataDevolucao.toLocalDate() : null
                    );

                    // Recupera a situação sincronizada com as datas do empréstimo.
                    emprestimo.setSituacao(rs.getString("situacao"));

                    // Adiciona o objeto preenchido à lista.
                    emprestimos.add(emprestimo);
                }
            }
        }

        // Retorna a lista completa de empréstimos.
        return emprestimos;
    }

    /**
     * Consulta somente os empréstimos que estão ativos.
     * Também recupera informações do leitor, livro e exemplar.
     *
     * @return Lista de objetos contendo os dados dos empréstimos ativos.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<EmprestimoRelatorio> listarAtivos() throws SQLException {
        sincronizarSituacoes();
        List<EmprestimoRelatorio> lista = new ArrayList<>();
        String sql = """
                SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo,
                       e.data_emprestimo, e.data_prevista, e.data_devolucao,
                       e.situacao
                FROM emprestimo e
                JOIN leitor l ON l.id_leitor = e.id_leitor
                JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar
                JOIN livro li ON li.id_livro = ex.id_livro
                WHERE e.data_devolucao IS NULL
                ORDER BY e.data_prevista, e.data_emprestimo DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRelatorio(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Gera um relatório de empréstimos dentro de um período.
     *
     * @param inicio Data inicial do relatório.
     * @param fim Data final do relatório.
     * @return Lista contendo os empréstimos encontrados no período.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<EmprestimoRelatorio> relatorioPorPeriodo(LocalDate inicio, LocalDate fim)
            throws SQLException {
        return new RelatorioDAO().gerarRelatorioPorPeriodo(inicio, fim, "todos");
    }

    /**
     * Atualiza os dados de um empréstimo existente.
     *
     * @param emprestimo Objeto contendo o ID e os novos dados.
     * @throws SQLException Caso ocorra um erro na atualização.
     */
    public void atualizar(Emprestimo emprestimo) throws SQLException {
        if (emprestimo == null || emprestimo.getIdEmprestimo() <= 0) {
            throw new IllegalArgumentException("Selecione um empréstimo válido.");
        }
        validarDatasEmprestimo(emprestimo.getDataEmprestimo(), emprestimo.getDataPrevista());
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                sincronizarSituacoes(conn);
                int idExemplar;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id_exemplar FROM emprestimo "
                                + "WHERE id_emprestimo = ? AND data_devolucao IS NULL "
                                + "AND situacao IN ('ativo', 'atrasado') FOR UPDATE")) {
                    stmt.setInt(1, emprestimo.getIdEmprestimo());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Empréstimo inexistente ou já finalizado.");
                        }
                        idExemplar = rs.getInt("id_exemplar");
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT situacao FROM exemplar WHERE id_exemplar = ? FOR UPDATE")) {
                    stmt.setInt(1, idExemplar);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next() || !"emprestado".equalsIgnoreCase(rs.getString("situacao"))) {
                            throw new SQLException(
                                    "O exemplar associado não está marcado como emprestado."
                            );
                        }
                    }
                }

                String sql = "UPDATE emprestimo SET data_emprestimo = ?, data_prevista = ? "
                        + "WHERE id_emprestimo = ? AND data_devolucao IS NULL "
                        + "AND situacao IN ('ativo', 'atrasado')";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDate(1, Date.valueOf(emprestimo.getDataEmprestimo()));
                    stmt.setDate(2, Date.valueOf(emprestimo.getDataPrevista()));
                    stmt.setInt(3, emprestimo.getIdEmprestimo());
                    stmt.executeUpdate();
                }
                sincronizarSituacoes(conn);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    /**
     * Exclui um empréstimo e, se ainda estiver aberto, libera seu exemplar.
     *
     * @param idEmprestimo Identificador do empréstimo.
     * @throws SQLException Caso ocorra um erro na exclusão ou na liberação.
     */
    public void excluir(int idEmprestimo) throws SQLException {
        if (idEmprestimo <= 0) {
            throw new IllegalArgumentException("Selecione um empréstimo válido.");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                sincronizarSituacoes(conn);
                int idExemplar;
                boolean emAberto;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id_exemplar, data_devolucao FROM emprestimo "
                                + "WHERE id_emprestimo = ? FOR UPDATE")) {
                    stmt.setInt(1, idEmprestimo);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Empréstimo não encontrado.");
                        }
                        idExemplar = rs.getInt("id_exemplar");
                        emAberto = rs.getDate("data_devolucao") == null;
                    }
                }

                if (emAberto) {
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "SELECT situacao FROM exemplar WHERE id_exemplar = ? FOR UPDATE")) {
                        stmt.setInt(1, idExemplar);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (!rs.next() || !"emprestado".equalsIgnoreCase(
                                    rs.getString("situacao"))) {
                                throw new SQLException(
                                        "O exemplar associado não está marcado como emprestado. "
                                                + "O cancelamento não foi realizado."
                                );
                            }
                        }
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM emprestimo WHERE id_emprestimo = ?")) {
                    stmt.setInt(1, idEmprestimo);
                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Não foi possível excluir o empréstimo.");
                    }
                }

                if (emAberto) {
                    boolean outroEmprestimoAtivo;
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "SELECT 1 FROM emprestimo WHERE id_exemplar = ? "
                                    + "AND data_devolucao IS NULL LIMIT 1")) {
                        stmt.setInt(1, idExemplar);
                        try (ResultSet rs = stmt.executeQuery()) {
                            outroEmprestimoAtivo = rs.next();
                        }
                    }

                    if (!outroEmprestimoAtivo) {
                        try (PreparedStatement stmt = conn.prepareStatement(
                                "UPDATE exemplar SET situacao = 'disponivel' "
                                        + "WHERE id_exemplar = ? AND situacao = 'emprestado'")) {
                            stmt.setInt(1, idExemplar);
                            if (stmt.executeUpdate() != 1) {
                                throw new SQLException(
                                        "Não foi possível liberar o exemplar do empréstimo."
                                );
                            }
                        }
                    }
                }
                sincronizarSituacoes(conn);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    /** Consulta empréstimos finalizados e em aberto para a tela de gerenciamento. */
    public List<EmprestimoRelatorio> listarTodosDetalhados() throws SQLException {
        sincronizarSituacoes();
        List<EmprestimoRelatorio> lista = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, l.nome, li.titulo, ex.codigo, "
                + "e.data_emprestimo, e.data_prevista, e.data_devolucao, e.situacao "
                + "FROM emprestimo e "
                + "JOIN leitor l ON l.id_leitor = e.id_leitor "
                + "JOIN exemplar ex ON ex.id_exemplar = e.id_exemplar "
                + "JOIN livro li ON li.id_livro = ex.id_livro "
                + "ORDER BY e.data_emprestimo DESC, e.id_emprestimo DESC";
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRelatorio(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Registra a devolução de um exemplar.
     *
     * @param idEmprestimo Identificador do empréstimo.
     * @param dataDevolucao Data em que o exemplar foi devolvido.
     * @throws SQLException Caso ocorra um erro na operação SQL.
     */
    public void registrarDevolucao(
            int idEmprestimo,
            LocalDate dataDevolucao
    ) throws SQLException {
        if (idEmprestimo <= 0 || dataDevolucao == null) {
            throw new IllegalArgumentException("Empréstimo e data de devolução são obrigatórios.");
        }
        if (dataDevolucao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de devolução não pode estar no futuro.");
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                sincronizarSituacoes(conn);
                int idExemplar;
                LocalDate dataEmprestimo;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id_exemplar, data_emprestimo, data_devolucao "
                                + "FROM emprestimo WHERE id_emprestimo = ? FOR UPDATE")) {
                    stmt.setInt(1, idEmprestimo);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Empréstimo não encontrado.");
                        }
                        if (rs.getDate("data_devolucao") != null) {
                            throw new SQLException("Este empréstimo já foi devolvido.");
                        }
                        idExemplar = rs.getInt("id_exemplar");
                        dataEmprestimo = rs.getDate("data_emprestimo").toLocalDate();
                    }
                }
                if (dataDevolucao.isBefore(dataEmprestimo)) {
                    throw new IllegalArgumentException(
                            "A devolução não pode ser anterior ao empréstimo."
                    );
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE emprestimo SET data_devolucao = ?, situacao = 'devolvido' "
                                + "WHERE id_emprestimo = ? AND data_devolucao IS NULL")) {
                    stmt.setDate(1, Date.valueOf(dataDevolucao));
                    stmt.setInt(2, idEmprestimo);
                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Este empréstimo já foi devolvido.");
                    }
                }

                boolean outroEmprestimoAtivo;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT 1 FROM emprestimo WHERE id_exemplar = ? "
                                + "AND data_devolucao IS NULL LIMIT 1 FOR UPDATE")) {
                    stmt.setInt(1, idExemplar);
                    try (ResultSet rs = stmt.executeQuery()) {
                        outroEmprestimoAtivo = rs.next();
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE exemplar SET situacao = ? "
                                + "WHERE id_exemplar = ? AND situacao = 'emprestado'")) {
                    stmt.setString(1, outroEmprestimoAtivo ? "emprestado" : "disponivel");
                    stmt.setInt(2, idExemplar);
                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException(
                                "O exemplar não está marcado como emprestado; devolução cancelada."
                        );
                    }
                }
                sincronizarSituacoes(conn);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    /**
     * Sincroniza as situações persistidas com as datas dos empréstimos e sua
     * relação com os exemplares. Deve ser chamada dentro da transação do chamador.
     */
    static void sincronizarSituacoes(Connection conn) throws SQLException {
        String atualizarEmprestimos = "UPDATE emprestimo SET situacao = CASE "
                + "WHEN data_devolucao IS NOT NULL THEN 'devolvido' "
                + "WHEN data_prevista < CURRENT_DATE THEN 'atrasado' "
                + "ELSE 'ativo' END "
                + "WHERE situacao <> CASE "
                + "WHEN data_devolucao IS NOT NULL THEN 'devolvido' "
                + "WHEN data_prevista < CURRENT_DATE THEN 'atrasado' "
                + "ELSE 'ativo' END";
        try (PreparedStatement stmt = conn.prepareStatement(atualizarEmprestimos)) {
            stmt.executeUpdate();
        }

        String marcarExemplaresEmprestados = "UPDATE exemplar SET situacao = 'emprestado' "
                + "WHERE situacao <> 'emprestado' AND EXISTS ("
                + "SELECT 1 FROM emprestimo WHERE emprestimo.id_exemplar = exemplar.id_exemplar "
                + "AND emprestimo.data_devolucao IS NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(marcarExemplaresEmprestados)) {
            stmt.executeUpdate();
        }

        String liberarExemplaresSemEmprestimo = "UPDATE exemplar SET situacao = 'disponivel' "
                + "WHERE situacao = 'emprestado' AND NOT EXISTS ("
                + "SELECT 1 FROM emprestimo WHERE emprestimo.id_exemplar = exemplar.id_exemplar "
                + "AND emprestimo.data_devolucao IS NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(liberarExemplaresSemEmprestimo)) {
            stmt.executeUpdate();
        }
    }

    public void sincronizarSituacoes() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                sincronizarSituacoes(conn);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    private static void validarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getIdExemplar() <= 0
                || emprestimo.getIdLeitor() <= 0
                || emprestimo.getDataEmprestimo() == null
                || emprestimo.getDataPrevista() == null) {
            throw new IllegalArgumentException("Informe leitor, exemplar e as duas datas.");
        }
        validarDatasEmprestimo(emprestimo.getDataEmprestimo(), emprestimo.getDataPrevista());
    }

    private static void validarDatasEmprestimo(
            LocalDate dataEmprestimo,
            LocalDate dataPrevista
    ) {
        if (dataEmprestimo == null || dataPrevista == null) {
            throw new IllegalArgumentException("Informe as duas datas do empréstimo.");
        }
        if (dataEmprestimo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do empréstimo não pode estar no futuro.");
        }
        if (dataPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException(
                    "A data prevista não pode ser anterior à data do empréstimo."
            );
        }
    }

    private static EmprestimoRelatorio mapearRelatorio(ResultSet rs) throws SQLException {
        Date dataDevolucao = rs.getDate("data_devolucao");
        return new EmprestimoRelatorio(
                rs.getInt("id_emprestimo"),
                rs.getString("nome"),
                rs.getString("titulo"),
                rs.getString("codigo"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_prevista").toLocalDate(),
                dataDevolucao == null ? null : dataDevolucao.toLocalDate(),
                rs.getString("situacao")
        );
    }

    private static void rollback(Connection conn, Exception original) {
        try {
            conn.rollback();
        } catch (SQLException rollbackError) {
            original.addSuppressed(rollbackError);
        }
    }
}
