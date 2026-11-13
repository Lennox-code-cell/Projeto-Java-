
package br.com.biblioteca.dao;

// Responsável por acessar e persistir os dados dos exemplares no banco.
import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Exemplar;

// Importações para trabalhar com o banco de dados.
import java.sql.*;

// Importações para trabalhar com listas.
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO {

    // Cadastra um novo exemplar no banco de dados.
    public void salvar(Exemplar exemplar) throws SQLException {
        validarDadosNovo(exemplar);

        // Comando SQL para inserir um exemplar.
        // Os pontos de interrogação serão substituídos pelos valores.
        String sql = "INSERT INTO exemplar (id_livro, codigo, situacao) VALUES (?, ?, ?)";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o ID do livro ao qual o exemplar pertence.
            stmt.setInt(1, exemplar.getIdLivro());

            // Define o código de identificação do exemplar.
            stmt.setString(2, exemplar.getCodigo());

            // Define a situação do exemplar.
            // Se estiver nula ou vazia, utiliza "disponivel" como padrão.
            stmt.setString(3,
                    exemplar.getSituacao() == null || exemplar.getSituacao().isBlank()
                            ? "disponivel"
                            : exemplar.getSituacao()
            );

            // Executa o INSERT no banco de dados.
            stmt.executeUpdate();
        }
    }

    // Retorna todos os exemplares cadastrados no banco.
    public List<Exemplar> listarTodos() throws SQLException {
        new EmprestimoDAO().sincronizarSituacoes();

        // Cria uma lista vazia para armazenar os exemplares encontrados.
        List<Exemplar> exemplares = new ArrayList<>();

        // Consulta todos os exemplares, ordenados pelo código.
        String sql = "SELECT * FROM exemplar ORDER BY codigo";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre cada registro retornado pela consulta.
            while (rs.next()) {

                // Cria um objeto Exemplar para representar o registro atual.
                Exemplar exemplar = new Exemplar();

                // Recupera os dados das colunas do banco.
                exemplar.setIdExemplar(rs.getInt("id_exemplar"));
                exemplar.setIdLivro(rs.getInt("id_livro"));
                exemplar.setCodigo(rs.getString("codigo"));
                exemplar.setSituacao(rs.getString("situacao"));

                // Adiciona o objeto à lista.
                exemplares.add(exemplar);
            }
        }

        // Retorna a lista com todos os exemplares encontrados.
        return exemplares;
    }

    // Retorna somente os exemplares que estão disponíveis para empréstimo.
    public List<Exemplar> listarDisponiveis() throws SQLException {
        new EmprestimoDAO().sincronizarSituacoes();

        // Lista que armazenará os exemplares disponíveis.
        List<Exemplar> exemplares = new ArrayList<>();

        // Consulta apenas os registros cuja situação é "disponivel".
        String sql = "SELECT * FROM exemplar ex WHERE ex.situacao = 'disponivel' "
                + "AND NOT EXISTS (SELECT 1 FROM emprestimo e "
                + "WHERE e.id_exemplar = ex.id_exemplar AND e.data_devolucao IS NULL) "
                + "ORDER BY ex.codigo";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre os resultados da consulta.
            while (rs.next()) {

                // Converte o registro atual em um objeto Exemplar.
                Exemplar exemplar = new Exemplar();

                exemplar.setIdExemplar(rs.getInt("id_exemplar"));
                exemplar.setIdLivro(rs.getInt("id_livro"));
                exemplar.setCodigo(rs.getString("codigo"));
                exemplar.setSituacao(rs.getString("situacao"));

                // Adiciona o exemplar disponível à lista.
                exemplares.add(exemplar);
            }
        }

        // Devolve a lista de exemplares disponíveis.
        return exemplares;
    }

    // Verifica se determinado exemplar está disponível para empréstimo.
    public boolean exemplarDisponivel(int idExemplar) throws SQLException {
        new EmprestimoDAO().sincronizarSituacoes();

        // Conta quantos registros correspondem ao ID informado
        // e possuem situação igual a "disponivel".
        String sql = "SELECT 1 FROM exemplar ex WHERE ex.id_exemplar = ? "
                + "AND ex.situacao = 'disponivel' "
                + "AND NOT EXISTS (SELECT 1 FROM emprestimo e "
                + "WHERE e.id_exemplar = ex.id_exemplar AND e.data_devolucao IS NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Substitui o primeiro parâmetro pelo ID do exemplar.
            stmt.setInt(1, idExemplar);

            // Executa a consulta.
            try (ResultSet rs = stmt.executeQuery()) {

                // Verifica se existe um resultado.
                if (rs.next()) {

                    // Se a quantidade for maior que zero, retorna true.
                    // Caso contrário, retorna false.
                    return true;
                }
            }
        }

        // Retorna false caso nenhum resultado seja encontrado.
        return false;
    }

    // Atualiza os dados de um exemplar já cadastrado.
    public void atualizar(Exemplar exemplar) throws SQLException {
        validarDados(exemplar);
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idLivroAtual;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id_livro FROM exemplar WHERE id_exemplar = ? FOR UPDATE")) {
                    stmt.setInt(1, exemplar.getIdExemplar());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Exemplar não encontrado.");
                        }
                        idLivroAtual = rs.getInt("id_livro");
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT 1 FROM emprestimo WHERE id_exemplar = ? "
                                + "AND data_devolucao IS NULL LIMIT 1")) {
                    stmt.setInt(1, exemplar.getIdExemplar());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException(
                                    "Não é possível alterar um exemplar com empréstimo ativo."
                            );
                        }
                    }
                }

                if (idLivroAtual != exemplar.getIdLivro()) {
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "SELECT 1 FROM emprestimo WHERE id_exemplar = ? LIMIT 1")) {
                        stmt.setInt(1, exemplar.getIdExemplar());
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                throw new SQLException(
                                        "Não é possível reassociar um exemplar com histórico de empréstimos."
                                );
                            }
                        }
                    }
                }

                String sql = "UPDATE exemplar SET id_livro = ?, codigo = ?, situacao = ? "
                        + "WHERE id_exemplar = ? AND situacao <> 'emprestado'";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, exemplar.getIdLivro());
                    stmt.setString(2, exemplar.getCodigo());
                    stmt.setString(3, exemplar.getSituacao());
                    stmt.setInt(4, exemplar.getIdExemplar());
                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Não foi possível atualizar o exemplar.");
                    }
                }
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                rollback(conn, ex);
                throw ex;
            }
        }
    }

    // Atualiza somente a situação de um exemplar.
    public void atualizarSituacao(int idExemplar, String situacao) throws SQLException {
        String estado = situacao == null ? ""
                : situacao.trim().toLowerCase(java.util.Locale.ROOT);
        if (!estado.equals("disponivel") && !estado.equals("indisponivel")) {
            throw new IllegalArgumentException("Situação de exemplar inválida.");
        }
        String sql = "UPDATE exemplar SET situacao = ? WHERE id_exemplar = ? "
                + "AND situacao <> 'emprestado' "
                + "AND NOT EXISTS (SELECT 1 FROM emprestimo "
                + "WHERE emprestimo.id_exemplar = exemplar.id_exemplar "
                + "AND emprestimo.data_devolucao IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idExemplar);
            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Exemplar inexistente ou com empréstimo ativo.");
            }
        }
    }

    // Exclui um exemplar do banco de dados pelo seu ID.
    public void excluir(int idExemplar) throws SQLException {

        // Comando SQL para remover o exemplar.
        String sql = "DELETE FROM exemplar WHERE id_exemplar = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o ID do exemplar que será excluído.
            stmt.setInt(1, idExemplar);

            // Executa o DELETE.
            stmt.executeUpdate();
        }
    }

    private static void validarDados(Exemplar exemplar) {
        if (exemplar == null || exemplar.getIdExemplar() <= 0
                || exemplar.getIdLivro() <= 0 || exemplar.getCodigo() == null
                || exemplar.getCodigo().isBlank()
                || exemplar.getSituacao() == null
                || !(exemplar.getSituacao().equals("disponivel")
                || exemplar.getSituacao().equals("indisponivel"))) {
            throw new IllegalArgumentException("Dados do exemplar inválidos.");
        }
    }

    private static void validarDadosNovo(Exemplar exemplar) {
        if (exemplar == null || exemplar.getIdLivro() <= 0
                || exemplar.getCodigo() == null || exemplar.getCodigo().isBlank()
                || exemplar.getSituacao() == null
                || !(exemplar.getSituacao().equals("disponivel")
                || exemplar.getSituacao().equals("indisponivel"))) {
            throw new IllegalArgumentException("Dados do exemplar inválidos.");
        }
    }

    private static void rollback(Connection conn, Exception original) {
        try {
            conn.rollback();
        } catch (SQLException rollbackError) {
            original.addSuppressed(rollbackError);
        }
    }
}
