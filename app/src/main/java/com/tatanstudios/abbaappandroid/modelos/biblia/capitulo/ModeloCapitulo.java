package com.tatanstudios.abbaappandroid.modelos.biblia.capitulo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloCapitulo {


    @SerializedName("id")
    public int id;

    // UNICAMENTE TITULO
    @SerializedName("titulo")
    public String titulo;

    private boolean isExpandlabe = false;


    public boolean isExpandlabe() {
        return isExpandlabe;
    }

    public void setExpandlabe(boolean expandlabe) {
        isExpandlabe = expandlabe;
    }

    public int getId() {
        return id;
    }

    @SerializedName("detalle")
    public List<ModeloCapituloBloque> modeloCapituloBloques;


    public String getTitulo() {
        return titulo;
    }

    public List<ModeloCapituloBloque> getModeloCapituloBloques() {
        return modeloCapituloBloques;
    }
}
