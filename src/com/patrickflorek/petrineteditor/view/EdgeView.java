package com.patrickflorek.petrineteditor.view;

import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Point;

import com.patrickflorek.petrineteditor.ThePetrinetEditor;
import com.patrickflorek.petrineteditor.view.Scale;

import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.Polygon;


import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JPopupMenu;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;

import java.awt.image.BufferedImage;

public class EdgeView extends JPanel {
    JComponent source;
    JComponent target;
    Point position;
    Dimension size;
    Integer offset = 50;
    Double scaledOffset;
    Scale scale = Scale.getScale();
    BufferedImage image;

    /**
     * @see Graphics2D
     * @see #paintComponent(Graphics g)
     */
    public EdgeView(JComponent source, JComponent target) {
        super();
        //this.setOpaque(false);
        this.source = source;
        this.target = target;
        this.size = new Dimension();
        this.calculateOffset();
        calculateSize();
        this.setPreferredSize(size);
        this.position = new Point();
        calculatePosition();

        this.setBackground(new Color(0, 0, 0, 0));
    }

    private void calculateOffset() {
        this.scaledOffset = (scale.getSize() / 100.0) * offset;
    }

    private void calculateSize() {
        this.size.setSize(
                Math.abs(Math.max(source.getX(), target.getX()) - Math.min(source.getX(), target.getX())) + scaledOffset,
                Math.abs(Math.max(source.getY(), target.getY()) - Math.min(source.getY(), target.getY())) + scaledOffset
        );

        if (false && ThePetrinetEditor.DEBUG)
            System.out.println("calculateSize Edge  " + (Math.max(source.getX(), target.getX()) - Math.min(source.getX(), target.getX()) - scaledOffset / 2) + ","
                    + (Math.max(source.getY(), target.getY()) - Math.min(source.getY(), target.getY()) - scaledOffset / 2));
    }

