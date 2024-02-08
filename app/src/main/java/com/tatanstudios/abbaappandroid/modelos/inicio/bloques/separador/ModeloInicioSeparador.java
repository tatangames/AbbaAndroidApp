package com.tatanstudios.abbaappandroid.modelos.inicio.bloques.separador;

public class ModeloInicioSeparador {

    private int hayMasDe5Videos;
    private int hayMasDe5Imagenes;
    private int hayMasDe5Insignias;


    public ModeloInicioSeparador(int hayMasDe5Videos, int hayMasDe5Imagenes, int hayMasDe5Insignias) {
        this.hayMasDe5Videos = hayMasDe5Videos;
        this.hayMasDe5Imagenes = hayMasDe5Imagenes;
        this.hayMasDe5Insignias = hayMasDe5Insignias;
    }

    public int getHayMasDe5Videos() {
        return hayMasDe5Videos;
    }

    public int getHayMasDe5Imagenes() {
        return hayMasDe5Imagenes;
    }

    public int getHayMasDe5Insignias() {
        return hayMasDe5Insignias;
    }


}
