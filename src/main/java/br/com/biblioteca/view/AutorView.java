
package br.com.biblioteca.view;

// Importa o Controller responsável pelas operações de autor.
import br.com.biblioteca.controller.AutorController;

// Importa a Model que representa um autor.
import br.com.biblioteca.model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Exibe e organiza a interface gráfica de Autor.
 *
 * Permite cadastrar, atualizar, excluir e visualizar
 * os autores registrados no sistema.
 */
public class AutorView extends JPanel {

    // Cria o Controller que conecta a interface à lógica do sistema.
    private final AutorController controller = new AutorController();

    // Campo de texto para digitar o nome do autor.
    private final JTextField txtNome = new JTextField();

    // Campo de texto para digitar a nacionalidade do autor.
    private final JTextField txtNacionalidade = new JTextField();

    // Tabela responsável por exibir os autores cadastrados.
    private final JTable tabela = new JTable();

    // Modelo que define as colunas e armazena os dados da tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Nacionalidade"}, 0
    );

    /**
     * Construtor da tela.
     * Configura os componentes, organiza o layout,
     * registra os eventos e carrega os dados iniciais.
     */
    public AutorView() {

        // Define o layout principal com regiões e espaçamento.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);

        // Cria o painel do formulário com 3 linhas e 2 colunas.
        // Os valores 8 e 8 definem os espaços entre os componentes.
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));

        // Adiciona uma borda com o título "Cadastro de autor".
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de autor"));

        // Adiciona o rótulo Nome ao formulário.
        form.add(new JLabel("Nome:"));

        // Adiciona o campo de digitação do nome.
        form.add(txtNome);

        // Adiciona o rótulo Nacionalidade ao formulário.
        form.add(new JLabel("Nacionalidade:"));

        // Adiciona o campo de digitação da nacionalidade.
        form.add(txtNacionalidade);

        // Adiciona um rótulo vazio para ocupar a posição da primeira coluna.
        form.add(new JLabel(""));

        // Cria um painel para organizar os botões horizontalmente,
        // alinhados à esquerda.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria o botão responsável por cadastrar um autor.
        JButton btnSalvar = new JButton("Salvar");

        // Cria o botão responsável por atualizar um autor selecionado.
        JButton btnAtualizar = new JButton("Atualizar");

        // Cria o botão responsável por excluir um autor selecionado.
        JButton btnExcluir = new JButton("Excluir");

        // Associa o clique do botão Salvar ao método salvar().
        btnSalvar.addActionListener(e -> salvar());

        // Associa o clique do botão Atualizar ao método atualizar().
        btnAtualizar.addActionListener(e -> atualizar());

        // Associa o clique do botão Excluir ao método excluir().
        btnExcluir.addActionListener(e -> excluir());

        // Adiciona os três botões ao painel de botões.
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        // Adiciona o painel de botões ao formulário.
        form.add(botoes);

        // Posiciona o formulário na parte superior da tela.
        add(form, BorderLayout.NORTH);

        // Adiciona a tabela ao centro, dentro de uma área com rolagem.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Registra um evento para detectar a seleção de uma linha da tabela.
        tabela.getSelectionModel().addListSelectionListener(e -> {

            // Verifica se a alteração da seleção terminou
            // e se existe alguma linha selecionada.
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {

                // Obtém o índice da linha selecionada.
                int row = tabela.getSelectedRow();

                // Copia o nome da coluna 1 para o campo txtNome.
                txtNome.setText(String.valueOf(tabela.getValueAt(row, 1)));

                // Copia a nacionalidade da coluna 2 para seu campo.
                txtNacionalidade.setText(String.valueOf(tabela.getValueAt(row, 2)));
            }
        });

        // Carrega os autores cadastrados assim que a tela é criada.
        carregarTabela();
    }

    /**
     * Cadastra um novo autor utilizando os dados digitados.
     */
    private void salvar() {
        try {

            // Envia o nome e a nacionalidade para o Controller.
            controller.cadastrar(txtNome.getText(), txtNacionalidade.getText());

            // Limpa os campos depois do cadastro.
            limparCampos();

            // Atualiza a tabela para mostrar o novo autor.
            carregarTabela();

            // Informa ao usuário que o cadastro foi realizado.
            JOptionPane.showMessageDialog(this, "Autor cadastrado com sucesso!");

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe uma mensagem caso ocorra erro de validação
            // ou erro relacionado ao banco de dados.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Atualiza os dados do autor selecionado na tabela.
     */
    private void atualizar() {

        // Obtém o índice da linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se o usuário selecionou algum autor.
        if (row < 0) {

            // Informa que é necessário selecionar um registro.
            JOptionPane.showMessageDialog(this, "Selecione um autor na tabela.");

            // Interrompe a execução do método.
            return;
        }

        try {

            // Obtém o ID do autor na primeira coluna da tabela.
            // Converte o valor para String e depois para int.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Envia o ID e os novos dados para o Controller.
            controller.atualizar(
                    id,
                    txtNome.getText(),
                    txtNacionalidade.getText()
            );

            // Limpa os campos após a atualização.
            limparCampos();

            // Atualiza a tabela para exibir os dados modificados.
            carregarTabela();

            // Informa que a atualização foi concluída.
            JOptionPane.showMessageDialog(this, "Autor atualizado com sucesso!");

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe erros de validação ou de banco de dados.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Exclui o autor selecionado na tabela.
     */
    private void excluir() {

        // Obtém o índice da linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se existe uma linha selecionada.
        if (row < 0) {

            // Solicita que o usuário selecione um autor.
            JOptionPane.showMessageDialog(this, "Selecione um autor para excluir.");

            // Interrompe a execução do método.
            return;
        }
        if (!Dialogos.confirmarExclusao(this, "o autor selecionado")) {
            return;
        }

        try {

            // Obtém o ID do autor na primeira coluna.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Solicita ao Controller a exclusão do autor.
            controller.excluir(id);

            // Limpa os campos e remove a seleção da tabela.
            limparCampos();

            // Atualiza a tabela para remover o autor excluído.
            carregarTabela();

            // Informa que a exclusão foi realizada.
            JOptionPane.showMessageDialog(this, "Autor excluído com sucesso!");

        } catch (SQLException ex) {

            // Exibe o erro caso a exclusão não possa ser realizada.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Consulta os autores no Controller e preenche a tabela.
     */
    private void carregarTabela() {

        // Remove todas as linhas atuais da tabela.
        tableModel.setRowCount(0);

        try {

            // Solicita ao Controller a lista de autores cadastrados.
            List<Autor> autores = controller.listarTodos();

            // Percorre cada autor retornado pela consulta.
            for (Autor autor : autores) {

                // Adiciona uma nova linha com os dados do autor.
                tableModel.addRow(new Object[]{
                        autor.getIdAutor(),
                        autor.getNome(),
                        autor.getNacionalidade()
                });
            }

        } catch (SQLException ex) {

            // Exibe uma mensagem caso ocorra erro ao consultar os dados.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro ao carregar dados",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Limpa os campos do formulário e remove a seleção da tabela.
     */
    private void limparCampos() {

        // Apaga o conteúdo do campo Nome.
        txtNome.setText("");

        // Apaga o conteúdo do campo Nacionalidade.
        txtNacionalidade.setText("");

        // Remove qualquer linha selecionada na tabela.
        tabela.clearSelection();
    }
}
