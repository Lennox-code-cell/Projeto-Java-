
package br.com.biblioteca.view;

/**
 * Exibe e organiza a interface gráfica de Relatorio.
 *
 * Permite consultar os empréstimos registrados no sistema
 * dentro de um período informado pelo usuário.
 */

// Importa o DAO responsável por consultar os relatórios no banco.
import br.com.biblioteca.controller.RelatorioController;
import br.com.biblioteca.model.EmprestimoRelatorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

// Herda JPanel para funcionar como um painel da interface Swing.
public class RelatorioView extends JPanel {

    private final RelatorioController controller = new RelatorioController();

    // Campo de data inicial.
    // Por padrão, apresenta a data de 30 dias atrás.
    private final JTextField txtDataInicio = new JTextField(
            LocalDate.now().minusDays(30).toString()
    );

    // Campo de data final.
    // Por padrão, apresenta a data atual.
    private final JTextField txtDataFim = new JTextField(
            LocalDate.now().toString()
    );

    // Tabela responsável por apresentar os resultados do relatório.
    private final JTable tabela = new JTable();
    private final JComboBox<String> comboSituacao = new JComboBox<>(
            new String[]{"Todos", "Ativos", "Atrasados", "Devolvidos"}
    );
    private final JButton botaoGerar = new JButton("Gerar relatório");

    // Modelo responsável por definir as colunas da tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Leitor",
                    "Livro",
                    "Exemplar",
                    "Empréstimo",
                    "Prevista",
                    "Devolução",
                    "Situação"
            },
            0
    );

    // Label responsável por mostrar a quantidade de registros encontrados.
    private final JLabel totalRegistros = new JLabel("Total de registros: 0");

    // Construtor responsável por configurar a interface.
    public RelatorioView() {

        // Define o layout principal com espaçamento de 10 pixels.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);
        tabela.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        // Cria o painel dos filtros com 3 linhas e 2 colunas.
        JPanel filtros = new JPanel(new GridLayout(4, 2, 8, 8));

        // Adiciona um título ao painel.
        filtros.setBorder(BorderFactory.createTitledBorder("Relatório por período"));

        // Adiciona o campo de data inicial.
        filtros.add(new JLabel("Data inicial:"));
        filtros.add(txtDataInicio);

        // Adiciona o campo de data final.
        filtros.add(new JLabel("Data final:"));
        filtros.add(txtDataFim);

        filtros.add(new JLabel("Situação:"));
        filtros.add(comboSituacao);

        botaoGerar.addActionListener(e -> gerarRelatorio());
        filtros.add(new JLabel(""));
        filtros.add(botaoGerar);

        // Posiciona os filtros na parte superior da interface.
        add(filtros, BorderLayout.NORTH);

        // Adiciona a tabela ao centro, com barras de rolagem.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Posiciona o total de registros na parte inferior.
        add(totalRegistros, BorderLayout.SOUTH);

    }

    // Método responsável por consultar e apresentar o relatório.
    private void gerarRelatorio() {

        try {
            LocalDate inicio = LocalDate.parse(txtDataInicio.getText().trim());
            LocalDate fim = LocalDate.parse(txtDataFim.getText().trim());
            String[] situacoes = {"todos", "ativo", "atrasado", "devolvido"};
            String situacao = situacoes[comboSituacao.getSelectedIndex()];
            botaoGerar.setEnabled(false);
            SwingTask.run(
                    this,
                    "Erro no relatório",
                    () -> controller.gerarPorPeriodo(inicio, fim, situacao),
                    this::exibirResultados,
                    ex -> {
                        botaoGerar.setEnabled(true);
                        SwingTask.showError(this, "Erro no relatório", ex);
                    }
            );
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe as datas no formato AAAA-MM-DD e confira o período.",
                    "Período inválido",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void exibirResultados(List<EmprestimoRelatorio> dados) {
        tableModel.setRowCount(0);
        for (EmprestimoRelatorio linha : dados) {
            tableModel.addRow(new Object[]{
                    linha.idEmprestimo(),
                    linha.nomeLeitor(),
                    linha.tituloLivro(),
                    linha.codigoExemplar(),
                    linha.dataEmprestimo(),
                    linha.dataPrevista(),
                    linha.dataDevolucao() == null ? "" : linha.dataDevolucao(),
                    linha.situacao()
            });
        }
        totalRegistros.setText("Total de registros: " + dados.size());
        botaoGerar.setEnabled(true);
    }
}
