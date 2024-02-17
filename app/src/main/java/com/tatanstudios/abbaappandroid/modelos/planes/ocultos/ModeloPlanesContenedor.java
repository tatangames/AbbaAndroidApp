package com.tatanstudios.abbaappandroid.modelos.planes.ocultos;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;

import java.util.List;

public class ModeloPlanesContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;


    @SerializedName("listado")
    private List<ModeloPlanesOcultos> modeloPlanesOcultos;


    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloPlanesOcultos> getModeloPlanesOcultos() {
        return modeloPlanesOcultos;
    }
}
