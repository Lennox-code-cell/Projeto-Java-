
package br.com.biblioteca.model;

/**
 * Representa um usuário cadastrado na biblioteca.
 *
 * O objeto armazena dados pessoais e o status de cadastro
 * do leitor, permitindo controlar sua participação no sistema.
 */
public class Leitor {

    // Identificador único do leitor no banco de dados.
    private int idLeitor;

    // Nome completo do leitor.
    private String nome;

    // CPF utilizado para identificar o leitor.
    private String cpf;

    // Número de telefone para contato.
    private String telefone;

    // Endereço de e-mail do leitor.
    private String email;

    // Endereço residencial do leitor.
    private String endereco;

    // Indica se o cadastro do leitor está ativo.
    // true = ativo; false = inativo.
    private boolean ativo;

    /**
     * Construtor vazio.
     * Permite criar um objeto Leitor e preencher seus dados posteriormente.
     */
    public Leitor() {
    }

    /**
     * Construtor completo.
     * Permite criar um leitor informando todos os seus atributos.
     *
     * @param idLeitor identificador do leitor
     * @param nome nome completo
     * @param cpf CPF do leitor
     * @param telefone telefone de contato
     * @param email endereço de e-mail
     * @param endereco endereço residencial
     * @param ativo indica se o cadastro está ativo
     */
    public Leitor(int idLeitor, String nome, String cpf,
                  String telefone, String email, String endereco,
                  boolean ativo) {

        this.idLeitor = idLeitor;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    // Retorna o identificador do leitor.
    public int getIdLeitor() {
        return idLeitor;
    }

    // Define ou altera o identificador do leitor.
    public void setIdLeitor(int idLeitor) {
        this.idLeitor = idLeitor;
    }

    // Retorna o nome do leitor.
    public String getNome() {
        return nome;
    }

    // Define ou altera o nome do leitor.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna o CPF do leitor.
    public String getCpf() {
        return cpf;
    }

    // Define ou altera o CPF do leitor.
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // Retorna o telefone do leitor.
    public String getTelefone() {
        return telefone;
    }

    // Define ou altera o telefone do leitor.
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // Retorna o e-mail do leitor.
    public String getEmail() {
        return email;
    }

    // Define ou altera o e-mail do leitor.
    public void setEmail(String email) {
        this.email = email;
    }

    // Retorna o endereço residencial do leitor.
    public String getEndereco() {
        return endereco;
    }

    // Define ou altera o endereço residencial.
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    // Retorna true se o cadastro estiver ativo.
    public boolean isAtivo() {
        return ativo;
    }

    // Define se o cadastro do leitor está ativo ou inativo.
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Retorna o nome do leitor quando o objeto é convertido
     * para String, facilitando sua exibição em componentes Swing.
     *
     * Caso o nome seja null, retorna "Leitor".
     */
    @Override
    public String toString() {
        return nome != null ? nome : "Leitor";
    }
}
