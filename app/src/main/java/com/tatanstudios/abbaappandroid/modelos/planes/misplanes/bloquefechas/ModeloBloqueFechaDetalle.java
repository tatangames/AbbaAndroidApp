package com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas;

import com.google.gson.annotations.SerializedName;

public class ModeloBloqueFechaDetalle {

    @SerializedName("id")
    private int id;

    @SerializedName("id_planes_bloques")
    private int idPlanesBloques;

    @SerializedName("posicion")
    private int posicion;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("completado")
    private int completado;



    public void setCompletado(int completado) {
        this.completado = completado;
    }

    public int getId() {
        return id;
    }

    public int getIdPlanesBloques() {
        return idPlanesBloques;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCompletado() {
        return completado;
    }



}
