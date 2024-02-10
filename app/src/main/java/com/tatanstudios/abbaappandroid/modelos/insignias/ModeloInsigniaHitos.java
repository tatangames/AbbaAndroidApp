package com.tatanstudios.abbaappandroid.modelos.insignias;

import com.google.gson.annotations.SerializedName;

public class ModeloInsigniaHitos {

    @SerializedName("fechaFormat")
    private String fechaFormat;

    @SerializedName("nivel")
    private int nivel;


    // para saber si existe el siguiente nivel
    private int esNextLevel;

    // para saber cual es el siguiente nivel
    private int cualNextLevel;

    // cuanto puntos faltan para el siguiente nivel
    private int hitCuantoFalta;

    @SerializedName("hitotexto1")
    private String textoCompletado;


    public ModeloInsigniaHitos(String fechaFormat, int esNextLevel, int cualNextLevel, int nivel,
                               int hitCuantoFalta, String textoCompletado) {
        this.fechaFormat = fechaFormat;
        this.esNextLevel = esNextLevel;
        this.cualNextLevel = cualNextLevel;
        this.nivel = nivel;
        this.hitCuantoFalta = hitCuantoFalta;
        this.textoCompletado = textoCompletado;
    }


    public String getTextoCompletado() {
        return textoCompletado;
    }

    public int getHitCuantoFalta() {
        return hitCuantoFalta;
    }

    public int getNivel() {
        return nivel;
    }

    public int getCualNextLevel() {
        return cualNextLevel;
    }

    public int getEsNextLevel() {
        return esNextLevel;
    }

    public String getFechaFormat() {
        return fechaFormat;
    }
}

