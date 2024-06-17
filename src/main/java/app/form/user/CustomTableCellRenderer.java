package app.form.user;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private int idNr;

    public CustomTableCellRenderer(String i) {
        this.idNr = Integer.parseInt(i);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        String currentTheme = UIManager.getLookAndFeel().getName();


        if(currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light")) {
            System.out.println("1----------------------");


            if (row % 2 == 0) {
                com.setBackground(new Color(0xECF1F9));
                //action.setBackground(com.getBackground());
                System.out.println("2");

            } else{
                System.out.println("3");

                com.setBackground(Color.WHITE);
                //action.setBackground(com.getBackground());
            }
            com.setForeground(Color.BLACK);


            if (idNr == row) {
                com.setBackground(new Color(0x2675BF));
                com.setForeground(Color.WHITE);
                idNr = -1;
            }

        } else if (currentTheme.equals("FlatLaf macOS Dark") || currentTheme.equals("FlatLaf Dark")) {

            if (row % 2 == 0) {
                com.setBackground(new Color(0x3C3F41));
                com.setBackground(com.getBackground());

            } else{

                com.setBackground(new Color(0x46494B));
                com.setBackground(com.getBackground());
            }
            if (idNr == row) {
                com.setBackground(new Color(0x2675BF));
                com.setForeground(Color.WHITE);
                idNr = -1;
            }

        } else {
            System.out.println("4---------------");

            if (row % 2 == 0) {
                System.out.println("5");

                com.setBackground(new Color(0xB1E0E6));
                //action.setBackground(com.getBackground());

            } else{
                System.out.println("6");

                com.setBackground(new Color(0xEEF0F4));
                //action.setBackground(com.getBackground());
            }
            com.setForeground(Color.BLACK);
            if (idNr == row) {
                com.setBackground(new Color(0x3EB2C2));
                com.setForeground(Color.WHITE);
                idNr = -1;
            }

        }


        if(isSelected){

            if("FlatLaf macOS Light".equals(currentTheme) || "FlatLaf macOS Light".equals(currentTheme) ) {
                com.setBackground(new Color(0x2675BF));
                com.setForeground(Color.white);

            } else if ("FlatLaf macOS Dark".equals(currentTheme) || "FlatLaf macOS Dark".equals(currentTheme)) {

                com.setBackground(new Color(0x2675BF));
                com.setForeground(Color.white);

                if (idNr == row) {
                    com.setBackground(new Color(0x2675BF));
                    com.setForeground(Color.white);
                    idNr = -1;
                }

            } else{
                com.setBackground(new Color(0x3EB2C2));
                com.setForeground(Color.white);

                if (idNr == row) {
                    com.setBackground(new Color(0x3EB2C2));
                    com.setForeground(Color.WHITE);
                    idNr = -1;
                }

            }

        }

        return com;
    }

}
