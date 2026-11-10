
package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas aos exemplares.
 * É responsável por organizar e validar as informações
 * antes de encaminhá-las ao DAO.
 */

// Importa o DAO responsável pelo acesso aos dados dos exemplares.
import br.com.biblioteca.dao.ExemplarDAO;

// Importa o modelo que representa um exemplar de livro.
import br.com.biblioteca.model.Exemplar;

// Importa SQLException para tratar possíveis erros no banco de dados.
import java.sql.SQLException;

// Importa List para trabalhar com listas de exemplares.
import java.util.List;

public class ExemplarController {

    // Cria um objeto ExemplarDAO para realizar operações no banco de dados.
    private final ExemplarDAO exemplarDAO = new ExemplarDAO();

    /**
     * Cadastra um novo exemplar no sistema.
     *
     * @param idLivro Identificador do livro ao qual o exemplar pertence.
     * @param codigo Código único de identificação do exemplar.
     * @param situacao Estado atual do exemplar.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void cadastrar(int idLivro, String codigo, String situacao)
            throws SQLException {

        validarDados(idLivro, codigo, situacao);

        // Cria um novo objeto da classe Exemplar.
        Exemplar exemplar = new Exemplar();

        // Define a qual livro esse exemplar pertence.
        exemplar.setIdLivro(idLivro);

        // Define o código de identificação, removendo espaços extras.
        exemplar.setCodigo(codigo.trim());

        // Define a situação do exemplar.
        // Se nenhuma situação for informada, utiliza "disponivel".
        exemplar.setSituacao(normalizarSituacao(situacao));

        // Envia o objeto ao DAO para salvar no banco de dados.
        exemplarDAO.salvar(exemplar);
    }

    /**
     * Lista todos os exemplares cadastrados.
     *
     * @return Lista contendo os exemplares encontrados.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Exemplar> listarTodos() throws SQLException {

        // Solicita ao DAO todos os exemplares cadastrados no banco.
        return exemplarDAO.listarTodos();
    }

    /**
     * Atualiza as informações de um exemplar existente.
     *
     * @param idExemplar Identificador do exemplar que será atualizado.
     * @param idLivro Novo livro associado ao exemplar.
     * @param codigo Novo código de identificação.
     * @param situacao Nova situação do exemplar.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void atualizar(
            int idExemplar,
            int idLivro,
            String codigo,
            String situacao
    ) throws SQLException {
        if (idExemplar <= 0) {
            throw new IllegalArgumentException("Selecione um exemplar válido.");
        }
        validarDados(idLivro, codigo, situacao);

        // Cria um objeto Exemplar para armazenar os novos dados.
        Exemplar exemplar = new Exemplar();

        // Define o ID do exemplar que será atualizado.
        exemplar.setIdExemplar(idExemplar);

        // Define o livro ao qual o exemplar estará associado.
        exemplar.setIdLivro(idLivro);

        // Define o novo código, removendo espaços extras.
        exemplar.setCodigo(codigo.trim());

        // Define a nova situação.
        // Caso seja nula, utiliza "disponivel".
        exemplar.setSituacao(normalizarSituacao(situacao));

        // Envia o objeto ao DAO para atualizar os dados no banco.
        exemplarDAO.atualizar(exemplar);
    }

    /**
     * Exclui um exemplar do sistema.
     *
     * @param idExemplar Identificador do exemplar que será excluído.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idExemplar) throws SQLException {

        // Solicita ao DAO a exclusão do exemplar pelo seu identificador.
        exemplarDAO.excluir(idExemplar);
    }

    public List<Exemplar> listarDisponiveis() throws SQLException {
        return exemplarDAO.listarDisponiveis();
    }

    private void validarDados(int idLivro, String codigo, String situacao) {
        if (idLivro <= 0) {
            throw new IllegalArgumentException("Selecione um livro válido.");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código do exemplar é obrigatório.");
        }
        normalizarSituacao(situacao);
    }

    private String normalizarSituacao(String situacao) {
        String valor = situacao == null || situacao.isBlank()
                ? "disponivel"
                : situacao.trim().toLowerCase(java.util.Locale.ROOT);
        if (!valor.equals("disponivel") && !valor.equals("indisponivel")) {
            throw new IllegalArgumentException(
                    "A situação deve ser disponível ou indisponível. Empréstimos são controlados pelo sistema."
            );
        }
        return valor;
    }
}
