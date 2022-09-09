package MovingObjects;

import Accesories.ImageProcessingKit;
import Graph.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import java.awt.*;

import Graph.Constants.MOVING_DIRECTION;
import Request.Request;
import Request.RequestFactory;
import Request.Broker;

import java.util.Random;
import javax.swing.Timer;
//import java.util.TimerTask;

public class Vehicle extends NodeWithRoutingTable {
    public enum VehicleState {RUNNING, STOPPED}
    VehicleState state;
    double speed_unit = 1.0D;  //set to 0 for speed 0
    double internalSpeed;   //internal speed
    double realSpeed;   //used to setup from user's point of view, unit in metre/second
    double currentMovingAngle;

    MOVING_DIRECTION currentDirection;
    double width ;  //in metre
    double height;  //in metre
    Edge currentContainingEdge; //current edge that contains this node
    //Timer timer = null;
    Timer runningVehicleTimer;   //timer for running vehicle

    List<Request> requestList;
    //Timer requestTimer; //timer for creating new requests
    RequestFactory requestFactory;
    ImageProcessingKit kit;    //image processing kit

    public ImageProcessingKit getKit() {
        return kit;
    }

    public void setKit(ImageProcessingKit kit) {
        if (kit == null){
            kit = new ImageProcessingKit();
        }
        this.kit = kit;
    }

    public Vehicle(int id) {
        super(id);
        commonInit();
    }

    public Vehicle(int id, int x, int y) {
        super(id, x, y);
        commonInit();
    }
    //RunningTask runningTask;

    public MOVING_DIRECTION getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(MOVING_DIRECTION currentDirection) {
        this.currentDirection = currentDirection;
    }

    public double getInternalSpeed() {
        return internalSpeed;
    }

    public void setInternalSpeed(double internalSpeed) {
        this.internalSpeed = internalSpeed;
        realSpeed = 1000 * Graph.getScale() * internalSpeed / Constants.TIMESLOT;
        if (internalSpeed == 0) speed_unit = 0D;
        else if (speed_unit == 0) speed_unit = 1D;
    }

    //unit metre/second
    public double getRealSpeed() {
        return realSpeed;
    }

    //unit metre/second
    public void setRealSpeed(double realSpeed) {
        this.realSpeed = realSpeed;
        internalSpeed = Constants.TIMESLOT * realSpeed / 1000 / Graph.getScale();
        if (internalSpeed == 0) speed_unit = 0D;
        else if (speed_unit == 0) speed_unit = 1D;
    }

    void commonInit() {
        width = 2;
        height = 3;
        setBackgroundColor(Color.YELLOW);
        setFrontColor(Color.BLACK);
        //speedUnit = 1;
        internalSpeed = 1.0D;
        state = VehicleState.STOPPED;
        currentDirection = MOVING_DIRECTION.RIGHT;
        //runningTask = new RunningTask();
    }

    public VehicleState getState() {
        return state;
    }

    public void setState(VehicleState state) {
        this.state = state;
    }

    public double getCurrentMovingAngle() {
        return currentMovingAngle;
    }

