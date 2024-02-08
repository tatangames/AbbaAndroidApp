package com.tatanstudios.abbaappandroid.modelos.planes.completados;

import com.google.gson.annotations.SerializedName;

public class ModeloPlanesCompletados {

    @SerializedName("success")
    private int success;

    @SerializedName("id")
    private int id;

    @SerializedName("idplan")
    private int idPlan;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("subtitulo")
    private String subtitulo;

    @SerializedName("descripcion")
    private String descripcion;


    public int getIdPlan() {
        return idPlan;
    }

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
