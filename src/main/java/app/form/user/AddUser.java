package app.form.user;

import app.model.User;
import app.notifications.Notifications;
import app.service.DBService;
import app.toolbox.MD5;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static app.form.user.UserForm.table;

public class AddUser {
    @Autowired
    private DBService dbService;
    static JTextField rowNumber = new JTextField();
    static JTextField useridText = new JTextField();
    static JTextField usernameText = new JTextField();
    static JTextField passwordText = new JTextField();
    static JTextField berechtigungText = new JTextField();
    private JFrame frame;
    private UserInfo userInfo;
    MD5 md5 = new MD5();

public AddUser(DBService dbService, JFrame frame ){
    this.userInfo = new UserInfo(dbService);
    this.dbService = dbService;
    this.frame = frame;
}


    public JPanel getjPanelRight(DefaultTableModel model) {

        String output = userInfo.getUserName();

        JLabel userForm = new JLabel("User Form");
        userForm.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN,25));

        JLabel userNameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel berechtigungLabel = new JLabel("Berechtigung");

        useridText = new JTextField(5);
        rowNumber = new JTextField(5);

        usernameText = new JTextField(25); // Dodajemo veličinu za unos teksta
        passwordText = new JTextField(25); // Korištenje JPasswordField za unos šifre
        berechtigungText = new JTextField(25);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton btnReset = new JButton("Reset");
        JPanel desniPanel = new JPanel(new MigLayout("insets 50", "", ""));
        //desniPanel.setBackground(Color.orange);

        desniPanel.add(userForm, "skip, align center, wrap, h 50!");
        desniPanel.add(userNameLabel, "align center");
        desniPanel.add(usernameText, "align center, wrap");

        desniPanel.add(passwordLabel, "align center");
        desniPanel.add(passwordText, "align center, wrap");

        desniPanel.add(berechtigungLabel, "align center");
        desniPanel.add(berechtigungText, "align center, wrap");

        desniPanel.add(addBtn,"skip, split 3");
        desniPanel.add(updateBtn);
        desniPanel.add(btnReset,"wrap");


        //btn click add
        addBtn.addActionListener(e ->{

            if(!usernameText.getText().isEmpty() && !passwordText.getText().isEmpty() && !berechtigungText.getText().isEmpty()){

                try {
                    dbService.insertDataUser(usernameText.getText() , md5.getMd5(passwordText.getText()), berechtigungText.getText());
                    // add jtable to model
                    model.setRowCount(0);
                    neuDataMysql(model);


                    //remove after update
                    usernameText.setText("");
                    passwordText.setText("");
                    berechtigungText.setText("");
                    useridText.setText("");

                    for (int i = 1; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer(rowNumber.getText()));
                    }

                }catch (Exception e1){
                    dbService.LogToDatabase("ERROR", "Fehler add user -> add (AddUser)"+ e1,output);
                }
            }else {
                JOptionPane.showMessageDialog(frame, "Bitte füllen Sie alle Felder aus!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            }



        });

        //btn click reset
        btnReset.addActionListener(e->{
            usernameText.setText("");
            passwordText.setText("");
            berechtigungText.setText("");
        });

        //btn click -> Edit
        updateBtn.addActionListener(e->{

            if(!usernameText.getText().isEmpty() && !passwordText.getText().isEmpty() && !berechtigungText.getText().isEmpty()){

                try {
                    dbService.updateDataUser(usernameText.getText() , md5.getMd5(passwordText.getText()), berechtigungText.getText(), Integer.valueOf(useridText.getText()));

                    //remove after update
                    usernameText.setText("");
                    passwordText.setText("");
                    berechtigungText.setText("");
                    useridText.setText("");


                    model.setRowCount(0);
                    neuDataMysql(model);

                    for (int i = 1; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer(rowNumber.getText()));
                    }
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "User upadte");

                }catch (Exception e1){
                    dbService.LogToDatabase("ERROR", "Fehler update user -> update (AddUser)"+ e1, output);
                }
            }else {
                //Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.CENTER, "Bitte füllen Sie alle Felder aus!");
               JOptionPane.showMessageDialog(frame, "Bitte füllen Sie alle Felder aus!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            }



    });

        return desniPanel;
    }


    private void neuDataMysql(DefaultTableModel model) {
        List<User> updatedUsers = dbService.findAll();
        for (User user : updatedUsers) {
            model.addRow(new Object[]{"", user.getUserid(), user.getUsername(), user.getPassword(), user.getBerechtigung()});
        }

    }

}
