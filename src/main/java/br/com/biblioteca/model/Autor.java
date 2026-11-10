
package br.com.biblioteca.model;

/**
 * Representa um autor cadastrado no sistema.
 * A entidade guarda apenas os dados básicos usados na associação com os livros.
 */
public class Autor {

    // Identificador único do autor no banco de dados.
    private int idAutor;

    // Nome completo do autor.
    private String nome;

    // Nacionalidade do autor.
    private String nacionalidade;

    // Construtor vazio.
    // Permite criar um objeto Autor sem informar os atributos inicialmente.
    public Autor() {
    }
    // Construtor com parâmetros.
    // Permite criar um autor informando todos os seus dados de uma vez.
    public Autor(int idAutor, String nome, String nacionalidade) {

        // Atribui o identificador recebido ao atributo da classe.
        this.idAutor = idAutor;

        // Atribui o nome recebido ao atributo da classe.
        this.nome = nome;

        // Atribui a nacionalidade recebida ao atributo da classe.
        this.nacionalidade = nacionalidade;
    }

    // Retorna o identificador do autor.
    public int getIdAutor() {
        return idAutor;
    }

    // Define ou modifica o identificador do autor.
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    // Retorna o nome do autor.
    public String getNome() {
        return nome;
    }

    // Define ou modifica o nome do autor.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna a nacionalidade do autor.
    public String getNacionalidade() {
        return nacionalidade;
    }

    // Define ou modifica a nacionalidade do autor.
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    // Sobrescreve o método toString() da classe Object.
    // Quando o objeto for convertido em texto, retorna seu nome.
    // Se o nome estiver nulo, retorna a palavra "Autor".
    @Override
    public String toString() {
        return nome != null ? nome : "Autor";
    }
}
