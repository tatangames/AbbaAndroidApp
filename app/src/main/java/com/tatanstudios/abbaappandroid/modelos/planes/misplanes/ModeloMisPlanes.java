package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanes {

    @SerializedName("success")
    private int success;

    @SerializedName("id")
    private int id;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("subtitulo")
    private String subtitulo;

    @SerializedName("descripcion")
    private String descripcion;


    public String getDescripcion() {
        return descripcion;
    }

    public int getSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }
}
