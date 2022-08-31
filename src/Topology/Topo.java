package Topology;

import Graph.*;
import MovingObjects.Vehicle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Topo {
    Graph graph;
    List<Node> vehiclesList;
    GraphDraw topoPanel;
    Timer routingTimer;     //timer for updating routing table

    public GraphDraw getTopoPanel() {
        return topoPanel;
    }

    public void setTopoPanel(GraphDraw topoPanel) {
        this.topoPanel = topoPanel;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Topo(){
        vehiclesList = new ArrayList<>();
        topoPanel = new GraphDraw();
    }
    public void addVehicle(Vehicle vehicle){
        if (vehicle != null){
            if (getVehicle(vehicle.getId()) == null)
                vehiclesList.add(vehicle);
        }
    }

    public Node getVehicle(int id){
        if (vehiclesList == null) return null;
        for (Node v:vehiclesList) {
            if (v.getId() == id) return v;

        }
        return null;
    }

    public void runAllVehicles() {
        for (Node node: vehiclesList){
            Vehicle v = (Vehicle) node;
            if (v.getState() != Vehicle.VehicleState.RUNNING) v.run();
        }

    }
    public void stopAllVehicles(){
        for (Node node: vehiclesList){
            Vehicle v = (Vehicle) node;
            if (v.getState() == Vehicle.VehicleState.RUNNING) v.stop();
        }

    }

    public void putVehicleToNode(int vehicleId, int nodeId){
        if (graph == null) return;
        Vehicle vehicle = (Vehicle) getVehicle(vehicleId);
        if (vehicle == null) return;
        Node node = graph.getNode(nodeId);
        if (node == null) return;
        vehicle.putTo(node);
    }
    public void putVehicleToEdge(int vehicleId, int edgeId, double relativePos){
        if (graph == null) return;
        Vehicle vehicle = (Vehicle) getVehicle(vehicleId);
        if (vehicle == null) return;
        Edge edge = graph.getEdge(edgeId);
        if (edge == null) return;
        vehicle.putTo(edge,relativePos);
    }
    public void show(){
        if (graph == null) return;
        topoPanel.setGraph(graph);
        topoPanel.setVehicleList(vehiclesList);
        topoPanel.run();
        //topoPanel.draw(topoPanel.getGraphics(),vehiclesList);
        //topoPanel.repaint();
    }
    public void run(){
        runAllVehicles();
        //topoPanel.run();
    }

    public List<Node> getVehiclesList() {
        return vehiclesList;
    }

    public void setVehiclesList(List<Node> vehiclesList) {
        this.vehiclesList = vehiclesList;
    }

    public void stop(){
        stopAllVehicles();
        //topoPanel.stop();
    }
    public void startUpdatingRoutingTable(){
        if (routingTimer == null){
            routingTimer = new Timer(50, e -> {
                for (Node node:vehiclesList){
                    Vehicle v = (Vehicle) node;
                    v.updateRoutingTable(this);
                }
            });
        }
        routingTimer.start();
    }
    public void stopUpdatingRoutingTable(){
        if (routingTimer != null)
            routingTimer.stop();
    }
}
