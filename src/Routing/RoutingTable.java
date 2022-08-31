package Routing;

import Graph.Graph;
import Graph.Node;
import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutingTable {
    Node container; //this one contains the routing table
    Graph graph;
    HashMap<Integer, Vehicle> nearVehicleList;  //this stores pairs of vehicle id and the vehicle itself
    double range;   //range of this routing table to check near vehicles

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public RoutingTable(){
        nearVehicleList = new HashMap<>();
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

    public void addVehicle(Vehicle vehicle){
        if (vehicle != null){
            if (!nearVehicleList.containsKey(vehicle.getId())){
                nearVehicleList.put(vehicle.getId(),vehicle);
            }
        }
    }
    public boolean isNear(Vehicle vehicle){
        if (graph.distance(container, vehicle)<= range) return true;
        else return false;
    }
    public void removeVehicle(int vehicleId){
        nearVehicleList.remove(vehicleId);
    }
}
