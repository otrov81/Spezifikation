package app.form.speziEdit;


import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;

public class PanelActionEdit extends JPanel {

    private ActionButtonEdit cmdEdit;
    private ActionButtonEdit cmdDelete;

    public PanelActionEdit() {
        initComponents();
    }

    public void initEvent(TableActionEventEdit event, int row) {
        cmdEdit.addActionListener(e -> event.onEdit(row));
        cmdDelete.addActionListener(e -> event.onDelete(row));
    }

    private void initComponents() {
        FlatSVGIcon editIcon = new FlatSVGIcon("svg/save.svg");
        cmdEdit = new ActionButtonEdit();
        cmdEdit.setIcon(new FlatSVGIcon(editIcon));

        FlatSVGIcon deleteIcon = new FlatSVGIcon("svg/delete.svg");
        cmdDelete = new ActionButtonEdit();
        cmdDelete.setIcon(new FlatSVGIcon(deleteIcon));

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
