package app.form.login;

import app.form.dashboard.Dashboard;
import app.model.User;
import app.notifications.Notifications;
import app.service.DBService;
import app.service.ManagerForm;
import app.toolbox.MD5;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static app.toolbox.StyleLoginUser.ChekStyle;

@Component
public class LoginForm extends JPanel {


    // Variables declaration - do not modify
    @Autowired
    private DBService dbService;
    private static JTextField txtUsern = new JTextField(); // Initialization of txtUsern


    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton cmdLogin;
    private JFrame frame;
    private UserInfo userInfo;
    MD5 md5 = new MD5();
    // End of variables declaration

    @Autowired
    public LoginForm(JFrame frame, DBService dbService) {
        this.dbService = dbService;
        this.frame = frame;
        init();
    }

    public static String getTxtUsername() {

        try {
            txtUsern.getText();
            return txtUsern.getText();
        }catch (NullPointerException e){
            return null;
        }


    }

    private void init() {

        userInfo = new UserInfo(dbService);

        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmdLogin = new JButton("Login");
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Geben Sie Ihren Benutzernamen ein");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Geben Sie Ihr Passwort ein");

        JLabel lbTitle = new JLabel("<html>Willkommen zurück! &#9786;</html>");
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER); // Postavljanje teksta na sredinu
        JLabel description = new JLabel("Bitte melden Sie sich an, um auf Ihr Konto zuzugreifen");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle,"");
        panel.add(description);
        panel.add(new JLabel("Benutzername"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Passwort"), "gapy 8");
        panel.add(txtPassword);
        panel.add(cmdLogin, "gapy 15,  h 30!, growx");

        cmdLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = md5.getMd5(new String(txtPassword.getPassword()));

            if (txtUsern != null)
                txtUsern = new JTextField(username);

            String output = userInfo.getUserName();

            User user = dbService.findByUsernameAndPassword(username, password);
            if (user != null) {
                User berechtigung = dbService.findByUsername(user.getUsername());
                ChekStyle(berechtigung);
                ManagerForm.getInstance().showForm(new Dashboard(frame,dbService));
                dbService.LogToDatabase("INFO", "Successful login!",output); // log
            } else {

                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Ungültiger Benutzername oder Passwort");
                //JOptionPane.showMessageDialog(frame, "Ungültiger Benutzername oder Passwort", "Login Fehler", JOptionPane.ERROR_MESSAGE);
                dbService.LogToDatabase("WRONG", "Invalid username or password!",output); // log
            }
        });

        cmdLogin.setToolTipText("Geben Sie Namen und Passwort ein!!");

        // Set hand cursor when mouse enters
        cmdLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Set hand cursor when mouse enters
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cmdLogin.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Set default cursor when mouse exits
            }
        });

        add(panel);

    }

}
