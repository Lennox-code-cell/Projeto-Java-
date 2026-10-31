package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Emprestimo.
 */

import br.com.biblioteca.controller.EmprestimoController;
import br.com.biblioteca.dao.EmprestimoDAO;
import br.com.biblioteca.dao.ExemplarDAO;
import br.com.biblioteca.dao.LeitorDAO;
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Leitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmprestimoView extends JPanel {
    private final EmprestimoController controller = new EmprestimoController();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final ExemplarDAO exemplarDAO = new ExemplarDAO();

    private final JComboBox<Leitor> comboLeitor = new JComboBox<>();
    private final JComboBox<Exemplar> comboExemplar = new JComboBox<>();
    private final JTextField txtDataEmprestimo = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    private final JTextField txtDataPrevista = new JTextField(LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE));
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Leitor", "Livro", "Exemplar", "Data Empréstimo", "Data Prevista", "Situação"}, 0
    );

    public EmprestimoView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Empréstimo"));
        form.add(new JLabel("Leitor:"));
        form.add(comboLeitor);
        form.add(new JLabel("Exemplar disponível:"));
        form.add(comboExemplar);
        form.add(new JLabel("Data empréstimo:"));
        form.add(txtDataEmprestimo);
        form.add(new JLabel("Data prevista:"));
        form.add(txtDataPrevista);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEmprestar = new JButton("Emprestar");
        JButton btnDevolver = new JButton("Registrar devolução");
        btnEmprestar.addActionListener(e -> emprestar());
        btnDevolver.addActionListener(e -> devolver());
        botoes.add(btnEmprestar);
        botoes.add(btnDevolver);
        form.add(botoes);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarCombos();
        carregarTabela();
    }

    private void emprestar() {
        try {
            Leitor leitor = (Leitor) comboLeitor.getSelectedItem();
            Exemplar exemplar = (Exemplar) comboExemplar.getSelectedItem();
            if (leitor == null || exemplar == null) {
                JOptionPane.showMessageDialog(this, "Selecione um leitor e um exemplar.");
                return;
            }

            controller.registrarEmprestimo(
                    exemplar.getIdExemplar(),
                    leitor.getIdLeitor(),
                    LocalDate.parse(txtDataEmprestimo.getText()),
                    LocalDate.parse(txtDataPrevista.getText())
            );
            carregarCombos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Empréstimo registrado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolver() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para registrar a devolução.");
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(tabela.getValueAt(row, 0)));
            controller.registrarDevolucao(id, LocalDate.now());
            carregarCombos();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCombos() {
        try {
            comboLeitor.removeAllItems();
            for (Leitor leitor : leitorDAO.listarTodos()) {
                comboLeitor.addItem(leitor);
            }

            comboExemplar.removeAllItems();
            for (Exemplar exemplar : exemplarDAO.listarDisponiveis()) {
                comboExemplar.addItem(exemplar);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Object[]> emprestimos = emprestimoDAO.listarAtivos();
            for (Object[] linha : emprestimos) {
                tableModel.addRow(linha);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar empréstimos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
