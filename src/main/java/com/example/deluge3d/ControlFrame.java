package com.example.deluge3d;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

public class ControlFrame extends JFrame{
    private JButton runButton = new JButton("Stop");
    private JRadioButton waterComingButton = new JRadioButton("Water is coming");
    private JRadioButton waterNotComingButton = new JRadioButton("Water is not coming");
    private JTextField minLevelField = new JTextField("Min. level:  "+String.format("%.3f",Settings.minLevel), 5);
    private JCheckBox PermeabilityOfBoundaries = new JCheckBox("PermeabilityOfBoundaries", false);
    private JButton exitButton = new JButton("Exit");

    public ControlFrame() {
        super("ControlFrame");
        this.setBounds(Settings.widthOfFrame,0,400,200);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));
        container.add(runButton);
        container.add(exitButton);


        ButtonGroup waterComingGroup = new ButtonGroup();
        waterComingGroup.add(waterComingButton);
        waterComingGroup.add(waterNotComingButton);

        container.add(waterComingButton);
        waterComingButton.setSelected(true);
        container.add(waterNotComingButton);
        container.add(PermeabilityOfBoundaries);
        PermeabilityOfBoundaries.setSelected(Settings.PermeabilityOfBoundaries);
        runButton.addActionListener(new runButtonEventListener());
        exitButton.addActionListener(new exitButtonEventListener());
        PermeabilityOfBoundaries.addActionListener(new PermeabilityOfBoundariesEventListener());
        minLevelField.addActionListener(new minLevelFieldEventListener());
        //container.add(minLevelField);
        this.setVisible(true);
    }

    public boolean waterComing(){
        return waterComingButton.isSelected();
    }

    class runButtonEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
                if (Objects.equals(runButton.getText(), "Stop")) {
                    runButton.setText("Run");
                    Main.stopTimer();
                }
                else if (Objects.equals(runButton.getText(), "Run")){
                    runButton.setText("Stop");
                    Main.RunTimer();
            }
        }
    }

    class exitButtonEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class PermeabilityOfBoundariesEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Settings.PermeabilityOfBoundaries = PermeabilityOfBoundaries.isSelected();
        }
    }

    class minLevelFieldEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Settings.minLevel = Float.parseFloat(minLevelField.getText().split(":")[1]);
        }
    }
}
