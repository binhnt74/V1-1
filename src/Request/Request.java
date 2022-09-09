package Request;

import Graph.Node;

import java.sql.Timestamp;
import java.time.Instant;

public class Request {
    enum RequestStatus {SENT, RECEIVED, REJECTED}
    static long currentId = 0;

    static long setNextId() {
        currentId ++;
        return currentId-1;
    }

    long id;
    Node source;    //source of the request
    Node dest;      //destination of the request
    double workload;    //number of images to process
    Timestamp timestamp;
    RequestStatus status;

    public Request(){
        this.id = setNextId();
        timestamp = Timestamp.from(Instant.now());
        workload = 1000;
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public double getWorkload() {
        return workload;
    }

    public void setWorkload(double workload) {
        this.workload = workload;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }



}
