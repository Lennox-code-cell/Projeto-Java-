
package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Livro.
 *
 * Permite cadastrar, atualizar, excluir e visualizar
 * os livros registrados no sistema de biblioteca.
 */

// Importa o Controller responsável pelas operações de livros.
import br.com.biblioteca.controller.LivroController;

// Importa os Controllers utilizados para carregar autores e categorias.
import br.com.biblioteca.controller.AutorController;
import br.com.biblioteca.controller.CategoriaController;

// Importa os Models utilizados nesta interface.
import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Herda JPanel para funcionar como um painel da interface Swing.
public class LivroView extends JPanel {

    // Controller responsável pelas operações relacionadas aos livros.
    private final LivroController controller = new LivroController();

    // Controller utilizado para consultar os autores cadastrados.
    private final AutorController autorController = new AutorController();

    // Controller utilizado para consultar as categorias cadastradas.
    private final CategoriaController categoriaController = new CategoriaController();

    // Campo utilizado para informar o título do livro.
    private final JTextField txtTitulo = new JTextField();

    // Campo utilizado para informar o ISBN do livro.
    private final JTextField txtIsbn = new JTextField();

    // Campo utilizado para informar o ano de publicação.
    private final JTextField txtAno = new JTextField();

    // Campo utilizado para informar a editora.
    private final JTextField txtEditora = new JTextField();

    // Lista suspensa que permite selecionar um objeto Autor.
    private final JComboBox<Autor> comboAutor = new JComboBox<>();

    // Lista suspensa que permite selecionar um objeto Categoria.
    private final JComboBox<Categoria> comboCategoria = new JComboBox<>();

    // Tabela responsável por exibir os livros cadastrados.
    private final JTable tabela = new JTable();

