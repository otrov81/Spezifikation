package app.toolbox;

import app.form.login.LoginForm;
import app.service.DBService;
import app.service.ManagerForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginActionListener implements ActionListener {
    @Autowired
    private DBService dbService;
    private JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
// Logika za login
        ManagerForm.getInstance().initSpeziForm();
        ManagerForm.getInstance().showForm(new LoginForm(frame,dbService));
    }
}
