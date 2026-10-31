package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Emprestimo.
 */

import br.com.biblioteca.dao.EmprestimoDAO;
import br.com.biblioteca.dao.ExemplarDAO;
import br.com.biblioteca.model.Emprestimo;

import java.sql.SQLException;
import java.time.LocalDate;

public class EmprestimoController {
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final ExemplarDAO exemplarDAO = new ExemplarDAO();

    public void registrarEmprestimo(int idExemplar, int idLeitor, LocalDate dataEmprestimo, LocalDate dataPrevista) throws SQLException {
        if (dataPrevista == null || dataEmprestimo == null) {
            throw new IllegalArgumentException("As datas do empréstimo são obrigatórias.");
        }
        if (dataPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A data prevista não pode ser anterior à data do empréstimo.");
        }
        if (!exemplarDAO.exemplarDisponivel(idExemplar)) {
            throw new IllegalArgumentException("Exemplar indisponível para empréstimo.");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setIdExemplar(idExemplar);
        emprestimo.setIdLeitor(idLeitor);
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataPrevista(dataPrevista);
        emprestimo.setDataDevolucao(null);
        emprestimo.setSituacao("ativo");
        emprestimoDAO.registrarEmprestimo(emprestimo);
    }

    public void registrarDevolucao(int idEmprestimo, LocalDate dataDevolucao) throws SQLException {
        if (dataDevolucao == null) {
            throw new IllegalArgumentException("A data da devolução é obrigatória.");
        }
        emprestimoDAO.registrarDevolucao(idEmprestimo, dataDevolucao);
    }
}
