package com.tatanstudios.abbaappandroid.modelos.comunidad;

import com.google.gson.annotations.SerializedName;

public class ModeloComunidad {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("iglesia")
    private String iglesia;

    @SerializedName("correo")
    private String correo;

    @SerializedName("pais")
    private String pais;

    @SerializedName("idpais")
    private int idpais;

    @SerializedName("fecha")
    private String fecha;

    public ModeloComunidad(int id, String nombre, String iglesia, String correo, String pais, int idpais) {
        this.id = id;
        this.nombre = nombre;
        this.iglesia = iglesia;
        this.correo = correo;
        this.pais = pais;
        this.idpais = idpais;
    }


    public String getFecha() {
        return fecha;
    }

    public int getIdpais() {
        return idpais;
    }

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
