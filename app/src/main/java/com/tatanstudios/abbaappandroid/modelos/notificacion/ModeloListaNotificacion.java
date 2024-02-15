package com.tatanstudios.abbaappandroid.modelos.notificacion;

import com.google.gson.annotations.SerializedName;

public class ModeloListaNotificacion {

   @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;


    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("hayimagen")
    private int hayimagen;

    @SerializedName("imagen")
    private String imagen;


    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getHayimagen() {
        return hayimagen;
    }

    public String getImagen() {
        return imagen;
    }


    public int getId() {
        return id;
    }
}
