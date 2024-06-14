package app.form.user;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelAction extends JPanel {

    private ActionButton cmdEdit;
    private ActionButton cmdDelete;

    public PanelAction() {
        initComponents();
        // Dodavanje osluškivača za promenu teme
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                   // updateIconColor();
                }
            }
        });
    }

    public void initEvent(TableActionEvent event, int row) {
        cmdEdit.addActionListener(e -> event.onEdit(row));
        cmdDelete.addActionListener(e -> event.onDelete(row));
    }

    private void initComponents() {
        // Kreiranje ikonica
        FlatSVGIcon editIcon = new FlatSVGIcon("svg/edit.svg");
        FlatSVGIcon deleteIcon = new FlatSVGIcon("svg/delete.svg");

        // Inicijalno postavljanje boje ikonica
       // updateIconColor();

        // Kreiranje komponenti za edit i delete dugmad
        cmdEdit = new ActionButton();
        cmdEdit.setIcon(new FlatSVGIcon(editIcon));

        cmdDelete = new ActionButton();
        cmdDelete.setIcon(new FlatSVGIcon(deleteIcon));

        // Postavljanje rasporeda komponenti
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmdEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cmdEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
    }

}