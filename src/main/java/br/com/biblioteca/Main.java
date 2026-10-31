package br.com.biblioteca;

/**
 * Classe principal que inicia a aplicação e abre a tela inicial do sistema.
 */

import javax.swing.SwingUtilities;

import br.com.biblioteca.view.TelaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Inicializa a interface gráfica em uma thread adequada para Swing.
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
