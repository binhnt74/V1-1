package Graph;

import MyUtils.DrawingUtils;

import java.awt.*;
import java.awt.geom.Arc2D;

public class ArcEdge extends Edge{
    double angle; //in range from 0 to PI
    enum ARC_DIRECTION {CLOCK_WISE, ANTI_CLOCK_WISE}; //true: clock-wise
    ARC_DIRECTION direction;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
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
        frontColor = Color.WHITE;
        angle = Math.PI/4;
        max_speed = 20;
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
//            double alfa = 0;
//            if (x1 == x2) alfa = Math.PI / 2;
//            else Math.atan((y2 - y1) / (x2 - x1));
//            int x1a;
//            if (x1 < x2) x1a = (int)source.RightPoint().getX();
//            else x1a = (int)source.LeftPoint().getX();
//
//            int y1a;
//            if (y1 < y2) y1a = (int)source.RightPoint().getY();
//            else y1a = (int)source.LeftPoint().getY();
//
//            int x2a;
//            if (x1 < x2) x2a = (int)dest.LeftPoint().getX();
//            else x2a = (int)dest.RightPoint().getX();
//
//            int y2a;
//            if (y1 < y2) y2a = (int)dest.LeftPoint().getY();
//            else y2a = (int)dest.RightPoint().getY();
            graphics.setColor(backgroundColor);
            Stroke curStroke = graphics.getStroke();
            Stroke stroke = new BasicStroke((float)max_speed);
            graphics.setStroke(stroke);
            Arc2D arc2D = new Arc2D.Float(Arc2D.CHORD);
            arc2D.setAngles(x1,y1,x2,y2);
            graphics.draw(arc2D);
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
}
