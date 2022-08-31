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
    Topo urbanTopo;

    public MainWind(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        Container panel = getContentPane();
        JButton startStopBut = new JButton("Start running vehicles");
        startStopBut.addActionListener(e -> {
            if (!runningState){
                urbanTopo.run();
                runningState = true;
                startStopBut.setText("Stop");
            }
            else
            {
                urbanTopo.stop();
                runningState = false;
                startStopBut.setText("Start");
            }

        });
        JButton routingBut = new JButton("Start updating RT");
        routingBut.addActionListener(e -> {
            if (!routingState){
                urbanTopo.startUpdatingRoutingTable();
                routingState = true;
                routingBut.setText("Stop updating RT");
            }
            else
            {
                urbanTopo.stopUpdatingRoutingTable();
                routingState = false;
                routingBut.setText("Start updating RT");
            }

        });
//        panel.add(startStopBut, BorderLayout.LINE_END);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        add(buttonPanel,BorderLayout.EAST);
        buttonPanel.add(startStopBut);
        //buttonPanel.add(Box.createVerticalGlue());
        Component space = Box.createRigidArea(new Dimension(0,10));
        buttonPanel.add(space);
        buttonPanel.add(routingBut);
        buttonPanel.add(space);
        JButton exitBut = new JButton("Exit");
        buttonPanel.add(exitBut);

        //panel.add(graphPanel, BorderLayout.CENTER);
        urbanTopo = TopoFactory.createUrbanTopo();
        add(urbanTopo.getTopoPanel(),BorderLayout.CENTER);
        //graphPanel.setGraph(g2);
        urbanTopo.getTopoPanel().setMainWind(this);

        pack();
        //graphPanel.repaint();
        //graphPanel.draw();
        setVisible(true);
        //graphPanel.run();
        runningState = false;
        urbanTopo.show();

    }
    public static void main(String[] args){
        new MainWind();

    }

}
