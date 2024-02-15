package com.tatanstudios.abbaappandroid.modelos.notificacion;

import com.google.gson.annotations.SerializedName;

public class ModeloListaNotificacionPaginate<T> {

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("listado")
    private T data;



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
