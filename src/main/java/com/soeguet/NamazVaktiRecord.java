package com.soeguet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NamazVaktiRecord(@JsonProperty("MiladiTarihKisa") String datum,
                               @JsonProperty("Gunes") String gunes,
                               @JsonProperty("Ogle") String ogle,
                               @JsonProperty("Ikindi") String ikindi,
                               @JsonProperty("Aksam") String aksam,
                               @JsonProperty("Yatsi") String yatsi) {

}
