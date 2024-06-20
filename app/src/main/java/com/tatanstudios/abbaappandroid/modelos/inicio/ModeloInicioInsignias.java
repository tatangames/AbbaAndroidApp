package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioInsignias {

    @SerializedName("id")
    private int id;

    @SerializedName("id_tipo_insignia")
    private int idTipoInsignia;

    @SerializedName("id_usuario")
    private int idUsuario;


    @SerializedName("fecha")
    private String fecha;

    @SerializedName("nivel")
    private int nivel;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("nivelvoy")
    private int nivelVoy;


    @SerializedName("hitohaynextlevel")
    private int hitohaynextlevel;


    @SerializedName("hitocuantofalta")
    private int hitocuantofalta;


    @SerializedName("imageninsignia")
    private String imageninsignia;

    @SerializedName("imagen")
    private String imagen;


    public int getNivel() {
        return nivel;
    }

    public ModeloInicioInsignias(int id, String titulo, String descripcion, int nivelVoy, String imageninsignia) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelVoy = nivelVoy;
        this.imageninsignia = imageninsignia;
    }


    public String getImagen() {
        return imagen;
    }

    public int getHitohaynextlevel() {
        return hitohaynextlevel;
    }

    public int getHitocuantofalta() {
        return hitocuantofalta;
    }

    public String getImageninsignia() {
        return imageninsignia;
    }

    public int getId() {
        return id;
    }

    public int getIdTipoInsignia() {
        return idTipoInsignia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNivelVoy() {
        return nivelVoy;
    }
}
