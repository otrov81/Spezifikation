package app.form.user;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelAction extends JPanel {

    private ActionButton cmdEdit;
    private ActionButton cmdDelete;
    private ActionButton cmdPdf;

    // Constructor for PanelAction
    public PanelAction() {
        initComponents();  // Initialize components
        // Adding a listener for theme change
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    // Uncomment the following line to update icon color on theme change
                    // updateIconColor();
                }
            }
        });
    }

    // Initialize events for the action buttons
    public void initEvent(TableActionEvent event, int row) {
        // Add action listener for edit button
        cmdEdit.addActionListener(e -> event.onEdit(row));
        // Add action listener for delete button
        cmdDelete.addActionListener(e -> event.onDelete(row));
        // Add action listener for PDF button
        cmdPdf.addActionListener(e -> event.onPdf(row));
    }

    // Method to initialize components
    private void initComponents() {
        // Creating icons for the buttons
        FlatSVGIcon editIcon = new FlatSVGIcon("svg/edit.svg");
        FlatSVGIcon deleteIcon = new FlatSVGIcon("svg/delete.svg");
        FlatSVGIcon pdfIcon = new FlatSVGIcon("svg/pdf.svg");

        // Creating and setting icons for the edit button
        cmdEdit = new ActionButton();
        cmdEdit.setIcon(new FlatSVGIcon(editIcon));

        // Creating and setting icons for the delete button
        cmdDelete = new ActionButton();
        cmdDelete.setIcon(new FlatSVGIcon(deleteIcon));

        // Creating and setting icons for the PDF button
        cmdPdf = new ActionButton();
        cmdPdf.setIcon(new FlatSVGIcon(pdfIcon));

        // Add MouseListeners to change cursor to hand
        MouseAdapter handCursorAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };

        cmdEdit.addMouseListener(handCursorAdapter);
        cmdDelete.addMouseListener(handCursorAdapter);
        cmdPdf.addMouseListener(handCursorAdapter);

        // Setting the layout for the panel
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()  // Adds a small gap before the first button
                                .addComponent(cmdEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Add edit button
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)  // Add gap between buttons
                                .addComponent(cmdDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Add delete button
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)  // Add gap between buttons
                                .addComponent(cmdPdf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Add PDF button
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))  // Add gap after the last button
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cmdEdit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Align edit button
                        .addComponent(cmdDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Align delete button
                        .addComponent(cmdPdf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)  // Align PDF button
        );
    }
}
