package app.form.speziEdit;

import app.form.dashboard.Dashboard;
import app.form.textbocke.TextBlocke;
import app.model.SpeziDetail;
import app.model.SpeziPdf;
import app.model.SpeziTextart;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.BackIcon;
import app.toolbox.ExcelPOI; // Import for Excel functionality
import app.toolbox.GeneratePDF;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class SpeziEdit extends JPanel {
    @Autowired
    private static DBService dbService;
    private JFrame frame;
    public static JTextField txtMand = new JTextField();
    public static JTextField txtSa = new JTextField();
    public static JTextField txtLdc = new JTextField();
    public static JTextField txtScpd = new JTextField();
    public static JTextField txtKunde = new JTextField();
    public static JTextField txtArtikel = new JTextField();
    public static JTextField txtSchluessel = new JTextField();
    public static JTextField txtZrn = new JTextField();
    public static JTextArea tarMemo = new JTextArea(10, 350);
    public static JTextArea tarTitel = new JTextArea(2, 20);
    public static JTextArea tarText = new JTextArea(7, 350);
    public static JComboBox<SpeziTextart> dropTextart;
    private static ArrayList<SpeziTextart> textList;
    public static JLabel labAutoname = new JLabel();
    public static JLabel labItem = new JLabel();
    static JTable table;
    private UserInfo userInfo;
    private GeneratePDF generatePDF;
    private static JComboBox<SpeziPdf> jcomPdf = new JComboBox<>();
    private static ArrayList<SpeziPdf> textListPdf;
    ExcelPOI excelPOI = new ExcelPOI(); // Instance of ExcelPOI class for Excel export


    public SpeziEdit(JFrame frame, DBService dbService) {
        this.frame = frame;
        this.dbService = dbService;
        this.userInfo = new UserInfo(dbService);
        this.generatePDF = new GeneratePDF();
        init();


    }

    private void init() {
        // Inicijalizirajte varijablu output ovdje
        String output = userInfo.getUserName();

        setLayout(new MigLayout("insets 2", "[grow]", "")); // postavljanje layout managera

        //setBackground(Color.RED); // postavljanje boje pozadine na contentPane

// add menu
        JPanel panMenu = new JPanel(new MigLayout("", "", ""));
        //panMenu.setBackground(Color.YELLOW);

        //add delete button
        JLabel labIconDelete = new JLabel();
        FlatSVGIcon iconZnr = new FlatSVGIcon("svg/delete_forever.svg");
        labIconDelete.setIcon(new FlatSVGIcon(iconZnr));
        labIconDelete.setToolTipText("Ganze Spezifikation zu löschen!");

        labIconDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Integer mand = Integer.valueOf(txtMand.getText());
                String sa = txtSa.getText();
                String schluessel = txtSchluessel.getText();
                String spcd = txtScpd.getText();
                try {

                    int dialogResult = JOptionPane.showConfirmDialog(frame, "<html><div style='text-align:center'>Sie beabsichtigen die komplette<br><b>Spezifikation zu löschen!</b></div></html>", "LÖSCHEN!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {

                        dbService.deleteSpeziDetailComplet(mand, sa, schluessel, spcd);
                        dbService.deleteSpeziKopfComplet(mand, sa, schluessel, spcd);
                        dbService.deleteAllMemo(mand, sa, schluessel, spcd);
                        ManagerForm.getInstance().showForm(new Dashboard(frame, dbService));

                    } else {

                    }


                } catch (Exception er) {
                    dbService.LogToDatabase("ERROR", "Kann nicht delete komplet kopf/details werden (SpeziEdit) " +er, output);
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconDelete.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        });


        //add dropdown jcombobox pdf
        textListPdf = new ArrayList<>();
        List<SpeziPdf> getTextPdf = dbService.selecAllPdf();
        for (SpeziPdf speziPdf : getTextPdf) {
            textListPdf.add(speziPdf);
            //textList.add(speziTextart);
        }
        dropDownPdf();

        //add Print button
        JLabel labIconPrint = new JLabel();
        FlatSVGIcon iconPrint = new FlatSVGIcon("svg/print.svg");
        labIconPrint.setIcon(new FlatSVGIcon(iconPrint));
        labIconPrint.setToolTipText("Print Spezifikation");


        labIconPrint.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {

                String reportName = extractLogo(jcomPdf.getSelectedItem().toString());

                generatePDF.generatePDF(frame, dbService,reportName,txtSa.getText(),txtSchluessel.getText(),txtScpd.getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconPrint.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        });

        // Add Excel export button (icon) to panMenu
        JLabel labIconExcel = new JLabel();
        FlatSVGIcon excelfieldIcon = new FlatSVGIcon("svg/excel.svg");
        labIconExcel.setIcon(new FlatSVGIcon(excelfieldIcon));
        labIconExcel.setToolTipText("Export to Excel");
        panMenu.add(labIconExcel, "span");

        // Handle click event for Excel export button
        labIconExcel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // Get column names and table data
                List<String> columnNames = getColumnNames();
                List<List<Object>> tableData = getTableData();

                // Open Excel file (if exists) or create new one
                excelPOI.openExcelFile();

                // Remove "Action" column from data for export
                columnNames.remove(0); // Remove first column ("Action")
                for (List<Object> rowData : tableData) {
                    rowData.remove(0); // Remove first cell ("Action") from each row
                }

                // Export data to Excel using ExcelPOI utility
                ExcelPOI.exportExcel(columnNames, tableData);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconExcel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        //add save button
        JLabel labIconSavMemo = new JLabel();
        FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
        labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));
        labIconSavMemo.setToolTipText("Save Memo...");



        labIconSavMemo.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {
                String mand = txtMand.getText();
                String sa = txtSa.getText();
                String schluessel = txtSchluessel.getText();
                String spcd = txtScpd.getText();
                String memo = tarMemo.getText();
                int checkMemo = dbService.checkIfMemoExist(Integer.valueOf(mand),sa,schluessel,spcd);

                if(checkMemo == 0) {
                    dbService.saveAllMemo(Integer.valueOf(mand), sa, schluessel, spcd, memo);
                }else {
                    dbService.updateAllMemo(memo, Integer.valueOf(mand), sa, schluessel, spcd);
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


        JLabel labMenu = BackIcon.getBackIcon(frame, dbService);
        panMenu.add(labMenu, "align left");
        panMenu.add(labIconDelete, "gap 45%");
        panMenu.add(jcomPdf,"w 200px");
        panMenu.add(labIconPrint);
        panMenu.add(labIconExcel);
        panMenu.add(labIconSavMemo);


        //add content
        JPanel panGlavni = new JPanel(new MigLayout("insets 2", "[center][center][center]", ""));
        //panMenu.setBackground(Color.yellow);

        //top panel
        Result result = getResult();

        // center panel
        ResultCenterpanel CenterPan = getResultCenterpanel();

        // add center panel
        CenterPan.panTHeader().add(CenterPan.panTHLeft(), "width 30%, height 100%");
        CenterPan.panTHeader().add(CenterPan.panTHCenter(), "width 40%, height 100%");
        CenterPan.panTHeader().add(CenterPan.panTHRight(), "width 30%, height 100%");

        //add to header
        result.panHeader().add(result.panHLeft(), "width 30%, height 100%");
        result.panHeader().add(result.panHCenter(), "width 40%, height 100%");
        result.panHeader().add(result.panHRight(), "width 30%, height 100%");

        panGlavni.add(result.panHeader(), "dock north, width 100%, height 500px");
        panGlavni.add(CenterPan.panTable(), "north, width 100%, height 100%");

        add(panGlavni, "align center, width 100%, height 100%");

        add(panMenu, "dock north, width 100%, height 100px");

    }

    private ResultCenterpanel getResultCenterpanel() {
        // Inicijalizirajte varijablu output ovdje
        String output = userInfo.getUserName();
        //add table pan
        JPanel panTable = new JPanel(new MigLayout("insets 2", "", ""));
        JPanel panTHeader = new JPanel(new MigLayout("", "", ""));
        panTable.add(panTHeader, "dock north, width 100%, height 200px");

        JPanel panTHLeft = new JPanel(new MigLayout("", "", ""));
        //panTHLeft.setBackground(Color.PINK);

        //add Text Art dropdown
        JLabel labTextart = new JLabel("Textart");

        textList = new ArrayList<>();
        List<SpeziTextart> getText = dbService.selectTextart(txtScpd.getText());

        for (SpeziTextart speziTextart : getText) {
            textList.add(new SpeziTextart(speziTextart.getTextart(), speziTextart.getZnr(), speziTextart.getTitel()));
            //textList.add(speziTextart);
        }


        dropDownItem();


        //add zrn
        JLabel labZrn = new JLabel("ZRN");
        txtZrn = new JTextField();
        JLabel labIconZnr = new JLabel();
        FlatSVGIcon iconZnr = new FlatSVGIcon("svg/save.svg");
        labIconZnr.setIcon(new FlatSVGIcon(iconZnr));
        labIconZnr.setToolTipText("PDF-Zeilen festlegen");
        //add klik auf labIconZnr
        labIconZnr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                labIconZnr.setCursor(new Cursor(Cursor.HAND_CURSOR));

                String znr = txtZrn.getText();
                Integer mand = Integer.valueOf(txtMand.getText());
                String sa = txtSa.getText();
                String schluessel = txtSchluessel.getText();
                String spcd = txtScpd.getText();
                SpeziTextart selectedSpeziTextart = (SpeziTextart) dropTextart.getSelectedItem();
                String textart = selectedSpeziTextart.getTextart();
                try {
                    dbService.updateSpeziDetailTableSort(znr, mand, sa, schluessel, spcd, textart);
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                } catch (Exception exception) {
                    dbService.LogToDatabase("ERROR", "Kann nicht aktualisiert sortierung werden (SpeziEdit) "+e,output);
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconZnr.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconZnr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        //add KEY
        JLabel labKey = new JLabel("KEY");
        JTextField txtKey = new JTextField();
        JLabel labIcon1 = new JLabel();
        FlatSVGIcon jambIcon = new FlatSVGIcon("svg/autorenew.svg");
        labIcon1.setIcon(new FlatSVGIcon(jambIcon));
        // Postavljanje info teksta
        labIcon1.setToolTipText("Update KEY");
        // Podešavanje brzine tooltip-a
        ToolTipManager.sharedInstance().setInitialDelay(0); // tooltip se pojavljuje odmah
        ToolTipManager.sharedInstance().setDismissDelay(2000); // tooltip nestaje nakon 2 sekunde



        //add klik auf jumb
        labIcon1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    ManagerForm.getInstance().showForm(new TextBlocke(frame, dbService));
                    labIcon1.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } catch (Exception e1) {
                    dbService.LogToDatabase("ERROR", "Kann nicht order open autorenew (SpeziEdit) "+e1,output);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIcon1.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIcon1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        //add neu field
        JLabel labIconNewField = new JLabel();
        FlatSVGIcon newfieldIcon = new FlatSVGIcon("svg/addField.svg");
        labIconNewField.setIcon(new FlatSVGIcon(newfieldIcon));
        labIconNewField.setToolTipText("Feld in PDF einfügen");

        //add klik auf jamb
        labIconNewField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //add neu field to details
                String text = "##########################";
                String key = txtKey.getText();
                Integer mand = Integer.valueOf(txtMand.getText());
                String sa = txtSa.getText();
                String schluessel = txtSchluessel.getText();
                String spcd = txtScpd.getText();
                String znr = txtZrn.getText();

                SpeziTextart selectedSpeziTextart = (SpeziTextart) dropTextart.getSelectedItem();

                String textart = selectedSpeziTextart.getTextart();
                String titel = selectedSpeziTextart.getTitel();


                try {
                    // Vaš kod za spremanje teksta
                    dbService.insertSpeziDetail(text, mand, sa, schluessel, spcd, znr, textart, titel);
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));


                } catch (DataIntegrityViolationException r) {
                   // JOptionPane.showMessageDialog(frame, "ooooo", "eeee", JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(frame, "Das Feld existiert bereits", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    // Ako se pojavi greška, prikažite odgovarajuću poruku korisniku
                    // JOptionPane.showMessageDialog(this, "Greška prilikom izmjene(update) podataka: " + r.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);


                }

                labIconNewField.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconNewField.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconNewField.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        panTHLeft.add(labTextart);
        panTHLeft.add(dropTextart, "wrap");
        panTHLeft.add(labZrn);
        panTHLeft.add(txtZrn, "split 2");
        panTHLeft.add(labIconZnr, "wrap");
        panTHLeft.add(labKey);
        panTHLeft.add(txtKey, "split 3");
        panTHLeft.add(labIcon1);
        panTHLeft.add(labIconNewField, "wrap");

        //panTHeader.setBackground(Color.orange);
        //panTable.setBackground(Color.GREEN);


        DefaultTableModel model = getDefaultTableModel();

        // add jtable to model
        table = getTable(model);

        //add height 40px
        table.setRowHeight(40);


        TableActionEventEdit event = getTableActionEvent(table);
        table.getColumnModel().getColumn(0).setCellRenderer(new TableActionCellRenderSpeziEdit());
        table.getColumnModel().getColumn(0).setCellEditor(new TableActionCellEditorSpeziEdit(event));


        JScrollPane scrollPaneTable = new JScrollPane(table);

        //-------------------------- addd table mouseListner

        table.addMouseListener(new TableMouseListener(dbService, table, txtKey));

        panTable.add(scrollPaneTable, "h 100%, w 100%");


        //panel head mitig
        JPanel panTHCenter = new JPanel(new MigLayout("", "", ""));

        //panTHCenter.setBackground(Color.cyan);


        //add title
        JLabel labTitel = new JLabel("Titel");
        tarTitel.setLineWrap(true);
        tarTitel.setWrapStyleWord(true);


        //add automatische name
        labAutoname = new JLabel();
        labItem = new JLabel();
        //labItem.setLineWrap(true);
        //labItem.setWrapStyleWord(true);

        JScrollPane scrollPaneAreaTitel = new JScrollPane(tarTitel);
        //JScrollPane scrollPaneAreaItem = new JScrollPane(labItem);

        panTHCenter.add(labTitel, "width 45%, ");
        panTHCenter.add(labAutoname, "wrap, width 50%");


        panTHCenter.add(scrollPaneAreaTitel, "width 45%");
        //panTHCenter.add(scrollPaneAreaItem," width 50%!");
        panTHCenter.add(labItem, " width 50%");


        //panel header right
        JPanel panTHRight = new JPanel(new MigLayout("", "", ""));
        //panTHRight.setBackground(Color.PINK);

        //addd Text
        JLabel labText = new JLabel("Text");
        tarText.setLineWrap(true);
        tarText.setWrapStyleWord(true);

        JScrollPane scrollPaneAreaText = new JScrollPane(tarText);

        panTHRight.add(labText, "wrap");
        panTHRight.add(scrollPaneAreaText);
        ResultCenterpanel CenterPan = new ResultCenterpanel(panTable, panTHeader, panTHLeft, panTHCenter, panTHRight);
        return CenterPan;
    }

    private static void dropDownItem() {
        dropTextart = new JComboBox<>(textList.toArray(new SpeziTextart[0]));
        dropTextart.setRenderer(new TextartRenderer(false));
        dropTextart.setMaximumSize(dropTextart.getPreferredSize());


// Dodajemo slušača samo jednom izvan MouseListenera
        dropTextart.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Postavite renderer koji prikazuje samo ime i prezime kada se padajuća lista otvori
                dropTextart.setRenderer(new TextartRenderer(true));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Vratite osnovni renderer koji prikazuje samo ime kada se padajuća lista zatvori
                dropTextart.setRenderer(new TextartRenderer(false));
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // Ne radimo ništa kada se padajuća lista otkaže
            }
        });
    }


    private record ResultCenterpanel(JPanel panTable, JPanel panTHeader, JPanel panTHLeft, JPanel panTHCenter,
                                     JPanel panTHRight) {
    }

    private static Result getResult() {
        //add header
        JPanel panHeader = new JPanel(new MigLayout("", "", ""));
        //panHeader.setBackground(Color.red);

        //add panel left
        JPanel panHLeft = new JPanel(new MigLayout("", "", ""));
        //panHLeft.setBackground(Color.yellow);

        //mand
        JLabel labMand = new JLabel("Mand");
        txtMand.setColumns(5);
        txtMand.setEditable(false);

        //SA
        JLabel labSa = new JLabel("SA");
        txtSa.setColumns(5);
        txtSa.setEditable(false);

        //spcd
        JLabel labSpcd = new JLabel("SCPD");
        txtScpd.setColumns(5);
        txtScpd.setEditable(false);

        //add ldc
        JLabel labLdc = new JLabel("LDC");
        txtLdc.setColumns(5);
        txtLdc.setEditable(false);

        panHLeft.add(labMand);
        panHLeft.add(txtMand, "wrap");
        panHLeft.add(labSa);
        panHLeft.add(txtSa, "wrap");
        panHLeft.add(labSpcd);
        panHLeft.add(txtScpd, "wrap");
        panHLeft.add(labLdc);
        panHLeft.add(txtLdc);

        //add header center
        JPanel panHCenter = new JPanel(new MigLayout("", "", ""));
        //panHCenter.setBackground(Color.GRAY);

        //add schluessel
        JLabel labSchluessel = new JLabel("Schluessel");
        txtSchluessel.setColumns(20);
        txtSchluessel.setEditable(false);

        //add Kunde
        JLabel labKunde = new JLabel("Kunde");
        txtKunde.setColumns(50);

        //add Artikel
        JLabel labArtikel = new JLabel("Artikel");
        txtArtikel.setColumns(50);

        panHCenter.add(labSchluessel);
        panHCenter.add(txtSchluessel, "wrap");
        panHCenter.add(labKunde);
        panHCenter.add(txtKunde, "wrap");
        panHCenter.add(labArtikel);
        panHCenter.add(txtArtikel);


        //add pan header right
        JPanel panHRight = new JPanel(new MigLayout("", "", ""));
        //panHRight.setBackground(Color.yellow);

        JLabel labMemo = new JLabel("Memo");
        tarMemo.setLineWrap(true);
        tarMemo.setWrapStyleWord(true);

        JScrollPane scrollPaneArea = new JScrollPane(tarMemo);
        Integer mand = !txtMand.getText().isEmpty() ? Integer.valueOf(txtMand.getText()) : null;


        String memo = dbService.selectIfMemoExist(mand,txtSa.getText(),txtSchluessel.getText(),txtScpd.getText());

        tarMemo.setText(memo);

        panHRight.add(labMemo, "wrap");
        panHRight.add(scrollPaneArea);
        Result result = new Result(panHeader, panHLeft, panHCenter, panHRight);
        return result;
    }

    private record Result(JPanel panHeader, JPanel panHLeft, JPanel panHCenter, JPanel panHRight) {
    }


    private void neuDataMysql(DefaultTableModel model) {
        List<SpeziDetail> selectDetailInfo = dbService.selectSpeziDetail(txtSa.getText(), txtSchluessel.getText(), txtScpd.getText());
        for (SpeziDetail speziDetail : selectDetailInfo) {
            model.addRow(new Object[]{"", speziDetail.getSchluessel(), speziDetail.getZnr(), speziDetail.getTextart(), speziDetail.getKey(),
                    speziDetail.getTitel(), speziDetail.getText(), speziDetail.getEdt(), speziDetail.getAedt()});
        }
    }

    private DefaultTableModel getDefaultTableModel() {
        // add modela for JTable

        DefaultTableModel model = new DefaultTableModel() {
            boolean[] canEdit = new boolean[]{true, false, false, false, false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        model.addColumn("Action");
        model.addColumn("Schluessel");
        model.addColumn("ZNR");
        model.addColumn("Textart");
        model.addColumn("Key");
        model.addColumn("Titel");
        model.addColumn("Text");
        model.addColumn("EDIT");
        model.addColumn("AEDIT");


        neuDataMysql(model);
        return model;
    }


    private TableActionEventEdit getTableActionEvent(JTable table) {
        // Inicijalizirajte varijablu output ovdje
        String output = userInfo.getUserName();
        return new TableActionEventEdit() {
            @Override
            public void onEdit(int row) {
                try {

                    Integer mand = Integer.valueOf(txtMand.getText());
                    String sa = txtSa.getText();
                    String schluessel = txtSchluessel.getText();
                    String text = tarText.getText();
                    String znr = (String) table.getValueAt(row, 2);
                    String textart = (String) table.getValueAt(row, 3);
                    String spcd = txtScpd.getText();
                    String key = "";

                    dbService.updateSpeziDetail(text, key, mand, sa, schluessel, spcd, znr, textart);
                    tarText.setText("");
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                } catch (Exception e) {
                    //log
                    dbService.LogToDatabase("ERROR", "Kann nicht aktualisiert werden (SpeziEdit) "+e,output);
                }


            }

            @Override
            public void onDelete(int row) {

                try {

                    int dialogResult = JOptionPane.showConfirmDialog(frame, "Wollen Sie die Datei wierklich löschen", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        Integer mand = Integer.valueOf(txtMand.getText());
                        String sa = txtSa.getText();
                        String schluessel = txtSchluessel.getText();
                        String znr = (String) table.getValueAt(row, 2);
                        String spcd = txtScpd.getText();

                        dbService.deleteSpeziDetail(mand, sa, schluessel, spcd, znr);

                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }

                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(row);
                    } else {

                    }

                } catch (Exception e) {
                    //log
                    dbService.LogToDatabase("ERROR", "Kann nicht delete werden (SpeziEdit) "+e,output);
                }


            }

        };
    }

    private JTable getTable(DefaultTableModel model) {
        return new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

                Component component = super.prepareRenderer(renderer, row, column);


                return component;
            }
        };
    }

    //add dropdown pdf
    private static void dropDownPdf() {
       jcomPdf = new JComboBox<>(textListPdf.toArray(new SpeziPdf[0]));
       jcomPdf.setRenderer(new TextPdfArt());
       jcomPdf.setMaximumSize(jcomPdf.getPreferredSize());

    }

    public static String extractLogo(String input) {
        String logo = null;
        // Pronađemo indeks početka ključa "logo"
        int index = input.indexOf("logo=");
        if (index != -1) {
            // Pronađemo indeks završetka vrijednosti za ključ "logo"
            int endIndex = input.indexOf(",", index);
            if (endIndex == -1) {
                // Ako ne postoji zarez, završetak je kraj stringa
                endIndex = input.length() - 1;
            }
            // Izdvojimo podstring koji sadrži samo vrijednost za ključ "logo"
            logo = input.substring(index + 5, endIndex);
        }
        return logo;
    }

    /**
     * Method to fetch column names from the JTable.
     *
     * @return List of column names
     */
    private List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            columnNames.add(table.getColumnName(i));
        }
        return columnNames;
    }

    /**
     * Method to fetch table data from the JTable.
     *
     * @return List of lists containing table data
     */
    private List<List<Object>> getTableData() {
        List<List<Object>> tableData = new ArrayList<>();
        for (int row = 0; row < table.getRowCount(); row++) {
            List<Object> rowData = new ArrayList<>();
            for (int col = 0; col < table.getColumnCount(); col++) {
                rowData.add(table.getValueAt(row, col));
            }
            tableData.add(rowData);
        }
        return tableData;
    }
}
