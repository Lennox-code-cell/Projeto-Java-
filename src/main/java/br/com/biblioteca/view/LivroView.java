package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Livro.
 */

import br.com.biblioteca.controller.AutorController;
import br.com.biblioteca.controller.CategoriaController;
import br.com.biblioteca.controller.LivroController;
import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LivroView extends JPanel {
    private final LivroController controller = new LivroController();
    private final AutorController autorController = new AutorController();
    private final CategoriaController categoriaController = new CategoriaController();

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtIsbn = new JTextField();
    private final JTextField txtAno = new JTextField();
    private final JTextField txtEditora = new JTextField();
    private final JComboBox<Autor> comboAutor = new JComboBox<>();
    private final JComboBox<Categoria> comboCategoria = new JComboBox<>();
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "ISBN", "Ano", "Editora", "Autor", "Categoria"}, 0
    );

    public LivroView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de livro"));
        form.add(new JLabel("Título:"));
        form.add(txtTitulo);
        form.add(new JLabel("ISBN:"));
        form.add(txtIsbn);
        form.add(new JLabel("Ano publicação:"));
        form.add(txtAno);
        form.add(new JLabel("Editora:"));
        form.add(txtEditora);
        form.add(new JLabel("Autor:"));
        form.add(comboAutor);
        form.add(new JLabel("Categoria:"));
        form.add(comboCategoria);
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

        carregarCombos();
        carregarTabela();
    }

    private void salvar() {
        try {
            int idAutor = ((Autor) comboAutor.getSelectedItem()).getIdAutor();
            int idCategoria = ((Categoria) comboCategoria.getSelectedItem()).getIdCategoria();
            int ano = Integer.parseInt(txtAno.getText().trim().isEmpty() ? "0" : txtAno.getText().trim());
            controller.cadastrar(txtTitulo.getText(), txtIsbn.getText(), ano, txtEditora.getText(), idAutor, idCategoria);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            int idAutor = ((Autor) comboAutor.getSelectedItem()).getIdAutor();
            int idCategoria = ((Categoria) comboCategoria.getSelectedItem()).getIdCategoria();
            int ano = Integer.parseInt(txtAno.getText().trim().isEmpty() ? "0" : txtAno.getText().trim());
            controller.atualizar(id, txtTitulo.getText(), txtIsbn.getText(), ano, txtEditora.getText(), idAutor, idCategoria);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.excluir(id);
            limparCampos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCombos() {
        try {
            comboAutor.removeAllItems();
            for (Autor autor : autorController.listarTodos()) {
                comboAutor.addItem(autor);
            }

            comboCategoria.removeAllItems();
            for (Categoria categoria : categoriaController.listarTodos()) {
                comboCategoria.addItem(categoria);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar combos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Livro> livros = controller.listarTodos();
            for (Livro livro : livros) {
                String nomeAutor = "";
                String nomeCategoria = "";
                for (int i = 0; i < comboAutor.getItemCount(); i++) {
                    Autor autor = comboAutor.getItemAt(i);
                    if (autor.getIdAutor() == livro.getIdAutor()) {
                        nomeAutor = autor.getNome();
                        break;
                    }
                }
                for (int i = 0; i < comboCategoria.getItemCount(); i++) {
                    Categoria categoria = comboCategoria.getItemAt(i);
                    if (categoria.getIdCategoria() == livro.getIdCategoria()) {
                        nomeCategoria = categoria.getNome();
                        break;
                    }
                }

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
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtIsbn.setText("");
        txtAno.setText("");
        txtEditora.setText("");
        tabela.clearSelection();
    }
}
