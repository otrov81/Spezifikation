package app.form.spezi;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;

public class PanelActionSpezi extends JPanel {

    private ActionButtonSpezi cmdEdit;
    private ActionButtonSpezi cmdDelete;

    public PanelActionSpezi() {
        initComponents();
    }

    public void initEvent(TableActionEventSpezi event, int row) {
        cmdEdit.addActionListener(e -> event.onEdit(row));
        cmdDelete.addActionListener(e -> event.onDelete(row));
    }

    private void initComponents() {
        // Load and set icons for the buttons
        FlatSVGIcon editIcon = new FlatSVGIcon("svg/edit.svg");
        cmdEdit = new ActionButtonSpezi();
        cmdEdit.setIcon(editIcon);

        FlatSVGIcon deleteIcon = new FlatSVGIcon("svg/delete.svg");
        cmdDelete = new ActionButtonSpezi();
        cmdDelete.setIcon(deleteIcon);

        // Set up the layout of the panel
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
