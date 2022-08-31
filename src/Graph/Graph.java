package Graph;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Node> nodeList = new ArrayList<Node>();
    //List<Vehicle> vehicleList;
    List<Edge> edgeList = new ArrayList<Edge>();
    double scale;  //number of metres per point
    //double scaleY;

    public Graph(int numberOfNodes) {
        for (int i = 1; i <= numberOfNodes; i++) {
            Node node;
            if (i % 2 == 0) node = new OvalNode(i);
            else node = new RectangleNode(i);
            nodeList.add(node);
        }
        scale = 100;

    }

    public Graph(int numberOfNodes, int numberOfEdges) {
        for (int i = 1; i <= numberOfNodes; i++) {
            Node node;
            if (i % 2 == 0) node = new OvalNode(i);
            else node = new RectangleNode(i);
            nodeList.add(node);
        }
        for (int i = 1; i <= numberOfEdges; i++) {
            edgeList.add(new LineEdge(i));
        }
        scale = 100;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

        public Node getNode(int id) {
        int i = 0;
        while (i < nodeList.size() && nodeList.get(i).getId() != id) {
            i++;
        }
        if (i < nodeList.size()) return nodeList.get(i);
        else return null;
    }

    public Edge getEdge(int id) {
        int i = 0;
        while (i < edgeList.size() && edgeList.get(i).getId() != id) {
            i++;
        }
        if (i < edgeList.size()) return edgeList.get(i);
        else return null;
    }

    //checking weather or not the node a is adjacent to node b, means that b->a is an edge
    //the opposite may not be true
    boolean checkAdjacentNodes(Node a, Node b) {
        if (a == null || b == null) return false;
        if (edgeList.size() == 0) return false;
        int i = 0;
        Edge ed;
        while (i < edgeList.size()) {
            ed = edgeList.get(i);
            if (ed.getEdgeType() == EdgeType.UNDIRECTED) {
                if ((ed.getSource() == a && ed.getDest() == b) ||
                        (ed.getSource() == b && ed.getDest() == a))
                    return true;
            } else {
                if (ed.getSource() == b && ed.getDest() == a) return true;
            }

            i++;
        }
        return false;

    }

    public List<Node> getAdjacentNodes(int nodeId) {
        List<Node> list = new ArrayList<>();
        Node curNode = getNode(nodeId);
        if (curNode == null) return null;
        //int i;
        for (Node node : nodeList)
            if (checkAdjacentNodes(node, curNode))
                list.add(node);
        return list;
    }

    public void print() {
        System.out.println("Graph info:");
        System.out.println("Number of nodes:" + nodeList.size());
        if (nodeList.size() > 0) {
            System.out.println("List of nodes:");
            for (Node node : nodeList) {
                System.out.println(node.getId() + " ");

            }
        }
        System.out.println("Number of edges:" + edgeList.size());
        if (edgeList.size() > 0) {
            System.out.println("List of edges:");
            for (Edge edge : edgeList) {
                System.out.println(edge.getId() + " ");

            }
        }
    }

    public void draw() {
        JFrame mainWind = new JFrame("Topology of city");
        mainWind.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GraphDraw panel = new GraphDraw();
        mainWind.add(panel);
        mainWind.pack();
        panel.setGraph(this);
        panel.setMainWind(mainWind);
        //frame.setSize(300, 400);
        mainWind.setVisible(true);
        panel.run();
    }

    public void addNode(Node node) {
        if (node == null) return;
        if (getNode(node.getId()) != null) {
            System.out.println("The node with this id already exists");
            return;
        }
        node.setCurrentGraph(this);
        nodeList.add(node);
    }

    public void addEdge(Edge edge) {
        if (edge == null) return;
        if (getEdge(edge.getId()) != null) {
            System.out.println("The edge with this id already exists");
            return;
        }
        edgeList.add(edge);

    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    //return the adjacent edges of the node
    public List<Edge> getAdjacentEdges(Node node) {
        List<Edge> eList = new ArrayList<Edge>();
        if (node == null) return eList;
        for (Edge edge : edgeList) {
            //System.out.println("Checking adjacent..");
            if (edge.getSource() == node) eList.add(edge);
            else if (edge.getDest() == node && edge.getEdgeType() == EdgeType.UNDIRECTED) eList.add(edge);
        }
        return eList;
    }
    //distance between two nodes
    public double distance(Node node1, Node node2){
        Point2D point1 = new Point2D.Double(node1.getX(), node1.getY());
        Point2D point2 = new Point2D.Double(node2.getX(), node2.getY());
        return scale*point1.distance(point2);
    }
}
