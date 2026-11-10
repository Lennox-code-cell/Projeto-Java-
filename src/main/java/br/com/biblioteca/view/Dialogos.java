package br.com.biblioteca.view;

import javax.swing.JOptionPane;
import java.awt.Component;

final class Dialogos {
    private Dialogos() {
    }

    static boolean confirmarExclusao(Component parent, String entidade) {
        return JOptionPane.showConfirmDialog(
                parent,
                "Deseja excluir " + entidade + "? O histórico relacionado será preservado.",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }
}
