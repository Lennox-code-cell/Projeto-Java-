package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Autor.
 */

import br.com.biblioteca.controller.AutorController;
import br.com.biblioteca.model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AutorView extends JPanel {
    private final AutorController controller = new AutorController();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtNacionalidade = new JTextField();
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Nacionalidade"}, 0
    );

    public AutorView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de autor"));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Nacionalidade:"));
        form.add(txtNacionalidade);
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
                txtNacionalidade.setText(String.valueOf(tabela.getValueAt(row, 2)));
            }
        });

        carregarTabela();
    }

    private void salvar() {
        try {
            controller.cadastrar(txtNome.getText(), txtNacionalidade.getText());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Autor cadastrado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um autor na tabela.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.atualizar(id, txtNome.getText(), txtNacionalidade.getText());
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Autor atualizado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um autor para excluir.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.excluir(id);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Autor excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Autor> autores = controller.listarTodos();
            for (Autor autor : autores) {
                tableModel.addRow(new Object[]{
                        autor.getIdAutor(),
                        autor.getNome(),
                        autor.getNacionalidade()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtNacionalidade.setText("");
        tabela.clearSelection();
    }
}
