package com.tatanstudios.abbaappandroid.modelos.menus;

public class ModeloFragmentPlanBotonera {

    private int id;
    private String texto;

    public ModeloFragmentPlanBotonera(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public int getId() {
        return id;
    }
}
