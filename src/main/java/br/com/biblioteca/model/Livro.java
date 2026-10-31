package br.com.biblioteca.model;

/**
 * Representa um livro do acervo da biblioteca.
 * A classe guarda os dados do título, edição e os identificadores de autor e categoria.
 */
public class Livro {
    private int idLivro;
    private String titulo;
    private String isbn;
    private int anoPublicacao;
    private String editora;
    private int idAutor;
    private int idCategoria;

    public Livro() {
    }

    public Livro(int idLivro, String titulo, String isbn, int anoPublicacao, String editora, int idAutor, int idCategoria) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return titulo != null ? titulo : "Livro";
    }
}
