package com.tatanstudios.abbaappandroid.network;

import android.content.SharedPreferences;

import com.tatanstudios.abbaappandroid.modelos.usuario.ModeloUsuario;

public class TokenManager {

    // GUARDAMOS DATOS DENTRO DE LA APP

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }


    // ID DEL USUARIO
    public void guardarClienteID(ModeloUsuario token) {
        editor.putString("ID", token.getId()).commit();
    }

    // TOKEN DE SEGURIDAD DEL SERVIDOR
    public void guardarClienteTOKEN(ModeloUsuario token) {
        editor.putString("TOKEN", token.getToken()).commit();
    }

    // ESTILO DE TEMA ELEGIDO
    public void guardarEstiloTema(int code) {
        editor.putInt("TEMA", code).commit();
    }

    // IDIOMA DE LA APLICACION
    public void guardarIdiomaApp(int code) {
        editor.putInt("IDIOMAAPP", code).commit();
    }

    // IDIOMA QUE VERA LOS TEXTOS TRAIDOS DEL SERVIDOR
    public void guardarIdiomaTexto(int code) {
        editor.putInt("IDIOMATEXTO", code).commit();
    }

    // TIPO DE LETRA PARA LEER DEVOCIONALES
    public void guardarTipoLetraTexto(int code) {
        editor.putInt("TIPOTEXTO", code).commit();
    }


    // SI EL IDIOMA YA FUE CAMBIADO, PARA NO PREGUNTAR IDIOMA DEL TELEFONO POR DEFECTO
    public void guardarIdiomaTelefono(int code) {
        editor.putInt("IDIOMACEL", code).commit();
    }

    // TAMANO DE LETRA PARA CUESTIONARIOS
    public void guardarTamanoLetraCuestionario(int tamano) {
            editor.putInt("TAMANOLETRA", tamano).commit();
    }


    // BORRAR UNAS REFERENCIAS
    public void deletePreferences(){
        editor.remove("ID").commit();
        editor.remove("TOKEN").commit();
    }

    public ModeloUsuario getToken(){
        ModeloUsuario token = new ModeloUsuario();
        token.setId(prefs.getString("ID", ""));
        token.setTema(prefs.getInt("TEMA", 0));
        token.setIdiomaApp(prefs.getInt("IDIOMAAPP", 0));
        token.setIdiomaTextos(prefs.getInt("IDIOMATEXTO", 0));
        token.setToken(prefs.getString("TOKEN", ""));
        token.setTipoLetra(prefs.getInt("TIPOTEXTO", 0));
        token.setIdiomaCel(prefs.getInt("IDIOMACEL", 0));
        token.setTamanoLetra(prefs.getInt("TAMANOLETRA", 0));

        return token;
    }

}


