package Graph;

import java.awt.*;

public class RSUNode extends OvalNode{

    public RSUNode(int id) {
        super(id);
        commInit();
    }

    public RSUNode(int id, int x, int y) {
        super(id, x, y);
        commInit();
    }

    void commInit(){
        width = height = 25;
        backgroundColor = Color.RED;
        frontColor = Color.BLUE;
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
        String st = "RSU " + String.valueOf(getId());
        graphics.setColor(frontColor);
        graphics.drawString(st,((int)getX() ) - fsize * st.length() / 4, ((int)getY()) + fsize / 2);

    }
}
