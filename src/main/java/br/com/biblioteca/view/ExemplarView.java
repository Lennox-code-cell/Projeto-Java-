
package br.com.biblioteca.view;

// Importa o Controller responsável pelas operações dos exemplares.
import br.com.biblioteca.controller.ExemplarController;

// Importa o Controller responsável por consultar os livros cadastrados.
import br.com.biblioteca.controller.LivroController;

// Importa os Models utilizados nesta interface.
import br.com.biblioteca.model.Exemplar;
import br.com.biblioteca.model.Livro;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Exibe e organiza a interface gráfica de Exemplar.
 *
 * Permite cadastrar, atualizar, excluir e listar os exemplares
 * físicos dos livros registrados na biblioteca.
 */
public class ExemplarView extends JPanel {

    // Controller responsável por cadastrar, atualizar,
    // excluir e consultar exemplares.
    private final ExemplarController controller = new ExemplarController();

    // Controller utilizado para consultar os livros cadastrados.
    private final LivroController livroController = new LivroController();

    // Seletor editável para localizar o livro pelo ISBN.
    private final JComboBox<Livro> comboLivro = new JComboBox<>();

    // Campo de texto utilizado para informar o código do exemplar.
    private final JTextField txtCodigo = new JTextField();

    // Campo de texto utilizado para informar a situação do exemplar.
    // Seu valor inicial é "disponivel".
    private final JTextField txtSituacao = new JTextField("disponivel");

    // Tabela responsável por exibir os exemplares cadastrados.
    private final JTable tabela = new JTable();
    private List<Exemplar> exemplares = List.of();

