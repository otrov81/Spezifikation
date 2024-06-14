package app.form.spezi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class ActionButtonSpezi extends JButton {
    private boolean mousePress;

    public ActionButtonSpezi() {
        // Set button properties
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(3, 3, 3, 3));

        // Add mouse listeners for different mouse events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePress = true;
                repaint(); // Repaint the button when pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePress = false;
                repaint(); // Repaint the button when released
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand cursor
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get button dimensions
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height); // Ensure the shape is a circle
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        // If the button is pressed, draw a filled circle
        if (mousePress) {
            g2.setColor(new Color(158, 158, 158));
            g2.fill(new Ellipse2D.Double(x, y, size, size));
        }

        g2.dispose();

        // Call the superclass's paintComponent method
        super.paintComponent(g);
    }
}
