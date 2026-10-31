package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Relatorio.
 */

import br.com.biblioteca.dao.RelatorioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RelatorioView extends JPanel {
    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private final JTextField txtDataInicio = new JTextField(LocalDate.now().minusDays(30).toString());
    private final JTextField txtDataFim = new JTextField(LocalDate.now().toString());
    private final JTable tabela = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Leitor", "Livro", "Exemplar", "Empréstimo", "Prevista", "Devolução", "Situação"}, 0
    );
    private final JLabel totalRegistros = new JLabel("Total de registros: 0");

    public RelatorioView() {
        setLayout(new BorderLayout(10, 10));
        tabela.setModel(tableModel);

        JPanel filtros = new JPanel(new GridLayout(3, 2, 8, 8));
        filtros.setBorder(BorderFactory.createTitledBorder("Relatório por período"));
        filtros.add(new JLabel("Data inicial:"));
        filtros.add(txtDataInicio);
        filtros.add(new JLabel("Data final:"));
        filtros.add(txtDataFim);
        filtros.add(new JLabel(""));

        JButton btnGerar = new JButton("Gerar relatório");
        btnGerar.addActionListener(e -> gerarRelatorio());
        filtros.add(btnGerar);

        add(filtros, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(totalRegistros, BorderLayout.SOUTH);

        gerarRelatorio();
    }

    private void gerarRelatorio() {
        tableModel.setRowCount(0);
        try {
            LocalDate inicio = LocalDate.parse(txtDataInicio.getText());
            LocalDate fim = LocalDate.parse(txtDataFim.getText());
            List<Object[]> dados = relatorioDAO.gerarRelatorioPorPeriodo(inicio, fim);

            for (Object[] linha : dados) {
                tableModel.addRow(linha);
            }

            totalRegistros.setText("Total de registros: " + dados.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro no relatório", JOptionPane.ERROR_MESSAGE);
        }
    }
}
