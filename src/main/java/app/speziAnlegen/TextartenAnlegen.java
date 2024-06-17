package app.speziAnlegen;

import app.model.SpeziTextart;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.BackIcon;
import app.toolbox.LimitedDocument;
import app.toolbox.MenuInfo;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TextartenAnlegen extends JPanel {

    @Autowired
    private DBService dbService;
    private JFrame frame;
    private MenuInfo menuInfo;
    private ArrayList<SpeziTextart> textList;
    private ArrayList<SpeziTextart> textDropdown;
    private JPanel panForTextart;
    private UserInfo userInfo;
    private JComboBox<SpeziTextart> combTextart = new JComboBox<>();

    //fix variable
    public static String fixedVariable = "D";

    public TextartenAnlegen(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        this.menuInfo = new MenuInfo();
        this.userInfo = new UserInfo(dbService);
        init();

    }

    public void init() {
        // Initialize the output variable here
        String output = userInfo.getUserName();

        menuInfo.createMenu(frame, dbService); // Assuming 'frame' is your JFrame instance

        setLayout(new MigLayout("", "", ""));
        setBackground(Color.pink);
        // add menu
        JPanel panMenu = new JPanel(new MigLayout("", "", ""));
        panMenu.setBackground(Color.YELLOW);

        JLabel labMenu = BackIcon.getBackIconDaschboad(frame, dbService);
        panMenu.add(labMenu, "span");

        // add content
        JPanel panGlavni = new JPanel(new MigLayout("", "", ""));
        panMenu.setBackground(Color.YELLOW);


        //for for textaarten
        textDropdown = new ArrayList<>();
        List<SpeziTextart> getTextart = dbService.selectTextartSpcd();

        for (SpeziTextart speziTextart : getTextart) {
            textDropdown.add(speziTextart);
        }
        DefaultComboBoxModel<SpeziTextart> model = new DefaultComboBoxModel<>(textDropdown.toArray(new SpeziTextart[0]));
        combTextart = new JComboBox<>(model);


        if (!textDropdown.isEmpty()) {
            boolean exists = false;
            for (SpeziTextart item : textDropdown) {
                if (item.getSpcd().equals(fixedVariable)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                // Dodajte odabranu vrijednost u model ako već nije prisutna
                SpeziTextart newSelectedItem = new SpeziTextart();
                newSelectedItem.setSpcd(fixedVariable);
                combTextart.addItem(newSelectedItem);
            }

            // Postavljanje odabrane vrijednosti
            for (int i = 0; i < combTextart.getItemCount(); i++) {
                SpeziTextart item = combTextart.getItemAt(i);
                if (item.getSpcd().equals(fixedVariable)) {
                    combTextart.setSelectedIndex(i);
                    break;
                }
            }
        }


        combTextart.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof SpeziTextart) {
                    SpeziTextart speziTextart = (SpeziTextart) value;
                    value = speziTextart.getSpcd();

                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Dodavanje ItemListener-a na JComboBox
        combTextart.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Dobivanje nove izabrane stavke iz JComboBox-a
                    SpeziTextart selectedTextart = (SpeziTextart) e.getItem();
                    fixedVariable=selectedTextart.getSpcd();

                    // Osvježavanje prikaza podataka na temelju nove izabrane stavke
                    updateTextartDetails(selectedTextart.getSpcd());
                    revalidate(); // Ponovno validirajte panel
                    repaint(); // Ponovno iscrtajte panel
                }
            }
        });


        //add new jpanel for for
        panForTextart = new JPanel(new MigLayout("", "", ""));
        panForTextart.setBackground(Color.pink);

        //add mand
        JLabel labAction = new JLabel("Action");
        JLabel labMand = new JLabel("Mand");
        JLabel labTextart = new JLabel("Textart");
        JLabel labSpcd = new JLabel("SPCD");
        JLabel labZnr = new JLabel("ZNR");
        JLabel labTitle = new JLabel("Titel");

        panForTextart.add(labAction);
        panForTextart.add(labMand);
        panForTextart.add(labTextart);
        panForTextart.add(labSpcd);
        panForTextart.add(labZnr);
        panForTextart.add(labTitle, "wrap");


        // Dobivanje izabrane stavke iz JComboBox-a
        SpeziTextart selectedTextart = (SpeziTextart) combTextart.getSelectedItem();

        //add daten
        //for for textaarten
        List<SpeziTextart> getTextartD = getSpeziTextartsReload(fixedVariable);


        for (SpeziTextart speziTextartDaten : getTextartD) {

            textList.add(speziTextartDaten);

            JTextField txtMand = new JTextField();
            txtMand.setText(String.valueOf(speziTextartDaten.getMand()));
            txtMand.setEditable(false);

            //add textart
            JTextField txtTextart = new JTextField(50);
            txtTextart.setText(String.valueOf(speziTextartDaten.getTextart()));
            txtTextart.setEditable(false);

            //add spcd
            JTextField txtSpcd = new JTextField();
            txtSpcd.setText(String.valueOf(speziTextartDaten.getSpcd()));
            txtSpcd.setEditable(false);

            // addd znr
            JTextField txtZnr = new JTextField();
            txtZnr.setText(String.valueOf(speziTextartDaten.getZnr()));
            txtZnr.setEditable(false);

            //add titel
            JTextField txtTitel = new JTextField(50);
            txtTitel.setText(String.valueOf(speziTextartDaten.getTitel()));

            //add delete button
            JLabel labIconSavMemo = new JLabel();
            FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
            labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));


            labIconSavMemo.addMouseListener(new MouseAdapter() {


                @Override
                public void mouseClicked(MouseEvent e) {
                    try {

                        fixedVariable = txtSpcd.getText();
                        Integer mand = Integer.valueOf(txtMand.getText());
                        String spcd = txtSpcd.getText();
                        String znr = txtZnr.getText();
                        String titel = txtTitel.getText();

                        int dialogResult = JOptionPane.showConfirmDialog(frame, "Sollen alle gespeicherten Spezifikationen aktualisieret werden?", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (dialogResult == JOptionPane.YES_OPTION) {

                            dbService.updateTextarteTitel(titel, mand, spcd, znr);

                            dbService.updateSpeziDetailTableTitel(titel, mand, spcd, znr);


                        } else {

                        }

                    } catch (Exception e1) {
                        //log
                        dbService.LogToDatabase("ERROR", "Kann nicht update werden (TextartenAnlegen) " + e1, output);
                    }

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    labIconSavMemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    labIconSavMemo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            });

            panForTextart.add(labIconSavMemo);
            panForTextart.add(txtMand);
            panForTextart.add(txtTextart);
            panForTextart.add(txtSpcd);
            panForTextart.add(txtZnr);
            panForTextart.add(txtTitel, "wrap");


        }// end for

        JTextField txtMand = new JTextField();
        txtMand.setText("1000");

        //add textart
        JTextField txtTextart = new JTextField(50);

        //add spcd
        JTextField txtSpcd = new JTextField();
        txtSpcd.setText(selectedTextart.getSpcd());

        // addd znr
        JTextField txtZnr = new JTextField();
        txtZnr.setDocument(new LimitedDocument(3));

        //add titel
        JTextField txtTitel = new JTextField(50);

        //add delete button
        JLabel labIconSavMemo = new JLabel();
        FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
        labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));


        labIconSavMemo.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    fixedVariable = txtSpcd.getText();
                    String textart = txtTextart.getText();
                    Integer mand = Integer.valueOf(txtMand.getText());
                    String spcd = txtSpcd.getText();
                    String znr = txtZnr.getText();
                    String titel = txtTitel.getText();
                    int checkZnr = dbService.checkIfZnrExistirt(mand,spcd,textart, znr);

                    if(checkZnr != 0){
                        JOptionPane.showMessageDialog(frame, "Wert ZNR existiert, bitte geben Sie einen anderen Wert ein", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    }else {

                        dbService.insetrTextartAll(mand, spcd, textart, znr, titel);
                        ManagerForm.getInstance().showForm(new TextartenAnlegen(frame, dbService));

                    }



                } catch (Exception e1) {
                    //log
                    dbService.LogToDatabase("ERROR", "Kann nicht insert werden (TextartenAnlegen) " + e1, output);
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        });

        panForTextart.add(labIconSavMemo);
        panForTextart.add(txtMand);
        panForTextart.add(txtTextart);
        panForTextart.add(txtSpcd);
        panForTextart.add(txtZnr);
        panForTextart.add(txtTitel, "wrap");


        panGlavni.add(combTextart, "wrap,w 300px!");
        panGlavni.add(panForTextart);

        JScrollPane scrollPaneGlavni = new JScrollPane(panGlavni);
        scrollPaneGlavni.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPaneGlavni, "align center, grow");
        //add component
        add(panMenu, "dock north, width 100%, height 50px");

    }

    private void updateTextartDetails(String selectedTextart) {

        // Inicijalizirajte varijablu output ovdje
        String output = userInfo.getUserName();

        panForTextart.removeAll(); // Uklanja sve komponente iz panela prije nego što dodamo nove

        List<SpeziTextart> getTextartD = getSpeziTextartsReload(selectedTextart);

        //add mand
        JLabel labAction = new JLabel("Action");
        JLabel labMand = new JLabel("Mand");
        JLabel labTextart = new JLabel("Textart");
        JLabel labSpcd = new JLabel("SPCD");
        JLabel labZnr = new JLabel("ZNR");
        JLabel labTitle = new JLabel("Titel");

        panForTextart.add(labAction);
        panForTextart.add(labMand);
        panForTextart.add(labTextart);
        panForTextart.add(labSpcd);
        panForTextart.add(labZnr);
        panForTextart.add(labTitle, "wrap");

        for (SpeziTextart speziTextartDaten : getTextartD) {
            JTextField txtMand = new JTextField();
            txtMand.setText(String.valueOf(speziTextartDaten.getMand()));
            txtMand.setEditable(false);

            // Ovdje dodajte ostale komponente
            //add textart
            JTextField txtTextart = new JTextField(50);
            txtTextart.setText(String.valueOf(speziTextartDaten.getTextart()));
            txtTextart.setEditable(false);

            //add spcd
            JTextField txtSpcd = new JTextField();
            txtSpcd.setText(String.valueOf(speziTextartDaten.getSpcd()));
            txtSpcd.setEditable(false);

            // addd znr
            JTextField txtZnr = new JTextField();
            txtZnr.setText(String.valueOf(speziTextartDaten.getZnr()));
            txtZnr.setEditable(false);

            //add titel
            JTextField txtTitel = new JTextField(50);
            txtTitel.setText(String.valueOf(speziTextartDaten.getTitel()));

            //add delete button
            JLabel labIconSavMemo = new JLabel();
            FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
            labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));


            labIconSavMemo.addMouseListener(new MouseAdapter() {


                @Override
                public void mouseClicked(MouseEvent e) {
                    try {

                        fixedVariable = txtSpcd.getText();
                        Integer mand = Integer.valueOf(txtMand.getText());
                        String spcd = txtSpcd.getText();
                        String znr = txtZnr.getText();
                        String titel = txtTitel.getText();

                        int dialogResult = JOptionPane.showConfirmDialog(frame, "Sollen alle gespeicherten Spezifikationen aktualisieret werden?", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (dialogResult == JOptionPane.YES_OPTION) {
                            dbService.updateTextarteTitel(titel, mand, spcd, znr);

                            dbService.updateSpeziDetailTableTitel(titel, mand, spcd, znr);


                        } else {

                        }

                    } catch (Exception e1) {
                        //log
                        dbService.LogToDatabase("ERROR", "Kann nicht update werden (TextartenAnlegen) " + e1, output);
                    }

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    labIconSavMemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    labIconSavMemo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            });


            panForTextart.add(labIconSavMemo);
            panForTextart.add(txtMand);
            panForTextart.add(txtTextart);
            panForTextart.add(txtSpcd);
            panForTextart.add(txtZnr);
            panForTextart.add(txtTitel, "wrap");
            // Dodajte ostale komponente

            // Ponovno iscrtavanje panela nakon promjene
            panForTextart.revalidate();
            panForTextart.repaint();
        }

        JTextField txtMand = new JTextField();
        txtMand.setText("1000");

        //add textart
        JTextField txtTextart = new JTextField(50);
        // Dobivanje izabrane stavke iz JComboBox-a
        SpeziTextart selectedTextart1 = (SpeziTextart) combTextart.getSelectedItem();
        //add spcd
        JTextField txtSpcd = new JTextField();
        txtSpcd.setText(selectedTextart1.getSpcd());

        // addd znr
        JTextField txtZnr = new JTextField();
        txtZnr.setDocument(new LimitedDocument(3));

        //add titel
        JTextField txtTitel = new JTextField(50);

        //add delete button
        JLabel labIconSavMemo = new JLabel();
        FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
        labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));


        labIconSavMemo.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    fixedVariable = txtSpcd.getText();
                    String textart = txtTextart.getText();
                    Integer mand = Integer.valueOf(txtMand.getText());
                    String spcd = txtSpcd.getText();
                    String znr = txtZnr.getText();
                    String titel = txtTitel.getText();

                    int checkZnr = dbService.checkIfZnrExistirt(mand,spcd,textart, znr);

                    if(checkZnr != 0){
                        JOptionPane.showMessageDialog(frame, "Wert ZNR existiert, bitte geben Sie einen anderen Wert ein", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    }else {

                        dbService.insetrTextartAll(mand, spcd, textart, znr, titel);
                        ManagerForm.getInstance().showForm(new TextartenAnlegen(frame, dbService));
                    }





                } catch (Exception e1) {
                    //log
                    dbService.LogToDatabase("ERROR", "Kann nicht insert werden (TextartenAnlegen) " + e1, output);
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        });

        panForTextart.add(labIconSavMemo);
        panForTextart.add(txtMand);
        panForTextart.add(txtTextart);
        panForTextart.add(txtSpcd);
        panForTextart.add(txtZnr);
        panForTextart.add(txtTitel, "wrap");
    }

    private List<SpeziTextart> getSpeziTextartsReload(String selectedTextart) {
        textList = new ArrayList<>();
        textList.clear();
        List<SpeziTextart> getTextartD = dbService.selectTextart(selectedTextart);
        textList.addAll(getTextartD);
        return getTextartD;
    }


}
