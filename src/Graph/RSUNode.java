package Graph;

import Request.RequestFactory;

import java.awt.*;

public class RSUNode extends NodeWithRoutingTable{
    double width;
    double height;
    RequestFactory requestFactory;

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

        if (getRtTable()!=null){
            //System.out.println("Drawing range circle");
            graphics.setColor(Color.RED);
            int vRange = (int)(getRtTable().getRange()/Graph.getScale());
            graphics.drawOval((int)getX()-vRange,(int)getY()-vRange, 2*vRange, 2*vRange);
        }

    }

    public void startCreatingRequests(){
        if (requestFactory == null){
            requestFactory = new RequestFactory();
        }
        requestFactory.startCreatingRequest(this);
    }

    public void stopCreatingRequests(){
        if (requestFactory == null) return;
        requestFactory.stopCreatingRequest();
    }

    public void startSendingRequest(){

    }
    public void stopSendingRequest(){

    }
}
