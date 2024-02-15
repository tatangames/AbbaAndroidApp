package com.tatanstudios.abbaappandroid.modelos.amigos;

public class ModeloAmigos {

    // sila solicitud
    private int idsolicitud;

    // id quien dara los puntos
    private int idusuario;

    public ModeloAmigos(int idsolicitud, int idusuario) {
        this.idsolicitud = idsolicitud;
        this.idusuario = idusuario;
    }

    public int getIdsolicitud() {
        return idsolicitud;
    }

    public void setIdsolicitud(int idsolicitud) {
        this.idsolicitud = idsolicitud;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }
}
