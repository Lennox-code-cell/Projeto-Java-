package br.com.biblioteca.model;

import java.time.LocalDate;

public enum SituacaoEmprestimo {
    ATIVO("ativo"),
    ATRASADO("atrasado"),
    DEVOLVIDO("devolvido");

    private final String valorBanco;

    SituacaoEmprestimo(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public String valorBanco() {
        return valorBanco;
    }

    public static SituacaoEmprestimo calcular(
            LocalDate dataPrevista,
            LocalDate dataDevolucao,
            LocalDate hoje
    ) {
        if (dataPrevista == null || hoje == null) {
            throw new IllegalArgumentException("Data prevista e data atual são obrigatórias.");
        }
        if (dataDevolucao != null) {
            return DEVOLVIDO;
        }
        return dataPrevista.isBefore(hoje) ? ATRASADO : ATIVO;
    }
}
