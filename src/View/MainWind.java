package View;

import Graph.GraphDraw;
import Topology.TopoFactory;
import Topology.Topo;

import javax.swing.*;
import java.awt.*;

public class MainWind extends JFrame {
    GraphDraw graphPanel;
    boolean runningState;
    Topo urbanTopo;

    public MainWind(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        Container panel = getContentPane();
        JButton startStopBut = new JButton("Start");
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
//        panel.add(startStopBut, BorderLayout.LINE_END);




        add(startStopBut, BorderLayout.EAST);
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
