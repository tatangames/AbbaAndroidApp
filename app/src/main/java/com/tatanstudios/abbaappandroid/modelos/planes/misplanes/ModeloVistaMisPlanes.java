package com.tatanstudios.abbaappandroid.modelos.planes.misplanes;

public class ModeloVistaMisPlanes {

    // PARA 2 VISTAS: VISTA CONTINUAR, Y LA LISTA DE PLANES

    private int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo
    public static final int TIPO_CONTINUAR = 0;
    public static final int TIPO_PLANES = 1;

    private ModeloMisPlanesBloque1 modeloMisPlanesBloque1;
    private ModeloMisPlanesBloque2 modeloMisPlanesBloque2;

    public ModeloVistaMisPlanes(int tipoVista, ModeloMisPlanesBloque1 modeloMisPlanesBloque1,
                                ModeloMisPlanesBloque2 modeloMisPlanesBloque2) {
        this.tipoVista = tipoVista;
        this.modeloMisPlanesBloque1 = modeloMisPlanesBloque1;
        this.modeloMisPlanesBloque2 = modeloMisPlanesBloque2;
    }


    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloMisPlanesBloque1 getModeloMisPlanesBloque1() {
        return modeloMisPlanesBloque1;
    }

    public ModeloMisPlanesBloque2 getModeloMisPlanesBloque2() {
        return modeloMisPlanesBloque2;
    }




}
