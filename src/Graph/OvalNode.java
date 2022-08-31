package Graph;

import java.awt.*;

public class OvalNode extends Node{

    double width;
    double height;

    public OvalNode(int id) {
        super(id);
        commInit();
    }
    public OvalNode(int id, double x, double y) {
        super(id, x, y);
        commInit();
    }

    void commInit(){
        width = height = 25;
        backgroundColor = Color.BLUE;
        frontColor = Color.WHITE;
    }

    @Override
    public void draw(){
        //System.out.println("Drawing oval node...");
        if (graphics ==null){
            System.out.println("Graphic system is null");
            return;
        }
        graphics.setColor(backgroundColor);

        graphics.fillOval((int)(x-width/2),(int)(y-height/2),(int)width,(int)height);

        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = String.valueOf(getId());
        graphics.setColor(frontColor);
        graphics.drawString(st,((int)getX() ) - fsize * st.length() / 4, ((int)getY()) + fsize / 2);

    }
    public Point TopPoint(){
        Point p = new Point();
        p.setLocation(x,y-height/2);
        return p;

    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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
    public void zoom(double scale){
        setHeight((int)(height*scale));
        setWidth((int)(width*scale));

    }
}
