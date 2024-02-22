package com.tatanstudios.abbaappandroid.modelos.biblia.grupos;

import java.util.List;

public class ModeloGrupo {

    private int id;
    private String titulo;
    private List<ModeloSubGrupo> modeloSubGrupos;

    private boolean isExpandlabe;

    public ModeloGrupo(int id, String titulo, List<ModeloSubGrupo> modeloSubGrupos, boolean isExpandlabe) {
        this.id = id;
        this.titulo = titulo;
        this.modeloSubGrupos = modeloSubGrupos;
        this.isExpandlabe = isExpandlabe;
    }


    public int getId() {
        return id;
    }

    public boolean isExpandlabe() {
        return isExpandlabe;
    }

    public void setExpandlabe(boolean expandlabe) {
        isExpandlabe = expandlabe;
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
