package Graph;

import java.awt.*;
import java.awt.geom.Arc2D;

public class CircleNode extends Node {
    double radius;

    public CircleNode(int id) {
        super(id);
        radius = 15;
    }

    public CircleNode(int id, int x, int y) {
        super(id, x, y);
        radius = 15;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Circle node...");
        if (graphics == null) {
            System.out.println("Graphic system is null");
            return;
        }
        graphics.setColor(Color.BLACK);
        Arc2D arc2D = new Arc2D.Double(Arc2D.OPEN);
        arc2D.setArcByCenter(x, y, radius, 0D, 2 * Math.PI, Arc2D.OPEN);
        graphics.draw(arc2D);

        //graphics.drawOval((x-radius),(y-radius),2*radius,2*radius);
    }

    public Point TopPoint() {
        Point p = new Point();
        p.setLocation(x, y - radius);
        return p;

    }

    public Point BottomPoint() {
        Point p = new Point();
        p.setLocation(x, y + radius);
        return p;
    }

    public Point LeftPoint() {
        Point p = new Point();
        p.setLocation(x - radius, y);
        return p;
    }

    public Point RightPoint() {
        Point p = new Point();
        p.setLocation(x + radius, y);
        return p;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void zoom(double scale) {
        setRadius((int) (radius * scale));

    }
}
