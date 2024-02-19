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

    @SerializedName("ignorarpre")
    private int ignorarpre;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("genero")
    private int genero;
    @SerializedName("ignorarshare")
    private int ignorarshare;


    public int getIgnorarshare() {
        return ignorarshare;
    }

    public int getGenero() {
        return genero;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    @SerializedName("listado")
    public List<ModeloPreguntas> modeloPreguntas;


    public int getIgnorarpre() {
        return ignorarpre;
    }

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
