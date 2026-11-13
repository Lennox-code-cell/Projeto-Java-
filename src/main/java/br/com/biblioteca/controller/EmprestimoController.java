
package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas aos empréstimos.
 * É responsável por validar as informações e coordenar
 * o registro de empréstimos e devoluções.
 */

// Importa o DAO responsável pelas operações de empréstimo no banco.
import br.com.biblioteca.dao.EmprestimoDAO;

// Importa o modelo que representa um empréstimo.
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.EmprestimoRelatorio;
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Leitor;

import br.com.biblioteca.dao.LeitorDAO;

// Importa SQLException para tratar erros relacionados ao banco.
import java.sql.SQLException;

// Importa LocalDate para trabalhar com datas sem horário.
import java.time.LocalDate;
import java.util.List;

public class EmprestimoController {

    // Cria o objeto responsável por acessar os dados dos empréstimos.
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final ExemplarController exemplarController = new ExemplarController();

    /**
     * Registra um novo empréstimo de livro.
     *
     * @param idExemplar Identificador do exemplar que será emprestado.
     * @param idLeitor Identificador do leitor que realizará o empréstimo.
     * @param dataEmprestimo Data em que o empréstimo será realizado.
     * @param dataPrevista Data prevista para a devolução.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void registrarEmprestimo(
            int idExemplar,
            int idLeitor,
            LocalDate dataEmprestimo,
            LocalDate dataPrevista
    ) throws SQLException {

        // Verifica se as datas do empréstimo foram informadas.
        if (dataPrevista == null || dataEmprestimo == null) {

            // Impede o registro caso alguma das datas esteja vazia.
            throw new IllegalArgumentException(
                    "As datas do empréstimo são obrigatórias."
            );
        }
        if (idExemplar <= 0 || idLeitor <= 0) {
            throw new IllegalArgumentException("Selecione um leitor e um exemplar válidos.");
        }
        if (dataEmprestimo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do empréstimo não pode estar no futuro.");
        }

        // Verifica se a data prevista é anterior à data do empréstimo.
        if (dataPrevista.isBefore(dataEmprestimo)) {

            // Impede o empréstimo caso as datas estejam incorretas.
            throw new IllegalArgumentException(
                    "A data prevista não pode ser anterior à data do empréstimo."
            );
        }

        // Cria um novo objeto para representar o empréstimo.
        Emprestimo emprestimo = new Emprestimo();

        // Define qual exemplar será emprestado.
        emprestimo.setIdExemplar(idExemplar);

        // Define qual leitor ficará responsável pelo exemplar.
        emprestimo.setIdLeitor(idLeitor);

        // Define a data em que o empréstimo foi realizado.
        emprestimo.setDataEmprestimo(dataEmprestimo);

        // Define a data prevista para a devolução.
        emprestimo.setDataPrevista(dataPrevista);

        // Define a data de devolução como nula,
        // pois o livro ainda não foi devolvido.
        emprestimo.setDataDevolucao(null);

        // Define a situação inicial do empréstimo como ativo.
        emprestimo.setSituacao("ativo");

        // Envia o objeto ao DAO para registrar o empréstimo no banco.
        emprestimoDAO.registrarEmprestimo(emprestimo);
    }

    /**
     * Registra a devolução de um exemplar.
     *
     * @param idEmprestimo Identificador do empréstimo.
     * @param dataDevolucao Data em que o exemplar foi devolvido.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void registrarDevolucao(
            int idEmprestimo,
            LocalDate dataDevolucao
    ) throws SQLException {

        // Verifica se a data da devolução foi informada.
        if (dataDevolucao == null) {

            // Impede a devolução caso a data não tenha sido preenchida.
            throw new IllegalArgumentException(
                    "A data da devolução é obrigatória."
            );
        }

        // Envia o ID do empréstimo e a data ao DAO,
        // que ficará responsável por atualizar o registro no banco.
        emprestimoDAO.registrarDevolucao(idEmprestimo, dataDevolucao);
    }

    public void atualizarEmprestimo(
            int idEmprestimo,
            LocalDate dataEmprestimo,
            LocalDate dataPrevista
    ) throws SQLException {
        if (idEmprestimo <= 0) {
            throw new IllegalArgumentException("Selecione um empréstimo válido.");
        }
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

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setIdEmprestimo(idEmprestimo);
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataPrevista(dataPrevista);
        emprestimoDAO.atualizar(emprestimo);
    }

    public void excluirEmprestimo(int idEmprestimo) throws SQLException {
        if (idEmprestimo <= 0) {
            throw new IllegalArgumentException("Selecione um empréstimo válido.");
        }
        emprestimoDAO.excluir(idEmprestimo);
    }

    public List<Leitor> listarLeitoresAtivos() throws SQLException {
        return leitorDAO.listarAtivos();
    }

    public List<Exemplar> listarExemplaresDisponiveis() throws SQLException {
        return exemplarController.listarDisponiveis();
    }

    public List<EmprestimoRelatorio> listarEmprestimosAtivos() throws SQLException {
        return emprestimoDAO.listarAtivos();
    }

    public List<EmprestimoRelatorio> listarTodosEmprestimos() throws SQLException {
        return emprestimoDAO.listarTodosDetalhados();
    }
}