    private void calculatePosition() {
        this.position.setLocation(
                Math.min(source.getX(), target.getX()),
                Math.min(source.getY(), target.getY())
        );

        if (false && ThePetrinetEditor.DEBUG)
            System.out.println("calculatePosition Edge  " + (Math.min(source.getX(), target.getX()) + scaledOffset) + "," + (Math.min(source.getY(), target.getY()) + scaledOffset));
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public Point getPreferredPosition() {
        return position;
    }

    public void paintComponent(Graphics g) {
        calculateSize();
        calculatePosition();

        this.setBounds((int) position.getX(), (int) position.getY(),
                size.width, size.height);

        super.paintComponent(g);

        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = image.createGraphics();

        Integer x1 = source.getX();
        Integer y1 = source.getY();
        Integer x2 = target.getX();
        Integer y2 = target.getY();

        Boolean isLeft = x1 > x2;
        Boolean isTop = y1 > y2;
        Polygon arrow = new Polygon();
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2.setColor(Color.BLACK);

        if (isLeft && isTop) {

            Double gradient = (Math.atan2(size.height - scaledOffset, size.width - scaledOffset));
            Double sinus = Math.sin(gradient);
            Double cosinus = Math.cos(gradient);
            Double rotation = gradient - Math.toRadians(45.0);
            Double x_translation = scaledOffset / 2 + cosinus * scaledOffset / 2;
            Double y_translation = scaledOffset / 2 + sinus * scaledOffset / 2;
            //Arrow is located in upper LEft
            g2.translate(x_translation, y_translation);
            g2.rotate(rotation);
            arrow.addPoint(0, 0);
            arrow.addPoint((int) (scaledOffset / 2), 0);
            arrow.addPoint(0, (int) (scaledOffset / 2));
            g2.fill(arrow);
            g2.rotate(-rotation);
            g2.translate(-x_translation, -y_translation);
            g2.draw(new Line2D.Double(scaledOffset / 2, scaledOffset / 2, size.width - scaledOffset / 2, size.height - scaledOffset / 2));
        }


        if (isLeft && !isTop) {
            //Arrow is located in bottom left corner
            Double gradient = (Math.atan2(size.height - scaledOffset, size.width - scaledOffset));
            Double sinus = Math.sin(gradient);
            Double cosinus = Math.cos(gradient);
            Double rotation = Math.toRadians(45.0) - gradient;
            Double x_translation = scaledOffset / 2 + cosinus * scaledOffset / 2;
            Double y_translation = size.height - scaledOffset / 2 - sinus * scaledOffset / 2;

            if (false && ThePetrinetEditor.DEBUG) {
                System.out.println("Steigung: " + gradient);
                System.out.println("Steigung in GRad: " + Math.toDegrees(gradient));
                System.out.println("Drehung: " + (Math.toRadians(45.0) - gradient));
                System.out.println("Drehung in Grad: " + Math.toDegrees(Math.toRadians(45.0) - gradient));
                System.out.println("Sinus: " + sinus);
                System.out.println("Cosinus: " + cosinus);
                System.out.println("x: " + x_translation);
                System.out.println("y: " + y_translation);
            }

            g2.translate(x_translation, y_translation);
            g2.rotate(rotation);
            arrow.addPoint(0, 0);
            arrow.addPoint((int) (scaledOffset / 2), 0);
            arrow.addPoint(0, (int) (-scaledOffset / 2));
            g2.fill(arrow);
            g2.rotate(-rotation);
            g2.translate(-x_translation, -y_translation);
            g2.draw(new Line2D.Double(scaledOffset / 2, size.height - scaledOffset / 2, size.width - scaledOffset / 2, scaledOffset / 2));
        }


        if (!isLeft && !isTop) {
            //Arrow is located in bottom left corner
            Double gradient = (Math.atan2(size.height - scaledOffset, size.width - scaledOffset));
            Double sinus = Math.sin(gradient);
            Double cosinus = Math.cos(gradient);
            Double rotation = gradient - Math.toRadians(45.0);
            Double x_translation = size.width - scaledOffset / 2 - cosinus * scaledOffset / 2;
            Double y_translation = size.height - scaledOffset / 2 - sinus * scaledOffset / 2;

            if (false && ThePetrinetEditor.DEBUG) {
                System.out.println("Steigung: " + gradient);
                System.out.println("Steigung in GRad: " + Math.toDegrees(gradient));
                System.out.println("Drehung: " + (Math.toRadians(45.0) - gradient));
                System.out.println("Drehung in Grad: " + Math.toDegrees(Math.toRadians(45.0) - gradient));
                System.out.println("Sinus: " + sinus);
                System.out.println("Cosinus: " + cosinus);
                System.out.println("x: " + x_translation);
                System.out.println("y: " + y_translation);
            }

            g2.translate(x_translation, y_translation);
            g2.rotate(rotation);
            arrow.addPoint(0, 0);
            arrow.addPoint((int) -scaledOffset / 2, 0);
            arrow.addPoint(0, (int) -scaledOffset / 2);
            g2.fill(arrow);
            g2.rotate(-rotation);
            g2.translate(-x_translation, -y_translation);
            g2.draw(new Line2D.Double(scaledOffset / 2, scaledOffset / 2, size.width - scaledOffset / 2, size.height - scaledOffset / 2));


        }


        if (!isLeft && isTop) {
            //Arrow is located in bottom LEft corner
            Double gradient = (Math.atan2(size.height - scaledOffset, size.width - scaledOffset));
            Double sinus = Math.sin(gradient);
            Double cosinus = Math.cos(gradient);
            Double rotation = Math.toRadians(45.0) - gradient;
            Double x_translation = size.width - scaledOffset / 2 - cosinus * scaledOffset / 2;
            Double y_translation = scaledOffset / 2 + sinus * scaledOffset / 2;

            if (false && ThePetrinetEditor.DEBUG) {
                System.out.println("Steigung: " + gradient);
                System.out.println("Steigung in GRad: " + Math.toDegrees(gradient));
                System.out.println("Drehung: " + (Math.toRadians(45.0) - gradient));
                System.out.println("Drehung in Grad: " + Math.toDegrees(Math.toRadians(45.0) - gradient));
                System.out.println("Sinus: " + sinus);
                System.out.println("Cosinus: " + cosinus);
                System.out.println("x: " + x_translation);
                System.out.println("y: " + y_translation);
            }

            g2.translate(x_translation, y_translation);
            g2.rotate(rotation);
            arrow.addPoint(0, 0);
            arrow.addPoint((int) (-scaledOffset / 2), 0);
            arrow.addPoint(0, (int) (scaledOffset / 2));
            g2.fill(arrow);
            g2.rotate(-rotation);
            g2.translate(-x_translation, -y_translation);
            g2.draw(new Line2D.Double(scaledOffset / 2, size.height - scaledOffset / 2, size.width - scaledOffset / 2, scaledOffset / 2));


        }


        g.drawImage(image, 0, 0, size.width, size.height, this);
    }

    /**
     * @param popupMenu Menu fuer delete, move, rename, settoken
     */
    public void addPopupMenu(final JPopupMenu popupMenu) {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        });
    }

    @Override
    public boolean contains(int x, int y) {
        boolean contains = false;
        try {
            if (x < image.getWidth() && y < image.getHeight()) {
                contains = (image.getRGB(x, y) == (Color.BLACK).getRGB());
            }
            if (false && ThePetrinetEditor.DEBUG)
                System.out.println("contains: " + x + " " + y);
        } catch (Exception e) {

        }
        return contains;

    }
}
