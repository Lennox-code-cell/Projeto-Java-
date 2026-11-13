package br.com.biblioteca;

/**
 * Classe auxiliar para validar a conexão com o banco de dados.
 */

import br.com.biblioteca.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection conexao = DatabaseConnection.getConnection()) {
            if (conexao != null && conexao.isValid(3)) {
                System.out.println("Conexão com o banco realizada com sucesso!");
                System.out.println("Banco: " +
                        conexao.getCatalog());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
