package com.soeguet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;

public class EzanRahmen extends JFrame {

    private JPanel hauptPanel;
    private JComboBox<String> datumComboBox;
    private JLabel sabahLabel;
    private JLabel ogleLabel;
    private JLabel ikindiLabel;
    private JLabel aksamLabel;
    private JLabel yatsiLabel;
    private JLabel nochTimeLabel;

    public EzanRahmen() throws HeadlessException {


        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequests = HttpRequest.newBuilder()
                                              .uri(URI.create("https://ezanvakti.herokuapp.com/vakitler/11005"))
                                              .build();

        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequests, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("httpResponse.body() = " + httpResponse.body());

        ObjectMapper objectMapper = new ObjectMapper();

        NamazVakti[] namazVakti;
        try {
            namazVakti = objectMapper.readValue(httpResponse.body(), NamazVakti[].class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, NamazVakti> hashMap = new HashMap<>();
        DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<>();

        for (NamazVakti namazVakti1 : namazVakti) {

            hashMap.put(namazVakti1.getDatum(), namazVakti1);
            defaultComboBoxModel.addElement(namazVakti1.getDatum());
            System.out.println("namazVakti1.toString() = " + namazVakti1.toString());
        }

        datumComboBox.setModel(defaultComboBoxModel);


        setContentPane(hauptPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Bremen - Namaz Vakitleri");
        setVisible(true);



        datumComboBox.addActionListener(actionEvent -> {


            String sabah = hashMap.get(datumComboBox.getEditor().getItem().toString()).getGunes();
            String ogle = hashMap.get(datumComboBox.getEditor().getItem().toString()).getOgle();
            String ikindi = hashMap.get(datumComboBox.getEditor().getItem().toString()).getIkindi();
            String aksam = hashMap.get(datumComboBox.getEditor().getItem().toString()).getAksam();
            String yatsi = hashMap.get(datumComboBox.getEditor().getItem().toString()).getYatsi();

            System.out.println("LocalTime.parse(sabah) = " + LocalTime.parse(sabah));

            System.out.println(LocalTime.now().compareTo(LocalTime.parse(sabah)));
            System.out.println(LocalTime.now().compareTo(LocalTime.parse(ogle)));
            System.out.println(LocalTime.now().compareTo(LocalTime.parse(ikindi)));

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
            }

            System.out.println("LocalTime.now().until(LocalTime.parse(ikindi), ChronoUnit.MINUTES) = " + LocalTime.now().until(LocalTime.parse(ikindi), ChronoUnit.MINUTES));


            sabahLabel.setText(sabah);
            ogleLabel.setText(ogle);
            ikindiLabel.setText(ikindi);
            aksamLabel.setText(aksam);
            yatsiLabel.setText(yatsi);

            int delay = 1000; //milliseconds
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
                    nochTimeLabel.setText(date);
                }
            };
            new Timer(delay, taskPerformer).start();




            setSize(getWidth()+1,getHeight()+1);
            pack();

        });
    }
}
