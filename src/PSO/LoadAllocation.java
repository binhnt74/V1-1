package PSO;

import java.util.Random;

public class LoadAllocation {
    int nodeId;
    double portion;     //percentage of workload allocated
    double bandwidth;
    double speed;
    double workload;

    public LoadAllocation(){
        bandwidth = 20;
        speed = 100;
        workload = 1000;
    }

    public int getNodeId() {
        return nodeId;
    }

        public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWorkload() {
        return workload;
    }

    public void setWorkload(double workload) {
        this.workload = workload;
    }

    public void initRandom(){
        Random random = new Random();
        speed = 20;  //random.nextInt(1000);
        bandwidth = 100; //2 + random.nextInt(100);
    }
}
