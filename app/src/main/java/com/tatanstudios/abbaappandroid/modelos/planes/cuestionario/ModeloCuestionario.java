package com.tatanstudios.abbaappandroid.modelos.planes.cuestionario;

import com.google.gson.annotations.SerializedName;

public class ModeloCuestionario {

    @SerializedName("success")
    public Integer success;

    @SerializedName("id")
    public Integer id;


    @SerializedName("titulo")
    public String titulo;

    @SerializedName("texto")
    public String texto;

    public String getTexto() {
        return texto;
    }

    public Integer getSuccess() {
        return success;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}
