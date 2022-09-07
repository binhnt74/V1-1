package Accesories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class ImageProcessor {

    double speed;   //number of images processed per second
    String name;

    public ImageProcessor(double speed){
        this.speed = speed;
    }

    public ImageProcessor(double minSpeed, double maxSpeed){
//        Random rand = new Random();
        //ImageProcessingKit.rand.setSeed(Timestamp.from(Instant.now()).getTime());
        this.speed = minSpeed + Math.random()*(maxSpeed-minSpeed);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
