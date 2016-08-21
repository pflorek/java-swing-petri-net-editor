package com.patrickflorek.petrineteditor.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.Graphics;

import com.patrickflorek.petrineteditor.ThePetrinetEditor;

import java.awt.event.InputEvent;

/**
 * @see FlowLayout
 * @see JPanel
 * @see JLabel
 * @see PlaceView
 * @see PetrinetView
 */
public class TransitionView extends JPanel {
    TransitionView self = this;
    JButton transition;
    MouseAdapter ma;
    JLabel label;
    Integer offset = 50;
    Boolean active = false;
    Scale globalSize = Scale.getScale();
    Boolean selected = false;

    private MouseAdapter selector = new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
            if (ThePetrinetEditor.DEBUG)
                System.out.println("Clicked transition on: " + me.getX() + " " + me.getY());
            if (me.getButton() == MouseEvent.BUTTON1 && (me.getModifiers() & InputEvent.CTRL_MASK) != 0) {

                System.out.println("Clicked Button1: ");

                selected = !selected;
            }
        }
    };

    public TransitionView(String name) {
        FlowLayout fl = new FlowLayout();
        fl.setHgap(0);
        fl.setVgap(0);
        this.setLayout(fl);
        this.setBackground(new Color(0, 0, 0, 0));

        transition = new JButton() {
            // Paint the round background and label.
            protected void paintComponent(Graphics g) {
                g.setColor(this.getBackground());
                g.fillRect(0, 0, this.getSize().width - 1,
                        this.getSize().height - 1);

                // This call will paint the label and the
                // focus rectangle.
                super.paintComponent(g);


                if (selected) {


                    g.setColor(Color.RED);

                    g.fillRect(2, 2, 7, 7);
                }


            }
        };
        transition.setPreferredSize(new Dimension((int) ((globalSize.getSize() / 100.0) * offset), (int) ((globalSize.getSize() / 100.0) * offset)));
        //transition.setBorder(null);
        //transition.setBorderPainted(false);
        transition.setContentAreaFilled(false);
        transition.setOpaque(false);
        transition.setBorder(new LineBorder(Color.BLACK, 1));
        transition.addMouseListener(selector);
        this.add(transition);
        label = new JLabel(name);
        this.add(label);
    }

    public void addMouseListener(final MouseListener l) {
        ma = new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                //MouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button)
                l.mouseClicked(new MouseEvent(self, me.getID(), me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger()));
            }
        };
        transition.addMouseListener(ma);
    }

    public void removeMouseListener(MouseListener l) {
        transition.removeMouseListener(ma);
        ma = null;
    }

    public void addPopupMenu(final JPopupMenu popupMenu) {
        transition.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    popupMenu.show(transition, me.getX(), me.getY());
                }
            }
        });
    }

    public void setText(final String text) {
        label.setText(text);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    public void activate() {
        transition.setBorder(new LineBorder(Color.GREEN, 1));
        active = true;

    }

    public void deactivate() {
        transition.setBorder(new LineBorder(Color.BLACK, 1));
        active = false;
    }

    public Boolean isSelected() {
        return selected;
    }
}
