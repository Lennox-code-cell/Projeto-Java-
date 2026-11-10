
package br.com.biblioteca.controller;

/**
 * Controla as operações relacionadas aos livros.
 * É responsável por validar os dados e encaminhá-los
 * para a camada DAO realizar as operações no banco.
 */

// Importa o DAO responsável pelo acesso aos dados dos livros.
import br.com.biblioteca.dao.LivroDAO;

// Importa o modelo que representa um livro.
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.util.Validacoes;

// Importa SQLException para tratar possíveis erros no banco.
import java.sql.SQLException;

// Importa List para trabalhar com listas de livros.
import java.util.List;

public class LivroController {

    // Cria um objeto LivroDAO para acessar o banco de dados.
    private final LivroDAO livroDAO = new LivroDAO();

    /**
     * Cadastra um novo livro no sistema.
     *
     * @param titulo Título do livro.
     * @param isbn Código ISBN do livro.
     * @param anoPublicacao Ano de publicação.
     * @param editora Nome da editora.
     * @param idAutor Identificador do autor.
     * @param idCategoria Identificador da categoria.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void cadastrar(
            String titulo,
            String isbn,
            int anoPublicacao,
            String editora,
            int idAutor,
            int idCategoria
    ) throws SQLException {

        // Verifica se o título foi informado ou se está vazio.
        if (titulo == null || titulo.trim().isEmpty()) {

            // Impede o cadastro caso o título não seja preenchido.
            throw new IllegalArgumentException(
                    "O título do livro é obrigatório."
            );
        }
        validarAssociacoes(anoPublicacao, idAutor, idCategoria);

        // Cria um novo objeto da classe Livro.
        Livro livro = new Livro();

        // Define o título do livro, removendo espaços extras.
        livro.setTitulo(titulo.trim());

        livro.setIsbn(Validacoes.normalizarIsbn(isbn));

        // Define o ano de publicação.
        livro.setAnoPublicacao(anoPublicacao);

        // Define o nome da editora ou uma String vazia caso seja nulo.
        livro.setEditora(editora != null ? editora.trim() : "");

        // Define o ID do autor associado ao livro.
        livro.setIdAutor(idAutor);

        // Define o ID da categoria associada ao livro.
        livro.setIdCategoria(idCategoria);

        // Envia o objeto ao DAO para salvar no banco de dados.
        livroDAO.salvar(livro);
    }

    /**
     * Lista todos os livros cadastrados.
     *
     * @return Lista contendo os livros encontrados.
     * @throws SQLException Caso ocorra um erro na consulta.
     */
    public List<Livro> listarTodos() throws SQLException {

        // Solicita ao DAO todos os livros cadastrados no banco.
        return livroDAO.listarTodos();
    }

    /**
     * Atualiza as informações de um livro existente.
     *
     * @param idLivro Identificador do livro.
     * @param titulo Novo título do livro.
     * @param isbn Novo ISBN.
     * @param anoPublicacao Novo ano de publicação.
     * @param editora Nova editora.
     * @param idAutor Novo identificador do autor.
     * @param idCategoria Novo identificador da categoria.
     * @throws SQLException Caso ocorra um erro no banco.
     */
    public void atualizar(
            int idLivro,
            String titulo,
            String isbn,
            int anoPublicacao,
            String editora,
            int idAutor,
            int idCategoria
    ) throws SQLException {

        // Verifica se o título foi informado corretamente.
        if (titulo == null || titulo.trim().isEmpty()) {

            // Impede a atualização caso o título esteja vazio.
            throw new IllegalArgumentException(
                    "O título do livro é obrigatório."
            );
        }
        if (idLivro <= 0) {
            throw new IllegalArgumentException("Selecione um livro válido.");
        }
        validarAssociacoes(anoPublicacao, idAutor, idCategoria);

        // Cria um objeto Livro para armazenar os novos dados.
        Livro livro = new Livro();

        // Define o ID do livro que será atualizado.
        livro.setIdLivro(idLivro);

        // Define o novo título, removendo espaços extras.
        livro.setTitulo(titulo.trim());

        livro.setIsbn(Validacoes.normalizarIsbn(isbn));

        // Atualiza o ano de publicação.
        livro.setAnoPublicacao(anoPublicacao);

        // Atualiza a editora ou utiliza uma String vazia.
        livro.setEditora(editora != null ? editora.trim() : "");

        // Atualiza o autor associado ao livro.
        livro.setIdAutor(idAutor);

        // Atualiza a categoria associada ao livro.
        livro.setIdCategoria(idCategoria);

        // Envia os novos dados ao DAO para atualizar no banco.
        livroDAO.atualizar(livro);
    }

    /**
     * Exclui um livro do sistema.
     *
     * @param idLivro Identificador do livro que será excluído.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idLivro) throws SQLException {

        // Solicita ao DAO a exclusão do livro pelo seu identificador.
        livroDAO.excluir(idLivro);
    }

    private void validarAssociacoes(int anoPublicacao, int idAutor, int idCategoria) {
        if (anoPublicacao < 0 || anoPublicacao > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Informe um ano de publicação válido.");
        }
        if (idAutor <= 0 || idCategoria <= 0) {
            throw new IllegalArgumentException("Selecione um autor e uma categoria válidos.");
        }
    }
}
