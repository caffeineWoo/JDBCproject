//Generated by GuiGenie - Copyright (c) 2004 Mario Awad.
//Home Page http://guigenie.cjb.net - Check often for new versions!

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class UI extends JPanel {
    private JLabel jcomp1;
    private JCheckBox jcomp2;
    private JCheckBox jcomp3;

    public UI() {
        //construct components
        jcomp1 = new JLabel ("테스트");
        jcomp2 = new JCheckBox ("EMPLOYEE");
        jcomp3 = new JCheckBox ("newCheckBox");

        //adjust size and set layout
        setPreferredSize (new Dimension (721, 427));
        setLayout (null);

        //add components
        add (jcomp1);
        add (jcomp2);
        add (jcomp3);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (25, 25, 100, 25);
        jcomp2.setBounds (90, 25, 100, 25);
        jcomp3.setBounds (195, 25, 100, 25);
    }


    public static void main (String[] args) {
        JFrame frame = new JFrame ("UI");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new UI());
        frame.pack();
        frame.setVisible (true);
    }
}
