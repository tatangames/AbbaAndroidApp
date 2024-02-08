package com.tatanstudios.abbaappandroid.modelos.rachas;

import com.google.gson.annotations.SerializedName;

public class ModeloRachas {

    @SerializedName("success")
    private int success;

    @SerializedName("diasesteanio")
    private int diasesteanio;
    @SerializedName("diasconcecutivos")
    private int diasconcecutivos;
    @SerializedName("nivelrachaalta")
    private int nivelrachaalta;


    @SerializedName("domingo")
    private int domingo;

    @SerializedName("lunes")
    private int lunes;

    @SerializedName("martes")
    private int martes;

    @SerializedName("miercoles")
    private int miercoles;

    @SerializedName("jueves")
    private int jueves;

    @SerializedName("viernes")
    private int viernes;

    @SerializedName("sabado")
    private int sabado;


    public ModeloRachas(int diasesteanio, int diasconcecutivos, int nivelrachaalta, int domingo, int lunes, int martes, int miercoles, int jueves, int viernes, int sabado) {
        this.diasesteanio = diasesteanio;
        this.diasconcecutivos = diasconcecutivos;
        this.nivelrachaalta = nivelrachaalta;
        this.domingo = domingo;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
    }

    public int getSuccess() {
        return success;
    }

    public int getDiasesteanio() {
        return diasesteanio;
    }

    public int getDiasconcecutivos() {
        return diasconcecutivos;
    }

    public int getNivelrachaalta() {
        return nivelrachaalta;
    }

    public int getDomingo() {
        return domingo;
    }

    public int getLunes() {
        return lunes;
    }

    public int getMartes() {
        return martes;
    }

    public int getMiercoles() {
        return miercoles;
    }

    public int getJueves() {
        return jueves;
    }

    public int getViernes() {
        return viernes;
    }

    public int getSabado() {
        return sabado;
    }
}