package com.tatanstudios.abbaappandroid.modelos.ajustes;

public class ModeloFragmentPerfil {

    private String letra;
    private String nombrePerfil;


    public ModeloFragmentPerfil(String letra, String nombrePerfil) {
        this.letra = letra;
        this.nombrePerfil = nombrePerfil;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }
}
