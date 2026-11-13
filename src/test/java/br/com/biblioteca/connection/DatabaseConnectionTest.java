package br.com.biblioteca.connection;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseConnectionTest {
    @Test
    void usaUrlMysqlPadraoECredenciaisDoAmbiente() throws SQLException {
        DatabaseConnection.Configuracao configuracao =
                DatabaseConnection.resolverConfiguracao(Map.of(
                        "DB_USER", "biblioteca_app",
                        "DB_PASSWORD", "senha-local"
                ));

        assertEquals("jdbc:mysql://127.0.0.1:3306/biblioteca", configuracao.url());
        assertEquals("biblioteca_app", configuracao.usuario());
        assertEquals("senha-local", configuracao.senha());
    }

    @Test
    void exigeCredenciaisSemRevelarValores() {
        SQLException erro = assertThrows(SQLException.class,
                () -> DatabaseConnection.resolverConfiguracao(Map.of(
                        "DB_USER", "usuario-local"
                )));

        assertEquals("A variável de ambiente DB_PASSWORD é obrigatória.", erro.getMessage());
        assertFalse(erro.getMessage().contains("usuario-local"));
    }

    @Test
    void rejeitaUrlDeOutroDriver() {
        SQLException erro = assertThrows(SQLException.class,
                () -> DatabaseConnection.resolverConfiguracao(Map.of(
                        "DB_URL", "jdbc:mariadb://localhost/biblioteca",
                        "DB_USER", "biblioteca_app",
                        "DB_PASSWORD", "senha-local"
                )));

        assertEquals("DB_URL deve usar o padrão jdbc:mysql://.", erro.getMessage());
        assertFalse(erro.getMessage().contains("senha-local"));
    }

    @Test
    void naoAceitaCredenciaisEmbutidasNaUrl() {
        SQLException erro = assertThrows(SQLException.class,
                () -> DatabaseConnection.resolverConfiguracao(Map.of(
                        "DB_URL", "jdbc:mysql://localhost/biblioteca?password=secreta",
                        "DB_USER", "biblioteca_app",
                        "DB_PASSWORD", "senha-local"
                )));

        assertEquals(
                "Não inclua credenciais em DB_URL; use DB_USER e DB_PASSWORD.",
                erro.getMessage()
        );
        assertFalse(erro.getMessage().contains("secreta"));
    }
}
