
package br.com.biblioteca.dao;

// Responsável por acessar e persistir os dados dos livros no banco.
import br.com.biblioteca.connection.DatabaseConnection;
import br.com.biblioteca.model.Livro;

// Importações para trabalhar com o banco de dados.
import java.sql.*;

// Importações para trabalhar com listas.
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // Cadastra um novo livro no banco de dados.
    public void salvar(Livro livro) throws SQLException {

        // Comando SQL para inserir um livro na tabela.
        // Os ? representam os parâmetros que serão preenchidos.
        String sql = "INSERT INTO livro (titulo, isbn, ano_publicacao, editora, id_autor, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o título do livro.
            stmt.setString(1, livro.getTitulo());

            // Define o ISBN, identificador internacional da publicação.
            stmt.setString(2, livro.getIsbn());

            // Define o ano de publicação.
            stmt.setInt(3, livro.getAnoPublicacao());

            // Define a editora responsável pela publicação.
            stmt.setString(4, livro.getEditora());

            // Define o ID do autor relacionado ao livro.
            stmt.setInt(5, livro.getIdAutor());

            // Define o ID da categoria relacionada ao livro.
            stmt.setInt(6, livro.getIdCategoria());

            // Executa o comando INSERT no banco de dados.
            stmt.executeUpdate();
        }
    }

    // Consulta todos os livros cadastrados no banco.
    public List<Livro> listarTodos() throws SQLException {

        // Cria uma lista vazia para armazenar os livros encontrados.
        List<Livro> livros = new ArrayList<>();

        // Busca todos os livros e organiza os resultados pelo título.
        String sql = "SELECT * FROM livro ORDER BY titulo";

        // Abre a conexão, prepara e executa a consulta.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre os registros retornados pelo banco.
            while (rs.next()) {

                // Cria um objeto Livro para representar o registro atual.
                Livro livro = new Livro();

                // Recupera os dados das colunas do banco.
                livro.setIdLivro(rs.getInt("id_livro"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setEditora(rs.getString("editora"));
                livro.setIdAutor(rs.getInt("id_autor"));
                livro.setIdCategoria(rs.getInt("id_categoria"));

                // Adiciona o objeto à lista de livros.
                livros.add(livro);
            }
        }

        // Retorna todos os livros encontrados.
        return livros;
    }

    // Atualiza os dados de um livro já cadastrado.
    public void atualizar(Livro livro) throws SQLException {

        // Comando SQL para modificar os dados do livro.
        // O WHERE identifica qual livro será atualizado.
        String sql = "UPDATE livro SET titulo = ?, isbn = ?, ano_publicacao = ?, editora = ?, id_autor = ?, id_categoria = ? WHERE id_livro = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os novos valores do livro.
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getEditora());
            stmt.setInt(5, livro.getIdAutor());
            stmt.setInt(6, livro.getIdCategoria());

            // Informa qual livro será atualizado por meio do seu ID.
            stmt.setInt(7, livro.getIdLivro());

            // Executa o comando UPDATE.
            stmt.executeUpdate();
        }
    }

    // Exclui um livro do banco de dados utilizando seu ID.
    public void excluir(int idLivro) throws SQLException {

        // Comando SQL para remover o livro.
        String sql = "DELETE FROM livro WHERE id_livro = ?";

        // Abre a conexão e prepara o comando SQL.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o ID do livro que será excluído.
            stmt.setInt(1, idLivro);

            // Executa o comando DELETE.
            stmt.executeUpdate();
        }
    }
}
