package com.soeguet;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class Overlay extends JDialog {

    private final BasicPlayer basicPlayer;
    private AdvancedPlayer overlayPlayer;
    private JProgressBar progressBar;
    private JPanel panel1;
    private JButton stopButton;
    private JButton pauseButton;
    private JButton abspielenButton;
    private Thread overlayThread;
    private int time;
    private Timer timer;

    public Overlay() {

        mp3TotalTime();

        setContentPane(panel1);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        basicPlayer = new BasicPlayer();

        ////
        abspielenButton.addActionListener(actionEvent -> {

            overlayThread = new Thread(() -> {

                System.out.println("neuer PLAY-Thread");

                try {

                    basicPlayer.open(new File("Yusuf Islam.mp3"));
                    basicPlayer.play();
                    basicPlayer.setGain(0.85);
                }
                catch (BasicPlayerException e) {
                    throw new RuntimeException(e);
                }
            });
            overlayThread.start();

            ActionListener taskPerformer = evt -> {

                System.out.println("basicPlayer.getStatus() = " + basicPlayer.getStatus());
                if (basicPlayer.getStatus() == 0) {

                    progressBar.setValue(time);
                    time++;
                } else if (basicPlayer.getStatus() == 2) {

                    time = 0;

                    timer.stop();
                }
            };

            timer = new Timer(1_000, taskPerformer);
            timer.start();
        });

        stopButton.addActionListener(actionEvent -> {

            System.out.println("neuer stoppthread!");

            try {
                basicPlayer.stop();
            }
            catch (BasicPlayerException e) {
                throw new RuntimeException(e);
            }
            dispose();

            System.out.println("Thread.currentThread() = " + Thread.currentThread());
            System.out.println("Thread.State.RUNNABLE = " + Thread.State.RUNNABLE);

            System.out.println("Thread.activeCount() = " + Thread.activeCount());
        });

        pauseButton.addActionListener(actionEvent -> {

            System.out.println("pausenbutton");

            System.out.println("neuer pausenthread!");

            System.out.println("basicPlayer.getStatus() = " + basicPlayer.getStatus());

            if (basicPlayer.getStatus() == 0) {

                try {
                    basicPlayer.pause();
                }
                catch (BasicPlayerException e) {
                    throw new RuntimeException(e);
                }
            } else {

                try {
                    basicPlayer.resume();
                }
                catch (BasicPlayerException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("basicPlayer.getStatus2() = " + basicPlayer.getStatus());
        });
    }

    private void mp3TotalTime() {

        int duration = 0;

        try {
            AudioFile audioFile = AudioFileIO.read(new File("Yusuf Islam.mp3"));
            duration = audioFile.getAudioHeader().getTrackLength();

            System.out.println("duration = " + duration);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        progressBar.setMaximum(duration);
    }

    public Thread getOverlayThread() {

        return overlayThread;
    }

    public void setOverlayThread(Thread overlayThread) {

        this.overlayThread = overlayThread;
    }
}
