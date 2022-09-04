package Routing;

import Graph.Graph;
import Graph.Node;
import Graph.RSUNode;
import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutingTable {
    Node container; //this one contains the routing table
    Graph graph;
    HashMap<Integer, Node> nearVehicleList;  //this stores pairs of vehicle id and the vehicle itself
    HashMap<Integer, Node> nearRSUList;  //this stores pairs of vehicle id and the vehicle itself
    double range;   //range of this routing table to check near vehicles

    public HashMap<Integer, Node> getNearRSUList() {
        return nearRSUList;
    }

    public void setNearRSUList(HashMap<Integer, Node> nearRSUList) {
        this.nearRSUList = nearRSUList;
    }

    public HashMap<Integer, Node> getNearVehicleList() {
        return nearVehicleList;
    }

    public void setNearVehicleList(HashMap<Integer, Node> nearVehicleList) {
        this.nearVehicleList = nearVehicleList;
    }

    public RoutingTable(){
        nearVehicleList = new HashMap<>();
        nearRSUList = new HashMap<>();
        range = 500;   //metre
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Node getContainer() {
        return container;
    }

    public void setContainer(Node container) {
        this.container = container;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void addVehicle(Node vehicle){
        if (vehicle != null){
            if (!nearVehicleList.containsKey(vehicle.getId())){
                nearVehicleList.put(vehicle.getId(),vehicle);
            }
        }
    }
    public void addRSU(Node rsuNode){
        if (rsuNode != null){
            if (!nearRSUList.containsKey(rsuNode.getId())){
                nearRSUList.put(rsuNode.getId(),rsuNode);
            }
        }
    }
    public boolean isNear(Node node){
        if (graph == null){
            System.out.println("The graph is null, this should be initialized");
            return false;
        }
        if (container == null) return false;
        if (container == node) return false;
        if (graph.distance(container, node)<= range) return true;
        else return false;
    }
    public void removeVehicle(int vehicleId){

        if (nearVehicleList.size()>0)
            nearVehicleList.remove(vehicleId);
    }
    public void removeRSU(int RSUId){
        if (nearRSUList.size()>0) nearRSUList.remove(RSUId);
    }

    //update this routing table from the list of available vehicles
    public void updateRoutingTable(List<Node> nodeList){
        if (nodeList == null) return;
        for (Node node: nodeList){
            if (node instanceof Vehicle){
                //Adding new near vehicle
                if (!nearVehicleList.containsKey(node.getId()) && isNear(node)) {
                    addVehicle(node);
                    //System.out.println("Vehicle " + node.getId() + " detected in range of " + container.getId());
                }
                //Removing out-of-range vehicles
                if (nearVehicleList.containsKey(node.getId()) && !isNear(node)) {
                    removeVehicle(node.getId());
                    //System.out.println("    Vehicle " + node.getId() + " moved out of range of " + container.getId());
                }
            }
            else if (node instanceof RSUNode){
                //Adding new near RSU
                if (!nearRSUList.containsKey(node.getId()) && isNear(node)) {
                    addRSU(node);
                    //System.out.println("RSU " + node.getId() + " detected in range of " +container.getId());
                }
                //Removing out-of-range RSU
                if (nearRSUList.containsKey(node.getId()) && !isNear(node)) {
                    removeRSU(node.getId());
                    //System.out.println("    RSU " + node.getId() + " moved out of range of " +container.getId());
                }
            }

        }
    }
    public int getNumberOfNearVehicles(){
        return nearVehicleList.size();
    }
}
