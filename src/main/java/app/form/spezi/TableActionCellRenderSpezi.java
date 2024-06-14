package app.form.spezi;

import app.toolbox.RowStylePanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class TableActionCellRenderSpezi implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component com = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        PanelActionSpezi action = new PanelActionSpezi();
        String currentTheme = UIManager.getLookAndFeel().getName();

        RowStylePanel.setPanelStyle(com, row, currentTheme);
        RowStylePanel.setPanelStyle(action, row, currentTheme);


//        PanelActionSpezi action = new PanelActionSpezi();
//
//        String currentTheme = UIManager.getLookAndFeel().getName();
//        GetRowStyle(row, currentTheme, com, action);


//        if(currentTheme == "FlatLaf macOS Light" || currentTheme == "FlatLaf Light" ) {
//            if (row % 2 == 0) {
//                com.setBackground(new Color(0xECF1F9));
//                action.setBackground(com.getBackground());
//            } else{
//                com.setBackground(Color.WHITE);
//                action.setBackground(com.getBackground());
//            }
//        }else {
//            if (row % 2 == 0) {
//                com.setBackground(new Color(0xB1E0E6));
//                action.setBackground(com.getBackground());
//            } else{
//                com.setBackground(new Color(0xEEF0F4));
//                action.setBackground(com.getBackground());
//            }
//
//        }


        if(isSelected){
            RowStylePanel.setTableStyle(table, action, currentTheme);


//            if(currentTheme == "FlatLaf macOS Light" || currentTheme == "FlatLaf Light" ) {
//                table.setSelectionBackground(new Color(0x2675BF));
//                table.setSelectionForeground(Color.WHITE);
//                action.setBackground(new Color(0x2675BF));
//
//            }else{
//                table.setSelectionBackground(new Color(0x3EB2C2));
//                table.setSelectionForeground(Color.WHITE);
//                action.setBackground(new Color(0x3EB2C2));
//
//            }

        }
        return action;
    }


}