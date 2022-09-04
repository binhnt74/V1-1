package Graph;

import MyUtils.DrawingUtils;
import Graph.Constants.MOVING_DIRECTION;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineEdge extends Edge {
    double width;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public LineEdge(int id) {
        super(id);
        width = 3;
        backgroundColor = Color.CYAN;
        frontColor = Color.RED;
    }
    @Override
    public void draw() {
        Node source = getSource();
        Node dest = getDest();
        if (source != null && dest != null) {
            double x1 =  source.getX();
            double y1 = source.getY();
            double x2 =  dest.getX();
            double y2 =  dest.getY();

            graphics.setColor(backgroundColor);
            Stroke curStroke = graphics.getStroke();
            Stroke stroke = new BasicStroke((float)width);
            graphics.setStroke(stroke);
            Line2D line2D = new Line2D.Double(x1,  y1,  x2,  y2);
            graphics.draw(line2D);
            Font font = graphics.getFont();
            int fsize = font.getSize();
            String st = String.valueOf(getId());
            graphics.setColor(frontColor);
            graphics.setStroke(curStroke);
            graphics.drawString(st,((int)((x1+x2)/2) ) - fsize * st.length() / 4, ((int)((y1+y2)/2)) + fsize / 2);
            if (getEdgeType() == EdgeType.DIRECTED){
                Stroke directionStroke = new BasicStroke(2);
                graphics.setStroke(directionStroke);
                DrawingUtils.drawArrow(graphics,(2*x1+x2)/3,(2*y1+y2)/3,(x1+2*x2)/3,(y1+2*y2)/3,6,40);
                graphics.setStroke(curStroke);

            }

        }
    }
    public double getAngle(){
        if (source==null || dest ==null) return 0;
        double x1 = source.getX();
        double y1 = source.getY();
        double x2 = dest.getX();
        double y2 = dest.getY();

        if (x1 == x2 && y1 ==y2) return 0;
        double angle = 0;
        if (x1 == x2){
            if (y1<y2) angle = -Math.PI/2;
            else angle = Math.PI/2;
            return angle;
        }

        if (y1 == y2){
            if (x1<x2) angle = 0D;
            else angle = Math.PI;
            return angle;
        }
        angle = Math.atan(1.0D*(y2-y1)/(x2-x1));
        return angle;
    }
    //return next point from the current position, direction and speed
    public Point2D nextStep(double curX, double curY, MOVING_DIRECTION curDirection, double speed){
        Point2D p = new Point2D.Double(curX,curY);
        double x1 = getSource().getX();
        double y1 = getSource().getY();
        double x2 = getDest().getX();
        double y2 = getDest().getY();

        double xn = curX;
        double yn = curY;
        int c = 1;
        if (curDirection == Constants.MOVING_DIRECTION.RIGHT) c = 1;
        else c = -1;
        if (x1==x2){
            xn = x1;
            if (y1<=y2)
                yn += c*speed;
            else
                yn -= c*speed;
            p.setLocation(xn, yn);
            return p;
        }
        if (y1 == y2){
            yn = y1;
            if (x1<=x2)
                xn += c*speed;
            else
                xn -= c*speed;
            p.setLocation(xn, yn);
            return p;
        }
        double alfa;
        if (x1<x2){
            if (y1>y2){
                //System.out.println("Case 1");
                //System.out.println("c = "+c);
                alfa = Math.atan((y1-y2)/(x2-x1));
                //System.out.println("  old xn = "+xn);
                xn += c*speed*Math.cos(alfa);
                //System.out.println("  new xn = "+xn);
                yn += -c*speed*Math.sin(alfa);
            }
            else {//good
                //System.out.println("Case 2");
                alfa = Math.atan((y2-y1)/(x2-x1));
                xn += c*speed*Math.cos(alfa);
                yn += c*speed*Math.sin(alfa);
            }

        }else {//x1>x2
            if (y1>y2){
                //System.out.println("Case 3");
                alfa = Math.atan((y1-y2)/(x1-x2));
                xn += -c*speed*Math.cos(alfa);
                yn += -c*speed*Math.sin(alfa);
            }
            else {
                //System.out.println("Case 4");
                alfa = Math.atan((y2-y1)/(x1-x2));
                xn += -c*speed*Math.cos(alfa);
                yn += c*speed*Math.sin(alfa);
            }
        }
        //System.out.println("Alfa = "+ alfa + ", before xn = "+ xn + ", yn = "+yn);

        //System.out.println( "   After  xn = "+ xn + ", yn = "+yn);
        p.setLocation(xn, yn);
        return p;
    }
}
