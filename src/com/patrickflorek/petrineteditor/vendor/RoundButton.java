package com.patrickflorek.petrineteditor.vendor;
/**
 * JDC Tech Tips: August 26, 1999: Creating Round Swing Buttons
 * http://java.sun.com/developer/TechTips/1999/tt0826.html
 */

import com.patrickflorek.petrineteditor.ThePetrinetEditor;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundButton extends JComponent {
    // Hit detection.
    Shape shape;
    private String text;
    private Boolean isArmed = false;

    public RoundButton(String text) {
        this.text = text;

// These statements enlarge the button so that it
// becomes a circle rather than an oval.
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,
                size.height);
        setPreferredSize(size);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                //isArmed = true;
                //@TODO
                //repaint();
                //errorprone swing shit
            }

            public void mouseReleased(MouseEvent me) {
                //isArmed = false;
                //@TODO
                //repaint();
                //errorprone swing shit
            }
        });

// This call causes the JButton not to paint
        // the background.
// This allows us to paint a round background.
        //setContentAreaFilled(false);
        //setRolloverEnabled(false);
        //setFocusable(false);
        //setBorderPainted(false);
        setOpaque(false);
        //setFocusPainted(false);
        //Border emptyBorder = BorderFactory.createEmptyBorder();
        //setBorder(emptyBorder);
        //setForeground(new Color(0,0,0,0));
    }

    // Paint the round background and label.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, getSize().width - 1,
                getSize().height - 1);

        if (isArmed()) {
            g.setColor(Color.lightGray);
        } else {

            g.setColor(UIManager.getColor("Button.background"));
        }
        g.fillOval(0, 0, getSize().width - 1,
                getSize().height - 1);
        //set label color
        g.setColor(Color.BLACK);
        // center the label
        int stringLen = (int)
                g.getFontMetrics().getStringBounds(getText(), g).getWidth();
        g.drawString(getText(), (getSize().width - stringLen) / 2, getSize().height / 2);

// Paint the border of the button using a simple stroke.
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getSize().width - 1,
                getSize().height - 1);
        if (false && ThePetrinetEditor.DEBUG == true)
            System.out.println("RoundButton's width:  " + getSize().width + " and height: " + getSize().height);
    }

    public boolean contains(int x, int y) {
// If the button has changed size, 
        // make a new shape object.
        if (shape == null ||
                !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0,
                    getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isArmed() {
        return isArmed;
    }

}