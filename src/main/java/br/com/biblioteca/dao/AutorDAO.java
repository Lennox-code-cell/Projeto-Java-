
package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Autor
 * no banco de dados.
 */

// Importa a classe responsável por estabelecer a conexão com o MySQL.
import br.com.biblioteca.connection.DatabaseConnection;

// Importa o modelo Autor, que representa os dados de um autor.
import br.com.biblioteca.model.Autor;

// Importa as classes necessárias para executar comandos SQL.
import java.sql.*;

// Importa ArrayList para armazenar os autores encontrados.
import java.util.ArrayList;

// Importa List para trabalhar com listas de autores.
import java.util.List;

public class AutorDAO {

    /**
     * Salva um novo autor no banco de dados.
     *
     * @param autor Objeto contendo os dados do autor.
     * @throws SQLException Caso ocorra um erro na operação SQL.
     */
    public void salvar(Autor autor) throws SQLException {

        // Define o comando SQL para inserir um novo autor.
        // Os pontos de interrogação representam os valores que serão preenchidos.
        String sql = "INSERT INTO autor (nome, nacionalidade) VALUES (?, ?)";

        // Abre a conexão com o banco de dados.
        // Prepara o comando SQL para execução.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o primeiro parâmetro (?) com o nome do autor.
            stmt.setString(1, autor.getNome());

            // Define o segundo parâmetro (?) com a nacionalidade.
            stmt.setString(2, autor.getNacionalidade());

            // Executa o comando INSERT no banco de dados.
            stmt.executeUpdate();
        }
        // Ao finalizar o bloco, a conexão e o PreparedStatement
        // são fechados automaticamente.
    }

    /**
     * Consulta e retorna todos os autores cadastrados.
     *
     * @return Lista contendo os autores encontrados.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Autor> listarTodos() throws SQLException {

        // Cria uma lista vazia para armazenar os autores.
        List<Autor> autores = new ArrayList<>();

        // Define o comando SQL para consultar todos os autores,
        // organizando os resultados em ordem alfabética pelo nome.
        String sql = "SELECT * FROM autor ORDER BY nome";

        // Abre a conexão e prepara o comando SQL.
        // O ResultSet recebe os registros retornados pela consulta.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre todos os registros retornados pelo banco.
            while (rs.next()) {

                // Cria um novo objeto Autor para cada registro encontrado.
                Autor autor = new Autor();

                // Recupera o ID do autor na coluna id_autor.
                autor.setIdAutor(rs.getInt("id_autor"));

                // Recupera o nome do autor.
                autor.setNome(rs.getString("nome"));

                // Recupera a nacionalidade do autor.
                autor.setNacionalidade(rs.getString("nacionalidade"));

                // Adiciona o autor preenchido à lista.
                autores.add(autor);
            }
        }

        // Retorna a lista contendo todos os autores encontrados.
        return autores;
    }

    /**
     * Atualiza os dados de um autor existente.
     *
     * @param autor Objeto contendo o ID e os novos dados do autor.
     * @throws SQLException Caso ocorra um erro na atualização.
     */
    public void atualizar(Autor autor) throws SQLException {

        // Define o comando SQL para atualizar nome e nacionalidade
        // do autor cujo ID corresponde ao informado.
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ? WHERE id_autor = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o primeiro parâmetro com o novo nome.
            stmt.setString(1, autor.getNome());

            // Define o segundo parâmetro com a nova nacionalidade.
            stmt.setString(2, autor.getNacionalidade());

            // Define o terceiro parâmetro com o ID do autor
            // que será atualizado.
            stmt.setInt(3, autor.getIdAutor());

            // Executa o comando UPDATE no banco de dados.
            stmt.executeUpdate();
        }
    }

    /**
     * Exclui um autor do banco de dados.
     *
     * @param idAutor Identificador do autor que será excluído.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idAutor) throws SQLException {

        // Define o comando SQL para excluir um autor
        // utilizando seu identificador.
        String sql = "DELETE FROM autor WHERE id_autor = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro (?) com o ID do autor.
            stmt.setInt(1, idAutor);

            // Executa o comando DELETE no banco de dados.
            stmt.executeUpdate();
        }
    }
}
