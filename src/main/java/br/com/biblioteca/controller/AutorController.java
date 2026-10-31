package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Autor.
 */

import br.com.biblioteca.dao.AutorDAO;
import br.com.biblioteca.model.Autor;

import java.sql.SQLException;
import java.util.List;

public class AutorController {
    private final AutorDAO autorDAO = new AutorDAO();

    public void cadastrar(String nome, String nacionalidade) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do autor é obrigatório.");
        }

        Autor autor = new Autor();
        autor.setNome(nome.trim());
        autor.setNacionalidade(nacionalidade != null ? nacionalidade.trim() : "");
        autorDAO.salvar(autor);
    }

    public List<Autor> listarTodos() throws SQLException {
        return autorDAO.listarTodos();
    }

    public void atualizar(int idAutor, String nome, String nacionalidade) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do autor é obrigatório.");
        }

        Autor autor = new Autor();
        autor.setIdAutor(idAutor);
        autor.setNome(nome.trim());
        autor.setNacionalidade(nacionalidade != null ? nacionalidade.trim() : "");
        autorDAO.atualizar(autor);
    }

    public void excluir(int idAutor) throws SQLException {
        autorDAO.excluir(idAutor);
    }
}
