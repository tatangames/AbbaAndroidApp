package com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas;

public class ModeloVistasPreguntas {

    public int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo

    public static final int TIPO_IMAGEN = 0;
    public static final int TIPO_TITULOP = 1;
    public static final int TIPO_PREGUNTA = 2;

    public static final int TIPO_BOTON = 3;

    public ModeloPreguntas modeloPreguntas;

    public ModeloVistasPreguntas(int tipoVista, ModeloPreguntas modeloPreguntas
    ) {
        this.tipoVista = tipoVista;
        this.modeloPreguntas = modeloPreguntas;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloPreguntas getModeloPreguntas() {
        return modeloPreguntas;
    }

    public void setTipoVista(int tipoVista) {
        this.tipoVista = tipoVista;
    }


}
