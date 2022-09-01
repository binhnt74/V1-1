package Graph;

import Routing.RoutingTable;
import Topology.Topo;

public class NodeWithRoutingTable extends Node{
    RoutingTable rtTable;   //routing table for this vehicle

    public NodeWithRoutingTable(int id) {
        super(id);
    }

    public NodeWithRoutingTable(int id, double x, double y) {
        super(id, x, y);
    }

    public RoutingTable getRtTable() {
        return rtTable;
    }

    public void setRtTable(RoutingTable rtTable) {
        this.rtTable = rtTable;
    }

    public void updateRoutingTable(Topo topo){
        if (rtTable == null){
            rtTable = new RoutingTable();
            rtTable.setGraph(topo.getGraph());
            rtTable.setContainer(this);
        }
        rtTable.updateRoutingTable(topo.getVehiclesList());
    }
}
