package MyUtils;

import Graph.Node;

public class AllocationInfo {
    Node node;  //node that this info is allocated for. It is normally a vehicle
    //double portion; //portion of workload to process
    double transmissionDuration;
    double processDuration;

    public double getTransmissionDuration() {
        return transmissionDuration;
    }

    public void setTransmissionDuration(double transmissionDuration) {
        this.transmissionDuration = transmissionDuration;
    }

    public double getProcessDuration() {
        return processDuration;
    }

    public void setProcessDuration(double processDuration) {
        this.processDuration = processDuration;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

}
