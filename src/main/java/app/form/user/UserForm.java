package app.form.user;

import app.model.User;
import app.service.DBService;
import app.toolbox.BackIcon;
import app.toolbox.MD5;
import app.toolbox.MenuInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import app.toolbox.ExcelPOI; // Import for Excel functionality

/**
 * JPanel class for managing user data, including table operations and Excel export.
 */
public class UserForm extends JPanel {

    @Autowired
    private static DBService dbService; // Dependency injection of DBService
    private MenuInfo menuInfo; // Instance of MenuInfo class
    private JFrame frame; // Instance of JFrame
    private String currentTheme; // Stores the current theme name
    private static String userNameStyle; // Stores the user name style (not used in current implementation)
    private static JTextField useridText; // JTextField for user ID (not used in current implementation)
    private static JTextField usernameText; // JTextField for username (not used in current implementation)
    private static JTextField passwordText; // JTextField for password (not used in current implementation)
    private static JTextField berechtigungText; // JTextField for authorization (not used in current implementation)
    static JTable table; // Static JTable instance to display data
    private AddUser addUser; // Instance of AddUser class for adding users

    ExcelPOI excelPOI = new ExcelPOI(); // Instance of ExcelPOI class for Excel export

    MD5 md5 = new MD5(); // Instance of MD5 class (not used in current implementation)

    /**
     * Constructor for UserForm.
     *
     * @param frame     The parent JFrame for the form
     * @param dbService The DBService instance for database operations
     */
    public UserForm(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        this.menuInfo = new MenuInfo();
        this.addUser = new AddUser(dbService, frame);
        init(); // Initialize the form components
    }

    /**
     * Method to initialize the UserForm panel.
     */
    public void init() {
        menuInfo.createMenu(frame, dbService); // Create menu bar

        setLayout(new MigLayout("insets 2", "[grow][50%]", "[grow][100%]"));

        // Add menu panel
        JPanel panMenu = new JPanel(new MigLayout("", "", ""));
        JLabel labMenu = BackIcon.getBackIconDaschboad(frame, dbService);
        panMenu.add(labMenu);

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

        // Create table model and populate with initial data
        DefaultTableModel model = getDefaultTableModel();

        // Set current theme based on UIManager
        currentTheme = UIManager.getLookAndFeel().getName();

        // Add "Excel export" button to the menu panel
        panMenu.add(labIconExcel);

        // Create JTable and add to panel with scroll functionality
        table = getTable(model);
        table.setRowHeight(40); // Set row height

        // Attach table action event handler for editing and deleting
        TableActionEvent event = getTableActionEvent(table);

        // Configure table columns for editing and rendering icons
        table.getColumnModel().getColumn(0).setCellRenderer(new TableActionCellRender());
        table.getColumnModel().getColumn(0).setCellEditor(new TableActionCellEditor(event));

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel for adding user data to the right of the table
        JPanel desniPanel = addUser.getjPanelRight(model);

        // Add components to the main panel using MigLayout constraints
        add(panMenu, "dock north, width 100%, height 100px");
        add(desniPanel, "cell 1 1, grow");
        add(scrollPane, "cell 0 1, grow");

        // Create TableRowSorter and set default sorting order
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
    }

    /**
     * Method to fetch updated data from MySQL and populate the table model.
     *
     * @param model The table model to populate
     */
    private void neuDataMysql(DefaultTableModel model) {
        List<User> updatedUsers = dbService.findAll();
        for (User user : updatedUsers) {
            model.addRow(new Object[]{"", user.getUserid(), user.getUsername(), user.getPassword(), user.getBerechtigung()});
        }
    }

    /**
     * Method to create and return the default table model with editable columns.
     *
     * @return The default table model
     */
    private DefaultTableModel getDefaultTableModel() {
        DefaultTableModel model = new DefaultTableModel() {
            boolean[] canEdit = new boolean[]{true, false, false, false};

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        model.addColumn("Action");
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Password");
        model.addColumn("Berechtigung");

        neuDataMysql(model); // Populate model with data
        return model;
    }

    /**
     * Method to handle table editing and deletion actions.
     *
     * @param table The JTable instance
     * @return TableActionEvent instance for handling actions
     */
    private TableActionEvent getTableActionEvent(JTable table) {
        return new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try {
                    TableColumnModel columnModel = table.getColumnModel();
                    String[] columnNames = {"Action", "ID", "Name", "Berechtigung", "Password"};
                    Map<String, Integer> columnIndexMap = new HashMap<>();

                    // Find column indexes for specified column names
                    for (int i = 0; i < columnModel.getColumnCount(); i++) {
                        String columnName = (String) columnModel.getColumn(i).getHeaderValue();
                        for (String name : columnNames) {
                            if (columnName.equals(name)) {
                                columnIndexMap.put(name, i);
                                break;
                            }
                        }
                    }

                    String username = "xx";
                    String berechtigung = "";
                    Long userid = 0L;

                    // Retrieve values from table based on column names
                    for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
                        if ("Name".equals(entry.getKey())) {
                            username = (String) table.getValueAt(row, entry.getValue());
                        } else if ("Berechtigung".equals(entry.getKey())) {
                            berechtigung = (String) table.getValueAt(row, entry.getValue());
                        } else if ("ID".equals(entry.getKey())) {
                            userid = (Long) table.getValueAt(row, entry.getValue());
                        }
                    }

                    // Set retrieved values to corresponding fields in AddUser panel
                    addUser.usernameText.setText(username);
                    addUser.passwordText.setText("");
                    addUser.berechtigungText.setText(berechtigung);
                    addUser.useridText.setText(String.valueOf(userid));
                    addUser.rowNumber.setText(String.valueOf(row));

                } catch (Exception e) {
                    dbService.LogToDatabase("ERROR", "Unable to edit (UserForm)", String.valueOf(e));
                }
            }

            @Override
            public void onDelete(int row) {
                try {
                    Long userID = (Long) table.getValueAt(row, 1); // Get user ID

                    // Confirm deletion with dialog
                    int dialogResult = JOptionPane.showConfirmDialog(frame, "Wollen Sie die Datei wierklich löschen", "Achtung!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        dbService.deleteById(userID); // Delete user from database

                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }

                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(row); // Remove row from table model
                    } else {
                        // Do nothing on cancel
                    }

                } catch (Exception e) {
                    dbService.LogToDatabase("ERROR", "Unable to delete (UserForm)", String.valueOf(e));
                }
            }

            @Override
            public void onPdf(int row) {

                // table db -> id
                Long userID = (Long) table.getValueAt(row, 1); // Get user ID
                // Implement PDF action
                System.out.println("PDF action triggered for row: " + row+" >>>> "+userID);
                // Add your logic for PDF action here
            }
        };
    }

    /**
     * Method to create a JTable with customized rendering.
     *
     * @param model The table model to use for data
     * @return The configured JTable instance
     */
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
