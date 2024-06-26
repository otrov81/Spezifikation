package app.form.restapiweb;

import app.model.WebRestApiModel;
import app.service.ApiService;
import app.service.DBService;
import app.toolbox.BackIcon;
import app.toolbox.MenuInfo;
import app.toolbox.UserInfo;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * The RestApiWeb class extends JPanel and represents a user interface component
 * for interacting with a REST API web service.
 */
@Component
public class RestApiWeb extends JPanel {

    @Autowired
    private JFrame frame;
    @Autowired
    private ApiService apiService;
    @Autowired
    private DBService dbService;

    private UserInfo userInfo;
    private MenuInfo menuInfo;

    /**
     * Default constructor for Spring to initialize the bean.
     */
    public RestApiWeb() {
        // No initialization done here as it's managed by Spring
    }

    /**
     * Constructor for manual initialization without Spring.
     *
     * @param frame     the main application frame
     * @param dbService the service for database operations
     */
    public RestApiWeb(JFrame frame, DBService dbService) {
        this.frame = frame;
        this.dbService = dbService;
        this.userInfo = new UserInfo(dbService); // Initialize UserInfo with the DB service
        this.menuInfo = new MenuInfo(); // Initialize MenuInfo
        this.apiService = new ApiService();
        init(); // Call the initialization method to setup the UI
    }

    private void init() {
        // Create the menu using MenuInfo
        menuInfo.createMenu(frame, dbService);

        // Set the layout manager for this panel using MigLayout with specific constraints
        setLayout(new MigLayout("fill, insets 0", "[grow]", "[]2px[]2px[grow]"));

        // Create a new panel for the menu with its own MigLayout
        JPanel menuPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", ""));
        menuPanel.setBackground(Color.YELLOW);
        // Add a back icon button to the menu panel aligned to the left
        menuPanel.add(BackIcon.getBackIconDaschboad(frame, dbService), "align left, wrap");

        // Create inputPanel using MigLayout
        JPanel inputPanel = new JPanel(new MigLayout("fill, insets 2", "[grow]", "[][]"));
        inputPanel.setBackground(Color.ORANGE);

        // Add components to inputPanel
        //JPanel panSearch = new JPanel(new MigLayout("", "", ""));
        //panSearch.setBackground(Color.GREEN);
       // inputPanel.add(panSearch, "wrap, grow, h 220px!, w 500px");

        // Create tablePanel using MigLayout
        JPanel tablePanel = new JPanel(new MigLayout("fill, insets 2", "[grow]", "[grow]"));
        tablePanel.setBackground(Color.CYAN);

        // Create table and add it to tablePanel with JScrollPane
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "PLZ", "Ort","FirmenbuchNummer"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, "grow");

        // Fetch data from API and populate tableModel
        try {
            List<WebRestApiModel> dataList = apiService.fetchData();
            for (WebRestApiModel data : dataList) {
                tableModel.addRow(new Object[]{data.getId(), data.getPlz(), data.getOrt(), data.getFirmenbuchNummer()});
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to fetch data from API", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add panels to RestApiWeb panel using MigLayout constraints
        add(menuPanel, "h 50px!,growx, wrap");
        add(inputPanel, "h 200px!, growx, wrap");
        add(tablePanel, "grow, push");

        // Add the menu panel to this main panel with specific layout constraints
        add(menuPanel, "dock north, growx, wrap");
    }
}
