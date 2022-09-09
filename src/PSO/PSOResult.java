package PSO;

import MyUtils.AllocationInfo;
import Request.Request;

import java.util.ArrayList;
import java.util.List;

public class PSOResult {

    List<AllocationInfo> allocationList;
    Request request;    //the request that requires this allocation

    public Request getRequest() {
        return request;
    }

    public List<AllocationInfo> getAllocationList() {
        return allocationList;
    }

    public void setAllocationList(List<AllocationInfo> allocationList) {
        this.allocationList = allocationList;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public PSOResult(){
        allocationList = new ArrayList<>();

    }
    public void addAllocationInfo(AllocationInfo info){
        if (info != null)
            allocationList.add(info);
    }

}
