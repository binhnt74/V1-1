package Request;

import Graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//This class is charging of sending and receiving requests/responses
public class Broker {
    static HashMap<Integer, List<Request>> requestLists = new HashMap<>();   //Lists of requests for node ids
    static HashMap<Integer, List<Response>> responseLists = new HashMap<>(); //Lists of responses for node ids

    public Broker() {
        requestLists = new HashMap<>();
        responseLists = new HashMap<>();
    }

    //send the request to the broker
    //this request must have already a source and destination
    public static void sendRequest (Request request){
        if (request == null) return;
        int id = request.getDest().getId();
        if (requestLists == null) return;
        if (requestLists.containsKey(id)){
            requestLists.get(id).add(request);
        }
        else {
            List<Request> list = new ArrayList<>();
            list.add(request);
            requestLists.put(id, list);
        }
    }

    //return list of requests that sent to the node
    public static List<Request> receiveRequest(Node node){
        if (node ==null) return null;
        if (!requestLists.containsKey(node.getId())) return null;
        List<Request> reqList = new ArrayList<>();
        for (Request req: requestLists.get(node.getId()))
            reqList.add(req);
        requestLists.remove(node.getId());
        return reqList;
    }
}
