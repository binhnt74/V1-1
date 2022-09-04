package Graph;

import java.awt.*;
import java.awt.geom.Point2D;
import Graph.Constants.MOVING_DIRECTION;

public abstract class Edge {
    private static final double  EPSILON = 0.01;
    protected static Graphics2D graphics = null;
    int id;
    Node source;    //source node
    Node dest;      //destination node
    EdgeType edgeType;  //undirected or directed
    double min_speed;   //minimal speed for this edge
    double max_speed;   //maximal speed for this edge
    String name;

    public double getMin_speed() {
        return min_speed;
    }

    public void setMin_speed(double min_speed) {
        this.min_speed = min_speed;
    }

    public double getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(double max_speed) {
        this.max_speed = max_speed;
    }

    double weight;  //or distance between two nodes source & dest
    Color backgroundColor;
    Color frontColor;   //for text color

    public Edge(int id) {
        this.id = id;
        edgeType = EdgeType.UNDIRECTED;
        min_speed = 0;
        max_speed = 28; //28m/s ~ 100km/h
    }

    public static Graphics2D getGraphics() {
        return graphics;
    }

    public static void setGraphics(Graphics2D graphics) {
        Edge.graphics = graphics;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(Color frontColor) {
        this.frontColor = frontColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void draw(){

    }
    //check whether node is on this edge
    public boolean contains(Node node){
        if (getSource()==null || getDest()==null) return false;
        double x = node.getX();
        double y = node.getY();
        double x1 = getSource().getX();
        double y1 = getSource().getY();
        double x2 = getDest().getX();
        double y2 = getDest().getY();

        if ((x==x1 && y==y1) || (x==x2 && y==y2)) return  true;
        if (x<Math.min(x1, x2) ||x>Math.max(x1,x2)) return false;
        if (y<Math.min(y1, y2) ||y>Math.max(y1,y2)) return false;
        if (x1==x2){
            if (x == x1 && MyUtils.DrawingUtils.between(y,y1,y2)) return true;
        }
        if (y1==y2){
            if (y == y1 && MyUtils.DrawingUtils.between(x,x1,x2)) return true;
        }

        if (Math.abs((x-x1)/(x2-x1) - (y-y1)/(y2-y1))<EPSILON){
            return true;
        }

        return false;
    }
    //return another node of the node of this edge
    public Node getAnotherNode(Node node){
        if (node == null) return null;
        if (node != source && node != dest) return null;
        if (node == source) return dest;
        else return source;
    }
    //return next point from the current position, direction and speed
    public Point2D nextStep(double curX, double curY, MOVING_DIRECTION curDirection, double speed){
        return new Point2D.Double(curX,curY);
    }
}
