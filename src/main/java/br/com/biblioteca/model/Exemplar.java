package br.com.biblioteca.model;

/**
 * Representa uma cópia física de um livro disponível para empréstimo.
 * Cada exemplar possui um código e status próprio dentro do acervo.
 */
public class Exemplar {
    private int idExemplar;
    private int idLivro;
    private String codigo;
    private String situacao;

    public Exemplar() {
    }

    public Exemplar(int idExemplar, int idLivro, String codigo, String situacao) {
        this.idExemplar = idExemplar;
        this.idLivro = idLivro;
        this.codigo = codigo;
        this.situacao = situacao;
    }

    public int getIdExemplar() {
        return idExemplar;
    }

    public void setIdExemplar(int idExemplar) {
        this.idExemplar = idExemplar;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return codigo != null ? codigo : "Exemplar";
    }
}
