package app.toolbox;

import app.Application;
import app.form.dashboard.Dashboard;
import app.form.login.LoginForm;
import app.form.spezi.SpeziForm;
import app.form.speziAnlegen.SpeziAnlegen;
import app.form.textEdit.TextEdit;
import app.form.textartenAnlegen.TextartenAnlegen;
import app.form.textbocke.TextBlocke;
import app.service.DBService;
import app.service.ManagerForm;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static app.toolbox.ScaledIcon.getScaledImage;

@Component
public class MenuInfo {
    private static String userNameStyle;
    private JFrame frame;

    public void createMenu(JFrame frame, DBService dbService) {
        this.frame = frame;
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        JMenuItem closeItem = new JMenuItem("Close");
        fileMenu.add(loginItem);
        fileMenu.add(dashboardItem);
        fileMenu.add(closeItem);

        JMenu helpMenu = new JMenu("Info");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);


        JMenu speziMenu = new JMenu("Spezi");
        JMenuItem suchenItem = new JMenuItem("Suche");
        JMenuItem bearbeitenItem = new JMenuItem("Anlegen/bearbeiten");
        JMenuItem textartenItem = new JMenuItem("Textarten");
        JMenuItem texteItem = new JMenuItem("Texte");
        speziMenu.add(suchenItem);
        speziMenu.add(bearbeitenItem);
        speziMenu.add(textartenItem);
        speziMenu.add(texteItem);

        JMenu themeMenu = new JMenu("Themes");
        JRadioButtonMenuItem lightItem = new JRadioButtonMenuItem("Light");
        JRadioButtonMenuItem lightCyanItem = new JRadioButtonMenuItem("Cyan");
        JRadioButtonMenuItem darkCyanItem = new JRadioButtonMenuItem("Dark");


        String currentTheme = UIManager.getLookAndFeel().getName();
        if(currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light") ) {
            lightItem.setSelected(true);
        } else if (currentTheme.equals("FlatLaf macOS Light") || currentTheme.equals("FlatLaf Light") ) {
            darkCyanItem.setSelected(true);
        }else{
            lightCyanItem.setSelected(true);
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(lightCyanItem);
        buttonGroup.add(lightItem);
        buttonGroup.add(darkCyanItem);

        //themeMenu.add(darkItem);
        themeMenu.add(lightItem);
        themeMenu.add(lightCyanItem);
        themeMenu.add(darkCyanItem);


        menuBar.add(fileMenu);
        menuBar.add(speziMenu);
        menuBar.add(themeMenu);
        menuBar.add(helpMenu);


        frame.setJMenuBar(menuBar);

        lightCyanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LoginForm.getTxtUsername().isEmpty()){
                    userNameStyle =System.getProperty("user.name");
                }else{
                    userNameStyle = LoginForm.getTxtUsername();
                }
                ManagerForm.getInstance().setThemesIntelli("/com/formdev/flatlaf/intellijthemes/themes/Cyan.theme.json");
                dbService.updateDataStyle("Cyan",userNameStyle);


            }
        });

        lightItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LoginForm.getTxtUsername().isEmpty()){
                    userNameStyle =System.getProperty("user.name");
                }else{
                    userNameStyle = LoginForm.getTxtUsername();
                }


                ManagerForm.getInstance().setThemes("com.formdev.flatlaf.themes.FlatMacLightLaf");

                dbService.updateDataStyle("FlatMacLightLaf",userNameStyle);
            }
        });
        //add darck
        darkCyanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LoginForm.getTxtUsername().isEmpty()){
                    userNameStyle =System.getProperty("user.name");
                }else{
                    userNameStyle = LoginForm.getTxtUsername();
                }


                ManagerForm.getInstance().setThemes("com.formdev.flatlaf.themes.FlatMacDarkLaf");

                dbService.updateDataStyle("FlatMacDarkLaf",userNameStyle);
            }
        });

        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new LoginForm(frame,dbService));

            }
        });

        dashboardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new Dashboard(frame,dbService));
            }
        });

        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(showCustomDialog, "About clicked");
                showCustomDialog(frame);

            }
        });
        //add spezi menu
        suchenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new SpeziForm(frame,dbService));
            }
        });
        bearbeitenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new SpeziAnlegen(frame,dbService));
            }
        });
        textartenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new TextartenAnlegen(frame,dbService));
            }
        });
        texteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setJMenuBar(null);
                ManagerForm.getInstance().initSpeziForm();
                ManagerForm.getInstance().showForm(new TextEdit(frame,dbService));
            }
        });
    }

    private static void showCustomDialog(JFrame frame) {
        // Kreiranje prilagođenog dijaloga
        JDialog dialog = new JDialog(frame, "About", true);
        dialog.setLayout(new MigLayout("align center, wrap 1")); // Postavljanje MigLayout-a

        // add icon
        ImageIcon icon = new ImageIcon(Application.class.getResource("/logo/Spezi.png")); // icon path
        ImageIcon iconSize = new ImageIcon( getScaledImage(icon.getImage(), 50));
        JLabel iconLabel = new JLabel(iconSize);
        //iconLabel.setPreferredSize(new Dimension(30, 30)); // Postavljanje dimenzija ikone
        iconLabel.setSize(new Dimension(30, 30));
        dialog.add(iconLabel, "align center, gaptop 20");

        // Kreiranje teksta o programu
        JTextArea textArea = new JTextArea("<html><b>Program name:</b> <i>SPEZI</i>\n\nDeveloper name: DIPL.ING. Pantelija Sogonic" +
                "\n\nPhone number: +43 676 585 18 16\n\nemail address: p.sogonic@gmail.com</html>");


        // Kreiranje teksta o programu
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText("<html><b>Program name:</b> <i>SPEZI</i><br><br>" +
                "<b>Developer name:</b> <i>DIPL.ING. Pantelija Sogonic</i><br><br>" +
                "<b>Phone number:</b> <i>+43 7221 733 99 204</i><br><br>" +
                "<b>Email address: </b><i><a href='mailto:p.sogonic@almi.at?subject=Spezi'>p.sogonic@almi.at</a></i></html>");

        editorPane.setEditable(false); // Tekst ne može biti izmenjen

        // Add HyperlinkListener to handle email link clicks
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                        try {
                            Desktop.getDesktop().mail(new URI(e.getDescription()));
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        editorPane.setEditable(false); // Tekst ne može biti izmenjen
        dialog.add(editorPane, "align center");

        // Kreiranje oznake za verziju programa
        JLabel versionLabel = new JLabel("Version: 1.7.2");
        dialog.add(versionLabel, "align right, gaptop 20"); // Pomeranje u desni gornji ugao s razmakom

        // Kreiranje dugmeta "OK"
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Zatvaranje dijaloga kada se klikne na dugme "OK"
            }
        });
        dialog.add(okButton, "align center,dock south,gaptop 20");

        // Postavljanje preferirane veličine dijaloga i pakovanje komponenti
        dialog.setPreferredSize(new Dimension(500, 400));
        dialog.pack();

        // Prikazivanje dijaloga
        dialog.setLocationRelativeTo(frame); // Prikazuje dijalog u sredini okvira
        dialog.setVisible(true);
    }


}
