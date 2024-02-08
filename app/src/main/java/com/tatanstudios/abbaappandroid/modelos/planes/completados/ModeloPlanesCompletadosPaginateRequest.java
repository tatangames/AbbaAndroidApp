package com.tatanstudios.abbaappandroid.modelos.planes.completados;

import com.google.gson.annotations.SerializedName;

public class ModeloPlanesCompletadosPaginateRequest {

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("iduser")
    private String iduser;

    @SerializedName("idiomaplan")
    private int idiomaplan;


    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public void setIdiomaplan(int idiomaplan) {
        this.idiomaplan = idiomaplan;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }


    public int getIdiomaplan() {
        return idiomaplan;
    }
}
