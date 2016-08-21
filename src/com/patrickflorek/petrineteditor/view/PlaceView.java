package com.patrickflorek.petrineteditor.view;

import javax.swing.JPanel;

import com.patrickflorek.petrineteditor.vendor.RoundButton;
import com.patrickflorek.petrineteditor.ThePetrinetEditor;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import javax.swing.SwingWorker;

/**
 * JPanel representing a place with label.
 */
public class PlaceView extends JPanel {
    MouseAdapter ma;
    RoundButton place;
    JLabel label;
    Integer token = 0;
    PlaceView self = this;
    Integer offset = 50;
    Scale globalSize = Scale.getScale();

    public PlaceView(String name) {
        FlowLayout fl = new FlowLayout();
        fl.setHgap(0);
        fl.setVgap(0);
        this.setLayout(fl);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(true);
        place = new RoundButton("");
        place.setPreferredSize(new Dimension((int) ((globalSize.getSize() / 100.0) * offset), (int) ((globalSize.getSize() / 100.0) * offset)));
        //place.setOpaque(true);
        this.add(place);
        label = new JLabel(name);
        this.add(label);
    }

    public Integer getToken() {
        return token;
    }

    /**
     * @param token
     */
    public void setToken(Integer token) {
        this.token = token;
        if (token == 0) {
            place.setText("");
        } else if (token == 1) {
            place.setText("\u2022");
        } else {
            place.setText(Integer.toString(token));
        }
        place.repaint();
    }

    /**
     * testing
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    /**
     * @param l MouseListener
     */
    public void addMouseListener(final MouseListener l) {
        ma = new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                //MouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button)
                l.mouseClicked(new MouseEvent(self, me.getID(), me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger()));
            }
        };
        place.addMouseListener(ma);
    }

    public void removeMouseListener(MouseListener l) {
        place.removeMouseListener(ma);
        ma = null;
    }

    /**
     * @param popupMenu Menu fuer delete, move, rename, settoken
     */
    public void addPopupMenu(final JPopupMenu popupMenu) {
        place.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    popupMenu.show(place, me.getX(), me.getY());
                }
            }
        });
    }

    public void setText(final String text) {
        label.setText(text);
    }
}
