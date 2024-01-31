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









}
