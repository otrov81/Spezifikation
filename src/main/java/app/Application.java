package app;

import app.form.login.LoginForm;
import app.notifications.Notifications;
import app.service.DBService;
import app.service.ManagerForm;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class Application extends JFrame {
    @Autowired
    private final DBService dbService;
    private JProgressBar progressBar;
    private String name = System.getProperty("user.name");

    @Autowired
    public Application(DBService dbService) {
        this.dbService = dbService;
        init();
    }

    private void init() {

        // call progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);
        Notifications.getInstance().setJFrame(this);

        ManagerForm.getInstance().initApplication(this);
        //sso ADFS
        new Thread(this::checkSSO).start();
    }

    private void checkSSO() {
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(200, 50));
        setLocationRelativeTo(null);
        setVisible(true);

// Simulation of a process that lasts for some time
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressBar.setValue(i);
        }

            ManagerForm.getInstance().showForm(new LoginForm(this, dbService));

        dispose();
        setUndecorated(false);
        setTitle("Spezifikation");
        setSize(new Dimension(1800, 1200));
        setLocationRelativeTo(null);
        // add icon
        ImageIcon icon = new ImageIcon(Application.class.getResource("/logo/Spezi.png")); // icon path
        setIconImage(icon.getImage());
        setVisible(true);
    }



    public static void main(String[] args) {
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatLightLaf.setup();
        //run Program
        SpringApplication.run(Application.class, args);

    }

}
