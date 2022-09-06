package Accesories;

public class ImageProcessor {
    double speed;   //number of images processed per second
    String name;

    public ImageProcessor(double speed){
        this.speed = speed;
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
