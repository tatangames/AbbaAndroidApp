package com.tatanstudios.abbaappandroid.modelos.biblia.grupos;

import java.util.List;

public class ModeloGrupo {

    private String titulo;
    private List<ModeloSubGrupo> modeloSubGrupos;

    public ModeloGrupo(String titulo, List<ModeloSubGrupo> modeloSubGrupos) {
        this.titulo = titulo;
        this.modeloSubGrupos = modeloSubGrupos;
    }

    public List<ModeloSubGrupo> getModeloSubGrupos() {
        return modeloSubGrupos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
