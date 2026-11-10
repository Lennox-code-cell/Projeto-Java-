
package br.com.biblioteca.controller;

import br.com.biblioteca.dao.CategoriaDAO;

// Importa o modelo Categoria, que representa uma categoria do sistema.
import br.com.biblioteca.model.Categoria;

// Importa SQLException para tratar possíveis erros de acesso ao banco.
import java.sql.SQLException;

// Importa List para trabalhar com listas de categorias.
import java.util.List;

public class CategoriaController {

    // Cria um objeto CategoriaDAO para acessar o banco de dados.
    // O final impede que essa referência seja substituída.
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();


    public void cadastrar(String nome, String descricao) throws SQLException {

        // Verifica se o nome foi informado ou se está vazio.
        if (nome == null || nome.trim().isEmpty()) {

            // Impede o cadastro e informa que o nome é obrigatório.
            throw new IllegalArgumentException(
                    "O nome da categoria é obrigatório."
            );
        }

        // Cria um novo objeto da classe Categoria.
        Categoria categoria = new Categoria();

        // Define o nome da categoria, removendo espaços extras.
        categoria.setNome(nome.trim());

        // Define a descrição.
        // Se for nula, utiliza uma String vazia.
        categoria.setDescricao(
                descricao != null ? descricao.trim() : ""
        );

        // Envia a categoria para o DAO salvar no banco de dados.
        categoriaDAO.salvar(categoria);
    }


    public List<Categoria> listarTodos() throws SQLException {

        // Solicita ao DAO que consulte todas as categorias no banco.
        return categoriaDAO.listarTodos();
    }

    /**
     * Atualiza os dados de uma categoria existente.
     *
     * @param idCategoria Identificador da categoria.
     * @param nome Novo nome da categoria.
     * @param descricao Nova descrição da categoria.
     * @throws SQLException Caso ocorra um erro no banco de dados.
     */
    public void atualizar(
            int idCategoria,
            String nome,
            String descricao
    ) throws SQLException {

        // Verifica se o nome foi informado corretamente.
        if (nome == null || nome.trim().isEmpty()) {

            // Impede a atualização caso o nome esteja vazio.
            throw new IllegalArgumentException(
                    "O nome da categoria é obrigatório."
            );
        }

        // Cria um objeto Categoria para armazenar os novos dados.
        Categoria categoria = new Categoria();

        // Define o ID da categoria que será atualizada.
        categoria.setIdCategoria(idCategoria);

        // Define o novo nome, removendo espaços extras.
        categoria.setNome(nome.trim());

        // Define a nova descrição ou uma String vazia se for nula.
        categoria.setDescricao(
                descricao != null ? descricao.trim() : ""
        );

        // Envia os novos dados ao DAO para atualizar no banco.
        categoriaDAO.atualizar(categoria);
    }

    /**
     * Exclui uma categoria pelo seu identificador.
     *
     * @param idCategoria ID da categoria que será excluída.
     * @throws SQLException Caso ocorra um erro na exclusão.
     */
    public void excluir(int idCategoria) throws SQLException {

        // Solicita ao DAO que exclua a categoria correspondente ao ID.
        categoriaDAO.excluir(idCategoria);
    }
}
