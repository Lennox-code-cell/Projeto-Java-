package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Exemplar.
 */

import br.com.biblioteca.controller.ExemplarController;
import br.com.biblioteca.controller.LivroController;
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExemplarView extends JPanel {
    private final ExemplarController controller = new ExemplarController();
    private final LivroController livroController = new LivroController();
    private final JComboBox<Livro> comboLivro = new JComboBox<>();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtSituacao = new JTextField("disponivel");
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Livro", "Código", "Situação"}, 0
    );

    public ExemplarView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de exemplar"));
        form.add(new JLabel("Livro:"));
        form.add(comboLivro);
        form.add(new JLabel("Código:"));
        form.add(txtCodigo);
        form.add(new JLabel("Situação:"));
        form.add(txtSituacao);
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
                txtCodigo.setText(String.valueOf(tabela.getValueAt(row, 2)));
                txtSituacao.setText(String.valueOf(tabela.getValueAt(row, 3)));
            }
        });

        carregarCombos();
        carregarTabela();
    }

    private void salvar() {
        try {
            Livro livro = (Livro) comboLivro.getSelectedItem();
            if (livro == null) {
                JOptionPane.showMessageDialog(this, "Selecione um livro.");
                return;
            }
            controller.cadastrar(livro.getIdLivro(), txtCodigo.getText(), txtSituacao.getText());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Exemplar cadastrado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um exemplar na tabela.");
            return;
        }

        try {
            Livro livro = (Livro) comboLivro.getSelectedItem();
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            if (livro == null) {
                JOptionPane.showMessageDialog(this, "Selecione um livro.");
                return;
            }
            controller.atualizar(id, livro.getIdLivro(), txtCodigo.getText(), txtSituacao.getText());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Exemplar atualizado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um exemplar para excluir.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.excluir(id);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Exemplar excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCombos() {
        try {
            comboLivro.removeAllItems();
            for (Livro livro : livroController.listarTodos()) {
                comboLivro.addItem(livro);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar livros", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Exemplar> exemplares = controller.listarTodos();
            for (Exemplar exemplar : exemplares) {
                String nomeLivro = "";
                for (int i = 0; i < comboLivro.getItemCount(); i++) {
                    Livro livro = comboLivro.getItemAt(i);
                    if (livro.getIdLivro() == exemplar.getIdLivro()) {
                        nomeLivro = livro.getTitulo();
                        break;
                    }
                }
                tableModel.addRow(new Object[]{
                        exemplar.getIdExemplar(),
                        nomeLivro,
                        exemplar.getCodigo(),
                        exemplar.getSituacao()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtSituacao.setText("disponivel");
        tabela.clearSelection();
    }
}
