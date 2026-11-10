package br.com.biblioteca.model;

import java.time.LocalDate;

public record EmprestimoRelatorio(
        int idEmprestimo,
        String nomeLeitor,
        String tituloLivro,
        String codigoExemplar,
        LocalDate dataEmprestimo,
        LocalDate dataPrevista,
        LocalDate dataDevolucao,
        String situacao
) {
}
