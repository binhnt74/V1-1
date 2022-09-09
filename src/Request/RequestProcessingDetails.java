package Request;

import Graph.Node;

import java.sql.Timestamp;
import java.time.Instant;

public class RequestProcessingDetails {
    enum PROCESSING_STATE {STARTED, TRANSMISSION_DONE, TRANSMISSION_FAIL, PROCESS_DONE, PROCESS_FAIL}    //
    Node processorNode; //node that processes this detail (normally a vehicle)

    //long requestId;
    long startProcessingTime;

    public Node getProcessorNode() {
        return processorNode;
    }

    public void setProcessorNode(Node processorNode) {
        this.processorNode = processorNode;
    }

    long transmissionDuration;    //in theory in millisecond
    long transmissionTime;     //time to finish transmission in reality
    long processDuration;
    long processTime;          //time to finish processing in reality
    PROCESSING_STATE state;
    public RequestProcessingDetails(){
        startProcessingTime = System.currentTimeMillis();
        state = PROCESSING_STATE.STARTED;
    }

    public long getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(long transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public PROCESSING_STATE getState() {
        return state;
    }

    public void setState(PROCESSING_STATE state) {
        this.state = state;
    }

    public long getStartProcessingTime() {
        return startProcessingTime;
    }

    public void setStartProcessingTime(long startProcessingTime) {
        this.startProcessingTime = startProcessingTime;
    }

    public double getTransmissionDuration() {
        return transmissionDuration;
    }

    public void setTransmissionDuration(long transmissionDuration) {
        this.transmissionDuration = transmissionDuration;
    }

    public double getProcessDuration() {
        return processDuration;
    }

    public void setProcessDuration(long processDuration) {
        this.processDuration = processDuration;
    }


}
