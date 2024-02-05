package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesBloque2 {

    // MODELO TODOS LOS PLANES QUE NO LLEVO CONTINUAR

    @SerializedName("id_planes")
    private int idPlanes;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("subtitulo")
    private String subtitulo;


    public ModeloMisPlanesBloque2(int idPlanes, String titulo, String imagen, String subtitulo) {
        this.idPlanes = idPlanes;
        this.titulo = titulo;
        this.imagen = imagen;
        this.subtitulo = subtitulo;
    }

    public int getIdPlanes() {
        return idPlanes;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public String getSubtitulo() {
        return subtitulo;
    }
}
