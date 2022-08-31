package Graph;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RectangleNode extends Node{
    int width;
    int height;

    public RectangleNode(int id) {
        super(id);
        width = 20;
        height = 25;
        backgroundColor = Color.GREEN;
        frontColor = Color.BLACK;
    }

    public RectangleNode(int id, int x, int y) {
        super(id, x, y);
        width = 20;
        height = 25;
        backgroundColor = Color.GREEN;
        frontColor = Color.BLACK;
    }
    @Override
    public void draw(){
        //System.out.println("Drawing rectangle node...");
        if (graphics ==null){
            System.out.println("Graphic system is null");
            return;
        }

        Rectangle2D.Double r = new Rectangle2D.Double((x-width/2),(y-height/2),width,height);
        graphics.setColor(backgroundColor);
        graphics.fill(r);

        graphics.setColor(frontColor);
        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = String.valueOf(getId());
        graphics.drawString(st,((int)getX() ) - fsize * st.length() / 4, ((int)getY()) + fsize / 2);

    }
    public Point TopPoint(){
        Point p = new Point();
        p.setLocation(x,y-height/2);
        return p;

    }
    public Point BottomPoint(){
        Point p = new Point();
        p.setLocation(x,y+height/2);
        return p;
    }
    public Point LeftPoint(){
        Point p = new Point();
        p.setLocation(x-width/2,y);
        return p;
    }
    public Point RightPoint(){
        Point p = new Point();
        p.setLocation(x+width/2,y);
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void zoom(double scale){
        setHeight((int)(height*scale));
        setWidth((int)(width*scale));

    }
}
