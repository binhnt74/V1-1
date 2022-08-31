package Graph;

import MovingObjects.Vehicle;
import MyUtils.DrawingUtils;

import javax.swing.*;
import java.awt.*;
//import java.util.Timer;
import java.awt.geom.Line2D;
import java.util.TimerTask;

public class GraphDraw extends JPanel {
    Graphics2D graphics;
    Graph graph;
    javax.swing.Timer timer2;
    Timer timer;
    UpdateTask updateTask;
    Window mainWind;
    java.util.List<Node> vehicleList;

    public java.util.List<Node> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(java.util.List<Node> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public Window getMainWind() {
        return mainWind;
    }

    public void setMainWind(Window mainWind) {
        this.mainWind = mainWind;
    }

    public GraphDraw() {
        setPreferredSize(new Dimension(600, 600));

        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void paint(Graphics g) {
        super.paint(g);

        draw(g);
    }

    public void draw(Graphics g) {
        if (g == null) {
            System.out.println("Graphics is null");
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        //if (graph == null) return;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        if (graph == null) {
            System.out.println("Graph is null");
            return;
        }

        Edge.setGraphics(g2d);
        //System.out.println("Graph is ready");
        for (Edge ed : graph.edgeList) {
            ed.draw();
        }

        Node.setGraphics(g2d);
        for (Node nd : graph.nodeList) {
            nd.draw();
        }
        if (vehicleList == null) return;
        for (Node nd : vehicleList) {
            nd.draw();
        }

        drawGraphScale(g2d);
        //repaint();

    }
    public void drawGraphScale(Graphics2D graphics){
        graphics.setColor(Color.BLACK);
        Stroke curStroke = graphics.getStroke();
        Stroke stroke = new BasicStroke((float)4);
        graphics.setStroke(stroke);
        int x1=10,y1=550,x2=550,y2=550;
//        Line2D line2D = new Line2D.Double(x1,  y1,  x2,  y2);
//        graphics.draw(line2D);
        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = String.valueOf(graph.getScale()*400);
        st += " m";
        graphics.setColor(Color.BLUE);
        graphics.setStroke(curStroke);
        graphics.drawString(st,((int)((x1+x2)/2) ) - fsize * st.length() / 4, ((int)((y1+y2)/2)) + fsize / 2+10);
        //graphics.drawString(st,((int)((10+400)/2) ) - fsize * st.length() / 4, ((int)((400+400)/2)) + fsize / 2);

            Stroke directionStroke = new BasicStroke(2);
            graphics.setStroke(directionStroke);
        DrawingUtils.drawArrow(graphics,x1,y1,x2,y2,6,40);
        DrawingUtils.drawArrow(graphics,x2,y2,x1,y1,6,40);
            //DrawingUtils.drawArrow(graphics,(2*10+410)/3,(2*400+400)/3,(10+2*410)/3,(400+2*400)/3,6,40);
            graphics.setStroke(curStroke);


    }
    public void draw(Graphics g, java.util.List<Vehicle> vehicleList) {
        if (g == null) {
            System.out.println("Graphics is null");
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        //if (graph == null) return;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        if (graph == null) {
            System.out.println("Graph is null");
            return;
        }

        Node.setGraphics(g2d);
        for (Node nd : vehicleList) {
            nd.draw();
        }
        //repaint();

    }

    public void run() {
//        if (timer==null) timer = new Timer();
//        if (updateTask ==null) updateTask = new UpdateTask();
//        timer.schedule(updateTask,1,500);
        //System.out.println("Running graph");
        if (timer2 == null) {
            //System.out.println("Timer 2 is null, starting");
            timer2 = new Timer(Constants.TIMESLOT, e -> {
                //revalidate();
                repaint();
                if (mainWind != null)
                    mainWind.repaint();
                //System.out.println("Repaint graph");
            });
        }
        timer2.start();
    }

    public void stop() {
//        if (timer!=null){
//            timer.cancel();
//        }
        if (timer2 != null)
        timer2.stop();
    }

    class UpdateTask extends TimerTask {

        @Override
        public void run() {
            repaint();
            if (mainWind != null)
                mainWind.repaint();
        }
    }
}
