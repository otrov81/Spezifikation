package app.form.speziAnlegen;

import app.form.speziEdit.SpeziEdit;
import app.model.SpeziSa;
import app.model.SpeziSpcd;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.BackIcon;
import app.toolbox.MenuInfo;
import app.toolbox.UserInfo;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;


public class SpeziAnlegen extends JPanel {


    @Autowired
    private DBService dbService;
    private JFrame frame;
    private MenuInfo menuInfo;
    private static ArrayList<SpeziSa> textList;
    private static ArrayList<SpeziSpcd> textListSpcd;
    private JLabel labLand = new JLabel("Land");
    private JTextField txtLand = new JTextField();
    private JComboBox combLand = new JComboBox<>();
    private JLabel labKunde = new JLabel("Kunde");
    private JTextField txtKunde = new JTextField();
    private JLabel labArtikel = new JLabel("Artikel");
    private JTextField txtArtikel = new JTextField();
    private JLabel labArtikelName = new JLabel();
    private JLabel labKabName = new JLabel();
    private JLabel labKundeName = new JLabel();
    private static ArrayList<String> textListLand;
    private JButton btnSpeziEdit = new JButton();
    private SpeziEdit speziEdit;
    private UserInfo userInfo;

    public SpeziAnlegen() {
        //default constructor
    }

    private Integer inputWidth = 3;
    private Integer inputWidthBig = 15;

    public SpeziAnlegen(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        this.menuInfo = new MenuInfo();
        this.speziEdit = new SpeziEdit(frame, dbService);
        this.userInfo = new UserInfo(dbService);
        init();

    }

