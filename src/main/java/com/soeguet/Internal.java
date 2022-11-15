package com.soeguet;

import javax.swing.*;

public class Internal extends JOptionPane {

    public Internal() {


        JButton jButton = new JButton();
        jButton.setName("TEST");
        add(jButton);
        setBounds(50,50,100,100);
        setVisible(true);
        requestFocus();
        setOpaque(false);

        System.out.println("INTERNAL!");
    }
}
