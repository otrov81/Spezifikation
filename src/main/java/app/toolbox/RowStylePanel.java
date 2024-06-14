package app.toolbox;
import javax.swing.*;
import java.awt.*;

public class RowStylePanel  {
public static void setPanelStyle(Component panel, int row, String currentTheme) {
    if (currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light")) {
        if (row % 2 == 0) {
            panel.setBackground(new Color(0xECF1F9));
        } else {
            panel.setBackground(Color.WHITE);
        }
    } else if (currentTheme.equals("FlatLaf macOS Dark") || currentTheme.equals("FlatLaf Dark")) {
        if (row % 2 == 0) {
            panel.setBackground(new Color(0x3C3F41));
        } else {
            panel.setBackground(new Color(0x46494B));
        }
    } else {
        if (row % 2 == 0) {
            panel.setBackground(new Color(0xB1E0E6));
        } else {
            panel.setBackground(new Color(0xEEF0F4));
        }
    }
}
    public static void setTableStyle(JTable table, Component action, String currentTheme) {
        Color selectionBackground = null;
        Color selectionForeground = Color.WHITE;

        if (currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light")) {
            selectionBackground = new Color(0x2675BF);
        } else if (currentTheme.equals("FlatLaf macOS Dark") || currentTheme.equals("FlatLaf Dark")) {
            selectionBackground = new Color(0x2675BF);
        } else {
            selectionBackground = new Color(0x3EB2C2);
        }

        table.setSelectionBackground(selectionBackground);
        table.setSelectionForeground(selectionForeground);
        action.setBackground(selectionBackground);
    }

}
