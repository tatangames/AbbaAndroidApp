package com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloBloqueFechaContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("portada")
    private String portada;


    // esto contendra todos los bloques fechas
    @SerializedName("listado")
    public List<ModeloBloqueFecha> modeloBloqueFechas;


    public int getSuccess() {
        return success;
    }

    public String getPortada() {
        return portada;
    }


    public List<ModeloBloqueFecha> getModeloBloqueFechas() {
        return modeloBloqueFechas;
    }
}
