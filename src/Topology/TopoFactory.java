package Topology;

import Graph.*;
import MovingObjects.Vehicle;

import java.util.Random;

public class TopoFactory {
    public static Graph createGraph(){
        Graph graph = new Graph(10, 12);

        int scale = 120;
        graph.setScale(scale);
        //init graph
        Node node;
        node = graph.getNode(1);
        node.setX(1*scale);node.setY(2*scale);

        node = graph.getNode(2);
        node.setX(1*scale);node.setY(3*scale);

        node = graph.getNode(3);
        node.setX(2*scale);node.setY(2*scale);

        node = graph.getNode(4);
        node.zoom(1.5);
        node.setX(2*scale);node.setY(3*scale);

        node = graph.getNode(5);
        node.setX(2*scale);node.setY(1*scale);

        node = graph.getNode(6);
        node.setX(3*scale);node.setY(1*scale);

        node = graph.getNode(7);
        node.zoom(2.0);
        node.setX(4*scale);node.setY(1*scale);

        node = graph.getNode(8);
        node.setX(4*scale);node.setY(2*scale);

        node = graph.getNode(9);
        node.zoom(0.6);
        node.setX(4*scale);node.setY(3*scale);

        node = graph.getNode(10);
        node.setX(3*scale);node.setY(3*scale);

        graph.getEdge(1).setSource(graph.getNode(1));
        graph.getEdge(1).setDest(graph.getNode(2));
        graph.getEdge(2).setSource(graph.getNode(1));
        graph.getEdge(2).setDest(graph.getNode(3));
        graph.getEdge(3).setSource(graph.getNode(2));
        graph.getEdge(3).setDest(graph.getNode(3));
        graph.getEdge(4).setSource(graph.getNode(2));
        graph.getEdge(4).setDest(graph.getNode(4));
        graph.getEdge(5).setSource(graph.getNode(1));
        graph.getEdge(5).setDest(graph.getNode(5));
        graph.getEdge(6).setSource(graph.getNode(5));
        graph.getEdge(6).setDest(graph.getNode(3));
        graph.getEdge(5).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(6).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(7).setSource(graph.getNode(5));
        graph.getEdge(7).setDest(graph.getNode(6));
        graph.getEdge(8).setSource(graph.getNode(6));
        graph.getEdge(8).setDest(graph.getNode(7));
        graph.getEdge(9).setSource(graph.getNode(7));
        graph.getEdge(9).setDest(graph.getNode(8));
        graph.getEdge(10).setSource(graph.getNode(3));
        graph.getEdge(10).setDest(graph.getNode(10));
        graph.getEdge(11).setSource(graph.getNode(6));
        graph.getEdge(11).setDest(graph.getNode(10));

        return graph;

    }

    public static Topo createUrbanTopo(){
        int n = 5;
        Graph graph = new Graph(n*n, 2*(n-1)*n);
        Topo topo = new Topo();
        topo.setGraph(graph);

        int scaleX = 120;
        int scaleY = 120;
        graph.setScale(scaleX);
        //graph.setScaleY(scaleY);

        //init graph
        for (int i = 1; i <= n; i++) {
            Node node;
            for (int j = 1; j <= n; j++) {
                node = graph.getNode(n*(i-1)+j);
                node.setX((i-1)*scaleX+30);
                node.setY((j-1)*scaleY+30);
            }
        }
        int edgeId = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n-1; j++) {
                graph.getEdge(edgeId).setSource(graph.getNode(n*(i-1)+j));
                graph.getEdge(edgeId).setDest(graph.getNode(n*(i-1)+j+1));
                edgeId++;
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n-1; j++) {
                graph.getEdge(edgeId).setSource(graph.getNode(i+n*(j-1)));
                graph.getEdge(edgeId).setDest(graph.getNode(i+n*j));
                edgeId++;
            }
        }
        Edge edge;
        for (int i = 29; i <= 32; i++) {
            edge = graph.getEdge(i);
            if (edge!=null) {
                if (edge instanceof LineEdge){
                    LineEdge lineEdge = (LineEdge) edge;
                    lineEdge.setWidth(10);
                    //lineEdge.setEdgeType(EdgeType.DIRECTED);

                }
            }
        }
        for (int i = 9; i <= 12; i++) {
            edge = graph.getEdge(i);
            if (edge!=null) {
                if (edge instanceof LineEdge){
                    LineEdge lineEdge = (LineEdge) edge;
                    lineEdge.setWidth(10);
                    //lineEdge.setEdgeType(EdgeType.DIRECTED);
                }
            }
        }

        graph.getEdge(25).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(26).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(27).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(28).setEdgeType(EdgeType.DIRECTED);
        graph.getEdge(33).setEdgeType(EdgeType.DIRECTED);
