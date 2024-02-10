package com.tatanstudios.abbaappandroid.modelos.comunidad;

import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesBloque1;

import java.util.List;

public class ModeloVistaComunidad {

    public int tipoVista;

    public static final int TIPO_BOTONERA = 0;

    public static final int TIPO_RECYCLER = 1;

    public static final int TIPO_NOAMIGO = 2;


    private ModeloComunidad modeloComunidad;


    public ModeloVistaComunidad(int tipoVista, ModeloComunidad modeloComunidad

    ) {
        this.tipoVista = tipoVista;
        this.modeloComunidad = modeloComunidad;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloComunidad getModeloComunidad() {
        return modeloComunidad;
    }
}
