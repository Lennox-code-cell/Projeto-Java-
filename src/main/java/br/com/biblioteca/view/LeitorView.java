
package br.com.biblioteca.view;

// Importa o Controller responsável pelas operações de leitores.
import br.com.biblioteca.controller.LeitorController;

// Importa o Model que representa um leitor.
import br.com.biblioteca.model.Leitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Exibe e organiza a interface gráfica de Leitor.
 *
 * Permite cadastrar, atualizar, excluir e listar leitores
 * por meio de uma interface gráfica desenvolvida com Swing.
 */
public class LeitorView extends JPanel {

    // Controller responsável por coordenar as operações dos leitores.
    private final LeitorController controller = new LeitorController();

    // Campo de texto utilizado para informar o nome do leitor.
    private final JTextField txtNome = new JTextField();

    // Campo de texto utilizado para informar o CPF.
    private final JTextField txtCpf = new JTextField();

    // Campo de texto utilizado para informar o telefone.
    private final JTextField txtTelefone = new JTextField();

    // Campo de texto utilizado para informar o e-mail.
    private final JTextField txtEmail = new JTextField();

    // Campo de texto utilizado para informar o endereço.
    private final JTextField txtEndereco = new JTextField();

    // Caixa de seleção utilizada para definir se o leitor está ativo.
    // O segundo argumento (true) indica que começa selecionada.
    private final JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    // Tabela responsável por exibir os leitores cadastrados.
    private final JTable tabela = new JTable();

