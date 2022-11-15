package com.soeguet;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.swing.*;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Sandkasten {

    public static void main(String[] args) {

        System.out.println(LocalTime.now().compareTo(LocalTime.parse("22:41")) == 1);
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse("19:03"))));
        JOptionPane.showMessageDialog(null,"NAMAZ OLDU", "ALARM",JOptionPane.INFORMATION_MESSAGE);
    }

}
