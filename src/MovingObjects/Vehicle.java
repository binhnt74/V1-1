package MovingObjects;

import Graph.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import java.awt.*;
import Graph.Constants.MOVING_DIRECTION;

import java.util.Random;
import javax.swing.Timer;
import java.util.TimerTask;

public class Vehicle extends Node {
    VehicleState state;
    final double SPEED_UNIT = 1;
    double speed;
    double currentMovingAngle;

    MOVING_DIRECTION currentDirection;
    double width = 20;

    public MOVING_DIRECTION getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(MOVING_DIRECTION currentDirection) {
        this.currentDirection = currentDirection;
    }

    int height = 15;
    Edge currentContainingEdge; //current edge that contains this node
    //Timer timer = null;
    javax.swing.Timer timer2;
    //RunningTask runningTask;

    public Vehicle(int id) {
        super(id);
        commonInit();
    }

    public Vehicle(int id, int x, int y) {
        super(id, x, y);
        commonInit();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    void commonInit() {
        width = 20;
        height = 15;
        setBackgroundColor(Color.YELLOW);
        setFrontColor(Color.BLACK);
        //speedUnit = 1;
        speed = 1;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Edge getCurrentContainingEdge() {
        return currentContainingEdge;
    }

    public void setCurrentContainingEdge(Edge currentContainingEdge) {
        this.currentContainingEdge = currentContainingEdge;
    }

    public void run() {
//        if (timer == null) timer = new Timer();
//
//        timer.schedule(runningTask, 1, ((int)(100/velocity)));

        if (timer2 == null)
            timer2 = new Timer((int)(Constants.TIMESLOT/ speed), e -> {
                //revalidate();
                runVehicle();
            });
        timer2.start();
    }

    Edge findAvailableEdge(Edge currentEdge, List<Edge> eList){
        int n = eList.size();
        if (n==0) return null;
        else if (n==1) return eList.get(0);
        else {

//                int i=0;
//                while (i<n && ((LineEdge)eList.get(i)).getAngle() != -Math.PI/2) {
//                    i++;
//                }
//                if (i<n) return eList.get(i);
//                else {
            Random rand = new Random();
            return eList.get(rand.nextInt(n));
//                }
        }

    }

    void runVehicle() {
        //double currentAngle = 0;
        Edge edge = getCurrentContainingEdge();
        if (edge == null) return;
        if (!(edge instanceof LineEdge)) return;
        edge = (LineEdge) edge;

        if (state != VehicleState.RUNNING) {
            currentMovingAngle = ((LineEdge) edge).getAngle();
            setState(VehicleState.RUNNING);
        }
        //if (state == VehicleState.PAUSED || state == VehicleState.STOPPED) return;

//        int dx = 0, dy = 0;
//
//        //currentAngle = ((LineEdge) edge).getAngle();
//
//        if (currentMovingAngle == Math.PI / 2) {
//            dy = -(int) speedUnit;
//        } else if (currentMovingAngle == -Math.PI / 2) {
//            dy = (int) speedUnit;
//        } else if (currentMovingAngle == 0D) {
//            dx = (int) speedUnit;
//        } else if (currentMovingAngle == Math.PI) {
//            dx = -(int) speedUnit;
//        } else {
//            dy = -(int) (speedUnit * Math.sin(currentMovingAngle));
//            dx = - (int) (speedUnit * Math.cos(currentMovingAngle));
//        }

//        setX(getX() + dx);
//        setY(getY() + dy);
        Point2D nextPoint = edge.nextStep(getX(), getY(), currentDirection, SPEED_UNIT);
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
        else if (eList.size() == 1) {//return to the old edge
            //System.out.println("No new ways to go");
            if (edge.getEdgeType() == EdgeType.UNDIRECTED) {
                //currentMovingAngle = Math.PI - currentMovingAngle;
                if (currentDirection==MOVING_DIRECTION.RIGHT)
                    currentDirection = MOVING_DIRECTION.OPPOSITE;
                else
                    currentDirection = MOVING_DIRECTION.RIGHT;
            } else {//Out of running edges, stop
                setState(VehicleState.STOPPED);
                if (timer2 != null)
                    timer2.stop();
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
                    if (currentDirection==MOVING_DIRECTION.RIGHT)
                        currentDirection = MOVING_DIRECTION.OPPOSITE;
                    else
                        currentDirection = MOVING_DIRECTION.RIGHT;
                }
            } else {
                setCurrentContainingEdge(newEdge);
                //if (newEdge instanceof LineEdge) {
                    //newEdge = (LineEdge) newEdge;
                    double angle = ((LineEdge) newEdge).getAngle();
                    if (nextNode == newEdge.getSource()) {
                        //currentMovingAngle = angle;
                        currentDirection = MOVING_DIRECTION.RIGHT;
                    }
                    //setCurrentMovingAngle(((LineEdge) newEdge).getAngle());
                    else {
                        //currentMovingAngle = -angle;
                        currentDirection = MOVING_DIRECTION.OPPOSITE;
                    }
                    //System.out.println("Current angle " + getCurrentMovingAngle());
//                } else
//                    currentMovingAngle = 0D;
            }
//                    System.out.println("Moving angle "+currentMovingAngle);
        }
    //}
    }

    public void reSchedule(double velocity){
        //timer.cancel();
        //timer.schedule(runningTask, 1, ((int)(100/velocity)));
    }
    public void stop() {
//        if (timer != null)
//            timer.cancel();
        timer2.stop();
        setState(VehicleState.STOPPED);
    }

    public void speedUp(double up) {
        speed *= up;
    }

    public void speedDown(double down) {
        speed /= down;
    }

    public void draw() {
        double m = Math.min(width, height) / 2;
        graphics.setColor(getBackgroundColor());
        graphics.fillOval((int)(getX() - m), (int)(getY() - m), (int)(m - 1), (int)(m - 1));
        graphics.fillOval((int)getX() + 1, (int)(getY() - m), (int)(m - 1), (int)(m - 1));
        graphics.fillOval((int)(getX() - m), (int)(getY() + 1), (int)(m - 1), (int)(m - 1));
        graphics.fillOval((int)(getX() + 1), (int)(getY() + 1), (int)(m - 1), (int)(m - 1));
        graphics.setColor(Color.RED);
        graphics.fillOval((int)(getX() - 3), (int)(getY() - 3), 6, 6);

        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = String.valueOf(getId());
        graphics.setColor(getFrontColor());
        graphics.drawString(st, ((int)getX()) - fsize * st.length() / 4, ((int)getY()) + fsize / 2);
        graphics.draw(new Rectangle2D.Double(getX() - width / 2, getY() - height / 2, width, height));

    }

    //Put this vehicle to the position of node




    Vehicle returnMe() {
        return this;
    }

    public enum VehicleState {STARTED, RUNNING, PAUSED, STOPPED}

    class RunningTask extends TimerTask {
        //Find an edge in eList that would be different to currentEdge
        Edge findAvailableEdge(Edge currentEdge, List<Edge> eList){
            int n = eList.size();
            if (n==0) return null;
            else if (n==1) return eList.get(0);
            else {

//                int i=0;
//                while (i<n && ((LineEdge)eList.get(i)).getAngle() != -Math.PI/2) {
//                    i++;
//                }
//                if (i<n) return eList.get(i);
//                else {
                    Random rand = new Random();
                    return eList.get(rand.nextInt(n));
//                }
            }

        }
        @Override
        public void run() {
            //double currentAngle = 0;
            Edge edge = getCurrentContainingEdge();
            if (edge == null) return;
            if (!(edge instanceof LineEdge)) return;
            edge = (LineEdge) edge;

            if (state == VehicleState.STARTED) {
                currentMovingAngle = ((LineEdge) edge).getAngle();
                setState(VehicleState.RUNNING);
            }
            if (state == VehicleState.PAUSED || state == VehicleState.STOPPED) return;

            int dx = 0, dy = 0;

            //currentAngle = ((LineEdge) edge).getAngle();

            if (currentMovingAngle == Math.PI / 2) {
                dy = -(int) SPEED_UNIT;
            } else if (currentMovingAngle == -Math.PI / 2) {
                dy = (int) SPEED_UNIT;
            } else if (currentMovingAngle == 0D) {
                dx = (int) SPEED_UNIT;
            } else if (currentMovingAngle == Math.PI) {
                dx = -(int) SPEED_UNIT;
            } else {
                dy = -(int) (SPEED_UNIT * Math.sin(currentMovingAngle));
                dx = - (int) (SPEED_UNIT * Math.cos(currentMovingAngle));
            }
            setX(getX() + dx);
            setY(getY() + dy);
            if (!edge.contains(returnMe())) {//if this node doest not belong to the current edge any more
                //System.out.println("Vehicle is out of current edge");

                Node newNode = edge.getDest();
                if (!newNode.coincides(returnMe())) newNode = edge.getSource();
                putTo(newNode);
                Graph curGraph = getCurrentGraph();
                if (curGraph==null) return;
                List<Edge> eList = getCurrentGraph().getAdjacentEdges(newNode);
                //System.out.println("New node found " + newNode.getId());

                if (eList.size() == 0) return;
                else if (eList.size() == 1) {//return to the old edge
                    //System.out.println("No new ways to go");
                    if (edge.getEdgeType() == EdgeType.UNDIRECTED) {
                        currentMovingAngle = Math.PI - currentMovingAngle;
                    } else {//Out of running edges, stop
                        setState(VehicleState.STOPPED);
//                        if (timer != null)
//                            timer.cancel();
                    }
                } else {
                    //System.out.println("Found " + (edgeList.size() - 1) + " another ways");
//
                    Edge newEdge = findAvailableEdge(edge, eList);
                    if (newEdge == null) return;
//                    System.out.println("Following edge " + newEdge.getId());
                    if (newEdge == edge) {//return to the old edge
                        if (edge.getEdgeType() == EdgeType.UNDIRECTED) {
                            currentMovingAngle = Math.PI - currentMovingAngle;
                        }
                    } else {
                        setCurrentContainingEdge(newEdge);
                        if (newEdge instanceof LineEdge) {
                            //newEdge = (LineEdge) newEdge;
                            double angle = ((LineEdge) newEdge).getAngle();
                            if (newNode == newEdge.getSource())
                            {
                                currentMovingAngle = angle;
                            }
                                //setCurrentMovingAngle(((LineEdge) newEdge).getAngle());
                            else {
                                currentMovingAngle = - angle;
                            }
                            //System.out.println("Current angle " + getCurrentMovingAngle());
                        } else
                            currentMovingAngle = 0D;
                    }
//                    System.out.println("Moving angle "+currentMovingAngle);
                }
            }
        }
    }
}
