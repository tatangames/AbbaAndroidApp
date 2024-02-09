package com.tatanstudios.abbaappandroid.network;

import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloDepartamentos;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloContenedorInicio;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanesPaginate;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanesPaginateMetaDatos;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanesPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletadosPaginate;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletadosPaginateMetaDatos;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletadosPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.ModeloCuestionario;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntasContenedor;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesPaginate;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesPaginateMetaDatos;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFechaContenedor;
import com.tatanstudios.abbaappandroid.modelos.usuario.ModeloUsuario;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    // informacion de un perfil
    @POST("app/solicitar/informacion/perfil")
    @FormUrlEncoded
    Observable<ModeloUsuario> informacionPerfil(@Field("iduser") String idUsuario);


    // editar la informacion del perfil
    @POST("app/actualizar/perfil/usuario")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPerfilUsuario(@Field("iduser") String idUsuario,
                                                      @Field("nombre") String nombre,
                                                      @Field("apellido") String apellido,
                                                      @Field("fechanac") String fechaNacimiento,
                                                      @Field("correo") String correo);



    // listado de planes nuevos con paginacion
    @POST("app/buscar/planes/nuevos")
    Observable<ModeloBuscarPlanesPaginate<ModeloBuscarPlanesPaginateMetaDatos>> listadoNuevosPlanes(
            @Body ModeloBuscarPlanesPaginateRequest request);


    // ver informacion de un plan para poder seleccionarlo
    @POST("app/plan/seleccionado/informacion")
    @FormUrlEncoded
    Observable<ModeloBuscarPlanes> informacionPlanSeleccionado(@Field("idplan") int idplan,
                                                               @Field("idiomaplan") int idiomaplan);


    // seleccionar plan nuevo
    @POST("app/plan/nuevo/seleccionar")
    @FormUrlEncoded
    Observable<ModeloUsuario> seleccionarPlanNuevo(@Field("idplan") int idplan,
                                                   @Field("iduser") String iduser);


    // listado de mis planes
    @POST("app/plan/listado/misplanes")
    Observable<ModeloMisPlanesPaginate<ModeloMisPlanesPaginateMetaDatos>> listadoMisPlanes(
            @Body ModeloMisPlanesPaginateRequest request);


    // informacion de bloque fechas de un plan
    @POST("app/plan/misplanes/informacion/bloque")
    @FormUrlEncoded
    Observable<ModeloBloqueFechaContenedor> informacionPlanBloque(@Field("iduser") String iduser,
                                                                  @Field("idiomaplan") int idiomaplan,
                                                                  @Field("idplan") int idplan);

    // actualizar check de bloque detalle fecha
    @POST("app/plan/misplanes/actualizar/check")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarBloqueFechaCheckbox(@Field("iduser") String iduser,
                                                          @Field("idblockdeta") int idBlockDeta,
                                                          @Field("valor") int valor,
                                                          @Field("idplan") int idplan);


    // buscar informacion del cuestionario de cada bloque detalle
    @POST("app/plan/misplanes/cuestionario/bloque")
    @FormUrlEncoded
    Observable<ModeloCuestionario> informacionCuestionarioBloqueDetalle(@Field("iduser") String iduser,
                                                                        @Field("idblockdeta") int idBlockDeta,
                                                                        @Field("idiomaplan") int idiomaplan);

    // listado de todas las preguntas
    @POST("app/plan/misplanes/preguntas/bloque")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> informacionPreguntasBloqueDetalle(@Field("iduser") String iduser,
                                                                            @Field("idblockdeta") int idBlockDeta,
                                                                            @Field("idiomaplan") int idioma);


    @POST("app/plan/misplanes/preguntas/usuario/actualizar")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> actualizarPreguntasUsuarioPlanes(@Field("iduser") String iduser,
                                                                           @Field("idblockdeta") int idBlockDeta,
                                                                           @FieldMap Map<String, String> listado);



    // informacion de listado de preguntas para compartirlas
    @POST("app/plan/misplanes/preguntas/infocompartir")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> infoPreguntasTextosParaCompartir(@Field("iduser") String iduser,
                                                                            @Field("idblockdeta") int idBlockDeta,
                                                                            @Field("idiomaplan") int idioma);


    // listado de planes completados
    @POST("app/plan/misplanes/completados")
    Observable<ModeloPlanesCompletadosPaginate<ModeloPlanesCompletadosPaginateMetaDatos>> listadoPlanesCompletados(
            @Body ModeloPlanesCompletadosPaginateRequest request);


    // informacion de tods el inicio
    @POST("app/inicio/bloque/completa")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> informacionBloqueInicio(@Field("iduser") String iduser,
                                                               @Field("idiomaplan") int idiomaplan);


    // obtener listado de todos los videos
    @POST("app/inicio/todos/losvideos")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLosVideos(@Field("iduser") String iduser,
                                                             @Field("idiomaplan") int idiomaplan);


    // obtener listado de todos las imagenes
    @POST("app/inicio/todos/lasimagenes")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLasImagenes(@Field("iduser") String iduser);


    // guardar texto de preguntas, sino esta el plan asociado, se asociara al usuario
    @POST("app/plan/inicio/preguntas/guardar/actualizar")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> guardarPreguntasCuestionarioInicio(@Field("iduser") String iduser,
                                                                             @Field("idblockdeta") int idBlockDeta,
                                                                             @FieldMap Map<String, String> listado);


}
