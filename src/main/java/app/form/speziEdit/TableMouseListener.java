package app.form.speziEdit;

import app.model.SpeziTextart;
import app.model.SpeziTexte;
import app.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static app.form.speziEdit.SpeziEdit.*;

public class TableMouseListener extends MouseAdapter {

    @Autowired
    private DBService dbService;
    private JTable table;
    private JTextField txtKey;


    public TableMouseListener(DBService dbService, JTable table, JTextField txtKey) {
        this.dbService = dbService;
        this.table = table;
        this.txtKey = txtKey;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // tarTitel.setText("");
        System.out.println("*******************");

        int column = table.columnAtPoint(e.getPoint());
        //int row = table.rowAtPoint(e.getPoint());

// Provera da li je indeks kolone jednak 0 (prva kolona)
        if (column != 0) {

            //super.mouseClicked(e);
            int row = table.getSelectedRow();
            String key = (String) table.getValueAt(row, 4);
            String titel = (String) table.getValueAt(row, 5);
            String znr = (String) table.getValueAt(row, 2);
            String textart = (String) table.getValueAt(row, 3);
            String text = (String) table.getValueAt(row, 6);

            // Dodajte tekst
            //List<SpeziTexte> textListItem = new ArrayList<>();
            List<SpeziTexte> getTextItem = dbService.selectSpeziTexteByKey(key, txtScpd.getText());

            labAutoname.setText("");
            labItem.setText("");
            for (SpeziTexte speziTexte : getTextItem) {


                labAutoname.setText("<html><b>" + textart + "</b></html>");

                if (text != null && text.length() > 2) {
                    labItem.setText("<html>" + text.replaceAll("&", "&amp;")
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;")
                            .replaceAll("\"", "&quot;")
                            .replaceAll("'", "&apos;")
                            .replaceAll("\n", "<br>") + "</html>");

                }else{
                    System.out.println("null text!!!!");
                }

            }


            if (textart != null && !textart.isEmpty()) {
                int index = -1;
                for (int i = 0; i < dropTextart.getItemCount(); i++) {
                    SpeziTextart speziTextart = dropTextart.getItemAt(i);
                    if (speziTextart.getTextart().equals(textart)) {
                        index = i;
                        break;
                    }
                }
                dropTextart.setSelectedIndex(index);
            }

            txtZrn.setText(znr);
            txtKey.setText(key);
            tarTitel.setText(titel);
            //tarTitel.setText("");
            //tarTitel.setText("<html><b>" + (!titel.isEmpty() ? titel : "") + "</b></html>");
            tarText.setText(text);


//        // dopel click
            if (e.getClickCount() == 2) {
            }
        }
    }
}

