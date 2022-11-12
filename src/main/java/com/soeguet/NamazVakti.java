package com.soeguet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NamazVakti {

    @JsonProperty("MiladiTarihKisa")
    private String datum;
    @JsonProperty("Gunes")
    private String gunes;
    @JsonProperty("Ogle")
    private String ogle;
    @JsonProperty("Ikindi")
    private String ikindi;
    @JsonProperty("Aksam")
    private String aksam;
    @JsonProperty("Yatsi")
    private String yatsi;

    public NamazVakti() {
    }

    public NamazVakti(String datum, String gunes, String ogle, String ikindi, String aksam, String yatsi) {
        this.datum = datum;
        this.gunes = gunes;
        this.ogle = ogle;
        this.ikindi = ikindi;
        this.aksam = aksam;
        this.yatsi = yatsi;
    }

    public String getGunes() {
        return gunes;
    }

    public void setGunes(String gunes) {
        this.gunes = gunes;
    }

    public String getOgle() {
        return ogle;
    }

    public void setOgle(String ogle) {
        this.ogle = ogle;
    }

    public String getIkindi() {
        return ikindi;
    }

    public void setIkindi(String ikindi) {
        this.ikindi = ikindi;
    }

    public String getAksam() {
        return aksam;
    }

    public void setAksam(String aksam) {
        this.aksam = aksam;
    }

    public String getYatsi() {
        return yatsi;
    }

    public void setYatsi(String yatsi) {
        this.yatsi = yatsi;
    }

    @Override
    public String toString() {
        return "NamazVakti{" +
                "datum='" + datum + '\'' +
                ", gunes='" + gunes + '\'' +
                ", ogle='" + ogle + '\'' +
                ", ikindi='" + ikindi + '\'' +
                ", aksam='" + aksam + '\'' +
                ", yatsi='" + yatsi + '\'' +
                '}';
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
