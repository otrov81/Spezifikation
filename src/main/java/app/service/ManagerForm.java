package app.service;

import app.Application;
import app.toolbox.LoginActionListener;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import javax.swing.*;
import java.awt.*;

public class ManagerForm {
    private Application application;
    private static ManagerForm instance;

    public static ManagerForm getInstance() {
        if (instance == null) {
            instance = new ManagerForm(new LoginActionListener());
        }
        return instance;
    }

    public ManagerForm(LoginActionListener loginActionListener) {

    }

    public void initApplication(Application application) {
        this.application = application;
    }

    public void initSpeziForm() {
        this.application = application;
    }

    public void showForm(JComponent form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();

            application.setContentPane(form);
            application.revalidate();
            application.repaint();

            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public void setThemes(String themenName) {
        try {
            UIManager.setLookAndFeel(themenName);
            SwingUtilities.updateComponentTreeUI(application);

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setThemesIntelli(String themenName) {

        try {
            IntelliJTheme.setup(ManagerForm.class.getResourceAsStream(themenName));
            SwingUtilities.updateComponentTreeUI(application);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
