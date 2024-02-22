package com.tatanstudios.abbaappandroid.modelos.biblia.versiculo;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.biblia.ModeloBiblia;

import java.util.List;

public class ModeloContenedorVersiculo {

    @SerializedName("success")
    public int success;

    @SerializedName("listado")
    public List<ModeloVersiculo> modeloVersiculos;


    public int getSuccess() {
        return success;
    }

    public List<ModeloVersiculo> getModeloVersiculos() {
        return modeloVersiculos;
    }
}
