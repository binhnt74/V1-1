package Accesories;

//Image Processing Kit
public class ImageProcessingKit {
    ImageProcessor processor;
    WifiTransceiver wifiHub;

    public ImageProcessingKit(){
        processor = new ImageProcessor(100);
        wifiHub = new WifiTransceiver(10);
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