    public void init() {
        String output = userInfo.getUserName();

        //add menu
        menuInfo.createMenu(frame, dbService); // Assuming 'frame' is your JFrame instance

        setLayout(new MigLayout("insets 2", "[grow]", "")); // postavljanje layout managera
        //setBackground(Color.CYAN); // postavljanje boje pozadine na contentPane

        // add menu
        JPanel panMenu = new JPanel(new MigLayout("", "", ""));
        //panMenu.setBackground(Color.YELLOW);

        JLabel labMenu = BackIcon.getBackIconDaschboad(frame, dbService);
        panMenu.add(labMenu, "span");

        //add JPanel
        JPanel panCenter = new JPanel(new MigLayout("", "", ""));
        //panCenter.setBackground(Color.PINK);

        //add mand
        JLabel labMand = new JLabel("Mand");
        JTextField txtMand = new JTextField();
        txtMand.setColumns(inputWidthBig);
        txtMand.setText(String.valueOf(1000));

        //add Satzart
        JLabel labSatzart = new JLabel("Satzart");
        JTextField txtSatzart = new JTextField();


        //add jcombox
        textList = new ArrayList<>();
        List<SpeziSa> getTextSa = dbService.selectDropdownSa();
        for (SpeziSa speziSa : getTextSa) {
            textList.add(speziSa);
        }

        txtSatzart.setColumns(inputWidth);
        DefaultComboBoxModel<SpeziSa> model = new DefaultComboBoxModel<>(textList.toArray(new SpeziSa[0]));
        JComboBox<SpeziSa> combSatzart = new JComboBox<>(model);

// Postavljanje rendere-a da se prikazuje samo text_sa
        combSatzart.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof SpeziSa) {
                    SpeziSa speziSa = (SpeziSa) value;
                    value = speziSa.getText_sa();

                    if (isSelected) {
                        String sa = speziSa.getSa(); // Čuvanje vrijednosti u varijablu da se izbjegne ponavljanje poziva
                        txtSatzart.setText(sa);

                        // Postavljanje vidljivosti elemenata ovisno o vrijednosti 'sa'
                        labLand.setVisible("01".equals(sa) || "04".equals(sa) || "05".equals(sa));
                        txtLand.setVisible("01".equals(sa) || "04".equals(sa) || "05".equals(sa));
                        combLand.setVisible("01".equals(sa) || "04".equals(sa) || "05".equals(sa));

                        labArtikel.setVisible("01".equals(sa) || "02".equals(sa));
                        txtArtikel.setVisible("01".equals(sa) || "02".equals(sa));
                        labArtikelName.setVisible("01".equals(sa) || "02".equals(sa));

                        labKunde.setVisible("02".equals(sa) || "03".equals(sa));
                        txtKunde.setVisible("02".equals(sa) || "03".equals(sa));
                        labKundeName.setVisible("02".equals(sa) || "03".equals(sa));

                    }
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });


        combSatzart.setMaximumSize(combSatzart.getPreferredSize());

        //add Spache
        textListSpcd = new ArrayList<>();
        List<SpeziSpcd> getTextSpcd = dbService.selectDropdownSpcd();
        for (SpeziSpcd speziSpcd : getTextSpcd) {
            textListSpcd.add(speziSpcd);
        }
        JLabel labSprach = new JLabel("Sprache");
        JTextField txtSprache = new JTextField();
        txtSprache.setColumns(inputWidth);

        DefaultComboBoxModel<SpeziSpcd> modelSpcd = new DefaultComboBoxModel<>(textListSpcd.toArray(new SpeziSpcd[0]));
        JComboBox<SpeziSpcd> combSprache = new JComboBox<>(modelSpcd);

        //set Render
        combSprache.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof SpeziSpcd) {
                    SpeziSpcd speziSpcd = (SpeziSpcd) value;
                    value = speziSpcd.getText1();
                    if (isSelected) {
                        String spcd = speziSpcd.getSpcd(); // Čuvanje vrijednosti u varijablu da se izbjegne ponavljanje poziva
                        txtSprache.setText(spcd);
                    }
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });


        //add land
        txtLand.setColumns(inputWidth);
        labLand.setVisible(false);
        txtLand.setVisible(false);


        //add Spache
        textListLand = new ArrayList<>();
        List<String> getTextLand = dbService.getLandFromErp();
        for (String erpLand : getTextLand) {
            textListLand.add(erpLand);
        }

        DefaultComboBoxModel<String> modelLand = new DefaultComboBoxModel<>(textListLand.toArray(new String[0]));
        combLand = new JComboBox<>(modelLand);

        //set Render
        combLand.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof String) {
// Razdvajanje stringa na dva dijela
                    String[] parts = value.toString().split("\\|");

// Dobijanje dva rezultujuća stringa
                    String firstPart = parts[0];
                    String secondPart = parts[1];

                    value = secondPart;

                    //value = speziLand;
                    if (isSelected) {
                        txtLand.setText(firstPart);
                    }
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        combLand.setVisible(false);

        //add kunde
        txtKunde.setColumns(inputWidthBig);
        labKunde.setVisible(false);
        txtKunde.setVisible(false);
        labKundeName.setVisible(true);

        txtKunde.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                String kName = dbService.getKundenNameFromErp(txtKunde.getText());

                // Razdvajanje stringa na dva dijela
                String[] parts = kName.toString().split("\\|");

                //  try {
                String firstPart = parts[0];
                //String secondPart = parts[1];

                String secondPart = "";

                if (parts.length > 1) { // Check if there are at least two elements
                    secondPart = parts[1];
                }

                if (txtKunde.getText().length() > 2) {
                    labKundeName.setText(firstPart);

                    if (secondPart.length() > 1) {
                        txtLand.setVisible(true);
                        labLand.setVisible(true);
                        txtLand.setText(secondPart);
                    } else {
                        txtLand.setVisible(false);
                        labLand.setVisible(false);

                    }
                } else {
                    txtLand.setVisible(false);
                }
//                } catch (Exception e1) {
//                    dbService.LogToDatabase("ERROR", "Invalid username or password! "+e1, output); // Ispravljeno ovdje
//                }


            }
        });

        //add artikel
        txtArtikel.setColumns(inputWidthBig);
        labArtikel.setVisible(false);
        txtArtikel.setVisible(false);
        labArtikelName.setVisible(false);
        labKabName.setVisible(false);

        txtArtikel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String aName = dbService.getArtikelNameFromErp(txtArtikel.getText());
                if (txtArtikel.getText().length() > 2)
                    labArtikelName.setText(aName);

                if (!txtKunde.getText().isEmpty() && !txtArtikel.getText().isEmpty()) {
                    Boolean kab = dbService.checkRABExistence(txtKunde.getText(), txtArtikel.getText());

                    if (kab) {
                        labKabName.setVisible(false);
                    } else {
                        labKabName.setVisible(true);
                        labKabName.setText("<html><b>*** KAB nicht vorhanden ***</b><html>");
                    }


                }
            }
        });

        //button
        btnSpeziEdit = new JButton("<html><div style='text-align: center;'>Spezifikation<br>anlegen/bearbeiten</div></html>");
        btnSpeziEdit.addActionListener(e -> {

                    //variablen
                    boolean startsWithArtikel = labArtikelName.getText().startsWith("*** Artikel");
                    boolean startsWithKunden = labKundeName.getText().startsWith("*** Kunden");
                    String spcd = txtSprache.getText();
                    String schluessel = txtLand.getText() + " " + txtArtikel.getText();
                    Integer mand = Integer.valueOf(txtMand.getText());
                    String artikel = txtArtikel.getText();
                    String sa = txtSatzart.getText();
                    String schluesselLand = txtLand.getText();
                    String artbez = labArtikelName.getText();
                    String ldc = txtLand.getText();
                    String kunde = txtKunde.getText();
                    String kundeName = labKundeName.getText();

                    // check 01
                    if (txtSatzart.getText().isEmpty() || "00".equals(txtSatzart.getText())) {
                        JOptionPane.showMessageDialog(frame, "Bitte SA eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    } else if ("01".equals(txtSatzart.getText())) {


                        if (txtSprache.getText().isEmpty() || "00".equals(txtSprache.getText()))
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

                        else if (txtLand.getText().isEmpty() || "00".equals(txtLand.getText())) {

                            int dialogResult = JOptionPane.showConfirmDialog(frame, "Spezifikation für Originalware?", "Eingabefehler", JOptionPane.YES_NO_OPTION);

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                txtLand.setText("ORI");

                            } else {
                                JOptionPane.showMessageDialog(frame, "Bitte Ländercode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                            }

                        } else if (txtArtikel.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Artikelnummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtMand.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Mandanten eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (startsWithArtikel)
                            JOptionPane.showMessageDialog(frame, "Bitte Artikelnummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if ("ORI".equals(txtLand.getText())) {
                            sa = "01";
                            Integer chekSpezi = dbService.checkSpeziFirst(schluessel, spcd, mand, sa);
                            if (chekSpezi != null && chekSpezi == 0) {

                                //add kopf + detail
                                dbService.getUpdateByLandName(mand, sa, schluessel, spcd, schluesselLand);
                                dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));

                            } else {

                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }
                        } else {
                            sa = "01";
                            Integer chekSpezi = dbService.checkSpeziFirst(schluessel, spcd, mand, sa);

                            if (chekSpezi != null && chekSpezi == 0) {
                                //check
                                int dialogResult = JOptionPane.showConfirmDialog(frame, "<html><div style='text-align:center;'>Keine passende Vorlage gefunden.<br>Trotzdem anlegen?</div></html>", "Neuanlage ohne Vorlage", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                    speziEdit.txtSa.setText(sa);
                                    speziEdit.txtScpd.setText(spcd);
                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                    speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                    speziEdit.txtSchluessel.setText(schluessel);
                                    speziEdit.txtLdc.setText(ldc);
//
                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));

                                } else {

                                }

                            } else {
                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }


                        }
                        ////////////////////////// edn 01

                    }


                    /////////////////////////////////////////////////////////////////////////////


                    else if ("02".equals(txtSatzart.getText())) {
                        if (txtSprache.getText().isEmpty() || "00".equals(txtSprache.getText()))
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtKunde.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Kundennummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtArtikel.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Artikelnummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (startsWithKunden)
                            JOptionPane.showMessageDialog(frame, "Bitte Kundennummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (startsWithArtikel)
                            JOptionPane.showMessageDialog(frame, "Bitte Artikelnummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

///logick-------------------------------------------------------------------------------------------------
                        else {
                            //check if exist standard = 02
                            sa = "02";
                            schluessel = txtKunde.getText() + txtArtikel.getText();
                            Integer chekSpezi = dbService.checkSpeziFirst(schluessel, spcd, mand, sa);

                            if (chekSpezi != null && chekSpezi == 0) {
                                // Poruka koja se prikazuje u dijalogu
                                String message = "<html><div style='text-align:center;'>Vorlage eines anderen Kunden verwenden?<br>(nein = Ländervorlage)</div></html>";
                                // Naslov dijaloga
                                String title = "Vorlage";
                                // Opcije dijaloga
                                Object[] options = {"Ja", "Nein", "Abbrechen"};

                                // Prikaz dijaloga i dobivanje odgovora korisnika
                                int response = JOptionPane.showOptionDialog(frame, message, title,
                                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                        null, options, options[2]);

                                // Provjera odgovora korisnika
                                if (response == JOptionPane.YES_OPTION) {

                                    JTextField textField = new JTextField();
                                    Object[] messageText = {
                                            "<html><div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bitte Kundennummer eingeben<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(nein Kdnr=Lädavorlage)</div></html>",
                                            textField
                                    };


                                    int choice = JOptionPane.showOptionDialog(
                                            frame,
                                            messageText,
                                            "Vorlage",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            options,
                                            options[0]
                                    );

                                    if (choice == JOptionPane.YES_OPTION) {
                                        // Kod za Ja
                                        String inputText = textField.getText();
                                        Integer checkExistingKunde = dbService.checkKunenNummer(inputText, spcd);
                                        //search 03 save 02
                                        if (checkExistingKunde != null && checkExistingKunde.intValue() != 0) {

                                            schluessel = txtKunde.getText() + txtArtikel.getText();
                                            schluesselLand = inputText;

                                            String saSpez = "03";

                                            dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                            dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                            speziEdit.txtMand.setText(String.valueOf(mand));
                                            speziEdit.txtSa.setText(sa);
                                            speziEdit.txtScpd.setText(spcd);
                                            //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                            speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                            speziEdit.txtSchluessel.setText(schluessel);
                                            speziEdit.txtLdc.setText(ldc);

                                            ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                                        } else {

                                            JOptionPane.showMessageDialog(frame, "Kundennummer existiert nicht!!!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);


                                            choice = JOptionPane.showOptionDialog(
                                                    frame,
                                                    messageText,
                                                    "Vorlage",
                                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                                    JOptionPane.PLAIN_MESSAGE,
                                                    null,
                                                    options,
                                                    options[0]
                                            );

                                            while (choice == JOptionPane.YES_OPTION) {

                                                String inputText1 = textField.getText();

                                                //check if exist
                                                sa = "03";
                                                schluessel = inputText1;
                                                Integer chekSpeziNew = dbService.checkSpeziFirst(schluessel, spcd, mand, sa);

                                                if (chekSpeziNew != null && chekSpeziNew == 0) {

                                                    JOptionPane.showMessageDialog(frame, "Kundennummer existiert nicht!!!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

                                                    choice = JOptionPane.showOptionDialog(
                                                            frame,
                                                            messageText,
                                                            "Vorlage",
                                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                                            JOptionPane.PLAIN_MESSAGE,
                                                            null,
                                                            options,
                                                            options[0]
                                                    );
                                                } else {

                                                    // Kod za Nein
                                                    //add kopf + detail
                                                    schluessel = txtKunde.getText() + txtArtikel.getText();
                                                    schluesselLand = inputText1;
                                                    String saSpez = "03";
                                                    sa = "02";
                                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                                    speziEdit.txtSa.setText(sa);
                                                    speziEdit.txtScpd.setText(spcd);
                                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                                    speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                                    speziEdit.txtSchluessel.setText(schluessel);
                                                    speziEdit.txtLdc.setText(ldc);

                                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                                }


                                                if (choice == JOptionPane.NO_OPTION) {
                                                    //add kopf + detail
                                                    schluessel = txtKunde.getText() + txtArtikel.getText();
                                                    String saSpez = "04";
                                                    sa = "02";
                                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                                    speziEdit.txtSa.setText(sa);
                                                    speziEdit.txtScpd.setText(spcd);
                                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                                    speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                                    speziEdit.txtSchluessel.setText(schluessel);
                                                    speziEdit.txtLdc.setText(ldc);

                                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                                }
                                            }


                                        }


                                    } else if (choice == JOptionPane.NO_OPTION) {
                                        //add kopf + detail
                                        schluessel = txtKunde.getText() + txtArtikel.getText();
                                        String saSpez = "04";
                                        sa = "02";
                                        dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                        dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                        speziEdit.txtMand.setText(String.valueOf(mand));
                                        speziEdit.txtSa.setText(sa);
                                        speziEdit.txtScpd.setText(spcd);
                                        //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                        speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                        speziEdit.txtSchluessel.setText(schluessel);
                                        speziEdit.txtLdc.setText(ldc);

                                        ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                    } else if (choice == JOptionPane.CANCEL_OPTION) {

                                    } else if (choice == JOptionPane.CLOSED_OPTION) {

                                    }


                                } else if (response == JOptionPane.NO_OPTION) {
                                    //schlussel mand spcd
                                    //add kopf + detail
                                    schluessel = txtKunde.getText() + txtArtikel.getText();

                                    String saSpez = "04";
                                    sa = "02";

                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                    speziEdit.txtSa.setText(sa);
                                    speziEdit.txtScpd.setText(spcd);
                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                    speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                    speziEdit.txtSchluessel.setText(schluessel);
                                    speziEdit.txtLdc.setText(ldc);

                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                                } else if (response == JOptionPane.CANCEL_OPTION) {
                                } else if (response == JOptionPane.CLOSED_OPTION) {
                                }

                            } else {
                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }

                        }///end haupt

                    } else if ("03".equals(txtSatzart.getText())) {


                        if (txtSprache.getText().isEmpty() || "00".equals(txtSprache.getText()))
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtKunde.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Kundennummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (startsWithKunden)
                            JOptionPane.showMessageDialog(frame, "Bitte Kundennummer eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

///logick-------------------------------------------------------------------------------------------------
                        schluessel = txtKunde.getText();

                        //chek if Kunde existirt //checkSpeziExistenceKunde(String schluessel, String spcd, Integer mand, String sa, String kunde)
                        Integer checkKunde = dbService.checkSpeziExistenceKunde(schluessel, spcd, mand, sa);

                        if (checkKunde != null && checkKunde == 0) {
                            //add yes no
                            // Poruka koja se prikazuje u dijalogu
                            String message = "<html><div style='text-align:center;'>Vorlage eines anderen Kunden verwenden?<br>(nein = Ländervorlage)</div></html>";
                            // Naslov dijaloga
                            String title = "Vorlage";
                            // Opcije dijaloga
                            Object[] options = {"Ja", "Nein", "Abbrechen"};

                            // Prikaz dijaloga i dobivanje odgovora korisnika
                            int response = JOptionPane.showOptionDialog(frame, message, title,
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                    null, options, options[2]);

                            // Provjera odgovora korisnika
                            if (response == JOptionPane.YES_OPTION) {

                                //add first yes click(ja)
                                JTextField textField = new JTextField();
                                Object[] messageText = {
                                        "<html><div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bitte Kundennummer eingeben<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(nein Kdnr=Lädavorlage)</div></html>",
                                        textField
                                };


                                int choice = JOptionPane.showOptionDialog(
                                        frame,
                                        messageText,
                                        "Vorlage",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        options,
                                        options[0]
                                );

                                while (choice == JOptionPane.YES_OPTION) {
                                    //first check kunden
                                    String inputText = textField.getText();
                                    schluessel = inputText;
                                    checkKunde = dbService.checkSpeziExistenceKunde(schluessel, spcd, mand, sa);
                                    if (checkKunde != null && checkKunde == 0) {
                                        //add new ask
                                        JOptionPane.showMessageDialog(frame, "Kundennummer existiert nicht!!!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

                                        choice = JOptionPane.showOptionDialog(
                                                frame,
                                                messageText,
                                                "Vorlage",
                                                JOptionPane.YES_NO_CANCEL_OPTION,
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                options,
                                                options[0]
                                        );


                                    } else {
                                        schluessel = txtKunde.getText();
                                        schluesselLand = inputText;

                                        String saSpez = "03";

                                        dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                        dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, kunde, artbez, ldc);

                                        speziEdit.txtMand.setText(String.valueOf(mand));
                                        speziEdit.txtSa.setText(sa);
                                        speziEdit.txtScpd.setText(spcd);
                                        speziEdit.txtKunde.setText(kunde+", "+labKundeName.getText());
                                        //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                        speziEdit.txtSchluessel.setText(schluessel);
                                        speziEdit.txtLdc.setText(ldc);
//
                                        ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                        break;

                                    }

                                } //end while
                                if (choice == JOptionPane.NO_OPTION) {
                                    schluessel = txtKunde.getText();
                                    schluesselLand = txtLand.getText();

                                    String saSpez = "04";

                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, kunde, artbez, ldc);

                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                    speziEdit.txtSa.setText(sa);
                                    speziEdit.txtScpd.setText(spcd);
                                    speziEdit.txtKunde.setText(kunde+", "+labKundeName.getText());
                                    //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                    speziEdit.txtSchluessel.setText(schluessel);
                                    speziEdit.txtLdc.setText(ldc);
//
                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                } else if (choice == JOptionPane.CANCEL_OPTION) {
                                    System.out.println("111 close");
                                }

                            } else if (response == JOptionPane.NO_OPTION) {
                                schluessel = txtKunde.getText();
                                schluesselLand = txtLand.getText();

                                String saSpez = "04";

                                dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, kunde, artbez, ldc);

                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                speziEdit.txtKunde.setText(kunde+", "+labKundeName.getText());
                                //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            } else if (response == JOptionPane.CLOSED_OPTION) {
                            }



                        } else { // if kunde exist auto to eidtSpezi
                            speziEdit.txtMand.setText(String.valueOf(mand));
                            speziEdit.txtSa.setText(sa);
                            speziEdit.txtScpd.setText(spcd);
                            speziEdit.txtKunde.setText(kunde+", "+kundeName);
                            //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                            speziEdit.txtSchluessel.setText(schluessel);
                            speziEdit.txtLdc.setText(ldc);
//
                            ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                        }





                        ////////////////////


            } else if ("04".equals(txtSatzart.getText())) {
                System.out.println("444444444444444444444444");


                        if (txtSprache.getText().isEmpty() || "00".equals(txtSprache.getText()))
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtSprache.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtLand.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Ländercode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);


///logick-------------------------------------------------------------------------------------------------
                        else {
                            System.out.println("alle felda ausfulen!!!!!!!!!!");

                            schluessel= txtLand.getText();
                            System.out.println(schluessel+">>>>"+spcd+">>>>"+mand+">>>>"+sa);
                            //check if land exist //String schluessel, String spcd, Integer mand, String sa
                            Integer checkLand = dbService.checkSpeziExistenceLand(schluessel, spcd, mand, sa);

                            System.out.println(checkLand+"<<<<<<<<<<<<<<<<<<<<<<");

                            if(checkLand != null && checkLand == 0){
                                //if not exist
                                // Poruka koja se prikazuje u dijalogu
                                String message = "<html><div style='text-align:center;'>Keine passende Vorlage gefunden.<br>Ja = Andere Ländervorlage verwenden?<br> Nein = Trotzdem anlegen?</div></html>";
                                // Naslov dijaloga
                                String title = "Vorlage";
                                // Opcije dijaloga
                                Object[] options = {"Ja", "Nein", "Abbrechen"};

                                // Prikaz dijaloga i dobivanje odgovora korisnika
                                int response = JOptionPane.showOptionDialog(frame, message, title,
                                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                        null, options, options[2]);

                                // Provjera odgovora korisnika
                                if (response == JOptionPane.YES_OPTION) {

                                    //add first yes click(ja)
                                    JTextField textField1 = new JTextField();
                                    Object[] messageText = {
                                            "<html><div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bitte Ländercode eingeben<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(nein Kdnr=Lädavorlage)</div></html>",
                                            textField1
                                    };


                                    int choice = JOptionPane.showOptionDialog(
                                            frame,
                                            messageText,
                                            "Vorlage",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            options,
                                            options[0]
                                    );

                                    while (choice == JOptionPane.YES_OPTION) {

                                        schluessel= textField1.getText();
                                        System.out.println(schluessel+">>>>"+spcd+">>>>"+mand+">>>>"+sa);
                                        //check if land exist //String schluessel, String spcd, Integer mand, String sa

                                        Integer checkLand1 = dbService.checkSpeziExistenceLand(schluessel, spcd, mand, sa);

                                        System.out.println(checkLand1+"<<<<<<<<<<<<<<<<<<<<<<");

                                        if(checkLand1 != null && checkLand1 == 0){
                                            System.out.println("kada ne pronadje land...");
                                            //add new ask
                                            JOptionPane.showMessageDialog(frame, "Ländercode existiert nicht!!!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

                                            choice = JOptionPane.showOptionDialog(
                                                    frame,
                                                    messageText,
                                                    "Vorlage",
                                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                                    JOptionPane.PLAIN_MESSAGE,
                                                    null,
                                                    options,
                                                    options[0]
                                            );

                                        }else{
                                            String saSpez = "04";
                                            schluesselLand= textField1.getText();
                                            schluessel = txtLand.getText();
                                            dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                            dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                            speziEdit.txtMand.setText(String.valueOf(mand));
                                            speziEdit.txtSa.setText(sa);
                                            speziEdit.txtScpd.setText(spcd);
                                            //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                            //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                            speziEdit.txtSchluessel.setText(schluessel);
                                            speziEdit.txtLdc.setText(ldc);
//
                                            ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                            break;

                                        }


                                    } //end while
                                    if (choice == JOptionPane.NO_OPTION) {
                                        String saSpez = "04";
                                        dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                        dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                        speziEdit.txtMand.setText(String.valueOf(mand));
                                        speziEdit.txtSa.setText(sa);
                                        speziEdit.txtScpd.setText(spcd);
                                        //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                        //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                        speziEdit.txtSchluessel.setText(schluessel);
                                        speziEdit.txtLdc.setText(ldc);
//
                                        ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));

                                    }




                                } else if (response == JOptionPane.NO_OPTION) {
                                    String saSpez = "04";
                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                    speziEdit.txtSa.setText(sa);
                                    speziEdit.txtScpd.setText(spcd);
                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                    //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                    speziEdit.txtSchluessel.setText(schluessel);
                                    speziEdit.txtLdc.setText(ldc);
//
                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }

                            }else{
                                System.out.println("ako postoji onda direkt pokazi");
                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }

                        } //ende programm




                    } else if ("05".equals(txtSatzart.getText())) {
                System.out.println("555555555555555555555555");
                        System.out.println("444444444444444444444444");


                        if (txtSprache.getText().isEmpty() || "00".equals(txtSprache.getText()))
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtSprache.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Sprachcode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        else if (txtLand.getText().isEmpty())
                            JOptionPane.showMessageDialog(frame, "Bitte Ländercode eintragen!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);


///logick-------------------------------------------------------------------------------------------------
                        else {

                            schluessel= txtLand.getText();
                            //check if land exist //String schluessel, String spcd, Integer mand, String sa
                            Integer checkLand = dbService.checkSpeziExistenceLand(schluessel, spcd, mand, sa);
                            if(checkLand != null && checkLand == 0){
                                //if not exist
                                // Poruka koja se prikazuje u dijalogu
                                String message = "<html><div style='text-align:center;'>Keine passende Vorlage gefunden.<br>Ja = Andere Ländervorlage verwenden?<br> Nein = Trotzdem anlegen?</div></html>";
                                // Naslov dijaloga
                                String title = "Vorlage";
                                // Opcije dijaloga
                                Object[] options = {"Ja", "Nein", "Abbrechen"};

                                // Prikaz dijaloga i dobivanje odgovora korisnika
                                int response = JOptionPane.showOptionDialog(frame, message, title,
                                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                        null, options, options[2]);

                                // Provjera odgovora korisnika
                                if (response == JOptionPane.YES_OPTION) {
                                    //add first yes click(ja)
                                    JTextField textField1 = new JTextField();
                                    Object[] messageText = {
                                            "<html><div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bitte Ländercode eingeben<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(nein Kdnr=Lädavorlage)</div></html>",
                                            textField1
                                    };


                                    int choice = JOptionPane.showOptionDialog(
                                            frame,
                                            messageText,
                                            "Vorlage",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            options,
                                            options[0]
                                    );

                                    while (choice == JOptionPane.YES_OPTION) {

                                        schluessel= textField1.getText();
                                        //check if land exist //String schluessel, String spcd, Integer mand, String sa

                                        Integer checkLand1 = dbService.checkSpeziExistenceLand(schluessel, spcd, mand, sa);
                                        if(checkLand1 != null && checkLand1 == 0){
                                            //add new ask
                                            JOptionPane.showMessageDialog(frame, "Ländercode existiert nicht!!!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);

                                            choice = JOptionPane.showOptionDialog(
                                                    frame,
                                                    messageText,
                                                    "Vorlage",
                                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                                    JOptionPane.PLAIN_MESSAGE,
                                                    null,
                                                    options,
                                                    options[0]
                                            );

                                        }else{
                                            String saSpez = "05";
                                            schluesselLand= textField1.getText();
                                            schluessel = txtLand.getText();
                                            dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                            dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                            speziEdit.txtMand.setText(String.valueOf(mand));
                                            speziEdit.txtSa.setText(sa);
                                            speziEdit.txtScpd.setText(spcd);
                                            //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                            //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                            speziEdit.txtSchluessel.setText(schluessel);
                                            speziEdit.txtLdc.setText(ldc);
//
                                            ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                            break;

                                        }


                                    } //end while
                                    if (choice == JOptionPane.NO_OPTION) {
                                        String saSpez = "05";
                                        dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                        dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                        speziEdit.txtMand.setText(String.valueOf(mand));
                                        speziEdit.txtSa.setText(sa);
                                        speziEdit.txtScpd.setText(spcd);

                                        speziEdit.txtSchluessel.setText(schluessel);
                                        speziEdit.txtLdc.setText(ldc);
//
                                        ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));

                                    }




                                } else if (response == JOptionPane.NO_OPTION) {
                                    String saSpez = "05";
                                    dbService.getUpdateByLandNameSpezial(mand, sa, schluessel, spcd, schluesselLand, saSpez);

                                    dbService.insertSpeziKopfNeuFirst(mand, sa, schluessel, spcd, artikel, artbez, ldc);

                                    speziEdit.txtMand.setText(String.valueOf(mand));
                                    speziEdit.txtSa.setText(sa);
                                    speziEdit.txtScpd.setText(spcd);
                                    //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                    //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                    speziEdit.txtSchluessel.setText(schluessel);
                                    speziEdit.txtLdc.setText(ldc);
//
                                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                                }

                            }else{
                                System.out.println("ako postoji onda direkt pokazi");
                                speziEdit.txtMand.setText(String.valueOf(mand));
                                speziEdit.txtSa.setText(sa);
                                speziEdit.txtScpd.setText(spcd);
                                //speziEdit.txtKunde.setText(kunde+", "+kundeName);
                                //speziEdit.txtArtikel.setText(artikel + ", " + artbez);
                                speziEdit.txtSchluessel.setText(schluessel);
                                speziEdit.txtLdc.setText(ldc);
//
                                ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                            }

                        } //ende programm





                    } else {
                System.out.println("Fehler...!!!!!!!!!!!");
            }


        });


        panCenter.add(labMand);
        panCenter.add(txtMand, "wrap");
        panCenter.add(labSatzart);
        panCenter.add(txtSatzart, "split 2");
        panCenter.add(combSatzart, "wrap,w 300px!");
        panCenter.add(labSprach);
        panCenter.add(txtSprache, "split 2");
        panCenter.add(combSprache, "wrap, w 300px!");
        panCenter.add(labLand);
        panCenter.add(txtLand, "split 2");
        panCenter.add(combLand, "wrap, w 300px!");

        panCenter.add(labKunde);
        panCenter.add(txtKunde, "split 2");
        panCenter.add(labKundeName, "wrap");
        panCenter.add(labArtikel);
        panCenter.add(txtArtikel, "split 3");
        panCenter.add(labArtikelName);
        panCenter.add(labKabName, "wrap");
        panCenter.add(btnSpeziEdit, "skip, h 40px, w 200px, wrap");

        //add to haupt conteiner
        add(panMenu, "dock north, width 100%, height 50px");
        add(panCenter, "align center, h 100%, w 50%");
    }


}
