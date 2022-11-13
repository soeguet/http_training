package com.soeguet;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;

public class Sandkasten {

    public static void main(String[] args) {

        File file = new File("Yusuf Islam.mp3");

        BasicPlayer basicPlayer = new BasicPlayer();
        try {
            basicPlayer.open(file);
        }
        catch (BasicPlayerException e) {
            throw new RuntimeException(e);
        }

        try {
            basicPlayer.play();
        }
        catch (BasicPlayerException e) {
            throw new RuntimeException(e);
        }
    }

}
