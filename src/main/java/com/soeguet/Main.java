package com.soeguet;

import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;

import javax.swing.*;

public class Main extends JFrame {

    public Main() {

        System.out.println("Thread.currentThread() = " + Thread.currentThread());
        System.out.println("Thread.State.RUNNABLE = " + Thread.State.RUNNABLE);

        System.out.println("Thread.activeCount() = " + Thread.activeCount());

        FlatXcodeDarkIJTheme.setup();



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Bremen - Namaz Vakitleri");
        setResizable(false);

        new EzanRahmen(this);
    }

    public static void main(String[] args) {

        new Main();
    }
}