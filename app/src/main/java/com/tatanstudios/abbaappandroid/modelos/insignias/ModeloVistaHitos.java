package com.tatanstudios.abbaappandroid.modelos.insignias;

import java.util.List;

public class ModeloVistaHitos {


    public int tipoVista;

    public static final int TIPO_IMAGEN = 0;

    public static final int TIPO_RECYCLER = 1;


    private ModeloDescripcionHitos modeloDescripcionHitos;

    private List<ModeloInsigniaHitos> modeloInsigniaHitos;



    public ModeloVistaHitos(int tipoVista, ModeloDescripcionHitos modeloDescripcionHitos,
                            List<ModeloInsigniaHitos> modeloInsigniaHitos

    ) {
        this.tipoVista = tipoVista;
        this.modeloDescripcionHitos = modeloDescripcionHitos;
        this.modeloInsigniaHitos = modeloInsigniaHitos;
    }

    public int getTipoVista() {
        return tipoVista;
    }


    public ModeloDescripcionHitos getModeloDescripcionHitos() {
        return modeloDescripcionHitos;
    }

    public List<ModeloInsigniaHitos> getModeloInsigniaHitos() {
        return modeloInsigniaHitos;
    }
}

