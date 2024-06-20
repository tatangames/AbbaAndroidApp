package com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas;

import com.google.gson.annotations.SerializedName;

public class ModeloPreguntas {

    @SerializedName("id")
    private int id;

    @SerializedName("id_plan_block_detalle")
    private int idPlanBlockDetalle;

    @SerializedName("visible")
    private int visible;

    @SerializedName("posicion")
    private int posicion;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("texto")
    private String texto;

    @SerializedName("respuesta")
    private String respuesta;

    @SerializedName("id_imagen_pregunta")
    private int idImagenPregunta;


    public ModeloPreguntas(int id,  String titulo, String texto, int imgPregunta, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.idImagenPregunta = imgPregunta;
        this.imagen = imagen;
    }


    public String getRespuesta() {
        return respuesta;
    }

    public String getImagen() {
        return imagen;
    }

    public int getIdImagenPregunta() {
        return idImagenPregunta;
    }

    public String getTexto() {
        return texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlanBlockDetalle() {
        return idPlanBlockDetalle;
    }

    public void setIdPlanBlockDetalle(int idPlanBlockDetalle) {
        this.idPlanBlockDetalle = idPlanBlockDetalle;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}