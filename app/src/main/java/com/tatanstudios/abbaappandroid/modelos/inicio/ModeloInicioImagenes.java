package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioImagenes {

    @SerializedName("id")
    private int id;


    @SerializedName("imagen")
    private String imagen;


    public ModeloInicioImagenes(int id, String imagen) {
        this.id = id;
        this.imagen = imagen;
    }


    public int getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }
}
