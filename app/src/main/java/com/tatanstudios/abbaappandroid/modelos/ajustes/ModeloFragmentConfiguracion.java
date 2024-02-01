package com.tatanstudios.abbaappandroid.modelos.ajustes;

public class ModeloFragmentConfiguracion {

    private String nombre;
    private int identificador;


    public ModeloFragmentConfiguracion(int identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

}
