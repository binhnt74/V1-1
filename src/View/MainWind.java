package View;

import Graph.GraphDraw;
import Topology.TopoFactory;
import Topology.Topo;

import javax.swing.*;
import java.awt.*;

public class MainWind extends JFrame {
    GraphDraw graphPanel;
    boolean runningState;
    boolean routingState;
    boolean createRequestState;
    boolean sendingRequestState;
    boolean processingRequestState;

    JPanel canvas;

    Topo topo;
    Topo urbanTopo, highwayTopo;

    public MainWind(){
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        Container panel = getContentPane();
        JButton startStopBut = new JButton("Start running vehicles");
        startStopBut.addActionListener(e -> {
            if (!runningState){
                topo.run();
                runningState = true;
                startStopBut.setText("Stop running vehicles");
            }
            else
            {
                topo.stop();
                runningState = false;
                startStopBut.setText("Start running vehicles");
            }

        });
        JButton routingBut = new JButton("Start updating RT");
        routingBut.addActionListener(e -> {
            if (!routingState){
                topo.startUpdatingRoutingTable();
                routingState = true;
                routingBut.setText("Stop updating RT");
            }
            else
            {
                topo.stopUpdatingRoutingTable();
                routingState = false;
                routingBut.setText("Start updating RT");
            }

        });
        JButton requestBut = new JButton("Start creating requests");
        requestBut.addActionListener(e -> {
            if (!createRequestState){
                topo.startCreatingRequests();
                requestBut.setText("Stop creating requests");
                createRequestState = true;
            }
            else {
                topo.stopCreatingRequests();
                requestBut.setText("Start creating requests");
                createRequestState = false;
            }

        });
        JButton sendingBut = new JButton("Start sending requests");
        sendingBut.addActionListener(e -> {
            if (!sendingRequestState){
                topo.startSendingRequests();
                sendingBut.setText("Stop sending requests");
                sendingRequestState = true;
            }
            else {
                topo.stopSendingRequests();
                sendingBut.setText("Start sending requests");
                sendingRequestState = false;
            }
        });
//        panel.add(startStopBut, BorderLayout.LINE_END);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        add(buttonPanel,BorderLayout.EAST);
        JLabel selectTopo = new JLabel("Select topo:");
        buttonPanel.add(selectTopo);

        JRadioButton urbanRBut = new JRadioButton("Urban");
        JRadioButton highwayRBut = new JRadioButton("Highway");
        buttonPanel.add(urbanRBut);
        buttonPanel.add(highwayRBut);

        urbanRBut.setActionCommand("Urban");
        urbanRBut.addActionListener(e -> {
            if (urbanTopo == null)
            {
                urbanTopo = TopoFactory.createUrbanTopo();
                urbanTopo.getTopoPanel().setMainWind(this);
            }
            topo = urbanTopo;
            topo.show();
            remove(canvas);
            canvas = topo.getTopoPanel();
            add(canvas,BorderLayout.CENTER);
            pack();
            canvas.repaint();
            //System.out.println("Urban button pressed");
            //repaint();
        });


        highwayRBut.setActionCommand("Highway");

        highwayRBut.addActionListener(e -> {
            if (highwayTopo == null)
            {
                highwayTopo = TopoFactory.createHighwayTopo();
                highwayTopo.getTopoPanel().setMainWind(this);
            }
            topo = highwayTopo;
            topo.show();
            remove(canvas);
            canvas = topo.getTopoPanel();
            add(canvas,BorderLayout.CENTER);
            pack();
            canvas.repaint();
            //System.out.println("Highway button pressed");
            //repaint();
        });
//        highwayRBut.doClick();
//        highwayRBut.setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(urbanRBut);
        buttonGroup.add(highwayRBut);

        JButton processRequestsBut = new JButton("Start processing requests");
        processRequestsBut.addActionListener(e -> {
            if (!processingRequestState){
                topo.startProcessingRequests();
                processRequestsBut.setText("Stop processing requests");
                processingRequestState = true;
            }
            else {
                topo.stopProcessingRequests();
                processRequestsBut.setText("Start processing requests");
                processingRequestState = false;
            }
        });

        buttonPanel.add(startStopBut);
        //buttonPanel.add(Box.createVerticalGlue());
        Component spaces[] = new Component[10];

        for (int i = 0; i < 10; i++) {
            spaces[i] = Box.createRigidArea(new Dimension(0,10));
        }

        buttonPanel.add(spaces[0]);
        buttonPanel.add(routingBut);

        buttonPanel.add(spaces[1]);
        buttonPanel.add(requestBut);

        buttonPanel.add(spaces[2]);
        buttonPanel.add(sendingBut);

        buttonPanel.add(spaces[3]);
        buttonPanel.add(processRequestsBut);

        buttonPanel.add(spaces[4]);
        JButton exitBut = new JButton("Exit");
        exitBut.addActionListener(e -> {
            System.exit(0);
        });
        buttonPanel.add(exitBut);

        //panel.add(graphPanel, BorderLayout.CENTER);
        //topo = TopoFactory.createUrbanTopo();
        //canvas = new JPanel();
        canvas = new JPanel();
        canvas.setPreferredSize(new Dimension(400,400));
        add(canvas,BorderLayout.CENTER);
        //graphPanel.setGraph(g2);
        //topo.getTopoPanel().setMainWind(this);


        pack();
        //graphPanel.repaint();
        //graphPanel.draw();
        setVisible(true);
        //graphPanel.run();
        runningState = false;

        urbanRBut.doClick();
        urbanRBut.setSelected(true);

        //topo.show();
        //System.out.println("Constructor done!");

    }
    public static void main(String[] args){
        new MainWind();

    }

}
