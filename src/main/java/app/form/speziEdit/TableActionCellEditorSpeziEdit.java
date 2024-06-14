package app.form.speziEdit;



import app.form.user.PanelAction;
import app.toolbox.RowStylePanel;

import javax.swing.*;
import java.awt.*;

public class TableActionCellEditorSpeziEdit extends DefaultCellEditor {

    private TableActionEventEdit event;

    public TableActionCellEditorSpeziEdit(TableActionEventEdit event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        return RowActionCellEditor(table, row);
    }

    private PanelActionEdit RowActionCellEditor(JTable table, int row) {
        PanelActionEdit action = new PanelActionEdit();

        action.initEvent(event, row);

        action.setBackground(table.getSelectionBackground());
        String currentTheme = UIManager.getLookAndFeel().getName();

        RowStylePanel.setTableStyle(table, action, currentTheme);


        return action;
    }

}

