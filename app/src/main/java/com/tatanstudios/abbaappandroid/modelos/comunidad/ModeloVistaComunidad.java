package com.tatanstudios.abbaappandroid.modelos.comunidad;

import java.util.List;

public class ModeloVistaComunidad {

    public int tipoVista;

    public static final int TIPO_BOTONERA = 0;

    public static final int TIPO_RECYCLER = 1;


    private List<ModeloComunidad> modeloComunidad;


    public ModeloVistaComunidad(int tipoVista, List<ModeloComunidad> modeloComunidad

    ) {
        this.tipoVista = tipoVista;
        this.modeloComunidad = modeloComunidad;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public List<ModeloComunidad> getModeloComunidad() {
        return modeloComunidad;
    }
}
