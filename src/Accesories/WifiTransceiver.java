package Accesories;

public class WifiTransceiver {
    double bandwidth;   //number of images transmitted/received per second
    String name;

    public WifiTransceiver(double speed){
        this.bandwidth = speed;
    }
    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
