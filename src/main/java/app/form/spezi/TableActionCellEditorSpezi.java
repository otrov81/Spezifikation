package app.form.spezi;


import app.toolbox.RowStylePanel;

import javax.swing.*;
import java.awt.*;

public class TableActionCellEditorSpezi extends DefaultCellEditor {

    private TableActionEventSpezi event;

    public TableActionCellEditorSpezi(TableActionEventSpezi event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        PanelActionSpezi action = new PanelActionSpezi();

        action.initEvent(event, row);

        action.setBackground(table.getSelectionBackground());
        String currentTheme = UIManager.getLookAndFeel().getName();
        RowStylePanel.setTableStyle(table, action, currentTheme);

//        if(currentTheme == "FlatLaf macOS Light" || currentTheme == "FlatLaf Light" ) {
//            table.setSelectionBackground(new Color(0x2675BF));
//            action.setBackground(new Color(0x2675BF));
//            table.setSelectionForeground(Color.WHITE);
//        }else{
//            table.setSelectionBackground(new Color(0x3EB2C2));
//            action.setBackground(new Color(0x3EB2C2));
//            table.setSelectionForeground(Color.WHITE);
//        }
        return action;
    }


}