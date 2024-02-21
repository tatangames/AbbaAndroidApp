package com.tatanstudios.abbaappandroid.modelos.biblia;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;

import java.util.List;

public class ModeloBibliaContenedor {

    @SerializedName("success")
    public int success;

    @SerializedName("hayinfo")
    public int hayinfo;


    @SerializedName("listado")
    public List<ModeloBiblia> modeloBiblias;



    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloBiblia> getModeloBiblias() {
        return modeloBiblias;
    }
}
