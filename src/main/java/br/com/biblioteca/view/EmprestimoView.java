
package br.com.biblioteca.view;

// Importa o Controller responsável pelas regras e operações de empréstimo.
import br.com.biblioteca.controller.EmprestimoController;

// Importa os Models utilizados nos campos de seleção.
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Leitor;
import br.com.biblioteca.model.EmprestimoRelatorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Exibe e organiza a interface gráfica de Emprestimo.
 *
 * Permite registrar empréstimos, registrar devoluções,
 * selecionar leitores e exemplares disponíveis e consultar
 * os empréstimos ativos da biblioteca.
 */
public class EmprestimoView extends JPanel {

    // Controller responsável por coordenar as operações de empréstimo.
    private final EmprestimoController controller = new EmprestimoController();

    // JComboBox que permite selecionar um leitor cadastrado.
    private final JComboBox<Leitor> comboLeitor = new JComboBox<>();

    // JComboBox que permite selecionar um exemplar disponível.
    private final JComboBox<Exemplar> comboExemplar = new JComboBox<>();

    // Campo de data do empréstimo.
    // Inicialmente preenchido com a data atual.
    private final JTextField txtDataEmprestimo = new JTextField(
            LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    );

    // Campo de data prevista para devolução.
    // Inicialmente preenchido com a data atual acrescida de 7 dias.
    private final JTextField txtDataPrevista = new JTextField(
            LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE)
    );

    // Tabela responsável por apresentar os empréstimos ativos.
    private final JTable tabela = new JTable();
    private final JButton botaoEmprestar = new JButton("Emprestar");
    private final JButton botaoDevolver = new JButton("Registrar devolução");

    // Modelo que define as colunas exibidas na tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Leitor",
                    "Livro",
                    "Exemplar",
                    "Data Empréstimo",
                    "Data Prevista",
                    "Situação"
            },
            0
    );

    // Construtor responsável por montar e inicializar a interface.
    public EmprestimoView() {

        // Define o layout principal com espaçamento de 10 pixels.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        // Cria o painel do formulário com 5 linhas e 2 colunas.
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));

        // Adiciona um título ao formulário.
        form.setBorder(BorderFactory.createTitledBorder("Empréstimo"));

        // Adiciona o campo de seleção do leitor.
        form.add(new JLabel("Leitor:"));
        form.add(comboLeitor);

        // Adiciona o campo de seleção do exemplar.
        form.add(new JLabel("Exemplar disponível:"));
        form.add(comboExemplar);

        // Adiciona o campo da data do empréstimo.
        form.add(new JLabel("Data empréstimo:"));
        form.add(txtDataEmprestimo);

        // Adiciona o campo da data prevista para devolução.
        form.add(new JLabel("Data prevista:"));
        form.add(txtDataPrevista);

        // Cria um painel para organizar os botões.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria os botões de empréstimo e devolução.
        // Define as ações executadas ao clicar nos botões.
        botaoEmprestar.addActionListener(e -> emprestar());
        botaoDevolver.addActionListener(e -> devolver());

        // Adiciona os botões ao painel.
        botoes.add(botaoEmprestar);
        botoes.add(botaoDevolver);

        // Adiciona o painel de botões ao formulário.
        form.add(botoes);

        // Posiciona o formulário na parte superior da interface.
        add(form, BorderLayout.NORTH);

        // Adiciona a tabela ao centro, permitindo rolagem.
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarDados();
    }

    // Método responsável por registrar um novo empréstimo.
    private void emprestar() {
        try {
            Leitor leitor = (Leitor) comboLeitor.getSelectedItem();
            Exemplar exemplar = (Exemplar) comboExemplar.getSelectedItem();
            if (leitor == null || exemplar == null) {
                JOptionPane.showMessageDialog(this, "Selecione um leitor e um exemplar.");
                return;
            }
            int idExemplar = exemplar.getIdExemplar();
            int idLeitor = leitor.getIdLeitor();
            LocalDate dataEmprestimo = LocalDate.parse(txtDataEmprestimo.getText().trim());
            LocalDate dataPrevista = LocalDate.parse(txtDataPrevista.getText().trim());
            iniciarOperacao(
                    () -> controller.registrarEmprestimo(
                            idExemplar, idLeitor, dataEmprestimo, dataPrevista),
                    "Empréstimo registrado com sucesso!"
            );
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            SwingTask.showError(this, "Dados do empréstimo inválidos", ex);
        }
    }

    // Método responsável por registrar a devolução de um exemplar.
    private void devolver() {

        // Obtém o índice da linha selecionada na tabela.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum empréstimo selecionado.
        if (row < 0) {

            // Solicita que o usuário selecione um empréstimo.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um empréstimo para registrar a devolução."
            );

            // Interrompe o método.
            return;
        }
        if (JOptionPane.showConfirmDialog(
                this,
                "Confirmar a devolução do exemplar selecionado?",
                "Confirmar devolução",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }
        int id = (int) tabela.getValueAt(row, 0);
        iniciarOperacao(
                () -> controller.registrarDevolucao(id, LocalDate.now()),
                "Devolução registrada com sucesso!"
        );
    }

    private void iniciarOperacao(SwingTask.SqlOperation operation, String successMessage) {
        definirBotoesAtivos(false);
        SwingTask.run(
                this,
                "Erro na operação",
                () -> {
                    operation.run();
                    return carregarDadosDoBanco();
                },
                dados -> {
                    exibirDados(dados);
                    definirBotoesAtivos(true);
                    JOptionPane.showMessageDialog(this, successMessage);
                },
                ex -> {
                    definirBotoesAtivos(true);
                    SwingTask.showError(this, "Erro na operação", ex);
                }
        );
    }

    private void carregarDados() {
        definirBotoesAtivos(false);
        SwingTask.run(
                this,
                "Erro ao carregar empréstimos",
                this::carregarDadosDoBanco,
                dados -> {
                    exibirDados(dados);
                    definirBotoesAtivos(true);
                },
                ex -> {
                    definirBotoesAtivos(true);
                    SwingTask.showError(this, "Erro ao carregar empréstimos", ex);
                }
        );
    }

    private DadosTela carregarDadosDoBanco() throws SQLException {
        return new DadosTela(
                controller.listarLeitoresAtivos(),
                controller.listarExemplaresDisponiveis(),
                controller.listarEmprestimosAtivos()
        );
    }

    private void exibirDados(DadosTela dados) {
        comboLeitor.removeAllItems();
        dados.leitores().forEach(comboLeitor::addItem);
        comboExemplar.removeAllItems();
        dados.exemplares().forEach(comboExemplar::addItem);
        tableModel.setRowCount(0);
        for (EmprestimoRelatorio linha : dados.emprestimos()) {
            tableModel.addRow(new Object[]{
                    linha.idEmprestimo(),
                    linha.nomeLeitor(),
                    linha.tituloLivro(),
                    linha.codigoExemplar(),
                    linha.dataEmprestimo(),
                    linha.dataPrevista(),
                    linha.situacao()
            });
        }
    }

    private void definirBotoesAtivos(boolean ativos) {
        botaoEmprestar.setEnabled(ativos);
        botaoDevolver.setEnabled(ativos);
    }

    private record DadosTela(
            List<Leitor> leitores,
            List<Exemplar> exemplares,
            List<EmprestimoRelatorio> emprestimos
    ) {
    }
}
