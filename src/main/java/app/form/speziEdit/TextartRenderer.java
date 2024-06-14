package app.form.speziEdit;

import app.model.SpeziTextart;

import javax.swing.*;

import static app.form.speziEdit.SpeziEdit.*;

public class TextartRenderer extends DefaultListCellRenderer {
    private boolean showFullName; // Varijabla koja označava treba li renderer prikazati ime i prezime ili samo ime

    public TextartRenderer(boolean showFullName) {
        this.showFullName = showFullName;
    }

    @Override
    public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof SpeziTextart) {
            SpeziTextart textart = (SpeziTextart) value;
            setText(showFullName ? textart.getTextart() + " | " + textart.getZnr() + " | " + textart.getTitel() : textart.getTextart());
            //System.out.println("********************************");
            // Ažurirajte komponente samo ako je selektiran ovaj stavak
            if (isSelected) {
                txtZrn.setText(textart.getZnr());
                tarTitel.setText(textart.getTitel());
                labAutoname.setText("");
                labItem.setText("");
                //tarText.setText(textart.get );

            }
        }
        return this;
    }
}

