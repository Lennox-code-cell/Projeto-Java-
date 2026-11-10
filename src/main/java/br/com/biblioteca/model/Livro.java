
package br.com.biblioteca.model;

/**
 * Representa um livro cadastrado no acervo da biblioteca.
 *
 * A classe armazena os dados bibliográficos da obra,
 * além dos identificadores do autor e da categoria relacionados.
 */
public class Livro {

    // Identificador único do livro no banco de dados.
    private int idLivro;

    // Título da obra bibliográfica.
    private String titulo;

    // Código ISBN utilizado para identificar a publicação.
    private String isbn;

    // Ano em que o livro foi publicado.
    private int anoPublicacao;

    // Nome da editora responsável pela publicação.
    private String editora;

    // Identificador do autor relacionado ao livro.
    private int idAutor;

    // Identificador da categoria à qual o livro pertence.
    private int idCategoria;

    /**
     * Construtor vazio.
     * Permite criar um objeto Livro e preencher os atributos posteriormente.
     */
    public Livro() {
    }

    /**
     * Construtor completo.
     * Permite criar um livro informando todos os seus atributos.
     *
     * @param idLivro identificador do livro
     * @param titulo título da obra
     * @param isbn código ISBN
     * @param anoPublicacao ano de publicação
     * @param editora nome da editora
     * @param idAutor identificador do autor
     * @param idCategoria identificador da categoria
     */
    public Livro(int idLivro, String titulo, String isbn, int anoPublicacao,
                 String editora, int idAutor, int idCategoria) {

        this.idLivro = idLivro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    // Retorna o identificador do livro.
    public int getIdLivro() {
        return idLivro;
    }

    // Define ou altera o identificador do livro.
    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    // Retorna o título do livro.
    public String getTitulo() {
        return titulo;
    }

    // Define ou altera o título do livro.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Retorna o ISBN do livro.
    public String getIsbn() {
        return isbn;
    }

    // Define ou altera o ISBN do livro.
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Retorna o ano de publicação.
    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    // Define ou altera o ano de publicação.
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    // Retorna o nome da editora.
    public String getEditora() {
        return editora;
    }

    // Define ou altera o nome da editora.
    public void setEditora(String editora) {
        this.editora = editora;
    }

    // Retorna o identificador do autor.
    public int getIdAutor() {
        return idAutor;
    }

    // Define ou altera o autor relacionado ao livro.
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    // Retorna o identificador da categoria.
    public int getIdCategoria() {
        return idCategoria;
    }

    // Define ou altera a categoria do livro.
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Retorna o título do livro quando o objeto é convertido
     * para String, facilitando sua exibição em componentes Swing.
     *
     * Caso o título seja null, retorna "Livro".
     */
    @Override
    public String toString() {
        return titulo != null ? titulo : "Livro";
    }
}
