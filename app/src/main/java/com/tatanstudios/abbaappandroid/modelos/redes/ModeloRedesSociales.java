package com.tatanstudios.abbaappandroid.modelos.redes;

import com.google.gson.annotations.SerializedName;

public class ModeloRedesSociales {


    @SerializedName("nombre")
    private String nombre;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("link")
    private String link;


    public ModeloRedesSociales(String nombre, String imagen, String link) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.link = link;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getLink() {
        return link;
    }
}
