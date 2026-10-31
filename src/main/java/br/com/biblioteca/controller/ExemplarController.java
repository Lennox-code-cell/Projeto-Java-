package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Exemplar.
 */

import br.com.biblioteca.dao.ExemplarDAO;
import br.com.biblioteca.model.Exemplar;

import java.sql.SQLException;
import java.util.List;

public class ExemplarController {
    private final ExemplarDAO exemplarDAO = new ExemplarDAO();

    public void cadastrar(int idLivro, String codigo, String situacao) throws SQLException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código do exemplar é obrigatório.");
        }

        Exemplar exemplar = new Exemplar();
        exemplar.setIdLivro(idLivro);
        exemplar.setCodigo(codigo.trim());
        exemplar.setSituacao(situacao != null ? situacao.trim() : "disponivel");
        exemplarDAO.salvar(exemplar);
    }

    public List<Exemplar> listarTodos() throws SQLException {
        return exemplarDAO.listarTodos();
    }

    public void atualizar(int idExemplar, int idLivro, String codigo, String situacao) throws SQLException {
        Exemplar exemplar = new Exemplar();
        exemplar.setIdExemplar(idExemplar);
        exemplar.setIdLivro(idLivro);
        exemplar.setCodigo(codigo.trim());
        exemplar.setSituacao(situacao != null ? situacao.trim() : "disponivel");
        exemplarDAO.atualizar(exemplar);
    }

    public void excluir(int idExemplar) throws SQLException {
        exemplarDAO.excluir(idExemplar);
    }
}
