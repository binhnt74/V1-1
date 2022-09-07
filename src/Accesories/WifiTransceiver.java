package Accesories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class WifiTransceiver {
//    static Random rand = new Random();
    double bandwidth;   //number of images transmitted/received per second
    String name;

    public WifiTransceiver(double bw){
        this.bandwidth = bw;
    }
    public WifiTransceiver(double minBW, double maxBW){
//        Random rand = new Random();
//        ImageProcessingKit.rand.setSeed(Timestamp.from(Instant.now()).getTime());
        this.bandwidth = minBW + Math.random()*(maxBW-minBW);
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