    // Define as colunas que serão apresentadas na tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Título",
                    "ISBN",
                    "Ano",
                    "Editora",
                    "Autor",
                    "Categoria"
            },
            0
    );

    // Construtor responsável por configurar a interface.
    public LivroView() {

        // Define o layout principal com espaçamento de 10 pixels.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);

        // Cria o painel do formulário com 7 linhas e 2 colunas.
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));

        // Adiciona um título ao formulário.
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de livro"));

        // Adiciona o campo de título.
        form.add(new JLabel("Título:"));
        form.add(txtTitulo);

        // Adiciona o campo de ISBN.
        form.add(new JLabel("ISBN:"));
        form.add(txtIsbn);

        // Adiciona o campo de ano de publicação.
        form.add(new JLabel("Ano publicação:"));
        form.add(txtAno);

        // Adiciona o campo de editora.
        form.add(new JLabel("Editora:"));
        form.add(txtEditora);

        // Adiciona a lista de autores.
        form.add(new JLabel("Autor:"));
        form.add(comboAutor);

        // Adiciona a lista de categorias.
        form.add(new JLabel("Categoria:"));
        form.add(comboCategoria);

        // Adiciona um espaço vazio para completar o formulário.
        form.add(new JLabel(""));

        // Cria o painel responsável por organizar os botões.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria os botões de operações.
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");

        // Define o método executado ao clicar em cada botão.
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());

        // Adiciona os botões ao painel.
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        // Adiciona o painel de botões ao formulário.
        form.add(botoes);

        // Posiciona o formulário na parte superior da interface.
        add(form, BorderLayout.NORTH);

        // Posiciona a tabela no centro, com barras de rolagem.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Carrega os autores e categorias nos respectivos combos.
        carregarCombos();

        // Carrega os livros cadastrados na tabela.
        carregarTabela();
    }

    // Método responsável por cadastrar um novo livro.
    private void salvar() {
        try {

            // Recupera o Autor selecionado no JComboBox
            // e obtém seu ID para armazenar como chave estrangeira.
            int idAutor = ((Autor) comboAutor.getSelectedItem()).getIdAutor();

            // Recupera a Categoria selecionada
            // e obtém seu ID para armazenar como chave estrangeira.
            int idCategoria = ((Categoria) comboCategoria.getSelectedItem()).getIdCategoria();

            // Converte o ano informado para inteiro.
            // Se o campo estiver vazio, utiliza 0 como valor padrão.
            int ano = Integer.parseInt(
                    txtAno.getText().trim().isEmpty()
                            ? "0"
                            : txtAno.getText().trim()
            );

            // Envia os dados do livro para o Controller realizar o cadastro.
            controller.cadastrar(
                    txtTitulo.getText(),
                    txtIsbn.getText(),
                    ano,
                    txtEditora.getText(),
                    idAutor,
                    idCategoria
            );

            // Limpa os campos após o cadastro.
            limparCampos();

            // Atualiza a tabela para mostrar o novo livro.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Livro cadastrado com sucesso!"
            );

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe erros de validação ou de acesso ao banco.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por atualizar os dados de um livro.
    private void atualizar() {

        // Recupera a linha selecionada na tabela.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum livro selecionado.
        if (row < 0) {

            // Informa ao usuário que é necessário selecionar um livro.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um livro na tabela."
            );

            // Interrompe a execução do método.
            return;
        }

        try {

            // Recupera o ID do livro na primeira coluna da tabela.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Obtém o ID do autor selecionado.
            int idAutor = ((Autor) comboAutor.getSelectedItem()).getIdAutor();

            // Obtém o ID da categoria selecionada.
            int idCategoria = ((Categoria) comboCategoria.getSelectedItem()).getIdCategoria();

            // Converte o ano para inteiro.
            // Caso esteja vazio, utiliza 0.
            int ano = Integer.parseInt(
                    txtAno.getText().trim().isEmpty()
                            ? "0"
                            : txtAno.getText().trim()
            );

            // Envia o ID e os novos dados para o Controller.
            controller.atualizar(
                    id,
                    txtTitulo.getText(),
                    txtIsbn.getText(),
                    ano,
                    txtEditora.getText(),
                    idAutor,
                    idCategoria
            );

            // Limpa os campos.
            limparCampos();

            // Atualiza a tabela com as informações modificadas.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Livro atualizado com sucesso!"
            );

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe erros de validação ou de acesso ao banco.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por excluir um livro.
    private void excluir() {

        // Recupera a linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum livro selecionado.
        if (row < 0) {

            // Solicita que o usuário selecione um livro.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um livro para excluir."
            );

            // Interrompe a execução.
            return;
        }
        if (!Dialogos.confirmarExclusao(this, "o livro selecionado")) {
            return;
        }

        try {

            // Recupera o ID do livro selecionado.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Solicita ao Controller a exclusão do livro.
            controller.excluir(id);

            // Limpa os campos.
            limparCampos();

            // Atualiza a tabela após a exclusão.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Livro excluído com sucesso!"
            );

        } catch (SQLException ex) {

            // Exibe erros relacionados à operação no banco.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por carregar os autores e categorias nos combos.
    private void carregarCombos() {
        try {

            // Remove os autores que já estavam carregados.
            comboAutor.removeAllItems();

            // Consulta os autores cadastrados no banco.
            for (Autor autor : autorController.listarTodos()) {

                // Adiciona cada objeto Autor à lista suspensa.
                comboAutor.addItem(autor);
            }

            // Remove as categorias que já estavam carregadas.
            comboCategoria.removeAllItems();

            // Consulta as categorias cadastradas no banco.
            for (Categoria categoria : categoriaController.listarTodos()) {

                // Adiciona cada objeto Categoria à lista suspensa.
                comboCategoria.addItem(categoria);
            }

        } catch (SQLException ex) {

            // Exibe erro caso não seja possível carregar os dados.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro ao carregar combos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por carregar os livros na tabela.
    private void carregarTabela() {

        // Remove todas as linhas existentes antes de carregar novamente.
        tableModel.setRowCount(0);

        try {

            // Solicita ao Controller a lista de livros cadastrados.
            List<Livro> livros = controller.listarTodos();

            // Percorre todos os livros retornados.
            for (Livro livro : livros) {

                // Variáveis utilizadas para armazenar os nomes relacionados.
                String nomeAutor = "";
                String nomeCategoria = "";

                // Percorre os autores carregados no JComboBox.
                for (int i = 0; i < comboAutor.getItemCount(); i++) {

                    // Recupera o autor da posição atual.
                    Autor autor = comboAutor.getItemAt(i);

                    // Compara o ID do autor com o ID associado ao livro.
                    if (autor.getIdAutor() == livro.getIdAutor()) {

                        // Armazena o nome do autor correspondente.
                        nomeAutor = autor.getNome();

                        // Interrompe a busca após encontrar o autor.
                        break;
                    }
                }

                // Percorre as categorias carregadas no JComboBox.
                for (int i = 0; i < comboCategoria.getItemCount(); i++) {

                    // Recupera a categoria da posição atual.
                    Categoria categoria = comboCategoria.getItemAt(i);

                    // Compara o ID da categoria com o ID do livro.
                    if (categoria.getIdCategoria() == livro.getIdCategoria()) {

                        // Armazena o nome da categoria correspondente.
                        nomeCategoria = categoria.getNome();

                        // Interrompe a busca após encontrar a categoria.
                        break;
                    }
                }

                // Adiciona os dados do livro como uma nova linha na tabela.
                tableModel.addRow(new Object[]{
                        livro.getIdLivro(),
                        livro.getTitulo(),
                        livro.getIsbn(),
                        livro.getAnoPublicacao(),
                        livro.getEditora(),
                        nomeAutor,
                        nomeCategoria
                });
            }

        } catch (SQLException ex) {

            // Exibe erro caso não seja possível consultar os livros.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro ao carregar dados",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por limpar os campos do formulário.
    private void limparCampos() {

        // Limpa o campo de título.
        txtTitulo.setText("");

        // Limpa o campo de ISBN.
        txtIsbn.setText("");

        // Limpa o campo de ano.
        txtAno.setText("");

        // Limpa o campo de editora.
        txtEditora.setText("");

        // Remove a seleção atual da tabela.
        tabela.clearSelection();
    }
}
