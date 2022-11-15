package com.soeguet;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

public class Sandkasten {

    public static void main(String[] args) {

        File file = new File("src/main/resources/mp3/Yusuf Islam.mp3");

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
        
        int duration;
        try {
            AudioFile audioFile = AudioFileIO.read(new File("src/main/resources/mp3/Yusuf Islam.mp3"));
            duration = audioFile.getAudioHeader().getTrackLength();

            System.out.println("duration = " + duration);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
