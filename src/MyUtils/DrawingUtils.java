package MyUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class DrawingUtils {
    static public void drawArrow(Graphics2D g, double x0, double y0, double x1,
                                 double y1, double headLength, double headAngle) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = { (int)(x1 +  (headLength * Math.cos(angle + offs))), (int)x1,
                (int)(x1 + (headLength * Math.cos(angle - offs))) };
        int[] ys = { (int)(y1 + (headLength * Math.sin(angle + offs))), (int)y1,
                (int)(y1 + (headLength * Math.sin(angle - offs))) };

        g.draw(new Line2D.Double(x0, y0, x1, y1));

        g.drawPolyline(xs, ys, 3);
    }
    //Check a is between b and c
    static public boolean between(double a, double b, double c){
        if (b<=c) {
            if (b<=a && a<=c) return true;
        }
        else if (c<=a && a<=b) return true;

        return false;

    }
}
