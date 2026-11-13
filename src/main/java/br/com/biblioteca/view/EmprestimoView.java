
package br.com.biblioteca.view;

// Importa o Controller responsável pelas regras e operações de empréstimo.
import br.com.biblioteca.controller.EmprestimoController;
import br.com.biblioteca.controller.LivroController;

// Importa os Models utilizados nos campos de seleção.
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Leitor;
import br.com.biblioteca.model.EmprestimoRelatorio;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
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
    private final LivroController livroController = new LivroController();
    private List<Livro> livros = List.of();

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
    private final JButton botaoAlterar = new JButton("Alterar");
    private final JButton botaoExcluir = new JButton("Excluir/Cancelar");
    private List<EmprestimoRelatorio> emprestimos = List.of();

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
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarSeletorExemplar();

        // Cria o painel do formulário com 5 linhas e 2 colunas.
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));

        // Adiciona um título ao formulário.
        form.setBorder(BorderFactory.createTitledBorder("Empréstimo"));

        // Adiciona o campo de seleção do leitor.
        form.add(new JLabel("Leitor:"));
        form.add(comboLeitor);

        // Adiciona o campo de seleção do exemplar pelo ISBN.
        form.add(new JLabel("ISBN do livro:"));
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
        botaoAlterar.addActionListener(e -> alterar());
        botaoExcluir.addActionListener(e -> excluir());
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarEmprestimoSelecionado();
            }
        });

        // Adiciona os botões ao painel.
        botoes.add(botaoEmprestar);
        botoes.add(botaoDevolver);
        botoes.add(botaoAlterar);
        botoes.add(botaoExcluir);

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
            Exemplar exemplar = obterExemplarInformado();
            if (leitor == null || exemplar == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um leitor e informe ou selecione o ISBN de um exemplar disponível."
                );
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
        EmprestimoRelatorio selecionado = emprestimoSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para registrar a devolução.");
            return;
        }
        int id = selecionado.idEmprestimo();
        iniciarOperacao(
                () -> controller.registrarDevolucao(id, LocalDate.now()),
                "Devolução registrada com sucesso!"
        );
    }

    private void alterar() {
        EmprestimoRelatorio selecionado = emprestimoSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo em aberto para alterar.");
            return;
        }
        if (selecionado.dataDevolucao() != null) {
            JOptionPane.showMessageDialog(this, "Empréstimos finalizados não podem ser alterados.");
            return;
        }
        try {
            LocalDate dataEmprestimo = LocalDate.parse(txtDataEmprestimo.getText().trim());
            LocalDate dataPrevista = LocalDate.parse(txtDataPrevista.getText().trim());
            iniciarOperacao(
                    () -> controller.atualizarEmprestimo(
                            selecionado.idEmprestimo(), dataEmprestimo, dataPrevista),
                    "Empréstimo alterado com sucesso!"
            );
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            SwingTask.showError(this, "Dados do empréstimo inválidos", ex);
        }
    }

    private void excluir() {
        EmprestimoRelatorio selecionado = emprestimoSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para excluir ou cancelar.");
            return;
        }
        boolean emAberto = selecionado.dataDevolucao() == null;
        String mensagem = emAberto
                ? "Cancelar este empréstimo e liberar o exemplar associado?"
                : "Excluir este empréstimo finalizado? O registro será removido do histórico.";
        String titulo = emAberto ? "Confirmar cancelamento" : "Confirmar exclusão";
        if (JOptionPane.showConfirmDialog(
                this, mensagem, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        ) != JOptionPane.YES_OPTION) {
            return;
        }
        iniciarOperacao(
                () -> controller.excluirEmprestimo(selecionado.idEmprestimo()),
                emAberto ? "Empréstimo cancelado com sucesso!"
                        : "Empréstimo excluído com sucesso!"
        );
    }

    private EmprestimoRelatorio emprestimoSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = tabela.convertRowIndexToModel(row);
        return modelRow < emprestimos.size() ? emprestimos.get(modelRow) : null;
    }

    private void carregarEmprestimoSelecionado() {
        EmprestimoRelatorio selecionado = emprestimoSelecionado();
        if (selecionado != null) {
            txtDataEmprestimo.setText(selecionado.dataEmprestimo().toString());
            txtDataPrevista.setText(selecionado.dataPrevista().toString());
        }
        botaoAlterar.setEnabled(selecionado != null && selecionado.dataDevolucao() == null);
        botaoDevolver.setEnabled(selecionado != null && selecionado.dataDevolucao() == null);
        botaoExcluir.setEnabled(selecionado != null);
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
                livroController.listarTodos(),
                controller.listarTodosEmprestimos()
        );
    }

    private void exibirDados(DadosTela dados) {
        tabela.clearSelection();
        livros = dados.livros();
        emprestimos = dados.emprestimos();
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

    private void configurarSeletorExemplar() {
        comboExemplar.setEditable(true);
        comboExemplar.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean selecionado,
                    boolean foco
            ) {
                super.getListCellRendererComponent(
                        lista,
                        valor,
                        indice,
                        selecionado,
                        foco
                );
                if (valor instanceof Exemplar exemplar) {
                    Livro livro = buscarLivro(exemplar.getIdLivro());
                    String isbn = livro == null || livro.getIsbn() == null
                            || livro.getIsbn().isBlank()
                            ? "Sem ISBN"
                            : livro.getIsbn();
                    setText(isbn + " - Exemplar: " + exemplar.getCodigo());
                }
                return this;
            }
        });
        comboExemplar.setEditor(new BasicComboBoxEditor() {
            @Override
            public void setItem(Object item) {
                if (item instanceof Exemplar exemplar) {
                    Livro livro = buscarLivro(exemplar.getIdLivro());
                    editor.setText(livro == null || livro.getIsbn() == null
                            || livro.getIsbn().isBlank()
                            ? exemplar.getCodigo()
                            : livro.getIsbn());
                } else {
                    super.setItem(item);
                }
            }
        });
    }

    private Exemplar obterExemplarInformado() {
        Object itemEditado = comboExemplar.getEditor().getItem();
        String valor = itemEditado == null ? "" : itemEditado.toString().trim();
        if (valor.isEmpty()) {
            return null;
        }

        String isbnInformado = valor.replaceAll("[-\\s]", "");
        for (int i = 0; i < comboExemplar.getItemCount(); i++) {
            Exemplar exemplar = comboExemplar.getItemAt(i);
            Livro livro = buscarLivro(exemplar.getIdLivro());
            if (livro != null && livro.getIsbn() != null
                    && livro.getIsbn().replaceAll("[-\\s]", "")
                    .equalsIgnoreCase(isbnInformado)) {
                return exemplar;
            }
            if ((livro == null || livro.getIsbn() == null || livro.getIsbn().isBlank())
                    && exemplar.getCodigo() != null
                    && exemplar.getCodigo().equalsIgnoreCase(valor)) {
                return exemplar;
            }
        }
        return null;
    }

    private Livro buscarLivro(int idLivro) {
        return livros.stream()
                .filter(livro -> livro.getIdLivro() == idLivro)
                .findFirst()
                .orElse(null);
    }

    private void definirBotoesAtivos(boolean ativos) {
        botaoEmprestar.setEnabled(ativos);
        if (!ativos) {
            botaoDevolver.setEnabled(false);
            botaoAlterar.setEnabled(false);
            botaoExcluir.setEnabled(false);
        } else {
            carregarEmprestimoSelecionado();
        }
    }

    private record DadosTela(
            List<Leitor> leitores,
            List<Exemplar> exemplares,
            List<Livro> livros,
            List<EmprestimoRelatorio> emprestimos
    ) {
    }
}
