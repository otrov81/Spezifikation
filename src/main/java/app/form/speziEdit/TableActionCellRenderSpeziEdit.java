package app.form.speziEdit;


import app.form.user.PanelAction;
import app.toolbox.RowStylePanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class TableActionCellRenderSpeziEdit implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component com = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        PanelActionEdit action = new PanelActionEdit();

        String currentTheme = UIManager.getLookAndFeel().getName();

        RowStylePanel.setPanelStyle(com, row, currentTheme);
        RowStylePanel.setPanelStyle(action, row, currentTheme);


        if (isSelected) {

            RowStylePanel.setTableStyle(table, action, currentTheme);

        }

        return action;
    }
}
