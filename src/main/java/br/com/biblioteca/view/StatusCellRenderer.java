package br.com.biblioteca.view;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.Locale;

final class StatusCellRenderer extends DefaultTableCellRenderer {
    private static final Color AVAILABLE = new Color(225, 245, 233);
    private static final Color ACTIVE = new Color(230, 240, 255);
    private static final Color LOANED = new Color(255, 244, 214);
    private static final Color OVERDUE = new Color(255, 229, 229);
    private static final Color UNAVAILABLE = new Color(235, 237, 240);

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );
        if (!isSelected && value != null) {
            String status = value.toString().toLowerCase(Locale.ROOT);
            if (status.equals("disponivel") || status.equals("devolvido")) {
                component.setBackground(AVAILABLE);
            } else if (status.equals("emprestado")) {
                component.setBackground(LOANED);
            } else if (status.equals("atrasado")) {
                component.setBackground(OVERDUE);
            } else if (status.equals("ativo")) {
                component.setBackground(ACTIVE);
            } else if (status.equals("indisponivel")) {
                component.setBackground(UNAVAILABLE);
            }
        }
        return component;
    }
}
