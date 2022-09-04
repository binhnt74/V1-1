package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
        g2.draw(new Arc2D.Double(55, 55, 300, 100, 0, 90, Arc2D.PIE));
        g2.setPaint(Color.GREEN);
        g2.draw(new Arc2D.Double(50, 50, 300, 300, 90, 90, Arc2D.OPEN));
        g2.setPaint(Color.BLUE);
        g2.draw(new Arc2D.Double(50, 55, 300, 300, 180, 90, Arc2D.CHORD));
        Arc2D arc = new Arc2D.Float(Arc2D.OPEN);
        double x1 = 85, y1 = 55;
        g2.draw(new Rectangle2D.Double(x1-1, y1-1, 3, 3));
        double x2 = 115, y2 = 150;
        g2.draw(new Rectangle2D.Double(x2-1, y2-1, 3, 3));
        double xc = (x1+x2)/2, yc = (y1+y2)/2;

        g2.setPaint(Color.RED);
        g2.draw(new Rectangle2D.Double(xc-1, yc-1, 3, 3));

        double d = Point2D.distance(x1,y1,x2,y2);
        double delta = Math.PI/2;
        double h = d/(2* Math.tan(delta/2));
        //System.out.println("d = "+ d + ", h = " + h);

        double alfa = Math.PI - Math.atan(((y2-y1)/(x2-x1)));
        double beta = Math.atan(-1D/Math.tan(alfa));
        //System.out.println("beta = " +beta);
        double dy = h* Math.sin(beta);
        double dx = h* Math.cos(beta);
        double r = h/Math.cos(delta/2);

        double x0 = xc - dx, y0 = yc+ dy;
        //g2.setPaint(Color.RED);
        g2.draw(new Rectangle2D.Double(x0-1, y0-1, 3, 3));
        //g2.setPaint(Color.BLUE);
        g2.draw(new Arc2D.Double(x0-r, y0-r, 2*r, 2*r, 180*(beta-delta/2)/Math.PI, 180*delta/Math.PI, Arc2D.OPEN));



    }
}