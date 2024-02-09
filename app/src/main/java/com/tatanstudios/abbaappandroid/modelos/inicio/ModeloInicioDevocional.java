package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioDevocional {

    @SerializedName("devohaydevocional")
    private int devohaydevocional;

    @SerializedName("devocuestionario")
    private String devocuestionario;

    @SerializedName("devoidblockdeta")
    private int devoidblockdeta;

    // saver si mostra bloque preguntas
    @SerializedName("devopreguntas")
    private int devopreguntas;



    public ModeloInicioDevocional(int devohaydevocional, String devocuestionario, int devoidblockdeta, int devopreguntas) {
        this.devohaydevocional = devohaydevocional;
        this.devocuestionario = devocuestionario;
        this.devoidblockdeta = devoidblockdeta;
        this.devopreguntas = devopreguntas;
    }


    public int getDevopreguntas() {
        return devopreguntas;
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
