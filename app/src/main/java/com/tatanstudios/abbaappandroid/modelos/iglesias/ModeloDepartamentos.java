package com.tatanstudios.abbaappandroid.modelos.iglesias;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloDepartamentos {

    @SerializedName("success")
    private int success;

    @SerializedName("listado")
    private List<ModeloIglesias> modeloIglesias;

    private int id;
    private String nombre;

    public ModeloDepartamentos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public int getSuccess() {
        return success;
    }

    public List<ModeloIglesias> getModeloIglesias() {
        return modeloIglesias;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}
