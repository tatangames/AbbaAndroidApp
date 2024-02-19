package com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloBloqueFechaContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("portada")
    private String portada;

    @SerializedName("haydiaactual")
    private int hayDiaActual;

    @SerializedName("idultimobloque")
    private int idUltimoBloque;

    // esto contendra todos los bloques fechas
    @SerializedName("listado")
    public List<ModeloBloqueFecha> modeloBloqueFechas;


    @SerializedName("posrecycler")
    private int posrecycler;


    public int getPosrecycler() {
        return posrecycler;
    }

    public int getSuccess() {
        return success;
    }

    public String getPortada() {
        return portada;
    }

    public int getHayDiaActual() {
        return hayDiaActual;
    }

    public int getIdUltimoBloque() {
        return idUltimoBloque;
    }

    public List<ModeloBloqueFecha> getModeloBloqueFechas() {
        return modeloBloqueFechas;
    }
}
