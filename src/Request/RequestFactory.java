package Request;

import Graph.Node;
import Graph.RSUNode;
import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class RequestFactory {
    List<Request> requestList;
    int creatingRequestTimeslot;    //timeslot in millisecond for creating new request
    int sendingRequestTimeslot;     //timeslot in millisecond for sending created requests
    int MAX;        //maximal number of requests in the requestList
    Timer creatingRequestTimer;
    Timer sendingRequestTimer;
    Node container;     //node that contains this request factory (a vehicle or RSU)

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public Node getContainer() {
        return container;
    }

    public void setContainer(Node container) {
        this.container = container;
    }

    public int getSendingRequestTimeslot() {
        return sendingRequestTimeslot;
    }

    public void setSendingRequestTimeslot(int sendingRequestTimeslot) {
        this.sendingRequestTimeslot = sendingRequestTimeslot;
    }

    public Timer getSendingRequestTimer() {
        return sendingRequestTimer;
    }

    public void setSendingRequestTimer(Timer sendingRequestTimer) {
        this.sendingRequestTimer = sendingRequestTimer;
    }

    public RequestFactory(){
        requestList = new ArrayList<>();
        creatingRequestTimeslot = 5000;
        sendingRequestTimeslot = 1000;
        MAX = 5;
    }

    public int getCreatingRequestTimeslot() {
        return creatingRequestTimeslot;
    }

    public void setCreatingRequestTimeslot(int creatingRequestTimeslot) {
        this.creatingRequestTimeslot = creatingRequestTimeslot;
    }

//    public static Request createRandomRSURequest(Node node){
//        Request request = new RSURequest();
//        request.setSource(node);
//        return request;
//    }
//
//    public static Request createRandomVehicleRequest(Node node){
//        Request request = new VehicleRequest();
//        request.setSource(node);
//        return request;
//    }

    public static Request createRandomRequest(Node node){
        Request request;
        if (node instanceof RSUNode) request = new RSURequest();
        else if (node instanceof Vehicle) {
            request = new VehicleRequest();
            ((VehicleRequest)request).setRandomLoad();
        }
        else return null;
        request.setSource(node);
        return request;
    }
    //remove the request
    public void removeRequest(int index){
        if (index >= requestList.size()) return;
        requestList.remove(index);
    }

//    public void startCreatingRSURequest(Node node){
//        if (timer == null){
//            timer = new Timer(timeslot, e -> {
//                if (requestList.size()<MAX){
//                    Request request = createRandomRSURequest(node);
//                    requestList.add(request);
//                    System.out.println("RSU request created by " + node.getId());
//                }
//            });
//        }
//        timer.start();
//    }
//    public void startCreatingVehicleRequest(Node node){
//        if (timer == null){
//            timer = new Timer(timeslot, e -> {
//                if (requestList.size()<MAX){
//                    Request request = createRandomVehicleRequest(node);
//                    requestList.add(request);
//                    System.out.println("Vehicle request created by " + node.getId());
//                }
//            });
//        }
//        timer.start();
//    }
    public void startCreatingRequest(Node node){
        if (creatingRequestTimer == null){
            creatingRequestTimer = new Timer(creatingRequestTimeslot, e -> {
                if (requestList.size()<MAX){
                    Request request = createRandomRequest(node);
                    requestList.add(request);
                    //System.out.println("Request created by " + node.getId());
                }
            });
        }
        creatingRequestTimer.start();
    }
    public void stopCreatingRequest(){
        creatingRequestTimer.stop();
    }

    public void startSendingRequests(){
        if (sendingRequestTimer == null){
            sendingRequestTimer = new Timer(sendingRequestTimeslot, e -> {
                if (container == null) {
                    sendingRequestTimer.stop();
                    return;
                }
                if (container instanceof Vehicle){
                    Vehicle vehicle = (Vehicle) container;
                    vehicle.sentRequest();
                    //System.out.println("Vehicle " + vehicle.getId() + " sends a request");
                }
//                else if (container instanceof RSUNode){
//                    RSUNode rsu = (RSUNode) container;
//                    rsu.startSendingRequest();
//                    //System.out.println("RSU " + rsu.getId() + " sends a request");
//                }


            });
        }
        sendingRequestTimer.start();
    }
    public void stopSendingRequests(){
        if (sendingRequestTimer == null) return;
        sendingRequestTimer.stop();
    }

}
