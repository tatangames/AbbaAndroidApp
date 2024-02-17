package com.tatanstudios.abbaappandroid.modelos.planes.ocultos;

import com.google.gson.annotations.SerializedName;

public class ModeloPlanesOcultos {


    @SerializedName("success")
    private int success;

    // id plan
    @SerializedName("id_planes")
    private int idplanes;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("estado")
    private boolean estado;


    public ModeloPlanesOcultos(int idplanes, String titulo, boolean estado) {
        this.idplanes = idplanes;
        this.titulo = titulo;
        this.estado = estado;
    }

    public ModeloPlanesOcultos(int idplanes, boolean estado) {
        this.idplanes = idplanes;
        this.estado = estado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getSuccess() {
        return success;
    }

    public int getIdplanes() {
        return idplanes;
    }

    public String getTitulo() {
        return titulo;
    }


    public boolean getEstado() {
        return estado;
    }
}
