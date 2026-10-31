package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas a Leitor.
 */

import br.com.biblioteca.dao.LeitorDAO;
import br.com.biblioteca.model.Leitor;

import java.sql.SQLException;
import java.util.List;

public class LeitorController {
    private final LeitorDAO leitorDAO = new LeitorDAO();

    public void cadastrar(String nome, String cpf, String telefone, String email, String endereco) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do leitor é obrigatório.");
        }

        Leitor leitor = new Leitor();
        leitor.setNome(nome.trim());
        leitor.setCpf(cpf != null ? cpf.trim() : "");
        leitor.setTelefone(telefone != null ? telefone.trim() : "");
        leitor.setEmail(email != null ? email.trim() : "");
        leitor.setEndereco(endereco != null ? endereco.trim() : "");
        leitor.setAtivo(true);
        leitorDAO.salvar(leitor);
    }

    public List<Leitor> listarTodos() throws SQLException {
        return leitorDAO.listarTodos();
    }

    public void atualizar(int idLeitor, String nome, String cpf, String telefone, String email, String endereco, boolean ativo) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do leitor é obrigatório.");
        }

        Leitor leitor = new Leitor();
        leitor.setIdLeitor(idLeitor);
        leitor.setNome(nome.trim());
        leitor.setCpf(cpf != null ? cpf.trim() : "");
        leitor.setTelefone(telefone != null ? telefone.trim() : "");
        leitor.setEmail(email != null ? email.trim() : "");
        leitor.setEndereco(endereco != null ? endereco.trim() : "");
        leitor.setAtivo(ativo);
        leitorDAO.atualizar(leitor);
    }

    public void excluir(int idLeitor) throws SQLException {
        leitorDAO.excluir(idLeitor);
    }
}
