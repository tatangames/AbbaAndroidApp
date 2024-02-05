package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesBloque1 {

    // MODELO CONTINUAR PLAN

    @SerializedName("id_planes")
    private int idPlanes;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("imagenportada")
    private String imagenPortada;

    public ModeloMisPlanesBloque1(int idPlanes, String titulo, String imagenPortada) {
        this.idPlanes = idPlanes;
        this.titulo = titulo;
        this.imagenPortada = imagenPortada;
    }

    public int getIdPlanes() {
        return idPlanes;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }
}
