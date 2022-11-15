package com.soeguet;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.swing.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.soeguet.Mp3Files.yusufIslam;

public class Overlay {

    private BasicPlayer basicPlayer;
    private JProgressBar progressBar;
    private JPanel panel1;
    private JButton stopButton;
    private JButton pauseButton;
    private JButton abspielenButton;
    private Thread overlayThread;
    private int time;
    private Timer timer;
    private int duration = 0;
    private JFrame jFrame;
    private EzanRahmen ezanRahmen;

    public Overlay() {

        ersteAufgaben();
    }

    public Overlay(JFrame jFrame) {

        this.jFrame = jFrame;
        ersteAufgaben();
    }

    private void ersteAufgaben() {

        System.out.println("Thread.currentThread() = " + Thread.currentThread());
        System.out.println("Thread.State.RUNNABLE = " + Thread.State.RUNNABLE);

        System.out.println("Thread.activeCount() = " + Thread.activeCount());

        //Max Zeit des geladenen MP3
        mp3TotalTime();

        //Fenster aufbauen
        fensterInit();

        /*
        Mechanismen
        */
        //Listener
        fensterListener();
        abspielenButtonListener();
        stopButtonListener();
        pausenButtonListener();
    }

    private void fensterInit() {

        jFrame.setContentPane(panel1);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        basicPlayer = new BasicPlayer();
    }

    private void fensterListener() {

//        this.addWindowListener(new WindowAdapter() {
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//
//                timer.cancel();
//                timer.purge();
//
//                try {
//                    basicPlayer.stop();
//                }
//                catch (BasicPlayerException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
    }

    private void abspielenButtonListener() {

        abspielenButton.addActionListener(actionEvent -> {

            overlayThread = new Thread(() -> {

                System.out.println("neuer PLAY-Thread");

                try {

                    basicPlayer.open(new File(yusufIslam));
                    basicPlayer.play();
                    basicPlayer.setGain(0.85);
                }
                catch (BasicPlayerException e) {
                    JOptionPane.showMessageDialog(null, "die angeforderte MP3 ist nicht auffindbar");
                    throw new RuntimeException(e);
                }
            });
            overlayThread.start();

            if (timer != null) {
                time = 0;
                timer.cancel();
                timer.purge();
            }
            timerStarten();
        });
    }

    private void timerStarten() {

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {

                System.out.println("basicPlayer.getStatus() = " + basicPlayer.getStatus());

                if (basicPlayer.getStatus() == 0) {

                    progressBar.setString(String.format("%02d:%02d / %02d:%02d", (time / 60), (time % 60), (duration / 60), (duration % 60)));
                    progressBar.setValue(time);
                    time++;
                } else if (basicPlayer.getStatus() == 2) {

                    time = 0;

                    timer.cancel();
                    timer.purge();
                    overlayThread.interrupt();
                }

                System.out.println("Thread.currentThread() = " + Thread.currentThread());
            }
        };

        timer = new Timer("meinTimer");
        timer.schedule(timerTask, 0, 1_000);
    }

    private void stopButtonListener() {

        stopButton.addActionListener(actionEvent -> {

            System.out.println("neuer stoppthread!");

            try {
                basicPlayer.stop();
            }
            catch (BasicPlayerException e) {
                throw new RuntimeException(e);
            }

            if (timer != null) {

                timer.cancel();
                timer.purge();

            }

            if (overlayThread != null) {

                overlayThread.interrupt();
            }

            System.out.println("Thread.currentThread() = " + Thread.currentThread());
            System.out.println("Thread.State.RUNNABLE = " + Thread.State.RUNNABLE);

            System.out.println("Thread.activeCount() = " + Thread.activeCount());




            if (ezanRahmen != null) {

                ezanRahmen.setjFrame(jFrame);
            } else {

                ezanRahmen = new EzanRahmen(jFrame);
            }
        });
    }

    private void pausenButtonListener() {

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

        try {
            AudioFile audioFile = AudioFileIO.read(new File(yusufIslam));
            duration = audioFile.getAudioHeader().getTrackLength();

            System.out.println("duration = " + duration);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "die angeforderte MP3 ist nicht auffindbar");

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

    public JFrame getjFrame() {

        return jFrame;
    }

    public void setjFrame(JFrame jFrame) {

        this.jFrame = jFrame;
    }
}
