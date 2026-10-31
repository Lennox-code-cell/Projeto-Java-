package br.com.biblioteca.view;

/**
 * Representa a entidade TelaPrincipal do domínio da biblioteca.
 */

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    public TelaPrincipal() {
        setTitle("Biblioteca Fácil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 800);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Autores", new AutorView());
        abas.addTab("Categorias", new CategoriaView());
        abas.addTab("Leitores", new LeitorView());
        abas.addTab("Livros", new LivroView());
        abas.addTab("Exemplares", new ExemplarView());
        abas.addTab("Empréstimos", new EmprestimoView());
        abas.addTab("Relatórios", new RelatorioView());

        JLabel titulo = new JLabel("Sistema de Gerenciamento de Biblioteca", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(titulo, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }
}
