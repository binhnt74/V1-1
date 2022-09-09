package Request;

import Graph.Node;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Thic class is responsible for managing of list of requests being processed by a node (vehicle)
public class RequestProcessingList {
    //Node ownerNode; //the node is processing the list of requests
    HashMap<Long, List<RequestProcessingDetails>> processingMap;  //List of requests being processed;
                                                            //each entry includes requestId and its requests
    HashMap<Long, Node> requesterList;  //List of node (requester) that issues the request id

    public HashMap<Long, List<RequestProcessingDetails>> getProcessingMap() {
        return processingMap;
    }

    public void setProcessingMap(HashMap<Long, List<RequestProcessingDetails>> processingMap) {
        this.processingMap = processingMap;
    }

    public HashMap<Long, Node> getRequesterList() {
        return requesterList;
    }

    public Node getRequester(Long requestId){
        return requesterList.get(requestId);
    }

    public void setRequesterList(HashMap<Long, Node> requesterList) {
        this.requesterList = requesterList;
    }

    public RequestProcessingList(){
        processingMap = new HashMap<>();
        requesterList = new HashMap<>();
    }

    public void addRequestProcessingDetail(long requestId, List<RequestProcessingDetails> requestProcessing){
        if (requestProcessing != null)
        processingMap.put(requestId, requestProcessing);
    }

    public void removeRequestProcessingDetail(long requestId){
        if (processingMap.containsKey(requestId))
            processingMap.remove(requestId);
    }

    //update state of processing request detail of requestId in current time
    public void updateState(long requestId, Node currentNode, long currentTime){
        if (processingMap.containsKey(requestId)){
            RequestProcessingDetails detail = null;
            List<RequestProcessingDetails> list = processingMap.get(requestId);
            for (RequestProcessingDetails det: list){
                if (det.getProcessorNode() == currentNode) {
                    detail = det;
                    break;
                }

            }
            if (detail == null) return;

            //if current time is before or equal start processing time
            if (currentTime<detail.getStartProcessingTime() + detail.getTransmissionDuration()) return;
            if (detail.getStartProcessingTime()+ detail.getTransmissionDuration()<=currentTime &&
                    currentTime < detail.getStartProcessingTime()+ detail.getTransmissionDuration()+detail.getProcessDuration()) {
                detail.setState(RequestProcessingDetails.PROCESSING_STATE.TRANSMISSION_DONE);
                detail.setTransmissionTime(currentTime);
            }
            else {
                detail.setState(RequestProcessingDetails.PROCESSING_STATE.PROCESS_DONE);
                detail.setProcessTime(currentTime);
            }

        }
    }

    public boolean isRequestDone(Long requestId){
        if (!processingMap.containsKey(requestId)) return false;
        List<RequestProcessingDetails> subTaskList = processingMap.get(requestId);
        for (RequestProcessingDetails subtask: subTaskList){
            if (subtask.getState() != RequestProcessingDetails.PROCESSING_STATE.PROCESS_DONE) return false;
        }
        return true;
    }

    public void addRequester(Long requestId, Node requester){
        requesterList.put(requestId,requester);
    }
    public void removeRequester(Long requestId){
        requesterList.remove(requestId);
    }
}
