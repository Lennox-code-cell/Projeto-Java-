
package br.com.biblioteca.view;

// Importa o Controller responsável pelas operações de Categoria.
import br.com.biblioteca.controller.CategoriaController;

// Importa o Model que representa uma categoria.
import br.com.biblioteca.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Exibe e organiza a interface gráfica de Categoria.
 *
 * Permite cadastrar, atualizar, excluir e listar categorias
 * por meio de uma interface gráfica desenvolvida com Swing.
 */
public class CategoriaView extends JPanel {

    // Instância do Controller que intermedia a View e o DAO.
    private final CategoriaController controller = new CategoriaController();

    // Campo de texto utilizado para informar o nome da categoria.
    private final JTextField txtNome = new JTextField();

    // Campo de texto utilizado para informar a descrição da categoria.
    private final JTextField txtDescricao = new JTextField();

    // Tabela responsável por exibir as categorias cadastradas.
    private final JTable tabela = new JTable();

    // Modelo que define as colunas e os dados exibidos na tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Descrição"}, 0
    );

    // Construtor: configura e inicializa os componentes da interface.
    public CategoriaView() {

        // Define o layout principal com regiões NORTH, CENTER etc.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);

        // Cria o painel do formulário com 3 linhas e 2 colunas.
        // Os valores 8 e 8 representam os espaçamentos entre os componentes.
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));

        // Adiciona uma borda com o título do formulário.
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de categoria"));

        // Adiciona o rótulo e o campo de nome ao formulário.
        form.add(new JLabel("Nome:"));
        form.add(txtNome);

        // Adiciona o rótulo e o campo de descrição ao formulário.
        form.add(new JLabel("Descrição:"));
        form.add(txtDescricao);

        // Adiciona um espaço vazio para completar a organização do GridLayout.
        form.add(new JLabel(""));

        // Cria um painel para organizar os botões horizontalmente.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria os botões de operações.
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");

        // Define o método executado quando cada botão é clicado.
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());

        // Adiciona os botões ao painel de botões.
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        // Adiciona o painel de botões ao formulário.
        form.add(botoes);

        // Posiciona o formulário na parte superior da interface.
        add(form, BorderLayout.NORTH);

        // Adiciona a tabela ao centro, dentro de um JScrollPane.
        // O JScrollPane permite rolar a tabela quando necessário.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Adiciona um listener para detectar a seleção de uma linha da tabela.
        tabela.getSelectionModel().addListSelectionListener(e -> {

            // Verifica se a seleção terminou de mudar e se existe uma linha selecionada.
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {

                // Obtém o índice da linha selecionada.
                int row = tabela.getSelectedRow();

                // Preenche o campo de nome com o valor da coluna 1.
                txtNome.setText(String.valueOf(tabela.getValueAt(row, 1)));

                // Preenche o campo de descrição com o valor da coluna 2.
                txtDescricao.setText(String.valueOf(tabela.getValueAt(row, 2)));
            }
        });

        // Carrega as categorias existentes no banco ao abrir a tela.
        carregarTabela();
    }

    // Método responsável por cadastrar uma nova categoria.
    private void salvar() {
        try {

            // Obtém os textos digitados e solicita o cadastro ao Controller.
            controller.cadastrar(txtNome.getText(), txtDescricao.getText());

            // Limpa os campos após o cadastro.
            limparCampos();

            // Atualiza a tabela para exibir a nova categoria.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe uma mensagem caso ocorra um erro de validação ou no banco.
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por atualizar uma categoria existente.
    private void atualizar() {

        // Obtém o índice da linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se o usuário selecionou alguma categoria.
        if (row < 0) {

            // Informa que é necessário selecionar uma categoria.
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela.");

            // Interrompe o método para evitar uma atualização sem ID.
            return;
        }

        try {

            // Obtém o ID da categoria na primeira coluna da tabela.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Envia o ID e os novos dados para o Controller.
            controller.atualizar(id, txtNome.getText(), txtDescricao.getText());

            // Limpa os campos.
            limparCampos();

            // Atualiza a tabela com os dados modificados.
            carregarTabela();

            // Informa que a atualização foi concluída.
            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe erros de validação ou de acesso ao banco.
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por excluir uma categoria.
    private void excluir() {

        // Obtém a linha selecionada na tabela.
        int row = tabela.getSelectedRow();

        // Verifica se existe uma categoria selecionada.
        if (row < 0) {

            // Informa ao usuário que precisa selecionar uma categoria.
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir.");

            // Interrompe a execução do método.
            return;
        }
        if (!Dialogos.confirmarExclusao(this, "a categoria selecionada")) {
            return;
        }

        try {

            // Obtém o ID da categoria selecionada.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Solicita ao Controller a exclusão da categoria.
            controller.excluir(id);

            // Limpa os campos do formulário.
            limparCampos();

            // Atualiza a tabela após a exclusão.
            carregarTabela();

            // Exibe uma mensagem de confirmação da operação.
            JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!");

        } catch (SQLException ex) {

            // Exibe erros relacionados à operação no banco de dados.
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por carregar as categorias na tabela.
    private void carregarTabela() {

        // Remove todas as linhas atuais da tabela.
        tableModel.setRowCount(0);

        try {

            // Solicita ao Controller a lista de categorias cadastradas.
            List<Categoria> categorias = controller.listarTodos();

            // Percorre cada categoria retornada.
            for (Categoria categoria : categorias) {

                // Adiciona uma nova linha com os dados da categoria.
                tableModel.addRow(new Object[]{
                        categoria.getIdCategoria(),
                        categoria.getNome(),
                        categoria.getDescricao()
                });
            }

        } catch (SQLException ex) {

            // Exibe uma mensagem caso ocorra erro ao consultar os dados.
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Erro ao carregar dados",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por limpar os campos do formulário.
    private void limparCampos() {

        // Limpa o campo de nome.
        txtNome.setText("");

        // Limpa o campo de descrição.
        txtDescricao.setText("");

        // Remove a seleção atual da tabela.
        tabela.clearSelection();
    }
}
