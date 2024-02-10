package com.tatanstudios.abbaappandroid.modelos.insignias;

public class ModeloDescripcionHitos {

    private String imagen;

    private String titulo;

    private String descripcion;

    private int nivelvoy;


    public ModeloDescripcionHitos(String imagen, String titulo, String descripcion, int nivelvoy) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelvoy = nivelvoy;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public int getNivelvoy() {
        return nivelvoy;
    }
}
