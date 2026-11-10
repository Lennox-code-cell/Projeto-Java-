
package br.com.biblioteca.model;

/**
 * Representa uma categoria de livros utilizada para organizar
 * e classificar o acervo bibliográfico da biblioteca.
 */
public class Categoria {

    // Identificador único da categoria no banco de dados.
    private int idCategoria;

    // Nome da categoria, por exemplo: Romance, Ficção ou História.
    private String nome;

    // Descrição com informações adicionais sobre a categoria.
    private String descricao;

    /**
     * Construtor vazio.
     * Permite criar um objeto Categoria sem informar os dados inicialmente.
     */
    public Categoria() {
    }

    /**
     * Construtor com parâmetros.
     * Permite criar uma categoria já preenchendo todos os seus atributos.
     *
     * @param idCategoria identificador da categoria
     * @param nome nome da categoria
     * @param descricao descrição da categoria
     */
    public Categoria(int idCategoria, String nome, String descricao) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Retorna o identificador da categoria.
    public int getIdCategoria() {
        return idCategoria;
    }

    // Define ou altera o identificador da categoria.
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Retorna o nome da categoria.
    public String getNome() {
        return nome;
    }

    // Define ou altera o nome da categoria.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna a descrição da categoria.
    public String getDescricao() {
        return descricao;
    }

    // Define ou altera a descrição da categoria.
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna o nome da categoria quando o objeto é convertido
     * para String, como acontece na exibição em um JComboBox.
     *
     * Caso o nome seja null, retorna "Categoria".
     */
    @Override
    public String toString() {
        return nome != null ? nome : "Categoria";
    }
}
