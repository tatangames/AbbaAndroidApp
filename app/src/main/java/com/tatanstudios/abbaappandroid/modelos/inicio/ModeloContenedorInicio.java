package com.tatanstudios.abbaappandroid.modelos.inicio;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abbaappandroid.modelos.rachas.ModeloRachas;
import com.tatanstudios.abbaappandroid.modelos.redes.ModeloRedesSociales;

import java.util.List;

public class ModeloContenedorInicio {


    @SerializedName("success")
    public int success;


    // *******************

    // RACHA DEL USUARIO


    @SerializedName("arrayracha")
    public List<ModeloRachas> modeloRachas;

    //*******************


    //******************


    @SerializedName("devohaydevocional")
    private int devohaydevocional;

    @SerializedName("devocuestionario")
    private String devocuestionario;

    @SerializedName("devoidblockdeta")
    private int devoidblockdeta;

    @SerializedName("devoplan")
    private int devoplan;



    // *********************

    @SerializedName("arrayfinalvideo")
    public List<ModeloInicioVideos> modeloInicioVideos;

    @SerializedName("videohayvideos")
    private int videohayvideos;


    // *********************

    @SerializedName("devopreguntas")
    private int devopreguntas;

    // *********************


    @SerializedName("imageneshayhoy")
    private int imageneshayhoy;


    public int getImageneshayhoy() {
        return imageneshayhoy;
    }

    @SerializedName("arrayfinalimagenes")
    public List<ModeloInicioImagenes> modeloInicioImagenes;


    //**********************


    @SerializedName("comparteappimagen")
    private String comparteappimagen;


    @SerializedName("comparteapptitulo")
    private String comparteapptitulo;

    @SerializedName("comparteappdescrip")
    private String comparteappdescrip;



    //*********************

    @SerializedName("insigniashay")
    private int insigniashay;

    @SerializedName("arrayfinalinsignias")
    public List<ModeloInicioInsignias> modeloInicioInsignias;


    //********************

    @SerializedName("videomayor5")
    private int videomayor5;

    @SerializedName("imagenesmayor5")
    private int imagenesmayor5;

    @SerializedName("insigniasmayor5")
    private int insigniasmayor5;


    //********************


    @SerializedName("hayredes")
    private int hayRedes;

    @SerializedName("arrayredes")
    public List<ModeloRedesSociales> modeloRedesSociales;


    public List<ModeloRedesSociales> getModeloRedesSociales() {
        return modeloRedesSociales;
    }

    public int getHayRedes() {
        return hayRedes;
    }

    public int getDevoplan() {
        return devoplan;
    }

    public int getDevopreguntas() {
        return devopreguntas;
    }

    public List<ModeloRachas> getModeloRachas() {
        return modeloRachas;
    }

    public int getVideomayor5() {
        return videomayor5;
    }

    public int getImagenesmayor5() {
        return imagenesmayor5;
    }

    public int getInsigniasmayor5() {
        return insigniasmayor5;
    }

    public List<ModeloInicioInsignias> getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }

    public int getInsigniashay() {
        return insigniashay;
    }

    public String getComparteappimagen() {
        return comparteappimagen;
    }

    public String getComparteapptitulo() {
        return comparteapptitulo;
    }

    public String getComparteappdescrip() {
        return comparteappdescrip;
    }

    public List<ModeloInicioImagenes> getModeloInicioImagenes() {
        return modeloInicioImagenes;
    }


    public int getVideohayvideos() {
        return videohayvideos;
    }

    public List<ModeloInicioVideos> getModeloInicioVideos() {
        return modeloInicioVideos;
    }

    public int getDevohaydevocional() {
        return devohaydevocional;
    }

    public String getDevocuestionario() {
        return devocuestionario;
    }

    public int getDevoidblockdeta() {
        return devoidblockdeta;
    }



    public int getSuccess() {
        return success;
    }


}