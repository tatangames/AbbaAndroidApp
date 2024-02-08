package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioVideos {

    @SerializedName("id")
    private int id;

    @SerializedName("id_tipo_video")
    private int id_tipo_video;

    @SerializedName("url_video")
    private String url_video;

    @SerializedName("posicion")
    private int posicion;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("titulo")
    private String titulo;


    public ModeloInicioVideos(int id, int id_tipo_video, String url_video, int posicion, String imagen, String titulo) {
        this.id = id;
        this.id_tipo_video = id_tipo_video;
        this.url_video = url_video;
        this.posicion = posicion;
        this.imagen = imagen;
        this.titulo = titulo;
    }


    public int getId() {
        return id;
    }

    public int getId_tipo_video() {
        return id_tipo_video;
    }

    public String getUrl_video() {
        return url_video;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTitulo() {
        return titulo;
    }
}