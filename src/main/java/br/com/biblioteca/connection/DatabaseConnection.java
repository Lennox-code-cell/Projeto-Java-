package br.com.biblioteca.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Locale;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://127.0.0.1:3306/biblioteca";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Configuracao configuracao = resolverConfiguracao(System.getenv());
        try {
            return DriverManager.getConnection(
                    configuracao.url(),
                    configuracao.usuario(),
                    configuracao.senha()
            );
        } catch (SQLException ex) {
            throw new SQLException(
                    "Falha ao conectar ao MySQL. Verifique DB_URL, DB_USER, DB_PASSWORD "
                            + "e se o servidor MySQL está acessível.",
                    ex
            );
        }
    }

    static Configuracao resolverConfiguracao(Map<String, String> ambiente) throws SQLException {
        String usuario = ambiente.get("DB_USER");
        String senha = ambiente.get("DB_PASSWORD");
        String url = ambiente.getOrDefault("DB_URL", DEFAULT_URL);

        if (usuario == null || usuario.isBlank()) {
            throw new SQLException("A variável de ambiente DB_USER é obrigatória.");
        }
        if (senha == null || senha.isBlank()) {
            throw new SQLException("A variável de ambiente DB_PASSWORD é obrigatória.");
        }
        if (url == null || url.isBlank()) {
            throw new SQLException("A variável de ambiente DB_URL não pode estar vazia.");
        }
        if (!url.startsWith("jdbc:mysql://")) {
            throw new SQLException("DB_URL deve usar o padrão jdbc:mysql://.");
        }
        String urlSemMaiusculas = url.toLowerCase(Locale.ROOT);
        if (urlSemMaiusculas.contains("user=")
                || urlSemMaiusculas.contains("password=")
                || url.matches("jdbc:mysql://[^/]*@.*")) {
            throw new SQLException(
                    "Não inclua credenciais em DB_URL; use DB_USER e DB_PASSWORD."
            );
        }

        return new Configuracao(url, usuario, senha);
    }

    record Configuracao(String url, String usuario, String senha) {
    }
}