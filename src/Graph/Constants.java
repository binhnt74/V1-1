package Graph;

public class Constants {
    public static final double COINCIDE_LIMIT = 4.5;
    public static final int TIMESLOT = 200;  //in millisecond
    public static final int UPD_RT_TIMESLOT = 50;  //TS of updating routing table in millisecond
    public static final int REQUEST_TIMESLOT = 1000;  //TS of creating new request in millisecond
    public static final int PROCESS_REQUEST_TIMESLOT = 500;  //TS of processing requests sent in millisecond
    public enum MOVING_DIRECTION {RIGHT, OPPOSITE}   //Right: from source to dest node, Opposite: the opposite
}
