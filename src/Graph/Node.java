package Graph;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected static Graphics2D graphics = null;
    int id; //node id
    double x;
    double y;
    Color backgroundColor;
    Color frontColor;   //for text color
    Graph currentGraph;  //graph that contains this node

    public Node(int id) {
        this.id = id;
        x = y = 0;

    }

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;

    }

    static public Graphics2D getGraphics() {
        return graphics;
    }

    static public void setGraphics(Graphics2D g) {
        graphics = g;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(Graph currentGraph) {
        this.currentGraph = currentGraph;
    }

    public Color getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(Color frontColor) {
        this.frontColor = frontColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(){}
    public Point TopPoint(){
        Point p = new Point();
//        p.setLocation(x,y-nodeWidth/2);
        return p;

    }
    public Point BottomPoint(){
        Point p = new Point();
//        p.setLocation(x,y+nodeWidth/2);
        return p;
    }
    public Point LeftPoint(){
        Point p = new Point();
//        p.setLocation(x-nodeWidth/2,y);
        return p;
    }
    public Point RightPoint(){
        Point p = new Point();
//        p.setLocation(x+nodeWidth/2,y);
        return p;
    }
    public void zoom(double scale){

    }
    public boolean coincides(Node node){
        if ((x- node.x)*(x- node.x) + (y- node.y)*(y- node.y)<Constants.COINCIDE_LIMIT) return true;
        else return false;
    }
    public boolean coincides(Point2D point){
        if ((x- point.getX())*(x- point.getX()) + (y- point.getY())*(y- point.getY())<Constants.COINCIDE_LIMIT) return true;
        else return false;
    }
    public void putTo(Node node) {
        setX(node.getX());
        setY(node.getY());
    }

    //Put this vehicle to a relative position on the edge from the source
    //Condition: 0<=p<=1
    public void putTo(Edge edge, double pos) {
        if (edge ==null) return;
        if (edge.getSource() == null || edge.getDest() == null) return;
        if (pos < 0) pos = 0;
        if (pos > 1) pos = 1;

        double x1 = edge.getSource().getX();
        double y1 = edge.getSource().getY();
        double x2 = edge.getDest().getX();
        double y2 = edge.getDest().getY();

        setX((int) (pos * (x2 - x1) + x1));
        setY((int) (pos * (y2 - y1) + y1));
    }



}
