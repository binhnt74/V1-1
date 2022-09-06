package Graph;

import MovingObjects.Vehicle;
import PSO.*;
import Request.Request;
import Request.RequestFactory;

import java.awt.*;
import java.util.List;

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
        String st = "RSU " + String.valueOf(getId());
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
        if (rtTable == null) return;
        Swarm swarm = new Swarm();
//        Particle[] p = new Particle[50];
//        swarm.setParticles(p);

        int dimension = rtTable.getNumberOfNearVehicles();
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
        int i = 0;
        for (Node node:rtTable.getNearVehicleList().values()){
            loadAllocation[i].setWorkload(request.getWorkload());
            Vehicle v = (Vehicle) node;
            loadAllocation[i].setSpeed(((Vehicle) node).getKit().getProcessor().getSpeed());

        }

        FitnessFunctions.setLoadAllocation(loadAllocation);

        //swarm.pso(100, FitnessFunctions.sphereFF);
        double[] best_position = PSOGeneral.pso(50, swarm, FitnessFunctions.minMax);
        System.out.println("Best position returned: ");
        Particle.printArray(best_position);
    }
}
