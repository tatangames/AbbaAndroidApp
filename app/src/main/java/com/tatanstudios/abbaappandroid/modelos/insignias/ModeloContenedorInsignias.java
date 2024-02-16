package com.tatanstudios.abbaappandroid.modelos.insignias;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;

import java.util.List;

public class ModeloContenedorInsignias {


    @SerializedName("success")
    private int success;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("nivelvoy")
    private int nivelvoy;

    @SerializedName("hitocuantofalta")
    private int hitoCuantoFalta;


    @SerializedName("textofalta")
    private String textofalta;


    @SerializedName("hitohaynextlevel")
    private int hitoHayNextLevel;

    @SerializedName("cualnextlevel")
    private int cualNextLevel;


    @SerializedName("hitoarray")
    private List<ModeloInsigniaHitos> modeloInsigniaHitos;


    @SerializedName("listado")
    private List<ModeloInicioInsignias> modeloInicioInsignias;

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloInicioInsignias> getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }

    public String getTextofalta() {
        return textofalta;
    }

    public String getImagen() {
        return imagen;
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

    public int getNivelvoy() {
        return nivelvoy;
    }

    public int getHitoCuantoFalta() {
        return hitoCuantoFalta;
    }

    public int getHitoHayNextLevel() {
        return hitoHayNextLevel;
    }

    public int getCualNextLevel() {
        return cualNextLevel;
    }

    public List<ModeloInsigniaHitos> getModeloInsigniaHitos() {
        return modeloInsigniaHitos;
    }
}

