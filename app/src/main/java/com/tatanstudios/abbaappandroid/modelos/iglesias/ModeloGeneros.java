package com.tatanstudios.abbaappandroid.modelos.iglesias;

public class ModeloGeneros {

    private int id;
    private String nombre;

    public ModeloGeneros(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}
