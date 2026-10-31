package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Leitor.
 */

import br.com.biblioteca.controller.LeitorController;
import br.com.biblioteca.model.Leitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LeitorView extends JPanel {
    private final LeitorController controller = new LeitorController();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtCpf = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtEndereco = new JTextField();
    private final JCheckBox chkAtivo = new JCheckBox("Ativo", true);
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CPF", "Telefone", "Email", "Endereço", "Ativo"}, 0
    );

    public LeitorView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de leitor"));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("CPF:"));
        form.add(txtCpf);
        form.add(new JLabel("Telefone:"));
        form.add(txtTelefone);
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);
        form.add(new JLabel("Endereço:"));
        form.add(txtEndereco);
        form.add(new JLabel("Situação:"));
        form.add(chkAtivo);
        form.add(new JLabel(""));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());

        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        form.add(botoes);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                int row = tabela.getSelectedRow();
                txtNome.setText(String.valueOf(tabela.getValueAt(row, 1)));
                txtCpf.setText(String.valueOf(tabela.getValueAt(row, 2)));
                txtTelefone.setText(String.valueOf(tabela.getValueAt(row, 3)));
                txtEmail.setText(String.valueOf(tabela.getValueAt(row, 4)));
                txtEndereco.setText(String.valueOf(tabela.getValueAt(row, 5)));
                chkAtivo.setSelected(Boolean.parseBoolean(String.valueOf(tabela.getValueAt(row, 6))));
            }
        });

        carregarTabela();
    }

    private void salvar() {
        try {
            controller.cadastrar(txtNome.getText(), txtCpf.getText(), txtTelefone.getText(), txtEmail.getText(), txtEndereco.getText());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Leitor cadastrado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um leitor na tabela.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.atualizar(id, txtNome.getText(), txtCpf.getText(), txtTelefone.getText(), txtEmail.getText(), txtEndereco.getText(), chkAtivo.isSelected());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Leitor atualizado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um leitor para excluir.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.excluir(id);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Leitor excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Leitor> leitores = controller.listarTodos();
            for (Leitor leitor : leitores) {
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
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        chkAtivo.setSelected(true);
        tabela.clearSelection();
    }
}
