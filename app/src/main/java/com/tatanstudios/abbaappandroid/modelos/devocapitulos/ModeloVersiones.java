package com.tatanstudios.abbaappandroid.modelos.devocapitulos;

import com.google.gson.annotations.SerializedName;

public class ModeloVersiones {

    @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;


    public ModeloVersiones(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }


    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}

