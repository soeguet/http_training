package com.soeguet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EzanRahmen extends JFrame {

    private final HashMap<String, NamazVakti> hashMap = new LinkedHashMap<>();
    private final NamazVakti[] namazVakti;
    private JPanel hauptPanel;
    private JComboBox<String> datumComboBox;
    private JLabel sabahLabel;
    private JLabel ogleLabel;
    private JLabel ikindiLabel;
    private JLabel aksamLabel;
    private JLabel yatsiLabel;
    private JLabel nochTimeLabel;
    private JLabel clockLabel;
    private String sabah, ogle, ikindi, aksam, yatsi;

    public EzanRahmen() throws HeadlessException {

        uhrzeit();

        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpInhalt();
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        namazVakti = eintraege(httpResponse);

        comboElemente();
        listeners();

        setContentPane(hauptPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Bremen - Namaz Vakitleri");
        setVisible(true);

        initScreen();
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

        setSize(getWidth() + 1, getHeight() + 1);
        pack();
    }

    private void listeners() {

        datumComboBox.addActionListener(actionEvent -> {
            backgroundNormal();

            String wert = String.valueOf(datumComboBox.getEditor().getItem().toString());

            sabah = hashMap.get(wert).getGunes();
            ogle = hashMap.get(wert).getOgle();
            ikindi = hashMap.get(wert).getIkindi();
            aksam = hashMap.get(wert).getAksam();
            yatsi = hashMap.get(wert).getYatsi();

            aktuelleVakit();

            //nochZeit();

            sabahLabel.setText(sabah);
            ogleLabel.setText(ogle);
            ikindiLabel.setText(ikindi);
            aksamLabel.setText(aksam);
            yatsiLabel.setText(yatsi);

            setSize(getWidth() + 1, getHeight() + 1);
            pack();
        });
    }

    private void nochZeit() {

        nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(ikindi), ChronoUnit.MINUTES)));
    }

    private void aktuelleVakit() {

        if (LocalTime.now().compareTo(LocalTime.parse(sabah)) < 0) {

            sabahLabel.setBackground(new Color(0x930C191E, true));
            nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(sabah), ChronoUnit.MINUTES)));
        } else if (LocalTime.now().compareTo(LocalTime.parse(ogle)) < 0) {

            ogleLabel.setBackground(new Color(0x930C191E, true));
            nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(ogle), ChronoUnit.MINUTES)));
        } else if (LocalTime.now().compareTo(LocalTime.parse(ikindi)) < 0) {

            ikindiLabel.setBackground(new Color(0x930C191E, true));
            nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(ikindi), ChronoUnit.MINUTES)));
        } else if (LocalTime.now().compareTo(LocalTime.parse(aksam)) < 0) {

            aksamLabel.setBackground(new Color(0x930C191E, true));
            nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(aksam), ChronoUnit.MINUTES)));
        } else if (LocalTime.now().compareTo(LocalTime.parse(yatsi)) < 0) {

            yatsiLabel.setBackground(new Color(0x930C191E, true));
            nochTimeLabel.setText(String.valueOf(LocalTime.now().until(LocalTime.parse(yatsi), ChronoUnit.MINUTES)));
        } else {

            nochTimeLabel.setText("Allah kabul etsin :D");
        }
    }

    private void backgroundNormal() {

        sabahLabel.setBackground(new Color(13487309, true));
        ogleLabel.setBackground(new Color(13487309, true));
        ikindiLabel.setBackground(new Color(13487309, true));
        aksamLabel.setBackground(new Color(13487309, true));
        yatsiLabel.setBackground(new Color(13487309, true));
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

        try {
            return objectMapper.readValue(httpResponse.body(), NamazVakti[].class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void uhrzeit() {

        int delay = 1000; //milliseconds
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
}
