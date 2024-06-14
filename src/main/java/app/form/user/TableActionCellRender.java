package app.form.user;

import app.toolbox.RowStylePanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class TableActionCellRender implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component com = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        PanelAction action = new PanelAction();

        String currentTheme = UIManager.getLookAndFeel().getName();

        RowStylePanel.setPanelStyle(com, row, currentTheme);
        RowStylePanel.setPanelStyle(action, row, currentTheme);


        if (isSelected) {

            RowStylePanel.setTableStyle(table, action, currentTheme);

        }

        return action;
    }


}