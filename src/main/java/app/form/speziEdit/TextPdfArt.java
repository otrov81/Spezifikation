package app.form.speziEdit;

import app.model.SpeziPdf;

import javax.swing.*;


public class TextPdfArt extends DefaultListCellRenderer {

    @Override
    public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof SpeziPdf) {
            SpeziPdf textart = (SpeziPdf) value;
            setText(textart.getLogo());
//            setText(showFullName ? textart.getTextart() + " | " + textart.getZnr() + " | " + textart.getTitel() : textart.getTextart());
//            //System.out.println("********************************");
//            // Ažurirajte komponente samo ako je selektiran ovaj stavak
//            if (isSelected) {
//                txtZrn.setText(textart.getZnr());
//                tarTitel.setText(textart.getTitel());
//                labAutoname.setText("");
//                labItem.setText("");
//                //tarText.setText(textart.get );
//
//            }
        }
        return this;
    }
}