    public void setCurrentMovingAngle(double currentMovingAngle) {
        this.currentMovingAngle = currentMovingAngle;
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

    public Edge getCurrentContainingEdge() {
        return currentContainingEdge;
    }

    public void setCurrentContainingEdge(Edge currentContainingEdge) {
        this.currentContainingEdge = currentContainingEdge;
    }

    public void run() {
        if (runningVehicleTimer == null)
            runningVehicleTimer = new Timer((int) (Constants.TIMESLOT / internalSpeed), e -> {
                //revalidate();
                runVehicle();
            });
        runningVehicleTimer.start();
    }

    Edge findAvailableEdge(Edge currentEdge, List<Edge> eList) {
        int n = eList.size();
        if (n == 0) return null;
        else if (n == 1) return eList.get(0);
        else {
            Random rand = new Random();
            return eList.get(rand.nextInt(n));

        }

    }

    void runVehicle() {
        //double currentAngle = 0;
        Edge edge = getCurrentContainingEdge();
        if (edge == null) return;
//        if (!(edge instanceof LineEdge)) return;
//        edge = (LineEdge) edge;

        if (state != VehicleState.RUNNING) {
            //currentMovingAngle = ((LineEdge) edge).getAngle();
            setState(VehicleState.RUNNING);
        }
        //if (state == VehicleState.PAUSED || state == VehicleState.STOPPED) return;

        Point2D nextPoint = edge.nextStep(getX(), getY(), currentDirection, speed_unit);
//        if (edge instanceof ArcEdge){
//            System.out.println("Following arc edge (x,y) = " + getX()+", "+getY());
//            System.out.println("   (xn,yn) = " + nextPoint.getX()+", "+nextPoint.getY());
//
//        }
        Node nextNode;
        if (currentDirection == MOVING_DIRECTION.RIGHT)
            nextNode = edge.getDest();
        else nextNode = edge.getSource();

        if (!nextNode.coincides(nextPoint)) {
            setX(nextPoint.getX());
            setY(nextPoint.getY());
            return;
        }
        putTo(nextNode);
        //if (!edge.contains(returnMe())) {//if this node doest not belong to the current edge any more
        //System.out.println("Vehicle is out of current edge");

//        Node newNode = edge.getDest();
//        if (!newNode.coincides(returnMe())) newNode = edge.getSource();
//        putTo(newNode);
        Graph curGraph = getCurrentGraph();
        if (curGraph == null) return;
        List<Edge> eList = getCurrentGraph().getAdjacentEdges(nextNode);
        //System.out.println("New node found " + newNode.getId());

        if (eList.size() == 0) return;
        else if (eList.size() == 1) {
//            System.out.println("Only one way left");
            if (edge.getEdgeType() == EdgeType.UNDIRECTED) {//return to the old edge
                //currentMovingAngle = Math.PI - currentMovingAngle;
                if (currentDirection == MOVING_DIRECTION.RIGHT)
                    currentDirection = MOVING_DIRECTION.OPPOSITE;
                else
                    currentDirection = MOVING_DIRECTION.RIGHT;
            } else {//Out of running edges, stop
                //setState(VehicleState.STOPPED);
                setCurrentContainingEdge(eList.get(0));
                if (nextNode == eList.get(0).getSource()) currentDirection = MOVING_DIRECTION.RIGHT;
                else currentDirection = MOVING_DIRECTION.OPPOSITE;
            }
        } else {
            //System.out.println("Found " + (edgeList.size() - 1) + " another ways");
//
            Edge newEdge = findAvailableEdge(edge, eList);
            if (newEdge == null) return;
//                    System.out.println("Following edge " + newEdge.getId());
            if (newEdge == edge) {//return to the old edge
                if (edge.getEdgeType() == EdgeType.UNDIRECTED) {
                    //currentMovingAngle = Math.PI - currentMovingAngle;
                    if (currentDirection == MOVING_DIRECTION.RIGHT)
                        currentDirection = MOVING_DIRECTION.OPPOSITE;
                    else
                        currentDirection = MOVING_DIRECTION.RIGHT;
                }
            } else {
                setCurrentContainingEdge(newEdge);

                //double angle = ((LineEdge) newEdge).getAngle();
                if (nextNode == newEdge.getSource()) {
                    //currentMovingAngle = angle;
                    currentDirection = MOVING_DIRECTION.RIGHT;
                }
                //setCurrentMovingAngle(((LineEdge) newEdge).getAngle());
                else {
                    //currentMovingAngle = -angle;
                    currentDirection = MOVING_DIRECTION.OPPOSITE;
                }

            }
//                    System.out.println("Moving angle "+currentMovingAngle);
        }
        //}
    }

    public void stop() {
//        if (timer != null)
//            timer.cancel();
        runningVehicleTimer.stop();
        setState(VehicleState.STOPPED);
    }

    public void speedUp(double up) {
        realSpeed *= up;
    }

    public void speedDown(double down) {
        realSpeed /= down;
    }

    public void draw() {
        double m = Math.min(width, height) / 2;
//        graphics.setColor(getBackgroundColor());
//        graphics.fillOval((int) (getX() - m), (int) (getY() - m), (int) (m - 1), (int) (m - 1));
//        graphics.fillOval((int) getX() + 1, (int) (getY() - m), (int) (m - 1), (int) (m - 1));
//        graphics.fillOval((int) (getX() - m), (int) (getY() + 1), (int) (m - 1), (int) (m - 1));
//        graphics.fillOval((int) (getX() + 1), (int) (getY() + 1), (int) (m - 1), (int) (m - 1));
//        graphics.setColor(Color.RED);
//        graphics.fillOval((int) (getX() - 3), (int) (getY() - 3), 6, 6);

        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = String.valueOf(getId());
        graphics.setColor(getFrontColor());
        graphics.drawString(st, ((int) getX()) - fsize * st.length() / 4, ((int) getY()) + fsize / 2);
//        graphics.draw(new Rectangle2D.Double(getX() - width / 2, getY() - height / 2, width, height));
        graphics.fill(new Rectangle2D.Double(getX() - width, getY() - height, width*2, height*2));

        if (getRtTable()!=null){
            //System.out.println("Drawing range circle");
            graphics.setColor(Color.RED);
            int vRange = (int)(getRtTable().getRange()/Graph.getScale());
            graphics.drawOval((int)getX()-vRange,(int)getY()-vRange, 2*vRange, 2*vRange);
        }

    }

    //Put this vehicle to the position of node


    Vehicle returnMe() {
        return this;
    }



    //update routing table from this topo

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
    public void sentRequest(){
        if (requestFactory == null) return;
        if (requestFactory.getContainer() == null){
            requestFactory.setContainer(this);
        }
        if (requestFactory.getRequestList().size()==0) return;
        if (getRtTable() == null) return;
        int i = 0;
        //Take the first request from the request list
        Request request = requestFactory.getRequestList().get(i);
        Node dest;
        //Find near RSU node to send the request
        if (getRtTable().getNearRSUList().size()>0){
            Collection<Node> collection = getRtTable().getNearRSUList().values();
            Node[] arr = new Node[getRtTable().getNearRSUList().size()];
            collection.toArray(arr);
            dest = arr[0];

            request.setDest(dest);
            Broker.sendRequest(request);
            //System.out.println("Request sent from " + getId() + " to " +dest.getId());
            requestFactory.removeRequest(i);
        }
//        else return;
//        {
//            if (getRtTable().getNumberOfNearVehicles()>0){
//                Collection<Node> collection = getRtTable().getNearVehicleList().values();
//                Node[] arr = new Node[getRtTable().getNearVehicleList().size()];
//                collection.toArray(arr);
//                dest = arr[0];
//            }
//            else return;
//        }

    }

    //send a request in requestFactory
    public void startSendingRequest(){
        if (requestFactory == null) return;
        if (requestFactory.getContainer() == null){
            requestFactory.setContainer(this);
        }
        requestFactory.startSendingRequests();

    }
    public void stopSendingRequests(){
        if (requestFactory == null) return;
        requestFactory.stopSendingRequests();
    }
    public void setupImageProcessingKit(ImageProcessingKit newKit){
        setKit(newKit);
    }
}