//        graph.getEdge(33).setSource(graph.getNode(9));
//        graph.getEdge(33).setDest(graph.getNode(4));

//        graph.getEdge(34).setSource(graph.getNode(14));
//        graph.getEdge(34).setDest(graph.getNode(9));
        graph.getEdge(34).setEdgeType(EdgeType.DIRECTED);

//        graph.getEdge(35).setSource(graph.getNode(19));
//        graph.getEdge(35).setDest(graph.getNode(14));
        graph.getEdge(35).setEdgeType(EdgeType.DIRECTED);

//        graph.getEdge(36).setSource(graph.getNode(24));
//        graph.getEdge(36).setDest(graph.getNode(19));
        graph.getEdge(36).setEdgeType(EdgeType.DIRECTED);

        edge = new LineEdge(41);
        edge.setSource(graph.getNode(6));
        edge.setDest(graph.getNode(2));
        graph.addEdge(edge);

        edge = new LineEdge(42);
        edge.setSource(graph.getNode(4));
        edge.setDest(graph.getNode(10));
        graph.addEdge(edge);

        edge = new LineEdge(43);
        edge.setSource(graph.getNode(22));
        edge.setDest(graph.getNode(16));
        graph.addEdge(edge);

        edge = new LineEdge(44);
        edge.setSource(graph.getNode(20));
        edge.setDest(graph.getNode(24));
        graph.addEdge(edge);

//        edge = new ArcEdge(50);
//        edge.setSource(graph.getNode(8));
//        edge.setDest(graph.getNode(12));
//        graph.addEdge(edge);
//
//        edge = new ArcEdge(51);
//        edge.setSource(graph.getNode(12));
//        edge.setDest(graph.getNode(18));
//        graph.addEdge(edge);

        int N = 10;
        Vehicle v[] = new Vehicle[N];

        for (int i = 0; i < N; i++) {
            v[i] = new Vehicle(200+i);
            topo.addVehicle(v[i]);
            graph.addNode(v[i]);
            //v[i].setId(200+i);
        }

//        v[0].putTo(graph.getEdge(41),0.2);
//        v[0].setCurrentContainingEdge(graph.getEdge(41));

        //v5.setSpeed(2*v5.getSpeed());
        //v[0].run();

        for (int i = 0; i < N; i++) {
            Random rand = new Random();
            int eNumber = 1+rand.nextInt(40);
            v[i].putTo(graph.getEdge(eNumber),0.5);
            v[i].setCurrentContainingEdge(graph.getEdge(eNumber));
            //addVehicle(v[i]);
            //graph.addNode(v[i]);
        }
        for (int i = 0; i < N/3; i++) {
            v[i].setRealSpeed(14);  //14m/s ~ 50km/h
        }
        for (int i = N/3; i < 2*N/3; i++) {
            v[i].setRealSpeed(28);  //28m/s ~ 100km/h
        }
        for (int i = 2*N/3; i < N; i++) {
            v[i].setRealSpeed(56);  //56m/s ~ 200km/h
        }
        v[0].putTo(graph.getNode(1));
        v[0].setCurrentContainingEdge(graph.getEdge(21));

        v[3].putTo(graph.getNode(2));
        v[3].setCurrentContainingEdge(graph.getEdge(25));

        v[7].putTo(graph.getNode(3));
        v[7].setCurrentContainingEdge(graph.getEdge(29));

//        for (int i = 10; i < N-10; i++) {
//            v[i].setRealSpeed(14);  //speed 14m/s ~ 50km/s
//
//        }
//        v[N-8].setRealSpeed(28);    //speed 28m/s ~ 100km/s
//
//        v[N-7].setRealSpeed(35);    //speed 35m/s ~ 150km/s
//
//        v[N-7].setRealSpeed(56);    //speed 56m/s ~ 200km/s

        int k = 4;
        RSUNode rsuNodes[] = new RSUNode[k];
        for (int i = 0; i < k; i++) {
            rsuNodes[i] = new RSUNode(300+i);
            //rsuNodes[i].getRtTable().setRange(rsuNodes[i].getRtTable().getRange()*2);
        }
