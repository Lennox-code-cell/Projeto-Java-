package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Categoria.
 */

import br.com.biblioteca.dao.CategoriaDAO;
import br.com.biblioteca.model.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaController {
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void cadastrar(String nome, String descricao) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome.trim());
        categoria.setDescricao(descricao != null ? descricao.trim() : "");
        categoriaDAO.salvar(categoria);
    }

    public List<Categoria> listarTodos() throws SQLException {
        return categoriaDAO.listarTodos();
    }

    public void atualizar(int idCategoria, String nome, String descricao) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(idCategoria);
        categoria.setNome(nome.trim());
        categoria.setDescricao(descricao != null ? descricao.trim() : "");
        categoriaDAO.atualizar(categoria);
    }

    public void excluir(int idCategoria) throws SQLException {
        categoriaDAO.excluir(idCategoria);
    }
}
