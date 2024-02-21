package com.tatanstudios.abbaappandroid.modelos.biblia.capitulo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloCapitulo {


    // UNICAMENTE TITULO
    @SerializedName("titulo")
    public String titulo;

    @SerializedName("detalle")
    public List<ModeloCapituloBloque> modeloCapituloBloques;


    public String getTitulo() {
        return titulo;
    }

    public List<ModeloCapituloBloque> getModeloCapituloBloques() {
        return modeloCapituloBloques;
    }
}
