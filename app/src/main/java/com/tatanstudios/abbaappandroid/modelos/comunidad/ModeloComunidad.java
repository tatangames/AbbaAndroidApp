package com.tatanstudios.abbaappandroid.modelos.comunidad;

import com.google.gson.annotations.SerializedName;

public class ModeloComunidad {

    @SerializedName("success")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("iglesia")
    private String iglesia;

    @SerializedName("correo")
    private String correo;

    @SerializedName("pais")
    private String pais;


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIglesia() {
        return iglesia;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPais() {
        return pais;
    }
}
