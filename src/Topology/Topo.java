package Topology;

import Graph.*;
import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Topo {
    Graph graph;
    List<Vehicle> vehiclesList;
    GraphDraw topoPanel;

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

    public Vehicle getVehicle(int id){
        if (vehiclesList == null) return null;
        for (Vehicle v:vehiclesList) {
            if (v.getId() == id) return v;

        }
        return null;
    }

    public void runAllVehicles() {
        for (Vehicle v: vehiclesList)
            if (v.getState() != Vehicle.VehicleState.RUNNING) v.run();
//            for (Node v: graph.getNodeList())
//                if (v instanceof Vehicle){
//                    if (((Vehicle) v).getState()!= Vehicle.VehicleState.RUNNING)
//                        ((Vehicle) v).run();
//                }

    }
    public void stopAllVehicles(){
        for (Vehicle v: vehiclesList)
            if (v.getState() == Vehicle.VehicleState.RUNNING) v.stop();
//        for (Node v: graph.getNodeList())
//            if (v instanceof Vehicle){
//                if (((Vehicle) v).getState()== Vehicle.VehicleState.RUNNING)
//                    ((Vehicle) v).stop();
//            }
    }

    public void putVehicleToNode(int vehicleId, int nodeId){
        if (graph == null) return;
        Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle == null) return;
        Node node = graph.getNode(nodeId);
        if (node == null) return;
        vehicle.putTo(node);
    }
    public void putVehicleToEdge(int vehicleId, int edgeId, double relativePos){
        if (graph == null) return;
        Vehicle vehicle = getVehicle(vehicleId);
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
    public void stop(){
        stopAllVehicles();
        //topoPanel.stop();
    }
}
