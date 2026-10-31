package br.com.biblioteca.model;

/**
 * Representa um usuário cadastrado da biblioteca.
 * O objeto armazena dados pessoais e o status de cadastro do leitor.
 */
public class Leitor {
    private int idLeitor;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private boolean ativo;

    public Leitor() {
    }

    public Leitor(int idLeitor, String nome, String cpf, String telefone, String email, String endereco, boolean ativo) {
        this.idLeitor = idLeitor;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public int getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(int idLeitor) {
        this.idLeitor = idLeitor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "Leitor";
    }
}
