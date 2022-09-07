package Accesories;

import java.util.Random;

//Image Processing Kit
public class ImageProcessingKit {
    public static Random rand = new Random();
    ImageProcessor processor;
    WifiTransceiver wifiHub;

    public ImageProcessingKit(){
        processor = new ImageProcessor(30, 300);
        wifiHub = new WifiTransceiver(300, 3000);
    }

    public ImageProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(ImageProcessor processor) {
        this.processor = processor;
    }

    public WifiTransceiver getWifiHub() {
        return wifiHub;
    }

    public void setWifiHub(WifiTransceiver wifiHub) {
        this.wifiHub = wifiHub;
    }
}
