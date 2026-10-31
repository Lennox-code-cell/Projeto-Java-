
package br.com.biblioteca.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza a criação da conexão com o banco de dados da biblioteca.
 * As credenciais e o endereço ficam configurados neste ponto para serem reutilizados
 * pelos DAOs do sistema.
 */
public class DatabaseConnection {

    // URL do banco MySQL utilizado pela aplicação.
    private static final String URL =
            "jdbc:mysql://127.0.0.1:33306/biblioteca";

    // Credenciais do usuário do banco.
    private static final String USER = "root";
    private static final String PASSWORD = "Luis+2007";

    private DatabaseConnection() {
    }

    /**
     * Obtém uma conexão ativa com o banco de dados.
     *
     * @return conexão JDBC pronta para uso
     * @throws SQLException quando a conexão falhar ou os parâmetros estiverem inválidos
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

