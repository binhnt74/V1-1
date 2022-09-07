package Request;

import MovingObjects.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VehicleRequest extends Request {
    List<Vehicle> nearVehicleList;

    public VehicleRequest(){
        super();
        nearVehicleList = new ArrayList<>();
    }

    public void setRandomLoad(){
        Random rand = new Random();
        setWorkload(50 + rand.nextInt(2000));
    }

    public List<Vehicle> getNearVehicleList() {
        return nearVehicleList;
    }

    public void setNearVehicleList(List<Vehicle> nearVehicleList) {
        this.nearVehicleList = nearVehicleList;
    }
}
