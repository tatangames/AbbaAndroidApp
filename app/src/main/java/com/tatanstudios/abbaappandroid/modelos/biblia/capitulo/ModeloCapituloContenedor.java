package com.tatanstudios.abbaappandroid.modelos.biblia.capitulo;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.biblia.ModeloBiblia;

import java.util.List;

public class ModeloCapituloContenedor {

    @SerializedName("success")
    public int success;

    @SerializedName("listado")
    public List<ModeloCapitulo> modeloCapitulos;


    public int getSuccess() {
        return success;
    }

    public List<ModeloCapitulo> getModeloCapitulos() {
        return modeloCapitulos;
    }
}
