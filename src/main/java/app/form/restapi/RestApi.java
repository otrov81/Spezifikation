package app.form.restapi;

import app.model.RestApiModel;
import app.service.DBService;
import app.toolbox.BackIcon;
import app.toolbox.MenuInfo;
import app.toolbox.RowStylePanel;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestApi extends JPanel {

    @Autowired
    private JFrame frame;
    private DBService dbService;
    private UserInfo userInfo;
    private MenuInfo menuInfo;

    private static JTextField mischungText = new JTextField(10);
    private static JTextField zutaten1Text = new JTextField("0", 10);
    private static JTextField zutaten2Text = new JTextField("0", 10);
    private static JTextField zutaten3Text = new JTextField("0", 10);

    private static JButton addBtn = new JButton("Add");
    private static JButton updateBtn = new JButton("Update");
    private static JButton deleteBtn = new JButton("Delete");

    private JTable table;
    private DefaultTableModel model;
    private static JLabel labIconDeleteAll = new JLabel();

    /**
     * Default constructor
     */
    public RestApi() {
        // Default constructor
    }

    /**
     * Constructor with parameters
     * @param frame The JFrame instance
     * @param dbService The DBService instance
     */
    public RestApi(JFrame frame, DBService dbService) {
        this.frame = frame;
        this.dbService = dbService;
        this.userInfo = new UserInfo(dbService);
        this.menuInfo = new MenuInfo();
        init();
    }

    /**
     * Initialize the UI components and layout
     */
    private void init() {
        menuInfo.createMenu(frame, dbService);

        setLayout(new MigLayout("fill, insets 2", "[grow]", "[]2px[]2px[grow]"));

        JPanel menuPanel = new JPanel(new MigLayout("", "", ""));
        menuPanel.setBackground(Color.YELLOW);
        menuPanel.add(BackIcon.getBackIconDaschboad(frame, dbService), "align left");

        JPanel inputPanel = new JPanel(new MigLayout("", "", ""));
        inputPanel.setBackground(Color.ORANGE);
        JPanel panSerach = getjPanelInput();
        inputPanel.add(panSerach, "wrap, h 220px!, w 500px!");

        JPanel tablePanel = new JPanel(new MigLayout(""));
        tablePanel.setBackground(Color.CYAN);

        add(menuPanel, "h 50!, growx, wrap");
        add(inputPanel, "h 200!, growx, wrap");
        add(tablePanel, "grow, push");

        model = getDefaultTableModel();

        table = new JTable(model);
        // Set selection model to allow selecting only one row
        table.setSelectionModel(new ForcedListSelectionModel());

        customizeTable();

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, "w 100%, h 100%");

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    handleTableSelection();
                }
            }
        });

        applyDocumentFilters();

        addBtn.addActionListener(e -> addNewRestApiToDatabase());

        updateBtn.addActionListener(e -> updateRestApiInDatabase());

        deleteBtn.addActionListener(e -> deleteRestApiById());

        // Add Excel export button (icon) to panel
        FlatSVGIcon excelfieldIcon = new FlatSVGIcon("svg/delete.svg");
        labIconDeleteAll.setIcon(new FlatSVGIcon(excelfieldIcon));
        labIconDeleteAll.setToolTipText("Export to Excel");

        // Handle click event for Excel export button
        labIconDeleteAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    // Confirm deletion with dialog
                    int dialogResult = JOptionPane.showConfirmDialog(frame, "Möchten Sie alle Daten löschen?", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        System.out.println("alles delete TRUNCATE `tblspez_restapi`;");
                        dbService.truncateRestApi();
                        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                        tableModel.setRowCount(0);
                    } else {
                        // Do nothing on cancel
                    }
                } catch (Exception del) {
                    System.out.println(del);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconDeleteAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconDeleteAll.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    /**
     * Method to delete a RestApi entry by ID
     */
    private void deleteRestApiById() {
        try {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            long id = (long) table.getValueAt(selectedRow, 0);

            // Confirm deletion with dialog
            int dialogResult = JOptionPane.showConfirmDialog(frame, "Wollen Sie die Datei wierklich löschen", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (dialogResult == JOptionPane.YES_OPTION) {

                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                        model.removeRow(selectedRow);
                    }
                });
            } else {
                // Do nothing on cancel
            }

            // Perform deletion in the database
            dbService.deleteRastApiByID(id);

            // Clear input fields after deletion
            clearInputFields();

        } catch (Exception ex) {
            System.out.println("Error deleting row: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Method to handle selection in the table
     */
//    private void handleTableSelection() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            int modelRow = table.convertRowIndexToModel(selectedRow); // Convert view row index to model row index
//
//            // Assuming model indices for columns, adjust these indices based on your actual table structure
//            String mischungColumn = String.valueOf(table.convertColumnIndexToView(model.findColumn("Mischung")));
//            int zutaten1Column = table.convertColumnIndexToView(model.findColumn("Zutaten 1"));
//            int zutaten2Column = table.convertColumnIndexToView(model.findColumn("Zutaten 2"));
//            int zutaten3Column = table.convertColumnIndexToView(model.findColumn("Zutaten 3"));
//
//            // Retrieve data from model using model indices
//            mischungText.setText(String.valueOf(table.getValueAt(modelRow, Integer.parseInt(mischungColumn))));
//            zutaten1Text.setText(String.valueOf(table.getValueAt(modelRow, zutaten1Column)));
//            zutaten2Text.setText(String.valueOf(table.getValueAt(modelRow, zutaten2Column)));
//            zutaten3Text.setText(String.valueOf(table.getValueAt(modelRow, zutaten3Column)));
//
//            System.out.println(String.valueOf(table.getValueAt(modelRow, Integer.parseInt(mischungColumn)))+"<<<<<<<<<<<"+mischungColumn);
//        }
//    }

    private void handleTableSelection() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = table.convertRowIndexToModel(selectedRow); // Convert view row index to model row index

            // Retrieve data from model using model indices
            long id = (long) table.getModel().getValueAt(modelRow, 0); // Assuming ID is in the first column
            String mischung = (String) table.getModel().getValueAt(modelRow, 1); // Assuming Mischung is in the second column
            long zut1 = (long) table.getModel().getValueAt(modelRow, 2); // Assuming Zutaten 1 is in the third column
            long zut2 = (long) table.getModel().getValueAt(modelRow, 3); // Assuming Zutaten 2 is in the fourth column
            long zut3 = (long) table.getModel().getValueAt(modelRow, 4); // Assuming Zutaten 3 is in the fifth column

            // Update text fields with selected data
            mischungText.setText(mischung);
            zutaten1Text.setText(String.valueOf(zut1));
            zutaten2Text.setText(String.valueOf(zut2));
            zutaten3Text.setText(String.valueOf(zut3));


        }
    }



    /**
     * Apply document filters to input fields to allow only numbers
     */
    private void applyDocumentFilters() {
        ((AbstractDocument) zutaten1Text.getDocument()).setDocumentFilter(new NumberOnlyFilterInput());
        ((AbstractDocument) zutaten2Text.getDocument()).setDocumentFilter(new NumberOnlyFilterInput());
        ((AbstractDocument) zutaten3Text.getDocument()).setDocumentFilter(new NumberOnlyFilterInput());
    }

    /**
     * Customize the appearance and behavior of the table
     */
    private void customizeTable() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setComparator(0, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                // Uspoređujemo Long vrijednosti o1 i o2
                return o1.compareTo(o2);
            }
        });
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        TableCellRenderer alternatingRowRenderer = new AlternatingRowColorRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(alternatingRowRenderer);
        }
    }




    /**
     * Create and return the default table model
     * @return DefaultTableModel instance
     */
    private DefaultTableModel getDefaultTableModel() {
        DefaultTableModel model = new DefaultTableModel() {
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false};

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        model.addColumn("ID");
        model.addColumn("Mischung");
        model.addColumn("Zutaten 1");
        model.addColumn("Zutaten 2");
        model.addColumn("Zutaten 3");
        model.addColumn("Zutaten Summe");

        populateTableModel(model);
        return model;
    }

    /**
     * Populate the table model with data from the database
     * @param model The DefaultTableModel instance
     */
    private void populateTableModel(DefaultTableModel model) {
        List<RestApiModel> restApiList = dbService.selectAllRestApi();
        for (RestApiModel restApi : restApiList) {




            long id = restApi.getId();
            String mischung = restApi.getMischung();
            long zut1 = restApi.getZut1();
            long zut2 = restApi.getZut2();
            long zut3 = restApi.getZut3();
            long sum = zut1 + zut2 + zut3;
            model.addRow(new Object[]{id, mischung, zut1, zut2, zut3, sum});
        }
    }

    /**
     * Create and return the input panel with components
     * @return JPanel instance
     */
    private JPanel getjPanelInput() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 0", "[grow]", "[]0[]"));

        JLabel mischungLabel = new JLabel("Mischung:");
        JLabel zutaten1Label = new JLabel("Zutaten 1:");
        JLabel zutaten2Label = new JLabel("Zutaten 2:");
        JLabel zutaten3Label = new JLabel("Zutaten 3:");

        panel.add(mischungLabel, "gapleft 5px");
        panel.add(mischungText, "wrap, w 150px, h 25px");
        panel.add(zutaten1Label, "gapleft 5px");
        panel.add(zutaten1Text, "wrap, w 150px, h 25px");
        panel.add(zutaten2Label, "gapleft 5px");
        panel.add(zutaten2Text, "wrap, w 150px, h 25px");
        panel.add(zutaten3Label, "gapleft 5px");
        panel.add(zutaten3Text, "wrap, w 150px, h 25px");
        panel.add(addBtn, "skip, split 3");
        panel.add(updateBtn, "");
        panel.add(deleteBtn, "");
        panel.add(labIconDeleteAll, "span");

        return panel;
    }

    /**
     * Add a new RestApi entry to the database
     */
    private void addNewRestApiToDatabase() {
        try {
            String mischung = mischungText.getText();
            long zut1 = Long.parseLong(zutaten1Text.getText());
            long zut2 = Long.parseLong(zutaten2Text.getText());
            long zut3 = Long.parseLong(zutaten3Text.getText());

            RestApiModel restApi = new RestApiModel();
            restApi.setMischung(mischung);
            restApi.setZut1((int) zut1);
            restApi.setZut2((int) zut2);
            restApi.setZut3((int) zut3);

            RestApiModel savedRestApi = dbService.addNewRestApi(restApi);

            if (savedRestApi != null && savedRestApi.getId() != 0) {
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.addRow(new Object[]{savedRestApi.getId(), mischung, zut1, zut2, zut3, (zut1 + zut2 + zut3)});
                    clearInputFields();
                });
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Zutaten.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error adding new entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Clear input fields
     */
    private void clearInputFields() {
        mischungText.setText("");
        zutaten1Text.setText("0");
        zutaten2Text.setText("0");
        zutaten3Text.setText("0");
    }

    /**
     * Update an existing RestApi entry in the database
     */
    private void updateRestApiInDatabase() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            long id = (long) table.getValueAt(selectedRow, 0);
            String mischung = mischungText.getText().trim();
            long zut1 = Long.parseLong(zutaten1Text.getText().trim());
            long zut2 = Long.parseLong(zutaten2Text.getText().trim());
            long zut3 = Long.parseLong(zutaten3Text.getText().trim());

            RestApiModel restApi = new RestApiModel();
            restApi.setId(id);
            restApi.setMischung(mischung);
            restApi.setZut1((int) zut1);
            restApi.setZut2((int) zut2);
            restApi.setZut3((int) zut3);

            dbService.updateRestApi(restApi);

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setValueAt(mischung, selectedRow, 1);
            model.setValueAt(zut1, selectedRow, 2);
            model.setValueAt(zut2, selectedRow, 3);
            model.setValueAt(zut3, selectedRow, 4);
            model.setValueAt(zut1 + zut2 + zut3, selectedRow, 5);

            clearInputFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Zutaten.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            //dbService.LogToDatabase("ERROR", "Error updating RestApi entry.", userInfo.getUserName());
            System.out.println(e+"<<<<<<<<<<<<<<<<<<");
        }
    }

    /**
     * Document filter allowing only numeric input
     */
    private class NumberOnlyFilterInput extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    /**
     * Custom cell renderer to alternate row colors
     */
    private class AlternatingRowColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String currentTheme = UIManager.getLookAndFeel().getName();
            RowStylePanel.setPanelStyle(c, row, currentTheme);

            if (isSelected) {
                RowStylePanel.setTableStyle(table, c, currentTheme);
            }

            return c;
        }
    }

    /**
     * Custom list selection model allowing single selection
     */
    public class ForcedListSelectionModel extends DefaultListSelectionModel {
        public ForcedListSelectionModel() {
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public void removeSelectionInterval(int index0, int index1) {
        }
    }
}
