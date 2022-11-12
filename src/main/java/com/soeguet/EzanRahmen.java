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
import java.util.HashSet;
import java.util.Set;

public class EzanRahmen extends JFrame {

    private JPanel hauptPanel;
    private JComboBox datumComboBox;

    public EzanRahmen() throws HeadlessException {

        setContentPane(hauptPanel);
        setSize(600,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


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

        Set<String> vaktiList = new HashSet<>();
        for (NamazVakti namazVakti1 : namazVakti) {

            vaktiList.add(namazVakti1.getDatum());
            System.out.println("namazVakti1.toString() = " + namazVakti1.toString());
        }

        datumComboBox.setModel(new DefaultComboBoxModel<String>(vaktiList.toArray(new String[0])));


        datumComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                System.out.println("ACTION!");
                System.out.println("vaktiList.contains(datumComboBox.getEditor().getItem().toString()) = " + vaktiList.(datumComboBox.getEditor().getItem().toString()));;

            }
        });
    }
}
