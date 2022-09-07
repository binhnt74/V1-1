package Graph;

import MovingObjects.Vehicle;
import MyUtils.Logging;
import PSO.*;
import Request.Request;
import Request.RequestFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RSUNode extends NodeWithRoutingTable{
    double width;
    double height;
    RequestFactory requestFactory;

    public RSUNode(int id) {
        super(id);
        commInit();
    }

    public RSUNode(int id, int x, int y) {
        super(id, x, y);
        commInit();
    }

    void commInit(){
        width = height = 25;
        backgroundColor = Color.RED;
        frontColor = Color.BLUE;
    }

    @Override
    public void draw(){
        //System.out.println("Drawing oval node...");
        if (graphics ==null){
            System.out.println("Graphic system is null");
            return;
        }
        graphics.setColor(backgroundColor);
        graphics.fillOval((int)(x-width/2),(int)(y-height/2),(int)width,(int)height);

        Font font = graphics.getFont();
        int fsize = font.getSize();
        String st = "RSU " + getId();
        graphics.setColor(frontColor);
        graphics.drawString(st,((int)getX() ) - fsize * st.length() / 4, ((int)getY()) + fsize / 2);

        if (getRtTable()!=null){
            //System.out.println("Drawing range circle");
            graphics.setColor(Color.RED);
            int vRange = (int)(getRtTable().getRange()/Graph.getScale());
            graphics.drawOval((int)getX()-vRange,(int)getY()-vRange, 2*vRange, 2*vRange);
        }

    }

    public void startCreatingRequests(){
        if (requestFactory == null){
            requestFactory = new RequestFactory();
        }
        requestFactory.startCreatingRequest(this);
    }

    public void stopCreatingRequests(){
        if (requestFactory == null) return;
        requestFactory.stopCreatingRequest();
    }

    public void startSendingRequest(){

    }
    public void stopSendingRequest(){

    }
    public void processRequest(Request request){
        System.out.println("Processing request from vehicle " + request.getSource().getId() +
                " sent to " + request.getDest().getId() + " at " + request.getTimestamp());
        System.out.println("Workload required " + request.getWorkload());

        if (rtTable == null) return;
        //if (!(request.getSource() instanceof Vehicle)) return;

        Swarm swarm = new Swarm();
//        Particle[] p = new Particle[50];
//        swarm.setParticles(p);

        List<Vehicle> vehicleList = rtTable.getNearVehicleList(request.getSource().getId());

        if (vehicleList == null) vehicleList = new ArrayList<>();

        vehicleList.add((Vehicle) request.getSource());

        int dimension = vehicleList.size();
        //List<Node> nearVehicleList = rtTable.getNearVehicleList().;

        int N = 50; //number of particles
        Particle[] p = new Portion[N];
        for (int i = 0; i < N; i++) {
            p[i] = new Portion(dimension);

        }

        swarm.setParticles(p);

        swarm.InitSwampOfParticles(dimension,-0.1,0.1);
        //swarm.printPosition();

        LoadAllocation[] loadAllocation = new LoadAllocation[N];
        for (int i = 0; i < N; i++) {
            loadAllocation[i] = new LoadAllocation();
            //loadAllocation[i].setNodeId(request.getDest().getId());
            //
            //loadAllocation[i].initRandom();
        }



        for (int i = 0; i< vehicleList.size(); i++ ){
            loadAllocation[i].setWorkload(request.getWorkload());
            //Vehicle v = (Vehicle) node;
            loadAllocation[i].setSpeed(vehicleList.get(i).getKit().getProcessor().getSpeed());
            Vehicle source = (Vehicle)request.getSource();
            //RSUNode dest = (RSUNode)request.getDest();
            loadAllocation[i].setBandwidth(Math.min(vehicleList.get(i).getKit().getWifiHub().getBandwidth(), source.getKit().getWifiHub().getBandwidth()));

        }

//        System.out.print("Near vehicle list: ");
//        String st="";
//        for (Vehicle v:vehicleList){
//            st += v.getId() + " ";
//        }
//        System.out.println(st);
//
//        System.out.println("Allocation ");
//        for (int i = 0;i<dimension;i++){
//            System.out.println("BW: " +loadAllocation[i].getBandwidth() + ", SP: " + loadAllocation[i].getSpeed());
//        }

        FitnessFunctions.setLoadAllocation(loadAllocation);

        //swarm.pso(100, FitnessFunctions.sphereFF);
        double[] best_position = PSOGeneral.pso(40, swarm, FitnessFunctions.minMax);

//        Logging.logger.log(Level.INFO, "Best position returned:");
        System.out.println("Best position returned: ");
        if (best_position != null) {
            Particle.printArray(best_position);
        }
//        Logging.logger.log(Level.INFO, "Best value returned: "+FitnessFunctions.minMax.apply(best_position) + "(s)");
        System.out.println("Best value returned: "+FitnessFunctions.minMax.apply(best_position) + "(s)\n");

    }
}
