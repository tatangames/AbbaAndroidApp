package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioDevocional {

    @SerializedName("devohaydevocional")
    private int devohaydevocional;

    @SerializedName("devocuestionario")
    private String devocuestionario;

    @SerializedName("devoidblockdeta")
    private int devoidblockdeta;


    public ModeloInicioDevocional(int devohaydevocional, String devocuestionario, int devoidblockdeta) {
        this.devohaydevocional = devohaydevocional;
        this.devocuestionario = devocuestionario;
        this.devoidblockdeta = devoidblockdeta;
    }

    public int getDevohaydevocional() {
        return devohaydevocional;
    }

    public void setDevohaydevocional(int devohaydevocional) {
        this.devohaydevocional = devohaydevocional;
    }

    public String getDevocuestionario() {
        return devocuestionario;
    }

    public void setDevocuestionario(String devocuestionario) {
        this.devocuestionario = devocuestionario;
    }

    public int getDevoidblockdeta() {
        return devoidblockdeta;
    }

    public void setDevoidblockdeta(int devoidblockdeta) {
        this.devoidblockdeta = devoidblockdeta;
    }
}