    // Modelo que define as colunas da tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Nome",
                    "CPF",
                    "Telefone",
                    "Email",
                    "Endereço",
                    "Ativo"
            },
            0
    );

    // Construtor responsável por configurar a interface gráfica.
    public LeitorView() {

        // Define o layout principal com espaçamento de 10 pixels.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);

        // Cria o painel do formulário com 7 linhas e 2 colunas.
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));

        // Adiciona um título ao formulário.
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de leitor"));

        // Adiciona o campo de nome.
        form.add(new JLabel("Nome:"));
        form.add(txtNome);

        // Adiciona o campo de CPF.
        form.add(new JLabel("CPF:"));
        form.add(txtCpf);

        // Adiciona o campo de telefone.
        form.add(new JLabel("Telefone:"));
        form.add(txtTelefone);

        // Adiciona o campo de e-mail.
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);

        // Adiciona o campo de endereço.
        form.add(new JLabel("Endereço:"));
        form.add(txtEndereco);

        // Adiciona o campo responsável pela situação do leitor.
        form.add(new JLabel("Situação:"));
        form.add(chkAtivo);

        // Adiciona um espaço vazio para completar o GridLayout.
        form.add(new JLabel(""));

        // Cria um painel para organizar os botões.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria os botões de operações.
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");

        // Define os métodos executados ao clicar nos botões.
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

        // Adiciona a tabela ao centro, permitindo rolagem.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Adiciona um listener para detectar a seleção de uma linha.
        tabela.getSelectionModel().addListSelectionListener(e -> {

            // Verifica se a seleção terminou de mudar
            // e se existe uma linha selecionada.
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {

                // Obtém o índice da linha selecionada.
                int row = tabela.getSelectedRow();

                // Preenche o campo de nome com a coluna 1.
                txtNome.setText(String.valueOf(tabela.getValueAt(row, 1)));

                // Preenche o campo de CPF com a coluna 2.
                txtCpf.setText(String.valueOf(tabela.getValueAt(row, 2)));

                // Preenche o campo de telefone com a coluna 3.
                txtTelefone.setText(String.valueOf(tabela.getValueAt(row, 3)));

                // Preenche o campo de e-mail com a coluna 4.
                txtEmail.setText(String.valueOf(tabela.getValueAt(row, 4)));

                // Preenche o campo de endereço com a coluna 5.
                txtEndereco.setText(String.valueOf(tabela.getValueAt(row, 5)));

                // Recupera o valor da coluna 6 e converte para boolean.
                // Se o valor for "true", a caixa será selecionada.
                chkAtivo.setSelected(
                        Boolean.parseBoolean(
                                String.valueOf(tabela.getValueAt(row, 6))
                        )
                );
            }
        });

        // Carrega os leitores cadastrados ao abrir a tela.
        carregarTabela();
    }

    // Método responsável por cadastrar um novo leitor.
    private void salvar() {
        try {

            // Obtém os dados digitados e solicita o cadastro ao Controller.
            controller.cadastrar(
                    txtNome.getText(),
                    txtCpf.getText(),
                    txtTelefone.getText(),
                    txtEmail.getText(),
                    txtEndereco.getText()
            );

            // Limpa os campos após o cadastro.
            limparCampos();

            // Atualiza a tabela para exibir o novo leitor.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Leitor cadastrado com sucesso!"
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

    // Método responsável por atualizar os dados de um leitor.
    private void atualizar() {

        // Obtém o índice da linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum leitor selecionado.
        if (row < 0) {

            // Informa que é necessário selecionar um leitor.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um leitor na tabela."
            );

            // Interrompe a execução.
            return;
        }

        try {

            // Recupera o ID do leitor na primeira coluna da tabela.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Envia o ID e os novos dados para o Controller.
            controller.atualizar(
                    id,
                    txtNome.getText(),
                    txtCpf.getText(),
                    txtTelefone.getText(),
                    txtEmail.getText(),
                    txtEndereco.getText(),
                    chkAtivo.isSelected()
            );

            // Limpa os campos.
            limparCampos();

            // Atualiza a tabela com os dados modificados.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Leitor atualizado com sucesso!"
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

    // Método responsável por excluir um leitor.
    private void excluir() {

        // Obtém a linha selecionada na tabela.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum leitor selecionado.
        if (row < 0) {

            // Informa que é necessário selecionar um leitor.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um leitor para excluir."
            );

            // Interrompe o método.
            return;
        }
        if (!Dialogos.confirmarExclusao(this, "o leitor selecionado")) {
            return;
        }

        try {

            // Recupera o ID do leitor selecionado.
            int id = Integer.parseInt(
                    String.valueOf(tabela.getValueAt(row, 0))
            );

            // Solicita ao Controller a exclusão do leitor.
            controller.excluir(id);

            // Limpa os campos do formulário.
            limparCampos();

            // Atualiza a tabela após a exclusão.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Leitor excluído com sucesso!"
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

    // Método responsável por carregar os leitores na tabela.
    private void carregarTabela() {

        // Remove todas as linhas atuais da tabela.
        tableModel.setRowCount(0);

        try {

            // Solicita ao Controller a lista de leitores cadastrados.
            List<Leitor> leitores = controller.listarTodos();

            // Percorre cada leitor retornado.
            for (Leitor leitor : leitores) {

                // Adiciona os dados do leitor como uma nova linha.
                tableModel.addRow(new Object[]{
                        leitor.getIdLeitor(),
                        leitor.getNome(),
                        leitor.getCpf(),
                        leitor.getTelefone(),
                        leitor.getEmail(),
                        leitor.getEndereco(),
                        leitor.isAtivo()
                });
            }

        } catch (SQLException ex) {

            // Exibe erro caso não seja possível consultar os leitores.
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

        // Limpa o campo de nome.
        txtNome.setText("");

        // Limpa o campo de CPF.
        txtCpf.setText("");

        // Limpa o campo de telefone.
        txtTelefone.setText("");

        // Limpa o campo de e-mail.
        txtEmail.setText("");

        // Limpa o campo de endereço.
        txtEndereco.setText("");

        // Restaura a situação padrão para ativo.
        chkAtivo.setSelected(true);

        // Remove a seleção atual da tabela.
        tabela.clearSelection();
    }
}