//        rsuNodes[0] = new RSUNode(300);
//        rsuNodes[1] = new RSUNode(301);
//        rsuNodes[2] = new RSUNode(302);
//        rsuNodes[3] = new RSUNode(303);

        rsuNodes[0].putTo(graph.getNode(7));
        rsuNodes[1].putTo(graph.getNode(9));
        rsuNodes[2].putTo(graph.getNode(17));
        rsuNodes[3].putTo(graph.getNode(19));

        for (int i = 0; i < k; i++) {
            graph.addNode(rsuNodes[i]);
        }

        //topo.setGraph(graph);
        return topo;

    }

    public static Topo createHighwayTopo(){
        int n = 4;
        Graph graph = new Graph(2*n, 2*n+2);
        Topo topo = new Topo();
        topo.setGraph(graph);

        int scale = 150;
        graph.setScale(scale);

        Node node;

        node = graph.getNode(1);
        node.setX(30);node.setY(30);

        node = graph.getNode(2);
        node.setX(30);node.setY(30+scale/3);

        node = graph.getNode(3);
        node.setX(30);node.setY(30+scale);

        node = graph.getNode(4);
        node.setX(30);node.setY(30+4*scale/3);

        node = graph.getNode(5);
        node.setX(30+3*scale);node.setY(30);

        node = graph.getNode(6);
        node.setX(30+3*scale);node.setY(30+scale/3);

        node = graph.getNode(7);
        node.setX(30+3*scale);node.setY(30+scale);

        node = graph.getNode(8);
        node.setX(30+3*scale);node.setY(30+4*scale/3);



        Edge edge;
        edge = graph.getEdge(1);
        edge.setSource(graph.getNode(1));
        edge.setDest(graph.getNode(5));
        edge.setEdgeType(EdgeType.DIRECTED);

        edge = graph.getEdge(2);
        edge.setSource(graph.getNode(2));
        edge.setDest(graph.getNode(6));
        edge.setEdgeType(EdgeType.DIRECTED);

        edge = graph.getEdge(3);
        edge.setSource(graph.getNode(7));
        edge.setDest(graph.getNode(3));
        edge.setEdgeType(EdgeType.DIRECTED);

        edge = graph.getEdge(4);
        edge.setSource(graph.getNode(8));
        edge.setDest(graph.getNode(4));
        edge.setEdgeType(EdgeType.DIRECTED);

        edge = graph.getEdge(5);
        edge.setSource(graph.getNode(1));
        edge.setDest(graph.getNode(2));

        edge = graph.getEdge(6);
        edge.setSource(graph.getNode(5));
        edge.setDest(graph.getNode(6));

        edge = graph.getEdge(7);
        edge.setSource(graph.getNode(3));
        edge.setDest(graph.getNode(4));

        edge = graph.getEdge(8);
        edge.setSource(graph.getNode(8));
        edge.setDest(graph.getNode(7));

        edge = graph.getEdge(9);
        edge.setSource(graph.getNode(2));
        edge.setDest(graph.getNode(3));

        edge = graph.getEdge(10);
        edge.setSource(graph.getNode(6));
        edge.setDest(graph.getNode(7));

        ArcEdge arcEdge = new ArcEdge(11);
        arcEdge.setSource(graph.getNode(6));
        arcEdge.setDest(graph.getNode(7));
        graph.addEdge(arcEdge);

        arcEdge = new ArcEdge(12);
        arcEdge.setSource(graph.getNode(2));
        arcEdge.setDest(graph.getNode(3));
        graph.addEdge(arcEdge);

        int k = 2;
        RSUNode rsuNodes[] = new RSUNode[k];
        rsuNodes[0] = new RSUNode(30);
        rsuNodes[1] = new RSUNode(31);


        rsuNodes[0].setX(30+3*scale/2);
        rsuNodes[0].setY(30+scale/6);
        rsuNodes[1].setX(30+3*scale/2);
        rsuNodes[1].setY(30+7*scale/6);
        //rsuNodes[1].putTo(graph.getNode(12));

        for (int i = 0; i < k; i++) {
            graph.addNode(rsuNodes[i]);
        }

        int N = 8;
        Vehicle v[] = new Vehicle[N];

        for (int i = 0; i < N; i++) {
            v[i] = new Vehicle(200+i);
            topo.addVehicle(v[i]);
            graph.addNode(v[i]);
            v[i].setRealSpeed(56);  //56m/s ~ 200km/h
            //v[i].setId(200+i);
        }
        v[0].putTo(graph.getEdge(1),0.8);
        v[0].setCurrentContainingEdge(graph.getEdge(1));

        v[1].putTo(graph.getEdge(1),0.9);
        v[1].setCurrentContainingEdge(graph.getEdge(1));

        v[2].putTo(graph.getEdge(2),0.8);
        v[2].setCurrentContainingEdge(graph.getEdge(2));

        v[3].putTo(graph.getEdge(2),0.9);
        v[3].setCurrentContainingEdge(graph.getEdge(2));

        v[4].putTo(graph.getEdge(3),0.8);
        v[4].setCurrentContainingEdge(graph.getEdge(3));

        v[5].putTo(graph.getEdge(3),0.9);
        v[5].setCurrentContainingEdge(graph.getEdge(3));

        v[6].putTo(graph.getEdge(4),0.1);
        v[6].setCurrentContainingEdge(graph.getEdge(4));

        v[7].putTo(graph.getEdge(4),0.2);
        v[7].setCurrentContainingEdge(graph.getEdge(4));

        return topo;
    }
}
