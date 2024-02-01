package com.tatanstudios.abbaappandroid.network;

import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloDepartamentos;
import com.tatanstudios.abbaappandroid.modelos.usuario.ModeloUsuario;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {


    // inicio de sesion
    @POST("app/login")
    @FormUrlEncoded
    Observable<ModeloUsuario> inicioSesion(@Field("correo") String correo,
                                           @Field("password") String password,
                                           @Field("idonesignal") String idonesignal);



    @POST("app/solicitar/listado/iglesias")
    @FormUrlEncoded
    Observable<ModeloDepartamentos> solicitarListadoIglesias(@Field("iddepa") int idDepartamento);


    // registrarse
    @POST("app/registro/usuario")
    @FormUrlEncoded
    Observable<ModeloUsuario> registroUsuario(@Field("nombre") String nombre,
                                              @Field("apellido") String apellido,
                                              @Field("edad") String fechaNacimiento,
                                              @Field("genero") int idGenero,
                                              @Field("iglesia") int idIglesia,
                                              @Field("correo") String correo,
                                              @Field("password") String password,
                                              @Field("idonesignal") String idonesignal,
                                              @Field("version") String version);


    // solicitar codigo para recuperacion de contraseña
    @POST("app/solicitar/codigo/contrasena")
    @FormUrlEncoded
    Observable<ModeloUsuario> solicitarCodigoPassword(@Field("correo") String correo,
                                                      @Field("idioma") int idioma);



    // verificar en servidor si codigo y correo coinciden
    @POST("app/verificar/codigo/recuperacion")
    @FormUrlEncoded
    Observable<ModeloUsuario> verificarCodigoCorreo(@Field("codigo") String codigo,
                                                    @Field("correo") String correo);


    // actualizar una nueva contrasena, con usuario se obtiene usuario
    @POST("app/actualizar/nueva/contrasena/reseteo")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPasswordReseteo(@Field("password") String password);

    // informacion del perfil del usuario
    @POST("app/solicitar/listado/opcion/perfil")
    @FormUrlEncoded
    Observable<ModeloUsuario> informacionListadoAjuste(@Field("iduser") String idUsuario);


    // actualizar contrasena en editar perfil
    @POST("app/actualizar/contrasena/")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPassword(@Field("iduser") String iduser,
                                                 @Field("password") String password);











}
