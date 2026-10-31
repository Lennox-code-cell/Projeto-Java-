package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Livro.
 */

import br.com.biblioteca.dao.LivroDAO;
import br.com.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class LivroController {
    private final LivroDAO livroDAO = new LivroDAO();

    public void cadastrar(String titulo, String isbn, int anoPublicacao, String editora, int idAutor, int idCategoria) throws SQLException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }

        Livro livro = new Livro();
        livro.setTitulo(titulo.trim());
        livro.setIsbn(isbn != null ? isbn.trim() : "");
        livro.setAnoPublicacao(anoPublicacao);
        livro.setEditora(editora != null ? editora.trim() : "");
        livro.setIdAutor(idAutor);
        livro.setIdCategoria(idCategoria);
        livroDAO.salvar(livro);
    }

    public List<Livro> listarTodos() throws SQLException {
        return livroDAO.listarTodos();
    }

    public void atualizar(int idLivro, String titulo, String isbn, int anoPublicacao, String editora, int idAutor, int idCategoria) throws SQLException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }

        Livro livro = new Livro();
        livro.setIdLivro(idLivro);
        livro.setTitulo(titulo.trim());
        livro.setIsbn(isbn != null ? isbn.trim() : "");
        livro.setAnoPublicacao(anoPublicacao);
        livro.setEditora(editora != null ? editora.trim() : "");
        livro.setIdAutor(idAutor);
        livro.setIdCategoria(idCategoria);
        livroDAO.atualizar(livro);
    }

    public void excluir(int idLivro) throws SQLException {
        livroDAO.excluir(idLivro);
    }
}
