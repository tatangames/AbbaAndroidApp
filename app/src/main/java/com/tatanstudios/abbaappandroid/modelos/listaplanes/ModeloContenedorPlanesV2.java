package com.tatanstudios.abbaappandroid.modelos.listaplanes;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesOcultos;

import java.util.List;

public class ModeloContenedorPlanesV2 {

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("listado")
    private List<ModeloMisPlanes> modeloPlanes;

    @SerializedName("listado2")
    private List<ModeloBuscarPlanes> modeloBuscarPlanes;

    @SerializedName("listado3")
    private List<ModeloPlanesCompletados> modeloPlanesCompletados;


    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }


    public List<ModeloMisPlanes> getModeloPlanes() {
        return modeloPlanes;
    }


    public List<ModeloBuscarPlanes> getModeloBuscarPlanes() {
        return modeloBuscarPlanes;
    }

    public List<ModeloPlanesCompletados> getModeloPlanesCompletados() {
        return modeloPlanesCompletados;
    }


}
