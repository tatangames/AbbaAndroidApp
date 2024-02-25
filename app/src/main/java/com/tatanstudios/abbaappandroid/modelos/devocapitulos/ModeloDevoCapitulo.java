package com.tatanstudios.abbaappandroid.modelos.devocapitulos;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesOcultos;

import java.util.List;

public class ModeloDevoCapitulo {

    @SerializedName("success")
    private int success;

    @SerializedName("haymasversiones")
    private int hayMasVersiones;

    @SerializedName("contenido")
    private String contenido;


    @SerializedName("versiones")
    private List<ModeloVersiones> modeloVersiones;


    public List<ModeloVersiones> getModeloVersiones() {
        return modeloVersiones;
    }

    public int getSuccess() {
        return success;
    }

    public int getHayMasVersiones() {
        return hayMasVersiones;
    }

    public String getContenido() {
        return contenido;
    }
}
