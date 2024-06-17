package app.form.user;


import app.toolbox.RowStylePanel;

import javax.swing.*;
import java.awt.*;

public class TableActionCellEditor extends DefaultCellEditor{

    private TableActionEvent event;

    public TableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        return RowActionCellEditor(table, row);
    }

    private PanelAction RowActionCellEditor(JTable table, int row) {
        PanelAction action = new PanelAction();

        action.initEvent(event, row);

        action.setBackground(table.getSelectionBackground());
        String currentTheme = UIManager.getLookAndFeel().getName();

        RowStylePanel.setTableStyle(table, action, currentTheme);


        return action;
    }


}