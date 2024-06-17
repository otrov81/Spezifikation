package app.form.spezi;

import app.form.speziEdit.SpeziEdit;
import app.model.SpeziView;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.BackIcon;
import app.toolbox.ExcelPOI; // Import for Excel functionality
import app.toolbox.MenuInfo;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SpeziForm extends JPanel {
    @Autowired
    private DBService dbService;
    private JFrame frame;
    private MenuInfo menuInfo;
    private JTable table;
    private SpeziEdit speziEdit;
    private UserInfo userInfo;
    ExcelPOI excelPOI = new ExcelPOI(); // Instance of ExcelPOI class for Excel export

    public SpeziForm(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        this.menuInfo = new MenuInfo();
        this.speziEdit = new SpeziEdit(frame, dbService);
        init();
    }

    private void init() {
        // Create the menu for the frame
        menuInfo.createMenu(frame, dbService);

        // Set layout for the main panel
        setLayout(new MigLayout("fill", "[grow]", ""));

        // Menu Panel on the left
        JPanel menuPanelLeft = new JPanel(new MigLayout("", "", ""));
        JLabel labMenu = BackIcon.getBackIconDaschboad(frame, dbService);
        menuPanelLeft.add(labMenu);

        // Add Excel export button (icon) to panMenu
        JLabel labIconExcel = new JLabel();
        FlatSVGIcon excelfieldIcon = new FlatSVGIcon("svg/excel.svg");
        labIconExcel.setIcon(new FlatSVGIcon(excelfieldIcon));
        labIconExcel.setToolTipText("Export to Excel");
        menuPanelLeft.add(labIconExcel, "span");

        // Panel for table and search fields
        JPanel tablePanel = new JPanel(new MigLayout("insets 4", "", ""));
        JLabel tabLabel = new JLabel();
        tablePanel.add(tabLabel, "wrap");

        // Search fields
        JLabel labSpcd = new JLabel("SpCd");
        JLabel labArtikelNr = new JLabel("Artikelnummer");
        JLabel labKundenNr = new JLabel("Kundennummer");
        JTextField txtSpcd = new JTextField(10);
        JTextField txtArtikelnr = new JTextField(20);
        JTextField txtKundennr = new JTextField(20);
        JButton btnSearch = new JButton("Suche");
        JButton btnReset = new JButton("Reset");

        // Search panel layout
        JPanel panSerach = new JPanel(new MigLayout("", "", ""));
        panSerach.add(labSpcd, "align label");
        panSerach.add(txtSpcd, "wrap");
        panSerach.add(labKundenNr, "align label");
        panSerach.add(txtKundennr, "wrap");
        panSerach.add(labArtikelNr, "align label");
        panSerach.add(txtArtikelnr, "wrap");
        panSerach.add(btnSearch, "skip, split 2");
        panSerach.add(btnReset);

        tablePanel.add(panSerach, "wrap, h 200px!, w 350px!");


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

        // Create table model and JTable
        DefaultTableModel model = getDefaultTableModel();
        table = getTable(model);
        table.setRowHeight(40);

        // Add event handling for table actions
        TableActionEventSpezi event = getTableActionEvent(table);

        // Set cell renderers and editors for action column
        table.getColumnModel().getColumn(0).setCellRenderer(new TableActionCellRenderSpezi());
        table.getColumnModel().getColumn(0).setCellEditor(new TableActionCellEditorSpezi(event));

        JScrollPane scrollPane = new JScrollPane(table);

        // Create and set a TableRowSorter for sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

        tablePanel.add(scrollPane, "grow, push, wrap");

        // Add components to the main panel
        add(menuPanelLeft, "dock north, h 50px");
        add(tablePanel, "grow");

        // Handle frame resize to adjust panel and scroll pane sizes
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newHeight = frame.getHeight() - menuPanelLeft.getHeight();
                tablePanel.setPreferredSize(new Dimension(frame.getWidth(), newHeight));
                scrollPane.setPreferredSize(new Dimension(frame.getWidth(), newHeight - 200));
                tablePanel.revalidate();
                tablePanel.repaint();
                scrollPane.revalidate();
            }
        });

        // Add button actions for search and reset
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                searchDataMysql(model, txtSpcd.getText(), txtKundennr.getText(), txtArtikelnr.getText());
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neuDataMysql(model);
                txtSpcd.setText("");
                txtArtikelnr.setText("");
                txtKundennr.setText("");
            }
        });
    }

    private DefaultTableModel getDefaultTableModel() {
        // Create table model with editable columns
        DefaultTableModel model = new DefaultTableModel() {
            boolean[] canEdit = new boolean[]{true, false, false, false, false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        model.addColumn("Action");
        model.addColumn("ID");
        model.addColumn("SA");
        model.addColumn("Schluessel");
        model.addColumn("Mand");
        model.addColumn("SPCD");
        model.addColumn("KundenNr");
        model.addColumn("Kunden");
        model.addColumn("ArtNr");
        model.addColumn("Artikel");
        model.addColumn("LDC");
        model.addColumn("Erfassungsdatum");
        model.addColumn("Änderungsdatum");

        neuDataMysql(model);
        return model;
    }

    private void neuDataMysql(DefaultTableModel model) {
        List<SpeziView> updatedUsers = dbService.SelectAllData();
        getTabeleDaten(model, updatedUsers);
    }

    private void searchDataMysql(DefaultTableModel model, String spdc, String kunde, String artikel) {
        List<SpeziView> updatedUsers = dbService.searchBySPCD(spdc, kunde, artikel);
        getTabeleDaten(model, updatedUsers);
    }

    private static void getTabeleDaten(DefaultTableModel model, List<SpeziView> updatedUsers) {
        for (SpeziView speziView : updatedUsers) {
            model.addRow(new Object[]{"",
                    speziView.getId(),
                    speziView.getSa(),
                    speziView.getSchluessel(),
                    speziView.getMand(),
                    speziView.getSpcd(),
                    speziView.getKunde(),
                    speziView.getKDNam(),
                    speziView.getArtikel(),
                    speziView.getArtbez(),
                    speziView.getLdc(),
                    speziView.getEdt(),
                    speziView.getAedt()
            });
        }
    }

    private TableActionEventSpezi getTableActionEvent(JTable table) {
        userInfo = new UserInfo(dbService);
        String output = userInfo.getUserName();
        return new TableActionEventSpezi() {
            @Override
            public void onEdit(int row) {
                try {
                    // Obtain the column model from the table
                    TableColumnModel columnModel = table.getColumnModel();

                    // Names of the columns you want to identify
                    String[] columnNames = {"SA", "ID", "Schluessel", "Mand", "SPCD", "KundenNr", "Kunden", "ArtNr", "Artikel", "LDC", "Erfassungsdatum", "Änderungsdatum"};

                    // Map to store column indexes by names
                    Map<String, Integer> columnIndexMap = new HashMap<>();

                    // Find the indexes of columns with corresponding names
                    for (int i = 0; i < columnModel.getColumnCount(); i++) {
                        String columnName = (String) columnModel.getColumn(i).getHeaderValue();
                        for (String name : columnNames) {
                            if (columnName.equals(name)) {
                                columnIndexMap.put(name, i);
                                break;
                            }
                        }
                    }

                    // Initialize variables for column values
                    String sa = "";
                    String schluessel = "";
                    Integer mand = 0;
                    String spcd = "";
                    String kunde = "";
                    String kundeName = "";
                    String artikel = "";
                    String artikelName = "";
                    String ldc = "";

                    // Get the values from the table
                    for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
                        if ("SA".equals(entry.getKey())) {
                            sa = (String) table.getValueAt(row, entry.getValue());
                        } else if ("Schluessel".equals(entry.getKey())) {
                            schluessel = (String) table.getValueAt(row, entry.getValue());
                        } else if ("Mand".equals(entry.getKey())) {
                            mand = (Integer) table.getValueAt(row, entry.getValue());
                        }else if ("KundenNr".equals(entry.getKey())) {
                            kunde = (String) table.getValueAt(row, entry.getValue());
                        }else if ("Kunden".equals(entry.getKey())) {
                            kundeName = (String) table.getValueAt(row, entry.getValue());
                        }else if ("ArtNr".equals(entry.getKey())) {
                            artikel = (String) table.getValueAt(row, entry.getValue());
                        }else if ("Artikel".equals(entry.getKey())) {
                            artikelName = (String) table.getValueAt(row, entry.getValue());
                        }else if ("LDC".equals(entry.getKey())) {
                            ldc = (String) table.getValueAt(row, entry.getValue());
                        } else if ("SPCD".equals(entry.getKey())) {
                            spcd = (String) table.getValueAt(row, entry.getValue());
                        } else if ("KundenNr".equals(entry.getKey())) {
                            kunde = (String) table.getValueAt(row, entry.getValue());
                        } else if ("Kunden".equals(entry.getKey())) {
                            kundeName = (String) table.getValueAt(row, entry.getValue());
                        } else if ("ArtNr".equals(entry.getKey())) {
                            artikel = (String) table.getValueAt(row, entry.getValue());
                        } else if ("Artikel".equals(entry.getKey())) {
                            artikelName = (String) table.getValueAt(row, entry.getValue());
                        } else if ("LDC".equals(entry.getKey())) {
                            ldc = (String) table.getValueAt(row, entry.getValue());
                        }
                    }

                    // Set the values in the edit form
                    speziEdit.txtMand.setText(String.valueOf(mand));
                    speziEdit.txtSa.setText(sa);
                    speziEdit.txtScpd.setText(spcd);
                    speziEdit.txtKunde.setText(kunde + ", " + kundeName);
                    speziEdit.txtArtikel.setText(artikel + ", " + artikelName);
                    speziEdit.txtSchluessel.setText(schluessel);
                    speziEdit.txtLdc.setText(ldc);

                    // Show the edit form
                    ManagerForm.getInstance().showForm(new SpeziEdit(frame, dbService));
                } catch (Exception e) {
                    dbService.LogToDatabase("ERROR", "Kann nicht edit werden (SpeziForm) " + e, output);
                }
            }

            @Override
            public void onDelete(int row) {
                try {
                    Integer ID = (Integer) table.getValueAt(row, 1);
                    int dialogResult = JOptionPane.showConfirmDialog(frame, "Wollen Sie die Datei wirklich löschen?", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        dbService.deleteSpeziViev(ID);

                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }

                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(row);
                    }
                } catch (Exception e) {
                    dbService.LogToDatabase("ERROR", "Kann nicht delete werden (SpeziForm) " + e, output);
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