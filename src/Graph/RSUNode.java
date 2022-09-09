package Graph;

import MovingObjects.Vehicle;
import MyUtils.AllocationInfo;
import PSO.*;
import Request.Request;
import Request.RequestFactory;
import Request.RequestProcessingList;
import Request.RequestProcessingDetails;
import Routing.RoutingTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RSUNode extends NodeWithRoutingTable {
    double width;
    double height;
    RequestFactory requestFactory;
    RequestProcessingList globalRequestProcessingList;
    int requestProcessingTimeSlot; //in millisecond
    Timer monitoringRequestProcessingTimer;

    public RSUNode(int id) {
        super(id);
        commInit();
    }

    public RSUNode(int id, int x, int y) {
        super(id, x, y);
        commInit();
    }

    void commInit() {
        width = height = 25;
        backgroundColor = Color.RED;
        frontColor = Color.BLUE;
        globalRequestProcessingList = new RequestProcessingList();
        requestProcessingTimeSlot = 20;
    }

    @Override
    public void draw() {
        //System.out.println("Drawing oval node...");
        if (graphics == null) {
            System.out.println("Graphic system is null");
            return;
        }
        graphics.setColor(backgroundColor);
        graphics.fillOval((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);

        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = "RSU " + getId();
        graphics.setColor(frontColor);
        graphics.drawString(st, ((int) getX()) - fsize * st.length() / 4, ((int) getY()) + fsize / 2);

        if (getRtTable() != null) {
            //System.out.println("Drawing range circle");
            graphics.setColor(Color.RED);
            int vRange = (int) (getRtTable().getRange() / Graph.getScale());
            graphics.drawOval((int) getX() - vRange, (int) getY() - vRange, 2 * vRange, 2 * vRange);
        }

    }

    public void startCreatingRequests() {
        if (requestFactory == null) {
            requestFactory = new RequestFactory();
        }
        requestFactory.startCreatingRequest(this);
    }

    public void stopCreatingRequests() {
        if (requestFactory == null) return;
        requestFactory.stopCreatingRequest();
    }

    public void startSendingRequest() {

    }

    public void stopSendingRequest() {

    }

    public PSOResult processRequest(Request request) {
        System.out.println("Processing request " + request.getId() + " from vehicle " + request.getSource().getId() +
                " sent to " + request.getDest().getId() + " at " + request.getTimestamp());
        System.out.println("Workload required " + request.getWorkload());

        if (rtTable == null) return null;
        //if (!(request.getSource() instanceof Vehicle)) return;

        Swarm swarm = new Swarm();
//        Particle[] p = new Particle[50];
//        swarm.setParticles(p);

        List<Vehicle> vehicleList = rtTable.getNearVehicleList(request.getSource().getId());

        if (vehicleList == null) vehicleList = new ArrayList<>();

        vehicleList.add((Vehicle) request.getSource());

        int dimension = vehicleList.size();
        //List<Node> nearVehicleList = rtTable.getNearVehicleList().;

        int N = 50; //number of particles
        Particle[] p = new Portion[N];
        for (int i = 0; i < N; i++) {
            p[i] = new Portion(dimension);

        }

        swarm.setParticles(p);

        swarm.InitSwampOfParticles(dimension, -0.1, 0.1);
        //swarm.printPosition();

        LoadAllocation[] loadAllocation = new LoadAllocation[N];
        for (int i = 0; i < N; i++) {
            loadAllocation[i] = new LoadAllocation();
            //loadAllocation[i].setNodeId(request.getDest().getId());
            //
            //loadAllocation[i].initRandom();
        }

        for (int i = 0; i < vehicleList.size(); i++) {
            loadAllocation[i].setWorkload(request.getWorkload());
            //Vehicle v = (Vehicle) node;
            loadAllocation[i].setSpeed(vehicleList.get(i).getKit().getProcessor().getSpeed());
            Vehicle source = (Vehicle) request.getSource();
            //RSUNode dest = (RSUNode)request.getDest();
            loadAllocation[i].setBandwidth(Math.min(vehicleList.get(i).getKit().getWifiHub().getBandwidth(), source.getKit().getWifiHub().getBandwidth()));

        }

//        System.out.print("Near vehicle list: ");
//        String st="";
//        for (Vehicle v:vehicleList){
//            st += v.getId() + " ";
//        }
//        System.out.println(st);
//
//        System.out.println("Allocation ");
//        for (int i = 0;i<dimension;i++){
//            System.out.println("BW: " +loadAllocation[i].getBandwidth() + ", SP: " + loadAllocation[i].getSpeed());
//        }

        FitnessFunctions.setLoadAllocation(loadAllocation);

        //swarm.pso(100, FitnessFunctions.sphereFF);
        double[] best_position = PSOGeneral.pso(40, swarm, FitnessFunctions.minMax);

//        Logging.logger.log(Level.INFO, "Best position returned:");
        System.out.println("Best position returned: ");
        if (best_position != null) {
            Particle.printArray(best_position);
        }
//        Logging.logger.log(Level.INFO, "Best value returned: "+FitnessFunctions.minMax.apply(best_position) + "(s)");
        System.out.println("Best value returned: " + FitnessFunctions.minMax.apply(best_position) + "(s)\n");
        PSOResult psoResult = new PSOResult();
        //int i;
        double transmitTime = 0, processTime = 0;
        for (int i = 0; i < vehicleList.size(); i++) {
            AllocationInfo allocationInfo = new AllocationInfo();
            allocationInfo.setNode(vehicleList.get(i));
//            allocationInfo.setPortion(best_position[i]);
            transmitTime = best_position[i] * loadAllocation[i].getWorkload() / loadAllocation[i].getBandwidth();
            allocationInfo.setTransmissionDuration(transmitTime);
            processTime = best_position[i] * loadAllocation[i].getWorkload() / loadAllocation[i].getSpeed();
            allocationInfo.setProcessDuration(processTime);
            psoResult.addAllocationInfo(allocationInfo);
        }
        psoResult.setRequest(request);
        return psoResult;

    }

    public void allocateWorkLoad(PSOResult psoResult) {
        if (psoResult == null) return;
        long requestId = psoResult.getRequest().getId();
        List<AllocationInfo> allocationInfoList = psoResult.getAllocationList();
        HashMap<Long, List<RequestProcessingDetails>> map = globalRequestProcessingList.getProcessingMap();
        globalRequestProcessingList.addRequester(requestId, psoResult.getRequest().getSource());
        List<RequestProcessingDetails> details;
        if (map.containsKey(requestId)) {
            details = map.get(requestId);
        } else {
            details = new ArrayList<>();
            map.put(requestId, details);
        }
        RequestProcessingDetails aDetail;

        for (int i = 0; i < allocationInfoList.size(); i++) {
            aDetail = new RequestProcessingDetails();
            aDetail.setProcessorNode(allocationInfoList.get(i).getNode());
            long transDuration = (long) (allocationInfoList.get(i).getTransmissionDuration() * 1000);
            aDetail.setTransmissionDuration(transDuration);
            long procDuration = (long) (allocationInfoList.get(i).getProcessDuration() * 1000);
            aDetail.setProcessDuration(procDuration);
            details.add(aDetail);
        }

    }

    //Monitoring and updating globalRequestProcessingList
    public void monitorProcessing() {
        if (globalRequestProcessingList.getProcessingMap().size() == 0) return;
        HashMap<Long, List<RequestProcessingDetails>> requestMap = globalRequestProcessingList.getProcessingMap();
        Set<Long> requestIDSet = requestMap.keySet();
        long currentTime;
        List<RequestProcessingDetails> currentList;
        Node currentNode;
        Node requesterNode;
        List<Long> successRequestList = new ArrayList<>();
        List<Long> failRequestList = new ArrayList<>();

        for (Long requestId : requestIDSet) {
            currentTime = System.currentTimeMillis();
            currentList = requestMap.get(requestId);
            for (RequestProcessingDetails detail : currentList) {
                currentNode = detail.getProcessorNode();
                requesterNode = globalRequestProcessingList.getRequester(requestId);
                if ((requesterNode == currentNode ) || RoutingTable.isNear((NodeWithRoutingTable) requesterNode, (NodeWithRoutingTable) currentNode)) {
                    globalRequestProcessingList.updateState(requestId, currentNode, currentTime);
                    //Checking any request done
                    if (globalRequestProcessingList.isRequestDone(requestId)){
//                        globalRequestProcessingList.removeRequestProcessingDetail(requestId);
//                        globalRequestProcessingList.removeRequester(requestId);
                        successRequestList.add(requestId);
                        System.out.println("Request " + requestId + " is DONE");
                    }
                }

                else {
//                    globalRequestProcessingList.removeRequestProcessingDetail(requestId);
//                    globalRequestProcessingList.removeRequester(requestId);
                    failRequestList.add(requestId);
                    System.out.println("Request " + requestId + " failed due to " + " vehicle " + currentNode.getId() + " went far away");
                }
            }

        }
        for (Long requestId: successRequestList){
            globalRequestProcessingList.removeRequestProcessingDetail(requestId);
                    globalRequestProcessingList.removeRequester(requestId);
        }
        for (Long requestId: failRequestList){
            globalRequestProcessingList.removeRequestProcessingDetail(requestId);
            globalRequestProcessingList.removeRequester(requestId);
        }

    }

    public void startMonitoringRequestProcessing() {
        if (monitoringRequestProcessingTimer == null) {
            monitoringRequestProcessingTimer = new Timer(requestProcessingTimeSlot, e -> {
                monitorProcessing();
            });
        }
        monitoringRequestProcessingTimer.start();
    }

    public void stopMonitoringRequestProcessing() {
        if (monitoringRequestProcessingTimer != null)
            monitoringRequestProcessingTimer.stop();
    }
}
