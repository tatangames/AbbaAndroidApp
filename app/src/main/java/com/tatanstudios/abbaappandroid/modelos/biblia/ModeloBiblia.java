package com.tatanstudios.abbaappandroid.modelos.biblia;

import com.google.gson.annotations.SerializedName;

public class ModeloBiblia {


    @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;
    @SerializedName("imagen")
    private String imagen;

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagen() {
        return imagen;
    }
}
