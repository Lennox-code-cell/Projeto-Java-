
package br.com.biblioteca.model;

/**
 * Representa uma cópia física de um livro disponível no acervo.
 *
 * Cada exemplar possui um código de identificação e uma situação
 * própria, permitindo controlar individualmente cada cópia.
 */
public class Exemplar {

    // Identificador único do exemplar no banco de dados.
    private int idExemplar;

    // Identificador do livro ao qual este exemplar pertence.
    private int idLivro;

    // Código utilizado para identificar fisicamente o exemplar.
    private String codigo;

    // Situação atual do exemplar.
    // Exemplos: Disponivel, Emprestado ou Indisponivel.
    private String situacao;

    /**
     * Construtor vazio.
     * Permite criar um objeto Exemplar sem preencher os atributos inicialmente.
     */
    public Exemplar() {
    }

    /**
     * Construtor completo.
     * Permite criar um exemplar informando todos os seus atributos.
     *
     * @param idExemplar identificador do exemplar
     * @param idLivro identificador do livro relacionado
     * @param codigo código de identificação do exemplar
     * @param situacao situação atual do exemplar
     */
    public Exemplar(int idExemplar, int idLivro, String codigo, String situacao) {
        this.idExemplar = idExemplar;
        this.idLivro = idLivro;
        this.codigo = codigo;
        this.situacao = situacao;
    }

    // Retorna o identificador do exemplar.
    public int getIdExemplar() {
        return idExemplar;
    }

    // Define ou altera o identificador do exemplar.
    public void setIdExemplar(int idExemplar) {
        this.idExemplar = idExemplar;
    }

    // Retorna o identificador do livro relacionado ao exemplar.
    public int getIdLivro() {
        return idLivro;
    }

    // Define ou altera o livro ao qual o exemplar pertence.
    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    // Retorna o código de identificação do exemplar.
    public String getCodigo() {
        return codigo;
    }

    // Define ou altera o código do exemplar.
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // Retorna a situação atual do exemplar.
    public String getSituacao() {
        return situacao;
    }

    // Define ou altera a situação do exemplar.
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * Retorna o código do exemplar quando o objeto é convertido
     * para String, facilitando sua exibição na interface gráfica.
     *
     * Caso o código seja null, retorna "Exemplar".
     */
    @Override
    public String toString() {
        return codigo != null ? codigo : "Exemplar";
    }
}
