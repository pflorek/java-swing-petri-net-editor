import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class JButtonFlickeringTest {
    public static void main(String[] args) {

        JFrame f = new JFrame("Test Frame");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout());
        JButton b = new JButton("OK");

        JComponent c = new RoundButton("blub");
        c.setPreferredSize(new Dimension(50, 50));

        p.add(b, BorderLayout.PAGE_END);
        p.add(c, BorderLayout.PAGE_END);

        f.add(p, BorderLayout.PAGE_END);

        f.setSize(300, 200);
        f.setVisible(true);

    }
}

class RoundButton extends JComponent {
    // Hit detection.
    Shape shape;
    private String text;
    private Boolean isArmed = false;

    public RoundButton(String text) {
        this.text = text;

        // These statements enlarge the button so that it becomes a circle rather than an oval.
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,
                size.height);
        setPreferredSize(size);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                isArmed = true;
                //@TODO
                repaint();
                //errorprone swing shit
            }

            public void mouseReleased(MouseEvent me) {
                isArmed = false;
                //@TODO
                repaint();
                //errorprone swing shit
            }
        });
        setOpaque(false);
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

        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getSize().width - 1,
                getSize().height - 1);

        System.out.println("RoundButton's width:  " + getSize().width + " and height: " + getSize().height);
    }

    public boolean contains(int x, int y) {
        // If the button has changed size, make a new shape object.
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
