package app.form.textbocke;

import app.form.speziEdit.SpeziEdit;
import app.model.SpeziTextart;
import app.model.SpeziTexte;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.BackIcon;
import app.toolbox.UserInfo;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextBlocke extends JPanel {

    @Autowired
    private DBService dbService;
    private JFrame frame;
    private JTextArea tarText;// = new JTextArea(5,100);
    private JTextArea tarText1;// = new JTextArea(5,100);
    private static ArrayList<SpeziTextart> textList;
    private UserInfo userInfo;
    private SpeziEdit speziEdit;

    public TextBlocke(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        this.speziEdit = speziEdit;
        this.userInfo = new UserInfo(dbService);
        init();

    }

    private void init() {

        String output = userInfo.getUserName();

        setLayout(new MigLayout("insets 2", "[grow]", "")); // Postavljanje layout managera

        //setBackground(Color.CYAN); // Postavljanje boje pozadine na contentPane

        // add menu
        JPanel panMenu = new JPanel(new MigLayout("", "", ""));
        //panMenu.setBackground(Color.YELLOW);

        JLabel labMenu = BackIcon.getBackEditSpezi(frame, dbService);
        panMenu.add(labMenu, "span");

        // add content
        JPanel panGlavni = new JPanel(new MigLayout("", "", ""));
        //panMenu.setBackground(Color.YELLOW);


        ///////////////
        SpeziTextart selectedValue = (SpeziTextart) speziEdit.dropTextart.getSelectedItem();
        String selectedV = selectedValue.getTextart();

        Insets insets = speziEdit.dropTextart.getInsets();

        textList = new ArrayList<>();
        List<SpeziTexte> getText = dbService.selectItemKeySpeek(SpeziEdit.txtMand.getText(), selectedV, speziEdit.txtScpd.getText());

        for (SpeziTexte speziTextart : getText) {
            textList.add(new SpeziTextart(speziTextart.getTextart(), speziTextart.getKey(), speziTextart.getText()));
            //textList.add(speziTextart);


            //add field for diminished variable
            JTextArea tarText2 = new JTextArea();


            JPanel panHeader = new JPanel(new MigLayout("", "", ""));
            //panHeader.setBackground(Color.PINK);

            // Dodavanje ostalih komponenti i logike za ovaj panel

            //add mand
            JLabel labMand = new JLabel("Mand");
            JTextField txtMand = new JTextField();
            txtMand.setText(speziEdit.txtMand.getText());

            //add panel head panItem + panText
            JPanel panItem = new JPanel(new MigLayout("insets 0", "", ""));
            //panItem.setBackground(Color.red);

            //add textart
            JLabel labTextart = new JLabel("Textart");
            JTextField txtTextart = new JTextField(17);
            txtTextart.setText(speziTextart.getTextart());

            //add spcd
            JLabel labSpcd = new JLabel("SPCD");
            JTextField txtSpcd = new JTextField();
            txtSpcd.setText(speziTextart.getSpcd());

            //add key
            JLabel labKey = new JLabel("Key");
            JTextField txtKey = new JTextField();
            txtKey.setText(speziTextart.getKey());
            txtKey.setEditable(false);

            //add text
            JLabel labText = new JLabel("Text");
            tarText2 = new JTextArea(15, 90);
            tarText2.setLineWrap(true); // Omogućite prelazak teksta na novi red kada je potrebno
            tarText2.setWrapStyleWord(true); // Omogućite prelazak riječi na novi red ako su predugačke

            tarText2.setText(speziTextart.getText());
            JScrollPane scrollPaneText = new JScrollPane(tarText2);
            scrollPaneText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Omogućite vertikalni scrollbar
            scrollPaneText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Isključite horizontalni scrollbar

            // Postavite veličinu JTextArea na 100% širine panela
            tarText2.setPreferredSize(new Dimension(0, tarText2.getPreferredSize().height));
            //add button
            JButton btnUdate = new JButton("Update");
            JButton btnDelete = new JButton("Delete");
            JButton btnUse = new JButton("Use");

            //add lab to glavi
            panItem.add(labMand);
            panItem.add(labTextart);
            panItem.add(labSpcd);
            panItem.add(labKey, "wrap");

            //add text filed to glavni
            panItem.add(txtMand);
            panItem.add(txtTextart);
            panItem.add(txtSpcd);
            panItem.add(txtKey, "wrap");
            panItem.add(btnUdate);
            panItem.add(btnDelete);
            panItem.add(btnUse);

            JPanel panText = new JPanel(new MigLayout("", "", ""));
            //panText.setBackground(Color.yellow);
            //panText.add(labText, "wrap");
            panText.add(scrollPaneText, "push,grow,wrap");

            //addd panItem to glavni panel
            panHeader.add(panItem, "height 200px!,grow");
            panHeader.add(panText, "height 200px!, push,grow,wrap");

            // Dodavanje panHeader panela u glavni panel
            panGlavni.add(panHeader, "pushx, growx, wrap");
            JTextArea finalTarText = tarText2;
            btnUdate.addActionListener(e -> {
                String text = finalTarText.getText(); // Dobijanje teksta iz JTextArea
                String mand = txtMand.getText(); // Dobijanje teksta iz JTextArea
                String textart = txtTextart.getText(); // Dobijanje teksta iz JTextArea
                String spcd = txtSpcd.getText(); // Dobijanje teksta iz JTextArea
                String key = txtKey.getText(); // Dobijanje teksta iz JTextArea
                System.out.println("Update text: << " + mand + " >>" + spcd + "---" + textart + "--" + text + ">>>>>>>>>>>><" + key); // Ispisivanje teksta u konzolu

                try {
                    // Vaš kod za spremanje teksta

                    if (key != null && !key.isEmpty()) {
                        dbService.updateImet(mand, spcd, textart, text, key);
                        ManagerForm.getInstance().showForm(new TextBlocke(frame, dbService));
                        JOptionPane.showMessageDialog(this, "update ", "Working", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Key value ", "EROR", JOptionPane.ERROR_MESSAGE);

                    }


                } catch (DataIntegrityViolationException r) {
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    dbService.LogToDatabase("ERROR", "Kann nicht Update werden (TextBlocke) " + r, output);
                }

            });

            btnDelete.addActionListener(e -> {
                String text = finalTarText.getText(); // Dobijanje teksta iz JTextArea
                String mand = txtMand.getText(); // Dobijanje teksta iz JTextArea
                String textart = txtTextart.getText(); // Dobijanje teksta iz JTextArea
                String spcd = txtSpcd.getText(); // Dobijanje teksta iz JTextArea
                String key = txtKey.getText(); // Dobijanje teksta iz JTextArea
                System.out.println("Update text: << " + mand + " >>" + spcd + "---" + textart + "--" + text + ">>>>>>>>>>>><" + key); // Ispisivanje teksta u konzolu

                try {

                    int dialogResult = JOptionPane.showConfirmDialog(this, "Wollen Sie die Datei wierklich löschen", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {

                        // Vaš kod za spremanje teksta
                        dbService.deleteImet(mand, spcd, textart, key);
                        ManagerForm.getInstance().showForm(new TextBlocke(frame, dbService));
                        JOptionPane.showMessageDialog(this, "Delete ", "Working", JOptionPane.INFORMATION_MESSAGE);

                    } else {

                    }


                } catch (DataIntegrityViolationException r) {
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    dbService.LogToDatabase("ERROR", "Kann nicht delete werden (TextartenAnlegen) " + r, output);
                }

            });

            btnUdate.addActionListener(e -> {
                String text = finalTarText.getText(); // Dobijanje teksta iz JTextArea
                String mand = txtMand.getText(); // Dobijanje teksta iz JTextArea
                String textart = txtTextart.getText(); // Dobijanje teksta iz JTextArea
                String spcd = txtSpcd.getText(); // Dobijanje teksta iz JTextArea
                String key = txtKey.getText(); // Dobijanje teksta iz JTextArea
                System.out.println("Update text: << " + mand + " >>" + spcd + "---" + textart + "--" + text + ">>>>>>>>>>>><" + key); // Ispisivanje teksta u konzolu

                try {
                    // Vaš kod za spremanje teksta
                    dbService.saveImet(mand, spcd, textart, key, text);
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                } catch (DataIntegrityViolationException r) {
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    dbService.LogToDatabase("ERROR", "Kann nicht update werden (Textblocke) " + r, output);
                }

            });

            // Postavljanje info teksta
            btnUse.setToolTipText("Ako zelite da ovo ubacite u tabelu. normali");

            btnUse.addActionListener(e -> {
                String text = finalTarText.getText(); // Dobijanje teksta iz JTextArea
                String mand = txtMand.getText(); // Dobijanje teksta iz JTextArea
                String textart = txtTextart.getText(); // Dobijanje teksta iz JTextArea
                String spcd = txtSpcd.getText(); // Dobijanje teksta iz JTextArea
                String key = txtKey.getText(); // Dobijanje teksta iz JTextArea
                String sa = speziEdit.txtSa.getText(); // Dobijanje teksta iz JTextArea
                String schluessel = speziEdit.txtSchluessel.getText();
                String znr = speziEdit.txtZrn.getText();
                System.out.println("Update text: << " + mand + " >>" + spcd + "---" + textart + "--" + text + ">>>>>>>>>>>><" + key + ">>>>>>" + sa + "schluessel-->" + schluessel + "<<<<<<<<<<" + znr); // Ispisivanje teksta u konzolu

                int checkZnr = dbService.checkIfKeyExistirt(Integer.valueOf(mand), textart, spcd, key);

//                if (checkZnr != 0 || key.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Wert key existiert, bitte geben Sie einen anderen Wert ein", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
//                } else {
                    try {
                        // Vaš kod za spremanje teksta
                        //dbService.saveImet(mand,spcd,textart,key,text);
                        dbService.updateSpeziDetail(text, key, Integer.valueOf(mand), sa, schluessel, spcd, znr, textart);
                        ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                    } catch (DataIntegrityViolationException r) {
                        // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                        dbService.LogToDatabase("ERROR", "Kann nicht use werden (TextBlocke) " + r, output);
                    }
               // }
            });


        }

        //add leer
        JPanel panHeader1 = new JPanel(new MigLayout("", "", ""));
        //panHeader1.setBackground(Color.PINK);

        // Dodavanje ostalih komponenti i logike za ovaj panel

        //add mand
        JLabel labMand1 = new JLabel("Mand");
        JTextField txtMand1 = new JTextField();
        txtMand1.setText(speziEdit.txtMand.getText());

        //add panel head panItem + panText
        JPanel panItem1 = new JPanel(new MigLayout("insets 0", "", ""));
        //panItem1.setBackground(Color.red);

        //add textart
        JLabel labTextart1 = new JLabel("Textart");
        JTextField txtTextart1 = new JTextField(17);
        txtTextart1.setText(selectedV);

        //add spcd
        JLabel labSpcd1 = new JLabel("SPCD");
        JTextField txtSpcd1 = new JTextField();
        txtSpcd1.setText(speziEdit.txtScpd.getText());

        //add key
        JLabel labKey1 = new JLabel("Key");
        JTextField txtKey1 = new JTextField();

        //add text
        JLabel labText1 = new JLabel("Text");
        tarText1 = new JTextArea(15, 90);
        tarText1.setLineWrap(true); // Omogućite prelazak teksta na novi red kada je potrebno
        tarText1.setWrapStyleWord(true); // Omogućite prelazak riječi na novi red ako su predugačke


        JScrollPane scrollPaneText1 = new JScrollPane(tarText1);
        scrollPaneText1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Omogućite vertikalni scrollbar
        scrollPaneText1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Isključite horizontalni scrollbar

        // Postavite veličinu JTextArea na 100% širine panela
        tarText1.setPreferredSize(new Dimension(0, tarText1.getPreferredSize().height));
        //add button
        JButton btnSave1 = new JButton("Save");
        JButton btnDelete1 = new JButton("Delete");
        JButton btnUse1 = new JButton("Use");

        //add lab to glavi
        panItem1.add(labMand1);
        panItem1.add(labTextart1);
        panItem1.add(labSpcd1);
        panItem1.add(labKey1, "wrap");

        //add text filed to glavni
        panItem1.add(txtMand1);
        panItem1.add(txtTextart1);
        panItem1.add(txtSpcd1);
        panItem1.add(txtKey1, "wrap");
        panItem1.add(btnSave1);
        panItem1.add(btnDelete1);
        panItem1.add(btnUse1);

        JPanel panText1 = new JPanel(new MigLayout("", "", ""));
        //panText1.setBackground(Color.yellow);
        //panText.add(labText, "wrap");
        panText1.add(scrollPaneText1, "push, grow,grow");

        //addd panItem to glavni panel
        panHeader1.add(panItem1, "height 200px!,");
        panHeader1.add(panText1, "height 200px!, push,grow,wrap");

        // Dodavanje panHeader panela u glavni panel
        panGlavni.add(panHeader1, "pushx, growx, wrap");

        JTextArea finalTarText1 = tarText1;
        //add new item
        btnSave1.addActionListener(e -> {

            String text = finalTarText1.getText(); // Dobijanje teksta iz JTextArea
            String mand = txtMand1.getText(); // Dobijanje teksta iz JTextArea
            String textart = txtTextart1.getText(); // Dobijanje teksta iz JTextArea
            String spcd = txtSpcd1.getText(); // Dobijanje teksta iz JTextArea
            String key = txtKey1.getText(); // Dobijanje teksta iz JTextArea
            String znr = txtKey1.getText();
            System.out.println("Saved text: " + text + "<< " + mand + " >>" + textart + "---" + spcd + "--" + key); // Ispisivanje teksta u konzolu

            int checkZnr = dbService.checkIfKeyExistirt(Integer.valueOf(mand), textart, spcd, key);

            if (checkZnr != 0 || key.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wert key existiert, bitte geben Sie einen anderen Wert ein", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            } else {

                // Unutar bloka try-catch uhvatite DataIntegrityViolationException
                try {
                    // Vaš kod za spremanje teksta
                    dbService.saveImet(mand, spcd, textart, key, text);
                    ManagerForm.getInstance().showForm(new TextBlocke(frame, dbService));

                    JOptionPane.showMessageDialog(this, "Save ", "Working", JOptionPane.INFORMATION_MESSAGE);

                } catch (DataIntegrityViolationException r) {
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    dbService.LogToDatabase("ERROR", "Kann nicht save werden (TextartenAnlegen) " + r, output);
                }
            }

        });

        btnDelete1.addActionListener(e -> {
            finalTarText1.setText(""); // Dobijanje teksta iz JTextArea
            // txtMand1.setText(""); // Dobijanje teksta iz JTextArea
            // txtTextart1.setText(""); // Dobijanje teksta iz JTextArea
            // txtSpcd1.setText(""); // Dobijanje teksta iz JTextArea
            // txtKey1.setText(""); // Dobijanje teksta iz JTextArea


        });
        btnUse1.setToolTipText("Ako zelite da ovo ubacite u tabelu, novi!!");


        btnUse1.addActionListener(e -> {
            String text = finalTarText1.getText(); // Dobijanje teksta iz JTextArea
            String mand = txtMand1.getText(); // Dobijanje teksta iz JTextArea
            String textart = txtTextart1.getText(); // Dobijanje teksta iz JTextArea
            String spcd = txtSpcd1.getText(); // Dobijanje teksta iz JTextArea
            String key = txtKey1.getText(); // Dobijanje teksta iz JTextArea
            String sa = speziEdit.txtSa.getText(); // Dobijanje teksta iz JTextArea
            String schluessel = speziEdit.txtSchluessel.getText();
            String znr = speziEdit.txtZrn.getText();
            System.out.println("Update text: << " + mand + " >>" + spcd + "---" + textart + "--" + text + ">>>>>>>>>>>><" + key); // Ispisivanje teksta u konzolu

            int checkZnr = dbService.checkIfKeyExistirt(Integer.valueOf(mand), textart, spcd, key);

            if (checkZnr != 0 || key.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wert key existiert, bitte geben Sie einen anderen Wert ein", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            } else {

                try {
                    // Vaš kod za spremanje teksta
                    //dbService.saveImet(mand,spcd,textart,key,text);
                    dbService.updateSpeziDetail(text, key, Integer.valueOf(mand), sa, schluessel, spcd, znr, textart);
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                } catch (DataIntegrityViolationException r) {
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    dbService.LogToDatabase("ERROR", "Kann nicht use werden (TextBlocke) " + r, output);
                }
            }

        });


        JScrollPane scrollPaneGlavni = new JScrollPane(panGlavni);
        scrollPaneGlavni.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPaneGlavni, "align center, grow");
        add(panMenu, "dock north, width 100%, height 50px");

    }


    // Metoda za kreiranje panHeader panela

}