    // Modelo que define as colunas da tabela.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Livro", "Código", "Situação"}, 0
    );

    // Construtor responsável por configurar a interface.
    public ExemplarView() {

        // Define o layout principal com espaçamento de 10 pixels.
        setLayout(new BorderLayout(10, 10));

        // Associa o modelo de dados à tabela.
        tabela.setModel(tableModel);
        tabela.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        configurarSeletorLivro();

        // Cria o painel do formulário com 4 linhas e 2 colunas.
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));

        // Adiciona um título ao formulário.
        form.setBorder(BorderFactory.createTitledBorder("Cadastro de exemplar"));

        // Adiciona o campo de seleção do livro pelo ISBN.
        form.add(new JLabel("ISBN do livro:"));
        form.add(comboLivro);

        // Adiciona o campo de código do exemplar.
        form.add(new JLabel("Código:"));
        form.add(txtCodigo);

        // Adiciona o campo de situação.
        form.add(new JLabel("Situação:"));
        form.add(txtSituacao);

        // Adiciona um espaço vazio para completar a organização do GridLayout.
        form.add(new JLabel(""));

        // Cria um painel para organizar os botões.
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Cria os botões responsáveis pelas operações.
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

        // Carrega os livros no campo de seleção.
        carregarCombos();

        // Carrega os exemplares cadastrados na tabela.
        carregarTabela();

        // Adiciona um listener para detectar a seleção de uma linha.
        tabela.getSelectionModel().addListSelectionListener(e -> {

            // Verifica se a seleção terminou de mudar
            // e se existe uma linha selecionada.
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {

                // Obtém o índice da linha selecionada.
                int row = tabela.getSelectedRow();

                // Preenche o campo de código com o valor da coluna 2.
                txtCodigo.setText(String.valueOf(tabela.getValueAt(row, 2)));

                // Preenche o campo de situação com o valor da coluna 3.
                txtSituacao.setText(String.valueOf(tabela.getValueAt(row, 3)));

                Exemplar selecionado = exemplarSelecionado();
                if (selecionado != null) {
                    selecionarLivro(selecionado.getIdLivro());
                }
            }
        });
    }

    // Método responsável por cadastrar um novo exemplar.
    private void salvar() {
        try {

            Livro livro = obterLivroInformado();

            // Verifica se o ISBN informado corresponde a um livro cadastrado.
            if (livro == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe ou selecione o ISBN de um livro cadastrado."
                );

                // Interrompe o método.
                return;
            }

            // Envia o ID do livro, o código e a situação
            // para o Controller realizar o cadastro.
            controller.cadastrar(
                    livro.getIdLivro(),
                    txtCodigo.getText(),
                    txtSituacao.getText()
            );

            // Limpa os campos do formulário.
            limparCampos();

            // Atualiza a tabela para exibir o novo exemplar.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Exemplar cadastrado com sucesso!"
            );

        } catch (IllegalArgumentException | SQLException ex) {

            // Exibe erros de validação ou de acesso ao banco de dados.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Método responsável por atualizar um exemplar existente.
    private void atualizar() {

        // Obtém o índice da linha selecionada.
        int row = tabela.getSelectedRow();

        // Verifica se existe um exemplar selecionado.
        if (row < 0) {

            // Informa que é necessário selecionar um exemplar.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um exemplar na tabela."
            );

            // Interrompe o método.
            return;
        }

        try {

            Livro livro = obterLivroInformado();

            // Recupera o ID do exemplar na primeira coluna da tabela.
            Exemplar selecionado = exemplarSelecionado();
            if (selecionado == null) {
                return;
            }
            int id = selecionado.getIdExemplar();

            // Verifica se algum livro foi selecionado.
            if (livro == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe ou selecione o ISBN de um livro cadastrado."
                );

                return;
            }

            // Envia ao Controller o ID do exemplar e os novos dados.
            controller.atualizar(
                    id,
                    livro.getIdLivro(),
                    txtCodigo.getText(),
                    txtSituacao.getText()
            );

            // Limpa os campos após a atualização.
            limparCampos();

            // Atualiza a tabela com os dados modificados.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Exemplar atualizado com sucesso!"
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

    // Método responsável por excluir um exemplar.
    private void excluir() {

        // Obtém a linha selecionada na tabela.
        int row = tabela.getSelectedRow();

        // Verifica se existe algum exemplar selecionado.
        if (row < 0) {

            // Informa que é necessário selecionar um exemplar.
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um exemplar para excluir."
            );

            // Interrompe a execução.
            return;
        }
        if (!Dialogos.confirmarExclusao(this, "o exemplar selecionado")) {
            return;
        }

        try {

            // Recupera o ID do exemplar selecionado.
            Exemplar selecionado = exemplarSelecionado();
            if (selecionado == null) {
                return;
            }
            int id = selecionado.getIdExemplar();

            // Solicita ao Controller a exclusão do exemplar.
            controller.excluir(id);

            // Limpa os campos do formulário.
            limparCampos();

            // Atualiza a tabela após a exclusão.
            carregarTabela();

            // Exibe uma mensagem de sucesso.
            JOptionPane.showMessageDialog(
                    this,
                    "Exemplar excluído com sucesso!"
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

    // Método responsável por carregar os livros no JComboBox.
    private void carregarCombos() {
        try {

            // Remove os livros anteriormente carregados.
            comboLivro.removeAllItems();

            // Consulta todos os livros cadastrados.
            for (Livro livro : livroController.listarTodos()) {

                // Adiciona cada livro ao campo de seleção.
                comboLivro.addItem(livro);
            }

        } catch (SQLException ex) {

            // Exibe erro caso não seja possível consultar os livros.
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro ao carregar livros",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void configurarSeletorLivro() {
        comboLivro.setEditable(true);
        comboLivro.setRenderer(new DefaultListCellRenderer() {
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
                if (valor instanceof Livro livro) {
                    String isbn = livro.getIsbn() == null || livro.getIsbn().isBlank()
                            ? "Sem ISBN"
                            : livro.getIsbn();
                    setText(isbn + " - " + livro.getTitulo());
                }
                return this;
            }
        });
        comboLivro.setEditor(new BasicComboBoxEditor() {
            @Override
            public void setItem(Object item) {
                if (item instanceof Livro livro) {
                    editor.setText(livro.getIsbn() == null || livro.getIsbn().isBlank()
                            ? livro.getTitulo()
                            : livro.getIsbn());
                } else {
                    super.setItem(item);
                }
            }
        });
    }

    private Livro obterLivroInformado() {
        Object itemSelecionado = comboLivro.getSelectedItem();
        if (itemSelecionado instanceof Livro livro) {
            return livro;
        }

        Object itemEditado = comboLivro.getEditor().getItem();
        String valor = itemEditado == null ? "" : itemEditado.toString().trim();
        if (valor.isEmpty()) {
            return null;
        }

        String isbnInformado = valor.replaceAll("[-\\s]", "");
        for (int i = 0; i < comboLivro.getItemCount(); i++) {
            Livro livro = comboLivro.getItemAt(i);
            String isbnCadastrado = livro.getIsbn();
            if (isbnCadastrado != null
                    && isbnCadastrado.replaceAll("[-\\s]", "")
                    .equalsIgnoreCase(isbnInformado)) {
                return livro;
            }
            if ((isbnCadastrado == null || isbnCadastrado.isBlank())
                    && livro.getTitulo() != null
                    && livro.getTitulo().equalsIgnoreCase(valor)) {
                return livro;
            }
        }
        return null;
    }

    private Exemplar exemplarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = tabela.convertRowIndexToModel(row);
        return modelRow < exemplares.size() ? exemplares.get(modelRow) : null;
    }

    private void selecionarLivro(int idLivro) {
        for (int i = 0; i < comboLivro.getItemCount(); i++) {
            if (comboLivro.getItemAt(i).getIdLivro() == idLivro) {
                comboLivro.setSelectedIndex(i);
                return;
            }
        }
    }

    // Método responsável por carregar os exemplares na tabela.
    private void carregarTabela() {

        // Remove todas as linhas atuais da tabela.
        tabela.clearSelection();
        tableModel.setRowCount(0);

        try {

            // Consulta todos os exemplares cadastrados.
            exemplares = controller.listarTodos();

            // Percorre a lista de exemplares retornada pelo Controller.
            for (Exemplar exemplar : exemplares) {

                // Inicializa o nome do livro como texto vazio.
                String nomeLivro = "";

                // Percorre os livros carregados no JComboBox.
                for (int i = 0; i < comboLivro.getItemCount(); i++) {

                    // Recupera o livro na posição atual.
                    Livro livro = comboLivro.getItemAt(i);

                    // Compara o ID do livro com o ID associado ao exemplar.
                    if (livro.getIdLivro() == exemplar.getIdLivro()) {

                        // Se os IDs forem iguais, recupera o título do livro.
                        nomeLivro = livro.getTitulo();

                        // Interrompe a busca porque o livro foi encontrado.
                        break;
                    }
                }

                // Adiciona os dados do exemplar como uma nova linha da tabela.
                tableModel.addRow(new Object[]{
                        exemplar.getIdExemplar(),
                        nomeLivro,
                        exemplar.getCodigo(),
                        exemplar.getSituacao()
                });
            }

        } catch (SQLException ex) {

            // Exibe erro caso não seja possível consultar os exemplares.
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

        // Limpa o campo de código.
        txtCodigo.setText("");

        // Restaura a situação padrão para "disponivel".
        txtSituacao.setText("disponivel");

        // Remove a seleção atual da tabela.
        tabela.clearSelection();
    }
}
