package com.tatanstudios.abbaappandroid.modelos.insignias.faltantes;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;

import java.util.List;

public class ModeloInsigniaFaltantesContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;


    @SerializedName("listado")
    private List<ModeloInicioInsignias> modeloInicioInsignias;


    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloInicioInsignias> getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }
}
