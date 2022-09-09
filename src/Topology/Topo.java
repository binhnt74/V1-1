package Topology;

import Graph.*;
import MovingObjects.Vehicle;
import PSO.PSOResult;
import Request.Broker;
import Request.Request;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Topo {
    Graph graph;
    List<Node> vehiclesList;
    List<Node> RSUList;
    GraphDraw topoPanel;
    Timer routingTimer;     //timer for updating routing table
    Timer processRequestTimer;  //timer for processing received requests
    int processingRequestTimeslot;  //in millisecond

    public int getProcessingRequestTimeslot() {
        return processingRequestTimeslot;
    }

    public void setProcessingRequestTimeslot(int processingRequestTimeslot) {
        this.processingRequestTimeslot = processingRequestTimeslot;
    }

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
        processingRequestTimeslot = 1000;
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

    public void updateRSUList(){
        if (RSUList == null) RSUList = new ArrayList<>();
        for(Node node: graph.getNodeList()){
            if (node instanceof RSUNode) RSUList.add(node);
        }
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
        updateRSUList();
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

    public List<Node> getRSUList() {
        return RSUList;
    }

    public void setRSUList(List<Node> RSUList) {
        this.RSUList = RSUList;
    }

    public void stop(){
        stopAllVehicles();
        //topoPanel.stop();
    }
    public void updatingRoutingTableForVehicles(double range){
        for (Node node:vehiclesList){
            Vehicle v = (Vehicle) node;
            v.updateRoutingTable(this);
            v.getRtTable().setRange(range);
        }
    }

    public void updatingRoutingTableForRSU(double range){
        for (Node node:RSUList){
            RSUNode v = (RSUNode) node;
            v.updateRoutingTable(this);
            v.getRtTable().setRange(range);
        }
    }
    public void startUpdatingRoutingTable(){
        if (routingTimer == null){
            routingTimer = new Timer(Constants.UPD_RT_TIMESLOT, e -> {
                updatingRoutingTableForVehicles(500);
                //System.out.println("RSUList size =" + RSUList.size());
                updatingRoutingTableForRSU(1500);
            });
        }
        routingTimer.start();
    }
    public void stopUpdatingRoutingTable(){
        if (routingTimer != null)
            routingTimer.stop();
    }
    public void startCreatingRequests(){
        for (Node node:  vehiclesList){
            Vehicle vehicle = (Vehicle) node;
            vehicle.startCreatingRequests();
        }
//        for (Node node:RSUList){
//            RSUNode rsuNode = (RSUNode) node;
//            rsuNode.startCreatingRequests();
//        }
    }
    public void stopCreatingRequests(){
        for (Node node:  vehiclesList){
            Vehicle vehicle = (Vehicle) node;
            vehicle.stopCreatingRequests();
        }
//        for (Node node:RSUList){
//            RSUNode rsuNode = (RSUNode) node;
//            rsuNode.stopCreatingRequests();
//        }
    }
    public void startSendingRequests(){
        for (Node node:  vehiclesList){
            Vehicle vehicle = (Vehicle) node;
            vehicle.startSendingRequest();
        }

    }
    public void stopSendingRequests(){

        for (Node node:  vehiclesList){
            Vehicle vehicle = (Vehicle) node;
            vehicle.stopSendingRequests();
        }
    }

    public void startProcessingRequests() {
        if (processRequestTimer == null){
            processRequestTimer = new Timer(processingRequestTimeslot, e -> {
                for (Node node:RSUList){
                    RSUNode rsuNode = (RSUNode) node;
                    if (Broker.hasNewRequest(node.getId())) {
                        List<Request> requestList = Broker.receiveRequest(node);
//                        System.out.println("RSU "+node.getId() +" received "+requestList.size()+" request(s)");
                        for (Request request: requestList) {
//                            Timestamp tBegin = Timestamp.from(Instant.now());
                            PSOResult psoResult = rsuNode.processRequest(request);
                            if (psoResult == null) {
                                System.out.println("Problem when processing request " + request.getId());
                                return;
                            }
                            rsuNode.allocateWorkLoad(psoResult);
//                            Timestamp tEnd = Timestamp.from(Instant.now());
//                            long period = tEnd.getTime()-tBegin.getTime();
//                            System.out.println("Processed request from " + tBegin + " to "+ tEnd);
//                            System.out.println("Time processed a request " + period);
                        }
                    }
                }
            });
        }
        processRequestTimer.start();
    }

    private void allocateWorkLoad(PSOResult psoResult) {
    }

//    private void processRequest(Request request) {
//        Node rsuNode = findRSUNode(request.getDest(), getRSUList());
//        if (rsuNode == null) return;
//        ((RSUNode)rsuNode).processRequest(request);
//    }

    private Node findRSUNode(Node dest, List<Node> nodeList) {
        if (nodeList == null) return null;
        else {
            for (Node node:nodeList)
                if (dest == node) return node;
        }
        return null;
    }

    public void stopProcessingRequests() {
        if (processRequestTimer !=null)
            processRequestTimer.stop();
    }

    public void startMonitoring() {
        for (Node rsu:RSUList) {
            ((RSUNode) rsu).startMonitoringRequestProcessing();
        }

    }

    public void stopMonitoring() {
        for (Node rsu:RSUList) {
            ((RSUNode) rsu).stopMonitoringRequestProcessing();
        }
    }
}
