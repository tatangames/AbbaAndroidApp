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


    @SerializedName("devocional")
    public String devocional;

    @SerializedName("redireccionar")
    private int redireccionar;

    @SerializedName("iddevobiblia")
    private int iddevobiblia;


    public ModeloCuestionario(Integer id, String titulo, String texto) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
    }


    public int getIddevobiblia() {
        return iddevobiblia;
    }

    public int getRedireccionar() {
        return redireccionar;
    }

    public String getDevocional() {
        return devocional;
    }

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
