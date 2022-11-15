package com.soeguet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EzanRahmen {

    private final HashMap<String, NamazVakti> hashMap = new LinkedHashMap<>();
    private NamazVakti[] namazVakti;
    private JPanel hauptPanel;
    private JComboBox<String> datumComboBox;
    private JLabel sabahLabel, ogleLabel, ikindiLabel, aksamLabel, yatsiLabel, nochTimeLabel, clockLabel;
    private String sabah, ogle, ikindi, aksam, yatsi;
    private Thread ezanThread;
    private AdvancedPlayer player;
    private JMenu test1, test2, test3;
    private JRadioButton yusufIslamRadioButton;
    private JCheckBox checkBox1;
    private JRadioButton radioButton2;
    private JMenuItem ezanMenuPunkt;
    private JRadioButton adthanMedinaRadioButton;
    private JRadioButton starWars3RadioButton;
    private JFrame jFrame;
    private Overlay overlay;
    private ButtonGroup buttonGroup;

    private final Color markiertFarbe = new Color(0x930C191E, false);

    public EzanRahmen() {

        ersteAufgabe();
    }

    public EzanRahmen(JFrame jFrame) {

        this.jFrame = jFrame;

        ersteAufgabe();
    }

    private void ersteAufgabe() {

        System.out.println("Thread.State.RUNNABLE = " + Thread.State.RUNNABLE);
        System.out.println("Thread.currentThread() = " + Thread.currentThread());
        System.out.println("Thread.activeCount() = " + Thread.activeCount());

        uhrzeit();

        ezanMenuPunkt.addActionListener(actionEvent -> {


            playSoundEzan();
        });

//        HttpResponse<String> httpResponse = null;
//        try {
//            httpResponse = httpInhalt();
//        }
//        catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        namazVakti = eintraege(httpResponse);
        namazVakti = eintraege(null);

        comboElemente();
        listeners();
        initScreen();

        jFrame.setContentPane(hauptPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private void playSoundEzan() {

        hauptPanel.setVisible(false);

        if (overlay != null) {

            overlay.setjFrame(jFrame);
        } else {

            overlay = new Overlay(jFrame);
        }
    }

    private void initScreen() {

        String wert = hashMap.entrySet().iterator().next().getKey();
        datumComboBox.getEditor().setItem(wert);

        ogle = hashMap.get(wert).getOgle();
        ikindi = hashMap.get(wert).getIkindi();
        aksam = hashMap.get(wert).getAksam();
        sabah = hashMap.get(wert).getGunes();
        yatsi = hashMap.get(wert).getYatsi();

        sabahLabel.setText(sabah);
        ogleLabel.setText(ogle);
        ikindiLabel.setText(ikindi);
        aksamLabel.setText(aksam);
        yatsiLabel.setText(yatsi);

        aktuelleVakit();
        //nochZeit();

        jFrame.setSize(jFrame.getWidth() + 1, jFrame.getHeight() + 1);
        jFrame.pack();
    }

    private void listeners() {

        datumComboBox.addActionListener(actionEvent -> {


            String wert = String.valueOf(datumComboBox.getEditor().getItem().toString());

            sabah = hashMap.get(wert).getGunes();
            ogle = hashMap.get(wert).getOgle();
            ikindi = hashMap.get(wert).getIkindi();
            aksam = hashMap.get(wert).getAksam();
            yatsi = hashMap.get(wert).getYatsi();

            //nochZeit();

            sabahLabel.setText(sabah);
            ogleLabel.setText(ogle);
            ikindiLabel.setText(ikindi);
            aksamLabel.setText(aksam);
            yatsiLabel.setText(yatsi);

            jFrame.setSize(jFrame.getWidth() + 1, jFrame.getHeight() + 1);
            jFrame.pack();
        });
    }

    private String verbleibendeZeit(String vakit) {

        long until = LocalTime.now().until(LocalTime.parse(vakit), ChronoUnit.MINUTES);

        if (until < 60) {
            return (LocalTime.now().until(LocalTime.parse(vakit), ChronoUnit.MINUTES) + 1) + " Minuten";
        } else {

            if ((int) (LocalTime.now().until(LocalTime.parse(vakit), ChronoUnit.MINUTES) / 60) == 1) {

            }
            return (int) (LocalTime.now().until(LocalTime.parse(vakit), ChronoUnit.MINUTES) / 60) + " Stunden und "
                    + (int) ((LocalTime.now().until(LocalTime.parse(vakit), ChronoUnit.MINUTES) % 60) + 1) + " Minuten";
        }
    }

    private void aktuelleVakit() {

        int delay = 1_000; //milliseconds

        ActionListener taskPerformer = evt -> {

            if (LocalTime.now().compareTo(LocalTime.parse(sabah)) < 0) {

                backgroundNormal();
                sabahLabel.setBackground(markiertFarbe);
                sabahLabel.setOpaque(true);
                nochTimeLabel.setText(verbleibendeZeit(sabah));

            } else if (LocalTime.now().compareTo(LocalTime.parse(ogle)) < 0) {

                backgroundNormal();
                ogleLabel.setBackground(markiertFarbe);
                ogleLabel.setOpaque(true);
                nochTimeLabel.setText(verbleibendeZeit(ogle));

            } else if (LocalTime.now().compareTo(LocalTime.parse(ikindi)) < 0) {

                backgroundNormal();
                ikindiLabel.setBackground(markiertFarbe);
                ikindiLabel.setOpaque(true);
                nochTimeLabel.setText(verbleibendeZeit(ikindi));

            } else if (LocalTime.now().compareTo(LocalTime.parse(aksam)) < 0) {

                backgroundNormal();
                aksamLabel.setBackground(markiertFarbe);
                aksamLabel.setOpaque(true);
                nochTimeLabel.setText(verbleibendeZeit(aksam));

            } else if (LocalTime.now().compareTo(LocalTime.parse(yatsi)) < 0) {

                backgroundNormal();
                yatsiLabel.setBackground(markiertFarbe);
                yatsiLabel.setOpaque(true);
                nochTimeLabel.setText(verbleibendeZeit(yatsi));

            } else {

                backgroundNormal();
                nochTimeLabel.setText("Allah kabul etsin :D");
            }

            //NAMAZ ALARM
            if (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse(sabah))) == 0 ||
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse(ogle))) == 0 ||
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse(ikindi))) == 0 ||
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse(aksam))) == 0 ||
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).compareTo(String.valueOf(LocalTime.parse(yatsi))) == 0) {

                JOptionPane.showMessageDialog(jFrame, "NAMAZ OLDU", "ALARM", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        new Timer(delay, taskPerformer).start();
    }

    private void backgroundNormal() {

        sabahLabel.setBackground(new Color(13487309, true));
        sabahLabel.setOpaque(false);
        ogleLabel.setBackground(new Color(13487309, true));
        ogleLabel.setOpaque(false);
        ikindiLabel.setBackground(new Color(13487309, true));
        ikindiLabel.setOpaque(false);
        aksamLabel.setBackground(new Color(13487309, true));
        aksamLabel.setOpaque(false);
        yatsiLabel.setBackground(new Color(13487309, true));
        yatsiLabel.setOpaque(false);
    }

    private void comboElemente() {

        DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<>();
        for (NamazVakti gebetszeit : namazVakti) {

            hashMap.put(gebetszeit.getDatum(), gebetszeit);
            defaultComboBoxModel.addElement(gebetszeit.getDatum());
        }

        datumComboBox.setModel(defaultComboBoxModel);
    }

    private NamazVakti[] eintraege(HttpResponse<String> httpResponse) {

        ObjectMapper objectMapper = new ObjectMapper();

//        String content = httpResponse.body();
        String content = "[{\"Aksam\":\"16:39\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d5.gif\",\"Gunes\":\"07:39\",\"GunesBatis\":\"16:32\",\"GunesDogus\":\"07:46\",\"HicriTarihKisa\":\"19.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"19 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:13\",\"Imsak\":\"05:45\",\"KibleSaati\":\"08:41\",\"MiladiTarihKisa\":\"14.11.2022\",\"MiladiTarihKisaIso8601\":\"14.11.2022\",\"MiladiTarihUzun\":\"14 Kas\\u0131m 2022 Pazartesi\",\"MiladiTarihUzunIso8601\":\"2022-11-14T00:00:00.0000000+03:00\",\"Ogle\":\"12:14\",\"Yatsi\":\"17:59\"},{\"Aksam\":\"16:37\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d6.gif\",\"Gunes\":\"07:41\",\"GunesBatis\":\"16:30\",\"GunesDogus\":\"07:48\",\"HicriTarihKisa\":\"20.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"20 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:11\",\"Imsak\":\"05:47\",\"KibleSaati\":\"08:40\",\"MiladiTarihKisa\":\"15.11.2022\",\"MiladiTarihKisaIso8601\":\"15.11.2022\",\"MiladiTarihUzun\":\"15 Kas\\u0131m 2022 Sal\\u0131\",\"MiladiTarihUzunIso8601\":\"2022-11-15T00:00:00.0000000+03:00\",\"Ogle\":\"12:14\",\"Yatsi\":\"17:57\"},{\"Aksam\":\"16:36\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d7.gif\",\"Gunes\":\"07:43\",\"GunesBatis\":\"16:29\",\"GunesDogus\":\"07:50\",\"HicriTarihKisa\":\"21.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"21 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:10\",\"Imsak\":\"05:48\",\"KibleSaati\":\"08:40\",\"MiladiTarihKisa\":\"16.11.2022\",\"MiladiTarihKisaIso8601\":\"16.11.2022\",\"MiladiTarihUzun\":\"16 Kas\\u0131m 2022 \\u00c7ar\\u015famba\",\"MiladiTarihUzunIso8601\":\"2022-11-16T00:00:00.0000000+03:00\",\"Ogle\":\"12:14\",\"Yatsi\":\"17:56\"},{\"Aksam\":\"16:35\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sondordun.gif\",\"Gunes\":\"07:45\",\"GunesBatis\":\"16:28\",\"GunesDogus\":\"07:52\",\"HicriTarihKisa\":\"22.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"22 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:09\",\"Imsak\":\"05:50\",\"KibleSaati\":\"08:40\",\"MiladiTarihKisa\":\"17.11.2022\",\"MiladiTarihKisaIso8601\":\"17.11.2022\",\"MiladiTarihUzun\":\"17 Kas\\u0131m 2022 Per\\u015fembe\",\"MiladiTarihUzunIso8601\":\"2022-11-17T00:00:00.0000000+03:00\",\"Ogle\":\"12:15\",\"Yatsi\":\"17:55\"},{\"Aksam\":\"16:33\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sd1.gif\",\"Gunes\":\"07:46\",\"GunesBatis\":\"16:26\",\"GunesDogus\":\"07:54\",\"HicriTarihKisa\":\"23.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"23 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:08\",\"Imsak\":\"05:51\",\"KibleSaati\":\"08:39\",\"MiladiTarihKisa\":\"18.11.2022\",\"MiladiTarihKisaIso8601\":\"18.11.2022\",\"MiladiTarihUzun\":\"18 Kas\\u0131m 2022 Cuma\",\"MiladiTarihUzunIso8601\":\"2022-11-18T00:00:00.0000000+03:00\",\"Ogle\":\"12:15\",\"Yatsi\":\"17:53\"},{\"Aksam\":\"16:32\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sd2.gif\",\"Gunes\":\"07:48\",\"GunesBatis\":\"16:25\",\"GunesDogus\":\"07:55\",\"HicriTarihKisa\":\"24.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"24 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:07\",\"Imsak\":\"05:52\",\"KibleSaati\":\"08:39\",\"MiladiTarihKisa\":\"19.11.2022\",\"MiladiTarihKisaIso8601\":\"19.11.2022\",\"MiladiTarihUzun\":\"19 Kas\\u0131m 2022 Cumartesi\",\"MiladiTarihUzunIso8601\":\"2022-11-19T00:00:00.0000000+03:00\",\"Ogle\":\"12:15\",\"Yatsi\":\"17:52\"},{\"Aksam\":\"16:31\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sd3.gif\",\"Gunes\":\"07:50\",\"GunesBatis\":\"16:23\",\"GunesDogus\":\"07:57\",\"HicriTarihKisa\":\"25.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"25 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:06\",\"Imsak\":\"05:54\",\"KibleSaati\":\"08:39\",\"MiladiTarihKisa\":\"20.11.2022\",\"MiladiTarihKisaIso8601\":\"20.11.2022\",\"MiladiTarihUzun\":\"20 Kas\\u0131m 2022 Pazar\",\"MiladiTarihUzunIso8601\":\"2022-11-20T00:00:00.0000000+03:00\",\"Ogle\":\"12:15\",\"Yatsi\":\"17:51\"},{\"Aksam\":\"16:29\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sd4.gif\",\"Gunes\":\"07:52\",\"GunesBatis\":\"16:22\",\"GunesDogus\":\"07:59\",\"HicriTarihKisa\":\"26.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"26 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:05\",\"Imsak\":\"05:55\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"21.11.2022\",\"MiladiTarihKisaIso8601\":\"21.11.2022\",\"MiladiTarihUzun\":\"21 Kas\\u0131m 2022 Pazartesi\",\"MiladiTarihUzunIso8601\":\"2022-11-21T00:00:00.0000000+03:00\",\"Ogle\":\"12:15\",\"Yatsi\":\"17:49\"},{\"Aksam\":\"16:28\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/sd5.gif\",\"Gunes\":\"07:54\",\"GunesBatis\":\"16:21\",\"GunesDogus\":\"08:01\",\"HicriTarihKisa\":\"27.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"27 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:04\",\"Imsak\":\"05:57\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"22.11.2022\",\"MiladiTarihKisaIso8601\":\"22.11.2022\",\"MiladiTarihUzun\":\"22 Kas\\u0131m 2022 Sal\\u0131\",\"MiladiTarihUzunIso8601\":\"2022-11-22T00:00:00.0000000+03:00\",\"Ogle\":\"12:16\",\"Yatsi\":\"17:48\"},{\"Aksam\":\"16:27\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/ictima.gif\",\"Gunes\":\"07:55\",\"GunesBatis\":\"16:20\",\"GunesDogus\":\"08:02\",\"HicriTarihKisa\":\"28.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"28 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:04\",\"Imsak\":\"05:58\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"23.11.2022\",\"MiladiTarihKisaIso8601\":\"23.11.2022\",\"MiladiTarihUzun\":\"23 Kas\\u0131m 2022 \\u00c7ar\\u015famba\",\"MiladiTarihUzunIso8601\":\"2022-11-23T00:00:00.0000000+03:00\",\"Ogle\":\"12:16\",\"Yatsi\":\"17:47\"},{\"Aksam\":\"16:26\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/ruyet.gif\",\"Gunes\":\"07:57\",\"GunesBatis\":\"16:19\",\"GunesDogus\":\"08:04\",\"HicriTarihKisa\":\"29.4.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"29 Rebiulahir 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:03\",\"Imsak\":\"05:59\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"24.11.2022\",\"MiladiTarihKisaIso8601\":\"24.11.2022\",\"MiladiTarihUzun\":\"24 Kas\\u0131m 2022 Per\\u015fembe\",\"MiladiTarihUzunIso8601\":\"2022-11-24T00:00:00.0000000+03:00\",\"Ogle\":\"12:16\",\"Yatsi\":\"17:46\"},{\"Aksam\":\"16:25\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/r1.gif\",\"Gunes\":\"07:59\",\"GunesBatis\":\"16:18\",\"GunesDogus\":\"08:06\",\"HicriTarihKisa\":\"1.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"1 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:02\",\"Imsak\":\"06:01\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"25.11.2022\",\"MiladiTarihKisaIso8601\":\"25.11.2022\",\"MiladiTarihUzun\":\"25 Kas\\u0131m 2022 Cuma\",\"MiladiTarihUzunIso8601\":\"2022-11-25T00:00:00.0000000+03:00\",\"Ogle\":\"12:17\",\"Yatsi\":\"17:45\"},{\"Aksam\":\"16:24\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/r2.gif\",\"Gunes\":\"08:00\",\"GunesBatis\":\"16:17\",\"GunesDogus\":\"08:07\",\"HicriTarihKisa\":\"2.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"2 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:01\",\"Imsak\":\"06:02\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"26.11.2022\",\"MiladiTarihKisaIso8601\":\"26.11.2022\",\"MiladiTarihUzun\":\"26 Kas\\u0131m 2022 Cumartesi\",\"MiladiTarihUzunIso8601\":\"2022-11-26T00:00:00.0000000+03:00\",\"Ogle\":\"12:17\",\"Yatsi\":\"17:44\"},{\"Aksam\":\"16:23\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/r3.gif\",\"Gunes\":\"08:02\",\"GunesBatis\":\"16:16\",\"GunesDogus\":\"08:09\",\"HicriTarihKisa\":\"3.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"3 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:01\",\"Imsak\":\"06:03\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"27.11.2022\",\"MiladiTarihKisaIso8601\":\"27.11.2022\",\"MiladiTarihUzun\":\"27 Kas\\u0131m 2022 Pazar\",\"MiladiTarihUzunIso8601\":\"2022-11-27T00:00:00.0000000+03:00\",\"Ogle\":\"12:17\",\"Yatsi\":\"17:43\"},{\"Aksam\":\"16:22\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/r4.gif\",\"Gunes\":\"08:03\",\"GunesBatis\":\"16:15\",\"GunesDogus\":\"08:10\",\"HicriTarihKisa\":\"4.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"4 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"14:00\",\"Imsak\":\"06:05\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"28.11.2022\",\"MiladiTarihKisaIso8601\":\"28.11.2022\",\"MiladiTarihUzun\":\"28 Kas\\u0131m 2022 Pazartesi\",\"MiladiTarihUzunIso8601\":\"2022-11-28T00:00:00.0000000+03:00\",\"Ogle\":\"12:18\",\"Yatsi\":\"17:42\"},{\"Aksam\":\"16:21\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/r5.gif\",\"Gunes\":\"08:05\",\"GunesBatis\":\"16:14\",\"GunesDogus\":\"08:12\",\"HicriTarihKisa\":\"5.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"5 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:59\",\"Imsak\":\"06:06\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"29.11.2022\",\"MiladiTarihKisaIso8601\":\"29.11.2022\",\"MiladiTarihUzun\":\"29 Kas\\u0131m 2022 Sal\\u0131\",\"MiladiTarihUzunIso8601\":\"2022-11-29T00:00:00.0000000+03:00\",\"Ogle\":\"12:18\",\"Yatsi\":\"17:41\"},{\"Aksam\":\"16:20\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/ilkdordun.gif\",\"Gunes\":\"08:07\",\"GunesBatis\":\"16:13\",\"GunesDogus\":\"08:14\",\"HicriTarihKisa\":\"6.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"6 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:59\",\"Imsak\":\"06:07\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"30.11.2022\",\"MiladiTarihKisaIso8601\":\"30.11.2022\",\"MiladiTarihUzun\":\"30 Kas\\u0131m 2022 \\u00c7ar\\u015famba\",\"MiladiTarihUzunIso8601\":\"2022-11-30T00:00:00.0000000+03:00\",\"Ogle\":\"12:18\",\"Yatsi\":\"17:40\"},{\"Aksam\":\"16:19\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i1.gif\",\"Gunes\":\"08:08\",\"GunesBatis\":\"16:12\",\"GunesDogus\":\"08:15\",\"HicriTarihKisa\":\"7.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"7 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:58\",\"Imsak\":\"06:08\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"01.12.2022\",\"MiladiTarihKisaIso8601\":\"01.12.2022\",\"MiladiTarihUzun\":\"01 Aral\\u0131k 2022 Per\\u015fembe\",\"MiladiTarihUzunIso8601\":\"2022-12-01T00:00:00.0000000+03:00\",\"Ogle\":\"12:19\",\"Yatsi\":\"17:39\"},{\"Aksam\":\"16:18\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i2.gif\",\"Gunes\":\"08:09\",\"GunesBatis\":\"16:11\",\"GunesDogus\":\"08:17\",\"HicriTarihKisa\":\"8.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"8 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:58\",\"Imsak\":\"06:10\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"02.12.2022\",\"MiladiTarihKisaIso8601\":\"02.12.2022\",\"MiladiTarihUzun\":\"02 Aral\\u0131k 2022 Cuma\",\"MiladiTarihUzunIso8601\":\"2022-12-02T00:00:00.0000000+03:00\",\"Ogle\":\"12:19\",\"Yatsi\":\"17:38\"},{\"Aksam\":\"16:18\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i3.gif\",\"Gunes\":\"08:11\",\"GunesBatis\":\"16:11\",\"GunesDogus\":\"08:18\",\"HicriTarihKisa\":\"9.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"9 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:57\",\"Imsak\":\"06:11\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"03.12.2022\",\"MiladiTarihKisaIso8601\":\"03.12.2022\",\"MiladiTarihUzun\":\"03 Aral\\u0131k 2022 Cumartesi\",\"MiladiTarihUzunIso8601\":\"2022-12-03T00:00:00.0000000+03:00\",\"Ogle\":\"12:19\",\"Yatsi\":\"17:38\"},{\"Aksam\":\"16:17\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i4.gif\",\"Gunes\":\"08:12\",\"GunesBatis\":\"16:10\",\"GunesDogus\":\"08:19\",\"HicriTarihKisa\":\"10.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"10 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:57\",\"Imsak\":\"06:12\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"04.12.2022\",\"MiladiTarihKisaIso8601\":\"04.12.2022\",\"MiladiTarihUzun\":\"04 Aral\\u0131k 2022 Pazar\",\"MiladiTarihUzunIso8601\":\"2022-12-04T00:00:00.0000000+03:00\",\"Ogle\":\"12:20\",\"Yatsi\":\"17:37\"},{\"Aksam\":\"16:17\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i5.gif\",\"Gunes\":\"08:14\",\"GunesBatis\":\"16:10\",\"GunesDogus\":\"08:21\",\"HicriTarihKisa\":\"11.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"11 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:57\",\"Imsak\":\"06:13\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"05.12.2022\",\"MiladiTarihKisaIso8601\":\"05.12.2022\",\"MiladiTarihUzun\":\"05 Aral\\u0131k 2022 Pazartesi\",\"MiladiTarihUzunIso8601\":\"2022-12-05T00:00:00.0000000+03:00\",\"Ogle\":\"12:20\",\"Yatsi\":\"17:37\"},{\"Aksam\":\"16:16\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i6.gif\",\"Gunes\":\"08:15\",\"GunesBatis\":\"16:09\",\"GunesDogus\":\"08:22\",\"HicriTarihKisa\":\"12.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"12 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:14\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"06.12.2022\",\"MiladiTarihKisaIso8601\":\"06.12.2022\",\"MiladiTarihUzun\":\"06 Aral\\u0131k 2022 Sal\\u0131\",\"MiladiTarihUzunIso8601\":\"2022-12-06T00:00:00.0000000+03:00\",\"Ogle\":\"12:21\",\"Yatsi\":\"17:36\"},{\"Aksam\":\"16:16\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/i7.gif\",\"Gunes\":\"08:16\",\"GunesBatis\":\"16:09\",\"GunesDogus\":\"08:23\",\"HicriTarihKisa\":\"13.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"13 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:15\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"07.12.2022\",\"MiladiTarihKisaIso8601\":\"07.12.2022\",\"MiladiTarihUzun\":\"07 Aral\\u0131k 2022 \\u00c7ar\\u015famba\",\"MiladiTarihUzunIso8601\":\"2022-12-07T00:00:00.0000000+03:00\",\"Ogle\":\"12:21\",\"Yatsi\":\"17:36\"},{\"Aksam\":\"16:15\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/dolunay.gif\",\"Gunes\":\"08:18\",\"GunesBatis\":\"16:08\",\"GunesDogus\":\"08:25\",\"HicriTarihKisa\":\"14.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"14 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:16\",\"KibleSaati\":\"08:37\",\"MiladiTarihKisa\":\"08.12.2022\",\"MiladiTarihKisaIso8601\":\"08.12.2022\",\"MiladiTarihUzun\":\"08 Aral\\u0131k 2022 Per\\u015fembe\",\"MiladiTarihUzunIso8601\":\"2022-12-08T00:00:00.0000000+03:00\",\"Ogle\":\"12:21\",\"Yatsi\":\"17:35\"},{\"Aksam\":\"16:15\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d1.gif\",\"Gunes\":\"08:19\",\"GunesBatis\":\"16:08\",\"GunesDogus\":\"08:26\",\"HicriTarihKisa\":\"15.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"15 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:17\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"09.12.2022\",\"MiladiTarihKisaIso8601\":\"09.12.2022\",\"MiladiTarihUzun\":\"09 Aral\\u0131k 2022 Cuma\",\"MiladiTarihUzunIso8601\":\"2022-12-09T00:00:00.0000000+03:00\",\"Ogle\":\"12:22\",\"Yatsi\":\"17:35\"},{\"Aksam\":\"16:15\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d2.gif\",\"Gunes\":\"08:20\",\"GunesBatis\":\"16:08\",\"GunesDogus\":\"08:27\",\"HicriTarihKisa\":\"16.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"16 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:18\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"10.12.2022\",\"MiladiTarihKisaIso8601\":\"10.12.2022\",\"MiladiTarihUzun\":\"10 Aral\\u0131k 2022 Cumartesi\",\"MiladiTarihUzunIso8601\":\"2022-12-10T00:00:00.0000000+03:00\",\"Ogle\":\"12:22\",\"Yatsi\":\"17:35\"},{\"Aksam\":\"16:15\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d3.gif\",\"Gunes\":\"08:21\",\"GunesBatis\":\"16:08\",\"GunesDogus\":\"08:28\",\"HicriTarihKisa\":\"17.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"17 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:19\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"11.12.2022\",\"MiladiTarihKisaIso8601\":\"11.12.2022\",\"MiladiTarihUzun\":\"11 Aral\\u0131k 2022 Pazar\",\"MiladiTarihUzunIso8601\":\"2022-12-11T00:00:00.0000000+03:00\",\"Ogle\":\"12:23\",\"Yatsi\":\"17:35\"},{\"Aksam\":\"16:14\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d4.gif\",\"Gunes\":\"08:22\",\"GunesBatis\":\"16:07\",\"GunesDogus\":\"08:29\",\"HicriTarihKisa\":\"18.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"18 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:20\",\"KibleSaati\":\"08:38\",\"MiladiTarihKisa\":\"12.12.2022\",\"MiladiTarihKisaIso8601\":\"12.12.2022\",\"MiladiTarihUzun\":\"12 Aral\\u0131k 2022 Pazartesi\",\"MiladiTarihUzunIso8601\":\"2022-12-12T00:00:00.0000000+03:00\",\"Ogle\":\"12:23\",\"Yatsi\":\"17:34\"},{\"Aksam\":\"16:14\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d5.gif\",\"Gunes\":\"08:23\",\"GunesBatis\":\"16:07\",\"GunesDogus\":\"08:30\",\"HicriTarihKisa\":\"19.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"19 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:21\",\"KibleSaati\":\"08:39\",\"MiladiTarihKisa\":\"13.12.2022\",\"MiladiTarihKisaIso8601\":\"13.12.2022\",\"MiladiTarihUzun\":\"13 Aral\\u0131k 2022 Sal\\u0131\",\"MiladiTarihUzunIso8601\":\"2022-12-13T00:00:00.0000000+03:00\",\"Ogle\":\"12:24\",\"Yatsi\":\"17:34\"}," +
                "{\"Aksam\":\"23:43\",\"AyinSekliURL\":\"http://namazvakti.diyanet.gov.tr/images/d6.gif\",\"Gunes\":\"08:24\",\"GunesBatis\":\"16:07\",\"GunesDogus\":\"08:31\",\"HicriTarihKisa\":\"20.5.1444\",\"HicriTarihKisaIso8601\":null,\"HicriTarihUzun\":\"20 Cemaziyelevvel 1444\",\"HicriTarihUzunIso8601\":null,\"Ikindi\":\"13:56\",\"Imsak\":\"06:22\",\"KibleSaati\":\"08:39\",\"MiladiTarihKisa\":\"14.12.2022\",\"MiladiTarihKisaIso8601\":\"14.12.2022\",\"MiladiTarihUzun\":\"14 Aral\\u0131k 2022 \\u00c7ar\\u015famba\",\"MiladiTarihUzunIso8601\":\"2022-12-14T00:00:00.0000000+03:00\",\"Ogle\":\"12:24\",\"Yatsi\":\"17:34\"}]";

        try {
            return objectMapper.readValue(content, NamazVakti[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void uhrzeit() {

        int delay = 500; //milliseconds
        ActionListener taskPerformer = evt -> {

            String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
            clockLabel.setText(date);
        };
        new Timer(delay, taskPerformer).start();
    }

    private HttpResponse<String> httpInhalt() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequests = HttpRequest.newBuilder()
                                              .uri(URI.create("https://ezanvakti.herokuapp.com/vakitler/11005"))
                                              .build();

        return httpClient.send(httpRequests, HttpResponse.BodyHandlers.ofString());
    }

    public JFrame getjFrame() {

        return jFrame;
    }

    public void setjFrame(JFrame jFrame) {

        this.jFrame = jFrame;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        yusufIslamRadioButton = new JRadioButton();
        adthanMedinaRadioButton = new JRadioButton();
        starWars3RadioButton = new JRadioButton();
        buttonGroup = new ButtonGroup();
        buttonGroup.add(yusufIslamRadioButton);
        buttonGroup.add(adthanMedinaRadioButton);
        buttonGroup.add(starWars3RadioButton);

        jFrame.setJMenuBar(new JMenuBar());
        jFrame.add(new JMenu("TEST1"));

        yusufIslamRadioButton.addActionListener(actionEvent -> System.out.println("HEY"));
    }
}
