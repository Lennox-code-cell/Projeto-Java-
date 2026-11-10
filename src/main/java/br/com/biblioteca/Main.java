package br.com.biblioteca;

/**
 * Classe principal que inicia a aplicação e abre a tela inicial do sistema.
 */

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

import br.com.biblioteca.view.TelaPrincipal;

import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        if (!FlatLightLaf.setup()) {
            throw new IllegalStateException("Não foi possível iniciar o tema da interface.");
        }
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.alternateRowColor", new Color(245, 247, 250));
        UIManager.put("Table.showHorizontalLines", true);
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
