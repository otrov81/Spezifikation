package app.form.speziEdit;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRendererSpezi {
    public static void setRenderer(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Ako je red paran, postavljamo boju pozadine na svjetlo sivu, inače na bijelu
                String currentTheme = UIManager.getLookAndFeel().getName();

                if (currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light")) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(0xECF1F9));

                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(0xB1E0E6));
                    } else {
                        c.setBackground(new Color(0xEEF0F4));
                    }
                }

                if (isSelected) {
                    if (currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light")) {
                        c.setBackground(new Color(0x2675BF));
                    } else {
                        c.setBackground(new Color(0x3EB2C2));
                    }

                }

                return c;
            }
        });
    }
}
