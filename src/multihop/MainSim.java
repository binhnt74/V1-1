package multihop;

import multihop.Constants;
//import multihop.RTable;
import multihop.*;
import multihop.node.NodeRSU;
import multihop.node.NodeVehicle;
import multihop.request.RequestBase;
import multihop.request.RequestRSU;
import multihop.request.RequestVehicle;
import multihop.util.AlgUtils;
import multihop.util.TopoUtils;
import multihop.util.TrafficUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainSim {

    static int TS = Constants.TS;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {

        /**
         * ------------------------------- Prams-------------------------------
         **/
        boolean DEBUG = true;
        boolean single;
        int hc = 2; // hop-count is number of path to a node

        /**
         * --- 1.Create topology---
         */

        // vehicle node
        int _m = 5, _n = 5;
        List<NodeVehicle> topo = new ArrayList<NodeVehicle>();
        topo = (List<NodeVehicle>) TopoUtils.createTopo(_m, _n, 10, Constants.TYPE.VEHICLE.ordinal());
        TopoUtils.updateTimeTopo(topo); // adding moving by time for Vehicle

        // RSU node
        _m = 3;
        _n = 3;
        List<NodeRSU> topoRSU = new ArrayList<NodeRSU>();
        topoRSU = (List<NodeRSU>) TopoUtils.createTopo(_m, _n, 20, Constants.TYPE.RSU.ordinal());

        // make connection child-parent-neighbor
        TopoUtils.setupTopoRSU(topoRSU, topo); // create neighbor
        TopoUtils.setupTopo(topo, topoRSU); // create neighbor

        /**
         * --- 2. Create N requests --- id workload request node time_start=time_arrival
         */
        Queue<RequestBase> reqPiority = new PriorityQueue<RequestBase>(); // store req by time and id
        reqPiority = TrafficUtils.createReqList(topo);

        // transfer queue to list (cz index in queue isn't right)
        List<RequestBase> req = new ArrayList<RequestBase>();
        while (reqPiority.size() != 0) {
            req.add(reqPiority.poll());
        }

        /**
         * Loop ts: 0 -- ts_1 -- ts_1+TS
         */
        final int nTS = Constants.TSIM - 1;

//		FileWriter listCWL;
//		listCWL = new FileWriter("topoPSO_cwl.txt");

        FileWriter fListReq; // list requests in each timeslot
        fListReq = new FileWriter("listREQ.txt");
        fListReq.write("\nTS=" + TS + " | nTS=" + nTS);
        fListReq.write("\nTS\tListReqs" + "\n");
        int testCase = 0;

        int[] testCaseList = { 1 }; // {1, 2, 6, 7 }
        for (Integer test : testCaseList) {
            testCase = test;
            for (int h = 1; h <= 1; h++) { // hopcount = 1 or 2
                hc = h;
                int opts = h == 1 ? 1 : 2; // hc=1 -> 1opts; hc=2 -> 2 opts
                // opts =1;
                for (int s = 0; s < opts; s++) {
                    single = s == 0 ? true : false; // single and multi opts

                    // clear node
                    for (NodeVehicle n : topo) {
                        n.getqReq().clear();
                        n.getDoneReq().clear();
                        n.setcWL(0);
                        n.setaWL(0);
                        // n.setpWL(0);
                    }

                    // single =false;

                    String o = single == true ? "s" : "m";

                    FileWriter myWriterPSO;
                    myWriterPSO = new FileWriter("topoPSO-" + hc + "." + o + "." + testCase + ".txt");

                    FileWriter myWriterPSOserv;
                    myWriterPSOserv = new FileWriter("topoPSO_tserv-" + hc + "." + o + "." + testCase + ".txt");

                    myWriterPSOserv.write("\n\n" + testCase + "." + hc + "." + single + "\n");
                    myWriterPSO.write("\n" + testCase + "." + hc + "." + single + "\n");

                    fListReq.write("\n" + testCase + "." + hc + "." + single + "\n");

                    for (int t = 1; t <= nTS; t++) {
                        System.out.println("\nts=" + t + " ----------------------------------------------------------");
                        double ts = TS * t;

                        /**
                         * --- 1. Create routing table---
                         */

                        HashMap<Integer, List<RTable>> mapRTable = new HashMap<Integer, List<RTable>>();
                        List<RTable> rtable = new ArrayList<RTable>();

                        // 1.1 prepare request to node in: ts_k < start < ts_k+1
                        debug("List REQs:\n ", DEBUG);
                        List<NodeVehicle> listNodeReq = new ArrayList<NodeVehicle>(); // node having reqs in ts
                        Queue<RequestBase> reqTS = new PriorityQueue<RequestBase>(); // reqTS having in ts

                        for (RequestBase r : req) {
                            double start = r.getTimeInit();
                            if ((start < ts) && (start >= (ts - TS))) {
                                listNodeReq.add(r.getSrcNode());
                                reqTS.add(r);
                                // debug(r.getSrcNode().getName() + "." + start + "\t", DEBUG);
                            }
                        }

                        // 1.2 updated create routing-table:rtable and routing-table-with-id:mapRTable
                        // with requests
                        for (RequestBase r : reqTS) {
                            List<RTable> rtableREQ = new ArrayList<RTable>(); // rtable of a request
                            int reqId = r.getId();
                            NodeVehicle reqNode = r.getSrcNode();
                            double WL = r.getWL();
                            // rtableREQ = createRoutingTable(topo, rtableREQ, r, listNodeReq, hc, single,
                            // t);
                            rtableREQ = TopoUtils.createRoutingTable(topo, rtableREQ, r, listNodeReq, hc, single, t);
                            rtable.addAll(rtableREQ); // merge all reqs
                            mapRTable.put(reqId, rtableREQ); // merge reqs with id
                        }

                        // TODO: create routing-table for RSU

                        /**
                         * --- 2. Run PSO ---
                         */

                        // if having req in queue
                        if (listNodeReq.size() != 0) {
                            // log list_reqs
                            fListReq.write(ts + "\t");
                            for (RequestBase r : reqTS) {
                                fListReq.write(r.getId() + "\t");
                            }
                            fListReq.write("\n");

                            System.out.println("\n***********PSO Running***********\n");

                            HashMap<Integer, Double> resultPSO = AlgUtils.getPSO(rtable, mapRTable, testCase, ts);
                            // get output of PSO-alg
                            Set<Integer> rID = resultPSO.keySet();
                            for (Integer id : rID) {
                                rtable.get(id).setRatio(resultPSO.get(id));
                            }

                            /**
                             * --- 3. Logging ---
                             */
                            calcTSerPSO(rtable, testCase);

                        }

                        /**
                         * --- 4. Process CWL and queue in node ---
                         */
                        // 4.1 assigned workload in node (all cWL) and adding queue
                        insertQ(topo, rtable, t);

                        // 4.2 update queue
                        updateQ(topo, ts);

                        // 4.2 calculate cWL
                        updateCWL(topo, ts);

                        // 4.3 moving to cloud

                        HashMap<Integer, List<RTable>> mapRTableRSU = new HashMap<Integer, List<RTable>>();
                        List<RTable> rtableRSU = new ArrayList<RTable>();

                        // 1.1 prepare request to node RSU base: moving-data
                        debug("List REQs:\n ", DEBUG);
                        List<NodeRSU> listNodeReqRSU = new ArrayList<NodeRSU>(); // node having reqs in ts
                        Queue<RequestRSU> reqTSRSU = new PriorityQueue<RequestRSU>(); // reqTS having in ts

                        // list node moving data and the requestID
                        for (NodeRSU n : topoRSU) {
                            if (n.getqReqV().peek() != null) {
                                listNodeReqRSU.add(n);
                            }
                        }
                        // listNodeReqRSU.forEach(l -> System.out.println("listNodeReqRSU: " +
                        // l.getName()));

                        // list request in a TS
                        for (NodeRSU n : topoRSU) {
                            for (RequestRSU rr : n.getqReqV()) { // multi req to 1 node
                                rr.setSrcNodeRSU(n);
                                reqTSRSU.add(rr);

                            }

                        }

                        for (RequestRSU r : reqTSRSU) {
                            List<RTable> rtableREQ = new ArrayList<RTable>(); // rtable of a request
                            int reqId = r.getId();
                            rtableREQ = TopoUtils.createRoutingTableRSU(topoRSU, r, listNodeReqRSU, hc, single, t);
                            rtableRSU.addAll(rtableREQ); // merge all reqs
                            mapRTableRSU.put(reqId, rtableREQ); // merge reqs with id
                        }

                        System.out.println("\n***********PSO Running RSU***********\n");

                        // HashMap<Integer, Double> resultPSORSU = AlgUtils.getPSO(rtableRSU,
                        // mapRTableRSU, testCase, ts);

                        HashMap<Integer, Double> resultPSORSU = AlgUtils.getPSORSU(rtableRSU, mapRTableRSU, testCase,
                                ts);

                        // get output of PSO-alg
                        Set<Integer> rID = resultPSORSU.keySet();
                        for (Integer id : rID) {
                            rtableRSU.get(id).setRatio(resultPSORSU.get(id));
                        }

                        // resultPSORSU.forEach((K, V) -> System.out.println(V));

                        int i = 0;
                        int[] id = new int[reqTSRSU.size()];
                        for (RequestRSU rr : reqTSRSU) {
                            double max = 0;
                            int i2 = 0;
                            for (RTable r : rtableRSU) {
                                if (r.getReq().getId() == rr.getId()) {
                                    if (r.getRatio() > max) {
                                        max = r.getRatio();
                                        id[i] = i2;
                                    }
                                    r.setRatio(0); // set all to 0
                                }
                                i2++;
                            }
                            i++;
                        }

                        for (Integer i2 : id) {
                            rtableRSU.get(i2).setRatio(1); // set max to 1
                        }

//						// done set ratio to rtable.

                        // TODO
                        calcTSerPSORSU(rtableRSU, testCase);

                        for (RTable r : rtableRSU) {
                            System.out.println(r.toStringRSU());
                        }

                        insertQRSU(topoRSU, rtableRSU, t);

//						System.out.println("TEST AFTER INSERT: ");
//						for (NodeRSU n : topoRSU) {
//							n.getqReq().forEach(nqr -> System.out.println(n.getName() + " : " + nqr.toString()));
//						}

                        updateQRSU(topoRSU, ts);

                        updateCWLRSU(topoRSU, ts);

                    } // endts
                    System.out.println("\n----- DONE REQ ------");
                    myWriterPSO.write("\nReqID\t" + "a(SrcNode)\t" + "i(DesNode)\t" + "k(Path)\t" + "p(Ratio)\t"
                            + "dtTrans\t" + "tArrival\t" + "start\t" + "end\t" + "timeSer(PSO)\t" + "t_wait\t"
                            + "t_proc\t" + "t_serv\t" + "moved_data" + "\n");

                    // log for vehicle topo
                    for (RequestBase r : req) {
                        // double endM=0;
                        for (NodeVehicle n : topo) {
                            for (RequestBase d : n.getDoneReq()) {
                                if (d.getId() == r.getId()) {
//									System.out.println("REQ: " + r.getId() + "\n" + "->" + n.getName() + "_"
//											+ d.getRoute() + ": " + d.getStart() + "\t" + d.getEnd());
                                    RequestVehicle dv = (RequestVehicle) d;
                                    double t_wait = dv.getStart() - dv.getTimeArrival();
                                    double t_proc = dv.getEnd() - dv.getStart();
                                    double t_serv = dv.getTimeTrans() + t_wait + t_proc;
                                    myWriterPSO.write(r.getId() + "\t" + dv.getSrcNode().getName() + "\t" + n.getName()
                                            + "\t" + dv.getRoute() + "\t" + dv.getRatio() + "\t" + dv.getTimeTrans()
                                            + "\t" + dv.getTimeArrival() + "\t" + dv.getStart() + "\t" + dv.getEnd()
                                            + "\t" + dv.getTimeSer() + "\t" + t_wait + "\t" + t_proc + "\t" + t_serv
                                            + "\t" + dv.getMovedData() + "\n");

                                }
                            }
                        }
                    }

                    myWriterPSO.write("\n RSU TOPO \n");
                    myWriterPSO.write("\nReqID\t" + "a(SrcNode)\t" + "i(DesNode)\t" + "k(Path)\t" + "p(Ratio)\t"
                            + "dtTrans\t" + "tArrival\t" + "start\t" + "end\t" + "timeSer(PSO)\t" + "t_wait\t"
                            + "t_proc\t" + "t_serv\t" + "moved_data\t" + "t_trans_vr\t" + "t_end_real" + "\n");

                    // log for RSU topo
                    for (RequestBase r : req) {
                        for (NodeRSU n : topoRSU) {
                            for (RequestBase d : n.getDoneReq()) {
                                if (d.getId() == r.getId()) {
                                    RequestRSU dv = (RequestRSU) d;
                                    double t_wait = dv.getStart() - dv.getTimeArrival();
                                    double t_proc = dv.getEnd() - dv.getStart();
                                    double t_serv = dv.getTimeTrans() + t_wait + t_proc;
                                    //double t_trans_vr = dv.getWL()/Constants.BW;
                                    double t_trans_vr = dv.getTimeVR();


                                    myWriterPSO.write(r.getId() + "\t" + dv.getSrcNode().getName() + "-"
                                            + dv.getSrcNodeRSU().getName() + "\t" + n.getName() + "\t" + dv.getRoute()
                                            + "\t" + dv.getRatio() + "\t" + dv.getTimeTrans() + "\t"
                                            + dv.getTimeArrival() + "\t" + dv.getStart() + "\t" + dv.getEnd() + "\t"
                                            + dv.getTimeSer() + "\t" + t_wait + "\t" + t_proc + "\t" + t_serv + "\t"
                                            + dv.getMovedData() + "\t" + t_trans_vr + "\t"  +
                                            "\n");

                                }
                            }
                        }

                    }

                    // calc avg
                    double wait = 0;
                    int count = 0;
                    for (RequestBase r : req) {
                        double endM = 0;
                        for (NodeVehicle n : topo) {
                            for (RequestBase d : n.getDoneReq()) {
                                if (d.getId() == r.getId()) {
                                    endM = endM > ((RequestVehicle) d).getEnd() ? endM : ((RequestVehicle) d).getEnd();
                                    wait += (((RequestVehicle) d).getStart() - ((RequestVehicle) d).getTimeArrival());
                                    count++;
                                }
                            }
                        }

                        // endM -= Math.floor(((RequestVehicle) r).getTimeArrival());
                        endM -= Math.floor(r.getTimeInit());
                        myWriterPSOserv.write("\n" + r.getId() + "\t" + endM);
                    }
//					System.out.println("AVG: " + wait / count);


                    myWriterPSOserv.write("\n RSU logging\n");
                    for (RequestBase r : req) {
                        double endM = 0;
                        for (NodeRSU n : topoRSU) {
                            for (RequestBase d : n.getDoneReq()) {
                                if (d.getId() == r.getId()) {
                                    endM = endM > ((RequestRSU) d).getEnd() ? endM : ((RequestRSU) d).getEnd();
                                    wait += (((RequestRSU) d).getStart() - ((RequestRSU) d).getTimeArrival());
                                    count++;
                                }
                            }
                        }

                        // endM -= Math.floor(((RequestVehicle) r).getTimeArrival());
                        endM -= Math.floor(r.getTimeInit());
                        myWriterPSOserv.write("\n" + r.getId() + "\t" + endM);
                    }




                    myWriterPSOserv.close();
                    myWriterPSO.close();
                } // end for each case

            }
        }
        System.out.println("----FINISH-----");
        fListReq.close();
//		listCWL.close();
//		myWriterPSOserv.close();
//		myWriterPSO.close();
    }

    private static void updateCWL(List<NodeVehicle> topo, double ts) {
        // System.out.println("\n----- Current WL: ");
        // listCWL.write("p");
        for (NodeVehicle n : topo) {
            double pWL = 0; // processed workload
            // double aWL = 0; // all assigned worload
            if ((n.getqReq().size() != 0) || n.getDoneReq().size() != 0) { // node in processing
//				System.out.println("Node " + n.getName());
//					n.getDoneReq().forEach((d) -> {
//						System.out.println("Done req: " + d.getStart() + " " + d.getEnd());
//					});
//					n.getqReq().forEach((q) -> System.out.println("Queue req: " + q.getStart() + " " + q.getEnd()));

                for (RequestBase d : n.getDoneReq()) {
                    pWL += (((RequestVehicle) d).getEnd() - ((RequestVehicle) d).getStart()) * n.getRes();
                }

                if (n.getqReq().peek() != null) {
                    double lastStart = ((RequestVehicle) n.getqReq().peek()).getStart();
                    if (lastStart < ts) {
                        pWL += (ts - lastStart) * n.getRes();
                    }
                }
//				System.out.print("Process WL: " + pWL + " / " + n.getaWL());
                n.setcWL((n.getaWL() - pWL) < 0 ? 0 : (n.getaWL() - pWL));
//				System.out.println("\tcWL: " + n.getcWL());
            }
            // listCWL.write(pWL + "\t");

//		if (listNodeReq.contains(n)) {
//			listCWL.write("\n" + t + "\t" + n.getId() + "\t" + n.getcWL());
//			if (n.getcWL()>0) {n.setcWL(0);}
//		}

        }

    }

    private static void updateCWLRSU(List<NodeRSU> topoRSU, double ts) {
        System.out.println("\n----- Current WL: ");
        // listCWL.write("p");
        for (NodeRSU n : topoRSU) {
            double pWL = 0; // processed workload
            // double aWL = 0; // all assigned worload
            if ((n.getqReq().size() != 0) || n.getDoneReq().size() != 0) { // node in processing
//				System.out.println("Node " + n.getName());
//					n.getDoneReq().forEach((d) -> {
//						System.out.println("Done req: " + d.getStart() + " " + d.getEnd());
//					});
//					n.getqReq().forEach((q) -> System.out.println("Queue req: " + q.getStart() + " " + q.getEnd()));

                for (RequestBase d : n.getDoneReq()) {
                    pWL += (((RequestRSU) d).getEnd() - ((RequestRSU) d).getStart()) * n.getRes();
                }

                if (n.getqReq().peek() != null) {
                    double lastStart = ((RequestRSU) n.getqReq().peek()).getStart();
                    if (lastStart < ts) {
                        pWL += (ts - lastStart) * n.getRes();
                    }
                }
//				System.out.print("Process WL: " + pWL + " / " + n.getaWL());
                n.setCWL((n.getaWL() - pWL) < 0 ? 0 : (n.getaWL() - pWL));
//				System.out.println("\tcWL: " + n.getcWL());
            }
            // listCWL.write(pWL + "\t");

//		if (listNodeReq.contains(n)) {
//			listCWL.write("\n" + t + "\t" + n.getId() + "\t" + n.getcWL());
//			if (n.getcWL()>0) {n.setcWL(0);}
//		}

        }
    }

    private static void updateQ(List<NodeVehicle> topo, double ts) {
        // System.out.println("\nUPDATE QUEUE");
        for (NodeVehicle n : topo) {
            boolean check = true;
            while (check && (n.getqReq().peek() != null)) {
                RequestVehicle rv = (RequestVehicle) n.getqReq().peek();
                double start1 = rv.getStart();
                double end1 = rv.getEnd();
                // System.out.print("Node: " + n.getName());
                // System.out.println(" REQ: " + start1 + " -> " + end1);
                check = false;
                if (end1 < ts) {
                    n.getqReq().peek().setDone(true);
                    n.getDoneReq().add(n.getqReq().peek()); // adding to done req
                    // System.out.println("doneREQ: " + n.getqReq().peek().getStart() + " -> " +
                    // end1);
                    n.getqReq().remove(); // req is done, removing
                    RequestVehicle nextReq = (RequestVehicle) n.getqReq().peek(); // update next request
                    // if data sent
                    if (nextReq != null) {
                        if (end1 > nextReq.getStart()) {
                            // System.out.println("update next req start at: " + end1);
                            ((RequestVehicle) n.getqReq().peek()).setStart(end1);
                            ((RequestVehicle) n.getqReq().peek())
                                    .setEnd(end1 + ((RequestVehicle) n.getqReq().peek()).getTimeProcess());
                            check = true;
                        } else if (end1 < nextReq.getStart() && nextReq.getStart() < ts) {
                            check = true;
                        }
                    }

                }
            }

        }
    }

    private static void updateQRSU(List<NodeRSU> topoRSU, double ts) {
        System.out.println("\nUPDATE QUEUE RSU");
        for (NodeRSU n : topoRSU) {
            boolean check = true;
            while (check && (n.getqReq().peek() != null)) {
                RequestRSU rv = (RequestRSU) n.getqReq().peek();
                double start1 = rv.getStart();
                double end1 = rv.getEnd();
                System.out.print(n.getName() + " ");
                System.out.println(rv.toString() + " " + start1 + " -> " + end1);
                check = false;
                if (end1 < ts) {
                    n.getqReq().peek().setDone(true);
                    n.getDoneReq().add(n.getqReq().peek()); // adding to done req
                    System.out.println("doneREQ: " + n.getqReq().peek().getStart() + " -> " + end1);
                    n.getqReq().remove(); // req is done, removing
                    RequestRSU nextReq = (RequestRSU) n.getqReq().peek(); // update next request
                    // if data sent
                    if (nextReq != null) {
                        if (end1 > nextReq.getStart()) {
                            // System.out.println("update next req start at: " + end1);
                            ((RequestRSU) n.getqReq().peek()).setStart(end1);
                            ((RequestRSU) n.getqReq().peek())
                                    .setEnd(end1 + ((RequestRSU) n.getqReq().peek()).getTimeProcess());
                            check = true;
                        } else if (end1 < nextReq.getStart() && nextReq.getStart() < ts) {
                            check = true;
                        }
                    }

                }
            }

        }
    }

    private static void insertQ(List<NodeVehicle> topo, List<RTable> rtable, int t) {
        // System.out.println("\nCALC assigned new workload and ADD new reqs to queue");
        // listCWL.write("a");
        for (NodeVehicle n : topo) {
            double aWL = 0; // all assigned workload as new-workload
            boolean check = false;
            for (RTable r : rtable) {
                double move_data = 0;
                if (r.getDes().equals(n.getName())) {
                    aWL += r.getRatio() * r.getReq().getWL();
                    double t_process = r.getRatio() * r.getReq().getWL() / r.getResource();

                    // calc moving data
                    if (r.getDes().equals(r.getReq().getSrcNode().getName())) { // source request node
                        move_data = aWL - Constants.RES[Constants.TYPE.VEHICLE.ordinal()];
                        if (move_data > 0) {
                            t_process = 1;
                            // listCWL.write("\n" + t + "\t" + n.getId() + "\t" + move_data);
                            aWL = Constants.RES[Constants.TYPE.VEHICLE.ordinal()];
                        } else {
                            move_data = 0;
                        }

                    }

                    // adding queue
                    double start = r.getTimeTrans() + (t - 1) * TS; // the arrival task to Node, PSO at
                    // ts
                    // double start = r.getTimeTrans()+(t)*TS;
                    NodeVehicle srcNode = r.getReq().getSrcNode();
                    RequestBase rq = r.getReq();

                    RequestVehicle rv = new RequestVehicle(rq.getId(), rq.getWL(), rq.getSrcNode(), rq.getTimeInit(),
                            rq.isDone(), n.getId(), r.getRoute(), start, t_process, r.getRatio(), r.getTimeTrans(),
                            start, (start + t_process), r.getTimeSer(), move_data);

                    n.getqReq().add(rv);

                    // moving to nodeRSU
                    if (r.getDes().equals(r.getReq().getSrcNode().getName())) {
                        double dtVR = 0;
                        // dt from V to R (fix 1hop and same BW)
                        // need increase BW (V-R)
                        dtVR = r.getRatio() * r.getReq().getWL() / Constants.BW;
                        RequestRSU rr = new RequestRSU(rv);
                        rr.setTimeVR(dtVR);
                        //System.out.println(rv.getTimeArrival());
                        Vector<NodeRSU> vnr = n.getNodeParent().get(t);
                        if (!vnr.isEmpty()) {
                            NodeRSU nr = n.getNodeParent().get(t).get(0);
                            nr.getqReqV().add(rr); // adding req to first parent
                            System.out.println(n.getName() + " move ->" + nr.getName() + " " + rr.toString());
                        } else {
                            System.out.println("NO RSU TO TRANSFER DATA, NODE: " + n.getName());
                        }
                    }

                    check = true;
                }
            }

            n.setaWL(n.getaWL() + aWL);
        }

    }

    private static void insertQRSU(List<NodeRSU> topoRSU, List<RTable> rtable, int t) {
        System.out.println("\ninsertQRSU");

        for (NodeRSU n : topoRSU) {
            n.getqReqV().clear(); // remove reqs from vehicle
            double aWL = 0; // all assigned workload as new-workload
            boolean check = false;
            for (RTable r : rtable) {
                double move_data = 0;
                if (r.getDes().equals(n.getName())) {
                    RequestRSU rq = (RequestRSU) r.getReq();
                    if (r.getRatio() == 1) {
                        aWL += r.getRatio() * r.getReq().getWL();
                        double t_process = r.getRatio() * r.getReq().getWL() / r.getResource();
                        // adding queue
                        double start = r.getTimeTrans() + (t - 1) * TS;
                        start+= rq.getTimeVR();

                        rq.setRatio(r.getRatio());
                        rq.setStart(start);
                        rq.setTimeArrival(start);
                        rq.setTimeProcess(t_process);
                        rq.setEnd(start + t_process);
                        rq.setTimeTrans(r.getTimeTrans());
                        rq.setTimeSer(r.getTimeSer());
                        rq.setRoute(r.getRoute());
                        n.getqReq().add(rq);
                        System.out.println("Insert to: " + n.getName() + " <- " + rq.toString());
                        n.getqReq().forEach(nqr->System.out.println(n.getName() + " : " +
                                nqr.toString()));
                    }
                    check = true;
                }

            }

            n.setaWL(n.getaWL() + aWL);

            // n.getqReq().forEach(nqr->System.out.println(n.getName() + " : " +
            // nqr.toString()));

        }

//		System.out.println("TEST AFTER INSERT: ");
//		for (NodeRSU n : topoRSU) {
//		n.getqReq().forEach(nqr->System.out.println(n.getName() + " : " + nqr.toString()));
//		}

    }

    private static void calcTSerPSO(List<RTable> rtable, int testCase) {
        // 3.1 t_ser based PSO in rtable
        for (RTable r : rtable) {
            double compute = 0;
            double trans = 0;
            double workLoad = r.getReq().getWL();
            double subWL = r.getRatio() * workLoad; // new WL
            double totalWL = subWL + r.getcWL(); // adding cWL
            compute = totalWL / r.getResource(); // totalTime

            if (testCase != 2) {
                // calc t_process for all paths to node ~ including t_wait
                for (RTable r2 : rtable) {
                    if (r2.getDes().equals(r.getDes())
                            && (r2.getId() != r.getId() || (r2.getReq().getId() != r.getReq().getId()))) {
                        // adding route 2hop-2path
                        compute += r2.getRatio() * r2.getReq().getWL() / r2.getResource();
                        subWL += r2.getRatio() * r2.getReq().getWL(); // adding newWL route2

                    }
                }
            }

            trans = (r.getRatio() * workLoad / Constants.BW) * r.getHop();

            if (r.getId() == 0) {
                trans = 0;
            }
            ;

            r.setTimeCompute(compute);
            r.setTimeTrans(trans);

            double ser = compute + trans;
            r.setTimeSer(ser);

        } // END 3.1: LOG TIME IN RTABLE

    }

    private static void calcTSerPSORSU(List<RTable> rtable, int testCase) {
        // 3.1 t_ser based PSO in rtable
        for (RTable r : rtable) {
            double compute = 0;
            double trans = 0;
            double trans_rv=0;
            double workLoad = r.getReq().getWL();
            double subWL = r.getRatio() * workLoad; // new WL
            double totalWL = subWL + r.getcWL(); // adding cWL
            compute = totalWL / r.getResource(); // totalTime

            if (testCase != 2) {
                // calc t_process for all paths to node ~ including t_wait
                for (RTable r2 : rtable) {
                    if (r2.getDes().equals(r.getDes())
                            && (r2.getId() != r.getId() || (r2.getReq().getId() != r.getReq().getId()))) {
                        // adding route 2hop-2path
                        compute += r2.getRatio() * r2.getReq().getWL() / r2.getResource();
                        subWL += r2.getRatio() * r2.getReq().getWL(); // adding newWL route2

                    }
                }
            }

            trans = (r.getRatio() * workLoad / Constants.BW) * r.getHop();


            if (r.getId() == 0) {
                trans = 0;
            }
            ;

            RequestRSU rr = (RequestRSU) r.getReq();
            //trans += rr.getTimeVR(); // adding time trans VR

            r.setTimeCompute(compute);
            r.setTimeTrans(trans);

            double ser = compute + trans;
            r.setTimeSer(ser);

        } // END 3.1: LOG TIME IN RTABLE

    }

    private static void debug(String s, boolean mode) {
        if (mode)
            System.out.println(s);
    }

}
