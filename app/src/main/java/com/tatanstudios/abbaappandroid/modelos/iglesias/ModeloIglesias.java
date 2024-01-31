package com.tatanstudios.abbaappandroid.modelos.iglesias;

public class ModeloIglesias {

    private int id;
    private String nombre;

    public ModeloIglesias(int id, String nombre) {
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
