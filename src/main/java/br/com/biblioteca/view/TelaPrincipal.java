
package br.com.biblioteca.view;

/**
 * Representa a janela principal do sistema Biblioteca.
 *
 * Responsável por reunir e organizar as telas do sistema
 * em uma única interface gráfica, utilizando abas de navegação.
 */

// Importa os componentes gráficos do Swing.
import javax.swing.*;

// Importa classes de layout e formatação visual.
import java.awt.*;

// Herda JFrame para representar uma janela principal.
public class TelaPrincipal extends JFrame {

    // Construtor responsável por montar a janela principal.
    public TelaPrincipal() {

        // Define o título exibido na barra superior da janela.
        setTitle("Biblioteca");

        // Define que o programa será encerrado ao fechar a janela.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define as dimensões da janela: 1250 pixels de largura
        // e 800 pixels de altura.
        setSize(1250, 800);

        // Posiciona a janela no centro da tela.
        setLocationRelativeTo(null);

        // Cria um componente que organiza outras telas em abas.
        JTabbedPane abas = new JTabbedPane();

        // Adiciona a tela de gerenciamento de autores.
        abas.addTab("Autores", new AutorView());

        // Adiciona a tela de gerenciamento de categorias.
        abas.addTab("Categorias", new CategoriaView());

        // Adiciona a tela de gerenciamento de leitores.
        abas.addTab("Leitores", new LeitorView());

        // Adiciona a tela de gerenciamento de livros.
        abas.addTab("Livros", new LivroView());

        // Adiciona a tela de gerenciamento de exemplares.
        abas.addTab("Exemplares", new ExemplarView());

        // Adiciona a tela de gerenciamento de empréstimos.
        abas.addTab("Empréstimos", new EmprestimoView());

        // Adiciona a tela de relatórios.
        abas.addTab("Relatórios", new RelatorioView());

        // Cria o título principal da aplicação.
        // SwingConstants.CENTER centraliza o texto no componente.
        JLabel titulo = new JLabel(
                "Sistema de Gerenciamento de Biblioteca",
                SwingConstants.CENTER
        );

        // Define a fonte, o estilo em negrito e o tamanho do título.
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Adiciona espaçamento ao redor do título.
        // Valores: topo, esquerda, inferior e direita.
        titulo.setBorder(
                BorderFactory.createEmptyBorder(20, 0, 10, 0)
        );

        // Cria um painel para organizar o cabeçalho.
        JPanel topo = new JPanel(new BorderLayout());

        // Posiciona o título no centro do painel superior.
        topo.add(titulo, BorderLayout.CENTER);

        // Adiciona o cabeçalho na parte superior da janela.
        add(topo, BorderLayout.NORTH);

        // Adiciona o conjunto de abas na região central da janela.
        add(abas, BorderLayout.CENTER);
    }
}
