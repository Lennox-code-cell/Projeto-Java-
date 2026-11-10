
package br.com.biblioteca.model;

// Importa a classe LocalDate para trabalhar com datas.
import java.time.LocalDate;

/**
 * Representa o registro de um empréstimo realizado pela biblioteca.
 *
 * O objeto acompanha as datas e o estado atual da operação,
 * desde a realização do empréstimo até a devolução do exemplar.
 */
public class Emprestimo {

    // Identificador único do empréstimo no banco de dados.
    private int idEmprestimo;

    // Identificador do exemplar que foi emprestado.
    private int idExemplar;

    // Identificador do leitor que realizou o empréstimo.
    private int idLeitor;

    // Data em que o empréstimo foi realizado.
    private LocalDate dataEmprestimo;

    // Data limite prevista para a devolução do exemplar.
    private LocalDate dataPrevista;

    // Data em que o exemplar foi efetivamente devolvido.
    // Pode permanecer null enquanto o empréstimo estiver aberto.
    private LocalDate dataDevolucao;

    // Situação atual do empréstimo.
    // Exemplos: Ativo, Devolvido ou Atrasado.
    private String situacao;

    /**
     * Construtor vazio.
     * Permite criar um empréstimo e preencher os dados posteriormente.
     */
    public Emprestimo() {
    }

    /**
     * Construtor completo.
     * Permite criar um empréstimo informando todos os seus atributos.
     *
     * @param idEmprestimo identificador do empréstimo
     * @param idExemplar identificador do exemplar emprestado
     * @param idLeitor identificador do leitor
     * @param dataEmprestimo data em que o empréstimo foi realizado
     * @param dataPrevista data prevista para a devolução
     * @param dataDevolucao data efetiva da devolução
     * @param situacao situação atual do empréstimo
     */
    public Emprestimo(int idEmprestimo, int idExemplar, int idLeitor,
                      LocalDate dataEmprestimo, LocalDate dataPrevista,
                      LocalDate dataDevolucao, String situacao) {

        this.idEmprestimo = idEmprestimo;
        this.idExemplar = idExemplar;
        this.idLeitor = idLeitor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.dataDevolucao = dataDevolucao;
        this.situacao = situacao;
    }

    // Retorna o identificador do empréstimo.
    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    // Define o identificador do empréstimo.
    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    // Retorna o identificador do exemplar emprestado.
    public int getIdExemplar() {
        return idExemplar;
    }

    // Define o identificador do exemplar.
    public void setIdExemplar(int idExemplar) {
        this.idExemplar = idExemplar;
    }

    // Retorna o identificador do leitor.
    public int getIdLeitor() {
        return idLeitor;
    }

    // Define o identificador do leitor.
    public void setIdLeitor(int idLeitor) {
        this.idLeitor = idLeitor;
    }

    // Retorna a data em que o empréstimo foi realizado.
    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    // Define a data do empréstimo.
    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    // Retorna a data prevista para a devolução.
    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    // Define a data prevista para a devolução.
    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    // Retorna a data em que o exemplar foi devolvido.
    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    // Define a data efetiva da devolução.
    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    // Retorna a situação atual do empréstimo.
    public String getSituacao() {
        return situacao;
    }

    // Define ou atualiza a situação do empréstimo.
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
