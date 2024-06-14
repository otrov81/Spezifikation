package app.toolbox;

import app.form.dashboard.Dashboard;
import app.form.spezi.SpeziForm;
import app.form.speziEdit.SpeziEdit;
import app.service.DBService;
import app.service.ManagerForm;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BackIcon {
    @Autowired
    private DBService dbService;
    private JFrame frame;

    public static FlatSVGIcon editIcon = new FlatSVGIcon("svg/back.svg");

    public static JLabel getBackEditSpezi(JFrame frame, DBService dbService) {
        JLabel labMenu = new JLabel();

        labMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ManagerForm.getInstance().showForm(new SpeziEdit(frame,dbService));
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        labMenu.setIcon(new FlatSVGIcon(editIcon));
        return labMenu;
    }

    public static JLabel getBackIcon(JFrame frame, DBService dbService) {
        JLabel labMenu = new JLabel();

        labMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ManagerForm.getInstance().showForm(new SpeziForm(frame,dbService));
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        labMenu.setIcon(new FlatSVGIcon(editIcon));
        return labMenu;
    }

    public static JLabel getBackIconDaschboad(JFrame frame, DBService dbService) {
        JLabel labMenu = new JLabel();

        labMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ManagerForm.getInstance().showForm(new Dashboard(frame,dbService));
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labMenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        labMenu.setIcon(new FlatSVGIcon(editIcon));
        return labMenu;
    }

}
