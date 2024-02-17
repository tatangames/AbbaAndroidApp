package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanes {

    @SerializedName("success")
    private int success;

    @SerializedName("id")
    private int id;

    @SerializedName("id_planes")
    private int idPlanes;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("subtitulo")
    private String subtitulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("fecha")
    private String fecha;


    public String getFecha() {
        return fecha;
    }

    private boolean checkValor;


    public boolean getCheckValor() {
        return checkValor;
    }

    public void setCheckValor(boolean checkValor) {
        this.checkValor = checkValor;
    }

    public int getIdPlanes() {
        return idPlanes;
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
