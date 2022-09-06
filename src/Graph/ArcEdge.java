package Graph;

import MyUtils.DrawingUtils;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import Graph.Constants.MOVING_DIRECTION;

public class ArcEdge extends Edge{
    double arcAngle; //in range from 0 to PI
    enum ARC_DIRECTION {CLOCK_WISE, ANTI_CLOCK_WISE}; //true: clock-wise
    ARC_DIRECTION direction;

    public double getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(double arcAngle) {
        this.arcAngle = arcAngle;
    }

    public ARC_DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(ARC_DIRECTION direction) {
        this.direction = direction;
    }

    public ArcEdge(int id) {
        super(id);
        backgroundColor = Color.GREEN;
        frontColor = Color.BLACK;
        arcAngle = Math.PI/2;
        max_speed = 20;
    }
    //return radius of this arc
    double getRadius(){
        double radius = 0;
        double x1 =  source.getX();
        double y1 = source.getY();
        double x2 =  dest.getX();
        double y2 =  dest.getY();
        //double xc = (x1+x2)/2, yc = (y1+y2)/2;

        double d = Point2D.distance(x1,y1,x2,y2);
        double delta = Math.PI/2;
        double h = d/(2* Math.tan(delta/2));

        double alfa = Math.PI - Math.atan(((y2-y1)/(x2-x1)));
        double beta = Math.atan(-1D/Math.tan(alfa));

        //double dy = h* Math.sin(beta);
        //double dx = h* Math.cos(beta);
        radius = h/Math.cos(delta/2);
        return radius;
    }

    //return center point of this arc
    Point2D getCenterPoint(){
        Point2D p = new Point2D.Double(0D,0D);
        double x1 =  source.getX();
        double y1 = source.getY();
        double x2 =  dest.getX();
        double y2 =  dest.getY();
        double xc = (x1+x2)/2, yc = (y1+y2)/2;

        double d = Point2D.distance(x1,y1,x2,y2);
        double delta = Math.PI/2;
        double h = d/(2* Math.tan(delta/2));

        double alfa = Math.PI - Math.atan(((y2-y1)/(x2-x1)));
        double beta = Math.atan(-1D/Math.tan(alfa));

        double dy = h* Math.sin(beta);
        double dx = h* Math.cos(beta);
        //double r = h/Math.cos(delta/2);

        double x0 = xc - dx, y0 = yc+ dy;
        p.setLocation(x0,y0);
        return p;
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
            double xc = (x1+x2)/2, yc = (y1+y2)/2;
            graphics.setColor(backgroundColor);
            Stroke curStroke = graphics.getStroke();
            Stroke stroke = new BasicStroke((float)max_speed/3);
            graphics.setStroke(stroke);
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
            //graphics.draw(new Rectangle2D.Double(x0-1, y0-1, 3, 3));
            //g2.setPaint(Color.BLUE);
            graphics.draw(new Arc2D.Double(x0-r, y0-r, 2*r, 2*r, 180*(beta-delta/2)/Math.PI, 180*delta/Math.PI, Arc2D.OPEN));

            Font font = graphics.getFont();
            int fsize = font.getSize();
            String st = String.valueOf(getId());
            graphics.setColor(frontColor);
            graphics.setStroke(curStroke);
            graphics.drawString(st,((int)((x1+x2)/2 +(r-h)*Math.cos(delta/2)) ) - fsize * st.length() / 4, ((int)((y1+y2)/2 - (r-h)*Math.sin(delta/2))) + fsize / 2);
            if (getEdgeType() == EdgeType.DIRECTED){
                Stroke directionStroke = new BasicStroke(2);
                graphics.setStroke(directionStroke);
                DrawingUtils.drawArrow(graphics,(2*x1+x2)/3,(2*y1+y2)/3,(x1+2*x2)/3,(y1+2*y2)/3,6,40);
                graphics.setStroke(curStroke);

            }

        }
    }

    //return next point from the current position, direction and speed
    public Point2D nextStep(double curX, double curY, MOVING_DIRECTION curDirection, double speed){
        Point2D p = new Point2D.Double(curX,curY);
        double x1 = getSource().getX();
        double y1 = getSource().getY();
        double x2 = getDest().getX();
        double y2 = getDest().getY();

        double xc = (x1+x2)/2, yc = (y1+y2)/2;

        double d = Point2D.distance(x1,y1,x2,y2);
        double delta = Math.PI/2;
        double h = d/(2* Math.tan(delta/2));

        double alfa = Math.PI/2;
        if (x2 != x1) alfa = Math.PI - Math.atan(((y2-y1)/(x2-x1)));

        double beta = Math.atan(-1D/Math.tan(alfa));

        double dy = h* Math.sin(beta);
        double dx = h* Math.cos(beta);
        //double r = h/Math.cos(delta/2);

        double x0 = xc - dx, y0 = yc+ dy;   //center point (x0,y0)
        double radius = h/Math.cos(delta/2);

        //Point2D center = getCenterPoint();

        //double xn = curX;
        //double yn = curY;
        int c1 = -1;
        if (curDirection != Constants.MOVING_DIRECTION.RIGHT) c1 = 1;

        //Point2D center = getCenterPoint();
        //double radius = getRadius();
        //double t = speed/2.0/radius;
        alfa = c1*2*Math.asin(speed/2.0/radius);
        double alfa1 = 0D;
        if (x0!=curX)
            alfa1 = Math.atan((y0 -curY )/(x0 -curX));

        double alfa2 = 0D;
        alfa2 = alfa1 - alfa;
        dx = radius/Math.sqrt(Math.tan(alfa2)*Math.tan(alfa2)+1);
        dy = Math.tan(alfa2)*dx;
//        int c2 = 1;
//        if (alfa2<0) c2 = -1;
        p.setLocation(x0+dx, y0+dy);
        return p;
    }
}
