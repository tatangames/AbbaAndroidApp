package com.tatanstudios.abbaappandroid.modelos.planes.cuestionario;

public class ModeloTipoLetraCuestionario {

    private int id;
    private String nombre;

    public ModeloTipoLetraCuestionario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
