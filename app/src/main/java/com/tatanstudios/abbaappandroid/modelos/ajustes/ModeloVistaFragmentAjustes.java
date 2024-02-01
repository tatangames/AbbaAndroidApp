package com.tatanstudios.abbaappandroid.modelos.ajustes;

public class ModeloVistaFragmentAjustes {


    // UTILIZADO EN FRAGMENT AJUSTES

    public int tipoVista;

    public static final int TIPO_PERFIL = 0;
    public static final int TIPO_ITEM_NORMAL = 1;
    public static final int TIPO_LINEA_SEPARACION = 2;

    public ModeloFragmentPerfil modeloFragmentPerfil;
    public ModeloFragmentConfiguracion modeloFragmentConfiguracion;

    public ModeloVistaFragmentAjustes(int tipoVista,
                                      ModeloFragmentPerfil modeloFragmentPerfil,
                                      ModeloFragmentConfiguracion modeloFragmentConfiguracion

    ) {
        this.tipoVista = tipoVista;
        this.modeloFragmentPerfil = modeloFragmentPerfil;
        this.modeloFragmentConfiguracion = modeloFragmentConfiguracion;
    }


    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloFragmentPerfil getModeloFragmentPerfil() {
        return modeloFragmentPerfil;
    }

    public ModeloFragmentConfiguracion getModeloFragmentConfiguracion() {
        return modeloFragmentConfiguracion;
    }
}
