package com.tatanstudios.abbaappandroid.modelos.biblia.capitulo;

import com.google.gson.annotations.SerializedName;

public class ModeloCapituloBloque {

    // ID BLOQUE
    @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;


    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}
