
package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas aos leitores.
 * É responsável por validar os dados e encaminhá-los
 * para a camada DAO realizar as operações no banco.
 */

// Importa o DAO responsável pelo acesso aos dados dos leitores.
import br.com.biblioteca.dao.LeitorDAO;

// Importa o modelo que representa um leitor.
import br.com.biblioteca.model.Leitor;
import br.com.biblioteca.util.Validacoes;

// Importa SQLException para tratar erros relacionados ao banco.
import java.sql.SQLException;

// Importa List para trabalhar com listas de leitores.
import java.util.List;

public class LeitorController {

    // Cria um objeto LeitorDAO para acessar o banco de dados.
    private final LeitorDAO leitorDAO = new LeitorDAO();

    /**
     * Cadastra um novo leitor no sistema.
     *
     * @param nome Nome completo do leitor.
     * @param cpf CPF do leitor.
     * @param telefone Telefone para contato.
     * @param email Endereço de e-mail.
     * @param endereco Endereço residencial.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void cadastrar(
            String nome,
            String cpf,
            String telefone,
            String email,
            String endereco
    ) throws SQLException {

        // Verifica se o nome foi informado ou se está vazio.
        if (nome == null || nome.trim().isEmpty()) {

            // Impede o cadastro caso o nome não seja preenchido.
            throw new IllegalArgumentException(
                    "O nome do leitor é obrigatório."
            );
        }
        String cpfNormalizado = Validacoes.normalizarCpf(cpf);
        Validacoes.validarTelefone(telefone);
        Validacoes.validarEmail(email);

        // Cria um novo objeto da classe Leitor.
        Leitor leitor = new Leitor();

        // Define o nome do leitor, removendo espaços extras.
        leitor.setNome(nome.trim());

        leitor.setCpf(cpfNormalizado);

        // Define o telefone ou uma String vazia caso seja nulo.
        leitor.setTelefone(telefone != null ? telefone.trim() : "");

        // Define o e-mail ou uma String vazia caso seja nulo.
        leitor.setEmail(email != null ? email.trim() : "");

        // Define o endereço ou uma String vazia caso seja nulo.
        leitor.setEndereco(endereco != null ? endereco.trim() : "");

        // Define o leitor como ativo no momento do cadastro.
        leitor.setAtivo(true);

        // Envia o objeto ao DAO para salvar no banco de dados.
        leitorDAO.salvar(leitor);
    }

    /**
     * Lista todos os leitores cadastrados.
     *
     * @return Lista contendo os leitores encontrados.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Leitor> listarTodos() throws SQLException {

        // Solicita ao DAO a consulta de todos os leitores.
        return leitorDAO.listarTodos();
    }

    /**
     * Atualiza as informações de um leitor existente.
     *
     * @param idLeitor Identificador do leitor.
     * @param nome Novo nome do leitor.
     * @param cpf Novo CPF.
     * @param telefone Novo telefone.
     * @param email Novo e-mail.
     * @param endereco Novo endereço.
     * @param ativo Define se o leitor continuará ativo.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void atualizar(
            int idLeitor,
            String nome,
            String cpf,
            String telefone,
            String email,
            String endereco,
            boolean ativo
    ) throws SQLException {

        // Verifica se o nome foi informado corretamente.
        if (nome == null || nome.trim().isEmpty()) {

            // Impede a atualização caso o nome esteja vazio.
            throw new IllegalArgumentException(
                    "O nome do leitor é obrigatório."
            );
        }
        if (idLeitor <= 0) {
            throw new IllegalArgumentException("Selecione um leitor válido.");
        }
        String cpfNormalizado = Validacoes.normalizarCpf(cpf);
        Validacoes.validarTelefone(telefone);
        Validacoes.validarEmail(email);

        // Cria um objeto Leitor para armazenar os novos dados.
        Leitor leitor = new Leitor();

        // Define o ID do leitor que será atualizado.
        leitor.setIdLeitor(idLeitor);

        // Define o novo nome, removendo espaços extras.
        leitor.setNome(nome.trim());

        leitor.setCpf(cpfNormalizado);

        // Atualiza o telefone ou utiliza uma String vazia se for nulo.
        leitor.setTelefone(telefone != null ? telefone.trim() : "");

        // Atualiza o e-mail ou utiliza uma String vazia se for nulo.
        leitor.setEmail(email != null ? email.trim() : "");

        // Atualiza o endereço ou utiliza uma String vazia se for nulo.
        leitor.setEndereco(endereco != null ? endereco.trim() : "");

        // Define se o leitor estará ativo ou inativo.
        leitor.setAtivo(ativo);

        // Envia os novos dados ao DAO para atualizar no banco.
        leitorDAO.atualizar(leitor);
    }

    /**
     * Exclui um leitor do sistema.
     *
     * @param idLeitor Identificador do leitor que será excluído.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idLeitor) throws SQLException {

        // Solicita ao DAO a exclusão do leitor pelo seu identificador.
        leitorDAO.excluir(idLeitor);
    }
}
