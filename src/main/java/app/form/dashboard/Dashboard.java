package app.form.dashboard;

import app.form.documentation.Documentation;
import app.form.login.LoginForm;
import app.form.spezi.SpeziForm;
import app.form.speziAnlegen.SpeziAnlegen;
import app.form.textEdit.TextEdit;
import app.form.textartenAnlegen.TextartenAnlegen;
import app.form.user.UserForm;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

@Component
public class Dashboard extends JPanel {

    @Autowired
    private DBService dbService;
    private UserInfo userInfo;
    private JButton butSpezi;
    private JButton btnClose;
    private JButton btnSpeziForm;
    private JFrame frame;
    private JButton btnSpeziEdit;
    private JButton btnTextarten;
    private JButton btnBlocke;
    private JButton btnLogin = new JButton("Login");
    private JButton btnDokumentation;

    public Dashboard() {
        // Default constructor
    }

    public Dashboard(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        init();
    }

    public void init() {
        frame.setJMenuBar(null);
        userInfo = new UserInfo(dbService);
        String output = userInfo.getUserName();

        // Set layout for the dashboard with three columns
        setLayout(new MigLayout("insets 2", "[][][]", ""));

        // Left Menu Panel
        JPanel menuPanelLeft = new JPanel(new MigLayout("insets 1", "", ""));

        // Load and scale logo
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource("logo/Almi.png");
        ImageIcon almiIcon = new ImageIcon(resourceUrl);
        Image image = almiIcon.getImage();
        Image scaledImage = image.getScaledInstance(-1, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel labIconAlmi = new JLabel(scaledIcon);
        menuPanelLeft.add(labIconAlmi, "gapleft 30px, h 100px!");

        // Center Menu Panel
        JPanel menuPanelCenter = new JPanel(new MigLayout("insets 1, align center, wrap", "", ""));
        JLabel welcomeText = new JLabel("Willkommen in der Spezifikationszentrale");
        welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeText.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
        menuPanelCenter.add(welcomeText, "span, align center, h 300px!");

        // Right Menu Panel
        JPanel menuPanelRight = new JPanel(new MigLayout("insets 1, align right", "", ""));
        menuPanelRight.add(new JLabel(output + " [" + userInfo.getUserRolle() + "]"), "span, h 40px, growx");

        // Add the original menu panels to the dashboard
        add(menuPanelLeft, "w 20%!, h 40px");
        add(menuPanelCenter, "w 48%!, h 40px");
        add(menuPanelRight, "w 30%!, h 40px, wrap");

        // Panel for buttons on the left
        JPanel xPanel = new JPanel(new MigLayout("insets 1", "", ""));
        butSpezi = new JButton("User");
        butSpezi.setToolTipText("User bearbeiten");

        butSpezi.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new UserForm(frame, dbService));
        });

        // Set hand cursor when mouse enters
        butSpezi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                butSpezi.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                butSpezi.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Add button only if the user is an admin
        if ("admin".equals(userInfo.getUserRolle())) {
            xPanel.add(butSpezi, "h 40px, w 200px, wrap");
        }

        // Documentation button
        btnDokumentation = new JButton("Dokumentation");
        btnDokumentation.setToolTipText("Dokumentation schreiben...");
        btnDokumentation.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new Documentation(frame, dbService));
        });

        // Set hand cursor when mouse enters
        btnDokumentation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDokumentation.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDokumentation.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Textarten button
        btnTextarten = new JButton("Textarten");
        btnTextarten.setToolTipText("Textarten bearbeiten...");
        btnTextarten.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new TextartenAnlegen(frame, dbService));
        });

        // Set hand cursor when mouse enters
        btnTextarten.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnTextarten.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnTextarten.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Texte button
        btnBlocke = new JButton("Texte");
        btnBlocke.setToolTipText("Texte bearbeiten...");
        btnBlocke.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new TextEdit(frame, dbService));
        });
        // Set hand cursor when mouse enters
        btnBlocke.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBlocke.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnBlocke.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });


        // Spezifikation anlegen/bearbeiten button
        btnSpeziEdit = new JButton("<html><div style='text-align: center;'>Spezifikation<br>anlegen/bearbeiten</div></html>");
        btnSpeziEdit.setToolTipText("Spezifikation bearbeiten....");
        btnSpeziEdit.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new SpeziAnlegen(frame, dbService));
        });

        // Set hand cursor when mouse enters
        btnSpeziEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSpeziEdit.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSpeziEdit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Close button
        btnClose = new JButton("Close");
        btnClose.setToolTipText("Programm beenden");
        btnClose.addActionListener(e -> {
            System.exit(0);
        });
        // Set hand cursor when mouse enters
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnClose.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Login button
        btnLogin.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new LoginForm(frame, dbService));
        });
        btnLogin.setToolTipText("Neues Login");

        // Set hand cursor when mouse enters
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Spezifikation suchen button
        btnSpeziForm = new JButton("Spezifikation suchen");
        btnSpeziForm.addActionListener(e -> {
            ManagerForm.getInstance().showForm(new SpeziForm(frame, dbService));
        });

        btnSpeziForm.setToolTipText("Spezifikation bearbeiten...");

        // Set hand cursor when mouse enters
        btnSpeziForm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSpeziForm.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSpeziForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        // Add buttons to the left panel
        xPanel.add(btnLogin, "h 40px, w 200px, wrap");
        xPanel.add(btnClose, "h 40px, w 200px, wrap");

        // Panel for buttons in the center
        JPanel yPanel = new JPanel(new MigLayout("insets 1", "", ""));
        yPanel.add(btnSpeziForm, "h 40px, w 200px, wrap");
        yPanel.add(btnSpeziEdit, "h 40px, w 200px, wrap");
        yPanel.add(btnTextarten, "h 40px, w 200px, wrap");
        yPanel.add(btnBlocke, "h 40px, w 200px, wrap");

        // Panel for buttons on the right
        JPanel zPanel = new JPanel(new MigLayout("insets 1", "", ""));
        zPanel.add(btnDokumentation, "h 40px, w 200px, wrap");

        // Add xPanel, yPanel, and zPanel below the original menu panels
        add(xPanel, "w 20%, h 95%, grow");
        add(yPanel, "w 50%, h 95%, grow");
        add(zPanel, "w 30%, h 95%, grow");
    }
}
