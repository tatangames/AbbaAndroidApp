package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloMisPlanesPaginate<T> {

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("listado")
    private T data;
    @SerializedName("haycontinuar")
    private int haycontinuar;


    @SerializedName("listacontinuar")
    private List<ModeloMisPlanesBloque1> modeloMisPlanesBloque1;

    public List<ModeloMisPlanesBloque1> getModeloMisPlanesBloque1() {
        return modeloMisPlanesBloque1;
    }

    public int getHaycontinuar() {
        return haycontinuar;
    }

    public T getData() {
        return data;
    }

    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }

}
