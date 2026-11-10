
package br.com.biblioteca.dao;

// Responsável por acessar e persistir os dados dos leitores no banco.
import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Leitor;

// Importações para operações com banco de dados.
import java.sql.*;

// Importações para trabalhar com listas.
import java.util.ArrayList;
import java.util.List;

public class LeitorDAO {

    // Cadastra um novo leitor no banco de dados.
    public void salvar(Leitor leitor) throws SQLException {

        // Comando SQL para inserir um leitor.
        // Os ? representam os valores que serão preenchidos posteriormente.
        String sql = "INSERT INTO leitor (nome, cpf, telefone, email, endereco, ativo) VALUES (?, ?, ?, ?, ?, ?)";

        // Abre a conexão com o banco e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o nome do leitor.
            stmt.setString(1, leitor.getNome());

            // Define o CPF do leitor.
            stmt.setString(2, leitor.getCpf());

            // Define o telefone.
            stmt.setString(3, leitor.getTelefone());

            // Define o endereço de e-mail.
            stmt.setString(4, leitor.getEmail());

            // Define o endereço residencial.
            stmt.setString(5, leitor.getEndereco());

            // Define se o leitor está ativo ou inativo.
            // O valor boolean representa true ou false.
            stmt.setBoolean(6, leitor.isAtivo());

            // Executa o comando INSERT no banco de dados.
            stmt.executeUpdate();
        }
    }

    // Consulta e retorna todos os leitores cadastrados.
    public List<Leitor> listarTodos() throws SQLException {

        // Cria uma lista vazia para armazenar os leitores encontrados.
        List<Leitor> leitores = new ArrayList<>();

        // Busca todos os leitores, organizando os resultados pelo nome.
        String sql = "SELECT * FROM leitor ORDER BY nome";

        // Abre a conexão, prepara e executa a consulta.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre todos os registros retornados pelo banco.
            while (rs.next()) {

                // Cria um novo objeto Leitor para representar o registro atual.
                Leitor leitor = new Leitor();

                // Recupera os valores das colunas e preenche o objeto.
                leitor.setIdLeitor(rs.getInt("id_leitor"));
                leitor.setNome(rs.getString("nome"));
                leitor.setCpf(rs.getString("cpf"));
                leitor.setTelefone(rs.getString("telefone"));
                leitor.setEmail(rs.getString("email"));
                leitor.setEndereco(rs.getString("endereco"));

                // Recupera o valor boolean da coluna ativo.
                leitor.setAtivo(rs.getBoolean("ativo"));

                // Adiciona o leitor à lista.
                leitores.add(leitor);
            }
        }

        // Retorna a lista completa de leitores.
        return leitores;
    }

    public List<Leitor> listarAtivos() throws SQLException {
        List<Leitor> leitores = new ArrayList<>();
        String sql = "SELECT id_leitor, nome FROM leitor WHERE ativo = TRUE ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Leitor leitor = new Leitor();
                leitor.setIdLeitor(rs.getInt("id_leitor"));
                leitor.setNome(rs.getString("nome"));
                leitores.add(leitor);
            }
        }
        return leitores;
    }

    // Atualiza os dados de um leitor que já está cadastrado.
    public void atualizar(Leitor leitor) throws SQLException {

        // Comando SQL que modifica os dados do leitor.
        // O WHERE identifica qual registro deverá ser atualizado.
        String sql = "UPDATE leitor SET nome = ?, cpf = ?, telefone = ?, email = ?, endereco = ?, ativo = ? WHERE id_leitor = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os novos valores dos campos.
            stmt.setString(1, leitor.getNome());
            stmt.setString(2, leitor.getCpf());
            stmt.setString(3, leitor.getTelefone());
            stmt.setString(4, leitor.getEmail());
            stmt.setString(5, leitor.getEndereco());

            // Atualiza a situação do leitor: ativo ou inativo.
            stmt.setBoolean(6, leitor.isAtivo());

            // Define o ID do leitor que receberá as alterações.
            stmt.setInt(7, leitor.getIdLeitor());

            // Executa o comando UPDATE.
            stmt.executeUpdate();
        }
    }

    public void excluir(int idLeitor) throws SQLException {
        String sql = "DELETE FROM leitor WHERE id_leitor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLeitor);
            stmt.executeUpdate();
        }
    }
}
