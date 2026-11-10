
package br.com.biblioteca.dao;

/**
 * Responsável por acessar e persistir os dados de Categoria
 * no banco de dados.
 */

// Importa a classe responsável por estabelecer a conexão com o MySQL.
import br.com.biblioteca.connection.DatabaseConnection;

// Importa o modelo Categoria, que representa uma categoria.
import br.com.biblioteca.model.Categoria;

// Importa as classes necessárias para executar comandos SQL.
import java.sql.*;

// Importa ArrayList para armazenar as categorias encontradas.
import java.util.ArrayList;

// Importa List para trabalhar com listas de categorias.
import java.util.List;

public class CategoriaDAO {

    /**
     * Salva uma nova categoria no banco de dados.
     *
     * @param categoria Objeto contendo os dados da categoria.
     * @throws SQLException Caso ocorra um erro na operação SQL.
     */
    public void salvar(Categoria categoria) throws SQLException {

        // Define o comando SQL para inserir uma nova categoria.
        // Os pontos de interrogação representam os valores que serão preenchidos.
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";

        // Abre a conexão com o banco de dados e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o primeiro parâmetro (?) com o nome da categoria.
            stmt.setString(1, categoria.getNome());

            // Define o segundo parâmetro (?) com a descrição da categoria.
            stmt.setString(2, categoria.getDescricao());

            // Executa o comando INSERT para salvar a categoria.
            stmt.executeUpdate();
        }
        // Fecha automaticamente a conexão e o PreparedStatement.
    }
    /**
     * Consulta e retorna todas as categorias cadastradas.
     *
     * @return Lista contendo todas as categorias encontradas.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Categoria> listarTodos() throws SQLException {

        // Cria uma lista vazia para armazenar as categorias.
        List<Categoria> categorias = new ArrayList<>();

        // Define o comando SQL para consultar todas as categorias,
        // organizando os resultados em ordem alfabética pelo nome.
        String sql = "SELECT * FROM categoria ORDER BY nome";

        // Abre a conexão, prepara o comando e executa a consulta.
        // O ResultSet recebe os registros encontrados.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre todos os registros retornados pelo banco.
            while (rs.next()) {

                // Cria um novo objeto Categoria para cada registro encontrado.
                Categoria categoria = new Categoria();

                // Recupera o identificador da categoria.
                categoria.setIdCategoria(rs.getInt("id_categoria"));

                // Recupera o nome da categoria.
                categoria.setNome(rs.getString("nome"));

                // Recupera a descrição da categoria.
                categoria.setDescricao(rs.getString("descricao"));

                // Adiciona a categoria preenchida à lista.
                categorias.add(categoria);
            }
        }

        // Retorna a lista com todas as categorias encontradas.
        return categorias;
    }

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param categoria Objeto contendo o ID e os novos dados.
     * @throws SQLException Caso ocorra um erro na atualização.
     */
    public void atualizar(Categoria categoria) throws SQLException {

        // Define o comando SQL para atualizar o nome e a descrição
        // da categoria que possui o ID informado.
        String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id_categoria = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o primeiro parâmetro com o novo nome.
            stmt.setString(1, categoria.getNome());

            // Define o segundo parâmetro com a nova descrição.
            stmt.setString(2, categoria.getDescricao());

            // Define o terceiro parâmetro com o ID da categoria.
            stmt.setInt(3, categoria.getIdCategoria());

            // Executa o comando UPDATE no banco de dados.
            stmt.executeUpdate();
        }
    }

    /**
     * Exclui uma categoria do banco de dados.
     *
     * @param idCategoria Identificador da categoria que será excluída.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idCategoria) throws SQLException {

        // Define o comando SQL para excluir uma categoria
        // utilizando seu identificador.
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro (?) com o ID da categoria.
            stmt.setInt(1, idCategoria);

            // Executa o comando DELETE no banco de dados.
            stmt.executeUpdate();
        }
    }
}
