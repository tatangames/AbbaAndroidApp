package com.tatanstudios.abbaappandroid.modelos.comunidad;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.rachas.ModeloRachas;

import java.util.List;

public class ModeloContedorComunidad {

    @SerializedName("success")
    public int success;

    @SerializedName("hayinfo")
    public int hayinfo;


    @SerializedName("listado")
    public List<ModeloComunidad> modeloComunidads;


    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloComunidad> getModeloComunidads() {
        return modeloComunidads;
    }
}
