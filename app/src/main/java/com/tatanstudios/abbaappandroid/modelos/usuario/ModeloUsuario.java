package com.tatanstudios.abbaappandroid.modelos.usuario;

import com.google.gson.annotations.SerializedName;

public class ModeloUsuario {

    @SerializedName("success")
    public int success;

    @SerializedName("id")
    public String id;

    @SerializedName("estado")
    public int estado;

    @SerializedName("token")
    public String token;

    @SerializedName("salmo")
    public String salmo;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("apellido")
    public String apellido;

    @SerializedName("fecha_nacimiento")
    public String fechaNacimiento;

    @SerializedName("correo")
    public String correo;

    @SerializedName("fecha_nac_raw")
    public String fechaNacimientoRaw;

    @SerializedName("plancompletado")
    public int planCompletado;

    @SerializedName("letra")
    public String letra;


    @SerializedName("hayimagen")
    private int hayImagen;


    @SerializedName("imagen")
    private String imagen;


    public int getHayImagen() {
        return hayImagen;
    }

    public String getImagen() {
        return imagen;
    }

    public String getLetra() {
        return letra;
    }

    public int getPlanCompletado() {
        return planCompletado;
    }


    public int getEstado() {
        return estado;
    }

    private int tema;
    private int idiomaApp; // 1: espanol, 2: ingles

    private int tipoLetra; // tipo de letra que tiene asignado en leer devocional
    private int idiomaCel; // saber el idioma del telefono

    private int tamanoLetra; // Tamano de letra para cuestioanrios


    public int getTamanoLetra() {
        return tamanoLetra;
    }


    public void setTamanoLetra(int tamanoLetra) {
        this.tamanoLetra = tamanoLetra;
    }

    public int getIdiomaCel() {
        return idiomaCel;
    }

    public void setIdiomaCel(int idiomaCel) {
        this.idiomaCel = idiomaCel;
    }

    public int getTipoLetra() {
        return tipoLetra;
    }

    public void setTipoLetra(int tipoLetra) {
        this.tipoLetra = tipoLetra;
    }

    public int getIdiomaApp() {
        return idiomaApp;
    }

    public void setIdiomaApp(int idiomaApp) {
        this.idiomaApp = idiomaApp;
    }





    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(int tema) {
        this.tema = tema;
    }

    public String getFechaNacimientoRaw() {
        return fechaNacimientoRaw;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public int getSuccess() {
        return success;
    }


    public String getSalmo() {
        return salmo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
