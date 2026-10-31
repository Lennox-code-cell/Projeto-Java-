package br.com.biblioteca.model;

import java.time.LocalDate;

/**
 * Representa o registro de um empréstimo realizado pela biblioteca.
 * O objeto acompanha as datas e o estado atual da operação.
 */
public class Emprestimo {
    private int idEmprestimo;
    private int idExemplar;
    private int idLeitor;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevista;
    private LocalDate dataDevolucao;
    private String situacao;

    public Emprestimo() {
    }

    public Emprestimo(int idEmprestimo, int idExemplar, int idLeitor, LocalDate dataEmprestimo,
                     LocalDate dataPrevista, LocalDate dataDevolucao, String situacao) {
        this.idEmprestimo = idEmprestimo;
        this.idExemplar = idExemplar;
        this.idLeitor = idLeitor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.dataDevolucao = dataDevolucao;
        this.situacao = situacao;
    }

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public int getIdExemplar() {
        return idExemplar;
    }

    public void setIdExemplar(int idExemplar) {
        this.idExemplar = idExemplar;
    }

    public int getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(int idLeitor) {
        this.idLeitor = idLeitor;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
