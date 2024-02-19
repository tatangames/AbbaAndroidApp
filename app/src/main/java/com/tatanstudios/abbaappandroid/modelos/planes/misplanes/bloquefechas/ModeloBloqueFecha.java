package com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloBloqueFecha {

    @SerializedName("id")
    private int id;

    @SerializedName("id_planes")
    private int id_planes;

    @SerializedName("texto_personalizado")
    private int textoPersonalizado;

    @SerializedName("abreviatura")
    private String abreviatura;

    @SerializedName("contador")
    private int contador;

    @SerializedName("mismodia")
    private int mismoDia;

    @SerializedName("textopersonalizado")
    private String txtPersonalizado;

    @SerializedName("detalle")
    public List<ModeloBloqueFechaDetalle> modeloBloqueFechas;


    // ******* EXTRAS


    private boolean estaPresionado;

    private boolean primerBloqueDrawable = true;


    public boolean getPrimerBloqueDrawable() {
        return primerBloqueDrawable;
    }

    public void setPrimerBloqueDrawable(boolean primerBloqueDrawable) {
        this.primerBloqueDrawable = primerBloqueDrawable;
    }





    public boolean getEstaPresionado() {
        return estaPresionado;
    }

    public void setEstaPresionado(boolean estaPresionado) {
        this.estaPresionado = estaPresionado;
    }





    public int getId() {
        return id;
    }

    public int getId_planes() {
        return id_planes;
    }

    public int getTextoPersonalizado() {
        return textoPersonalizado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public int getContador() {
        return contador;
    }

    public int getMismoDia() {
        return mismoDia;
    }

    public String getTxtPersonalizado() {
        return txtPersonalizado;
    }

    public List<ModeloBloqueFechaDetalle> getModeloBloqueFechas() {
        return modeloBloqueFechas;
    }
}
