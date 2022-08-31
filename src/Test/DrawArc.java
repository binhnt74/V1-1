package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class DrawArc extends JComponent {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Arc Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DrawArc());
        frame.pack();
        frame.setSize(new Dimension(420, 450));
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2.0f));
        g2.setPaint(Color.RED);
        g2.draw(new Arc2D.Double(55, 55, 300, 300, 0, 90, Arc2D.PIE));
        g2.setPaint(Color.GREEN);
        g2.draw(new Arc2D.Double(50, 50, 300, 300, 90, 90, Arc2D.OPEN));
        g2.setPaint(Color.BLUE);
        g2.draw(new Arc2D.Double(50, 55, 300, 300, 180, 90, Arc2D.CHORD));
        Arc2D arc = new Arc2D.Float(Arc2D.OPEN);
        arc.setArcByTangent(new Point2D.Float(30,30), new Point2D.Float(50,50), new Point2D.Float(30,70), 50);

        g2.setPaint(Color.BLACK);
        g2.draw(arc);

    }
}