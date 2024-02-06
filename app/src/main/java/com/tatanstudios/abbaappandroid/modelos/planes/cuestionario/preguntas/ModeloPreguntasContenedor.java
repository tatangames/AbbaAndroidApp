package com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPreguntasContenedor{

    @SerializedName("success")
    private int success;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("hayrespuesta")
    private int hayRespuesta;

    @SerializedName("listado")
    public List<ModeloPreguntas> modeloPreguntas;


    public int getSuccess() {
        return success;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getHayRespuesta() {
        return hayRespuesta;
    }

    public List<ModeloPreguntas> getModeloPreguntas() {
        return modeloPreguntas;
    }
}