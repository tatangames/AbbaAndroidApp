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

    @SerializedName("usuariobuscado")
    private int usuariobuscado;

    @SerializedName("listado")
    private List<ModeloPlanesOcultos> modeloPlanesOcultos;

    @SerializedName("listadoplan")
    private List<ModeloMisPlanes> modeloMisPlanes;


    public List<ModeloMisPlanes> getModeloMisPlanes() {
        return modeloMisPlanes;
    }


    public int getUsuariobuscado() {
        return usuariobuscado;
    }

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
