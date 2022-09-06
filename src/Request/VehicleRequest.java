package Request;

import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRequest extends Request {
    List<Vehicle> nearVehicleList;

    public VehicleRequest(){
        super();
        nearVehicleList = new ArrayList<>();
    }

    public List<Vehicle> getNearVehicleList() {
        return nearVehicleList;
    }

    public void setNearVehicleList(List<Vehicle> nearVehicleList) {
        this.nearVehicleList = nearVehicleList;
    }
}
