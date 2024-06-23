package com.tatanstudios.abbaappandroid.network;

import com.tatanstudios.abbaappandroid.modelos.biblia.ModeloBibliaContenedor;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapituloContenedor;
import com.tatanstudios.abbaappandroid.modelos.biblia.versiculo.ModeloContenedorVersiculo;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloContedorComunidad;
import com.tatanstudios.abbaappandroid.modelos.devocapitulos.ModeloDevoCapitulo;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloDepartamentos;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloContenedorInicio;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloContenedorInsignias;
import com.tatanstudios.abbaappandroid.modelos.insignias.faltantes.ModeloInsigniaFaltantesContenedor;
import com.tatanstudios.abbaappandroid.modelos.listaplanes.ModeloContenedorPlanesV2;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacionPaginate;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacionPaginateMetaDato;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacionPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.ModeloCuestionario;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntasContenedor;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFechaContenedor;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesContenedor;
import com.tatanstudios.abbaappandroid.modelos.usuario.ModeloUsuario;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
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
                                           @Field("password") String password);



    @POST("app/solicitar/listado/iglesias")
    @FormUrlEncoded
    Observable<ModeloDepartamentos> solicitarListadoIglesias(@Field("iddepa") int idDepartamento);


    // registrarse
    @POST("app/registro/usuario/v2")
    @FormUrlEncoded
    Observable<ModeloUsuario> registroUsuario(@Field("nombre") String nombre,
                                              @Field("apellido") String apellido,
                                              @Field("edad") String fechaNacimiento,
                                              @Field("genero") int idGenero,
                                              @Field("iglesia") int idIglesia,
                                              @Field("correo") String correo,
                                              @Field("password") String password,
                                              @Field("idonesignal") String idonesignal,
                                              @Field("version") String version,
                                              @Field("paisotros") String paisotros,
                                              @Field("ciudadotros") String ciudadotros
                                              );


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
    @POST("app/actualizar/contrasena")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPassword(@Field("iduser") String iduser,
                                                 @Field("password") String password);

    // informacion de un perfil
    @POST("app/solicitar/informacion/perfil")
    @FormUrlEncoded
    Observable<ModeloUsuario> informacionPerfil(@Field("iduser") String idUsuario);


    @POST("app/actualizar/perfil/usuario")
    Observable<ModeloUsuario> actualizarPerfilUsuario(@Body RequestBody body
    );


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
                                                          @Field("idplan") int idplan,
                                                          @Field("idiomaplan") int idiomaplan);


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
                                                                           @Field("idiomaplan") int idioma,
                                                                           @FieldMap Map<String, String> listado);



    // informacion de listado de preguntas para compartirlas
    @POST("app/plan/misplanes/preguntas/infocompartir")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> infoPreguntasTextosParaCompartir(@Field("iduser") String iduser,
                                                                            @Field("idblockdeta") int idBlockDeta,
                                                                            @Field("idiomaplan") int idioma);



    // informacion de tods el inicio
    @POST("app/inicio/bloque/completa")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> informacionBloqueInicio(@Field("iduser") String iduser,
                                                               @Field("idiomaplan") int idiomaplan,
                                                               @Field("idonesignal") String idonesignal);


    // obtener listado de todos los videos
    @POST("app/inicio/todos/losvideos")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLosVideos(@Field("iduser") String iduser,
                                                             @Field("idiomaplan") int idiomaplan);


    // obtener listado de todos las imagenes
    @POST("app/inicio/todos/lasimagenes")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLasImagenes(@Field("iduser") String iduser);




    // informacion de la insignia seleccionada
    @POST("app/insignia/individual/informacion")
    @FormUrlEncoded
    Observable<ModeloContenedorInsignias> informacionInsigniaSeleccionada(@Field("iduser") String iduser,
                                                                          @Field("idiomaplan") int idiomaplan,
                                                                          @Field("idinsignia") int idinsignia);


    // informacion de niveles de insignias
    @POST("app/insignia/niveles/informacion")
    @FormUrlEncoded
    Observable<ModeloContenedorInsignias> listadoNivelesInsigniasInfo(@Field("idtipoinsignia") int idtipoinsignia);







    // obtener listado de todas las insignias
    @POST("app/inicio/todos/lasinsignias")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLasInsignias(@Field("iduser") String iduser,
                                                                @Field("idiomaplan") int idiomaplan);




    // obtener listado de todas las insignias faltantes por ganar
    @POST("app/listado/insignias/faltantes")
    @FormUrlEncoded
    Observable<ModeloInsigniaFaltantesContenedor> obtenerTodosLasInsigniasFaltantes(@Field("iduser") String iduser,
                                                                                    @Field("idiomaplan") int idiomaplan);




    // listado de solicitudes de comunidad aceptados
    @POST("app/comunidad/listado/solicitud/aceptadas")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> listadoComunidadAceptado(@Field("iduser") String iduser);



    // enviar solicitud a otro usuario para ver su info
    @POST("app/comunidad/enviar/solicitud")
    @FormUrlEncoded
    Observable<ModeloUsuario> enviarSolicitudComunidad(@Field("iduser") String iduser,
                                                       @Field("correo") String correo);



    // elimina solicitud ya sea aceptada o pendiente
    @POST("app/comunidad/solicitud/eliminar")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> borrarSolicitudPendiente(@Field("iduser") String iduser,
                                                                 @Field("idsolicitud") int idsolicitud);


    // compartir aplicacion, solo para insginia
    @POST("app/compartir/aplicacion")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> compartirApp(@Field("iduser") String iduser,
                                                     @Field("idiomaplan") int idiomaplan);


    // compartir devocional
    // AQUI SE UTILIZA EN PANTALLAS BOTON COMPARTIR EN 2
    // FragmentPreguntasPlanBloque

    // AL LLENAR TODOS LO CAMPOS Y DARLE AL BOTON COMPARTIR, MUESTRA LA PANTALLA CON
    // EL TEXTO ORDENADO Y MANDA PETICION API SUMAR CONTADOR
    @POST("app/compartir/devocional")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> compartirDevocional(@Field("iduser") String iduser,
                                                     @Field("idiomaplan") int idiomaplan);


    // Listado de Notificaciones
    @POST("app/notificaciones/listado")
    Observable<ModeloListaNotificacionPaginate<ModeloListaNotificacionPaginateMetaDato>> listadoNotificaciones(
            @Body ModeloListaNotificacionPaginateRequest request);




    // listado solicitudes pendientes comunidad Enviadas
    @POST("app/comunidad/listado/solicitud/pendientes/enviadas")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> listadoSolicitudPendienteEnviadas(@Field("iduser") String iduser);



    // listado solicitudes pendientes comunidad Recibidas
    @POST("app/comunidad/listado/solicitud/pendientes/recibidas")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> listadoSolicitudPendienteRecibidas(@Field("iduser") String iduser);

    // aceptar solicitud que he recibido
    @POST("app/comunidad/aceptarsolicitud/recibido")
    @FormUrlEncoded
    Observable<ModeloUsuario> aceptarSolicitudRecibida(@Field("iduser") String iduser,
                                                       @Field("idsolicitud") int idsolicitud);


    // listado de insignias comunidad
    @POST("app/comunidad/informacion/insignias")
    @FormUrlEncoded
    Observable<ModeloContenedorInsignias> listadoInsigniasComunidad(@Field("idsolicitud") int idsolicitud,
                                                        @Field("idiomaplan") int idiomaplan,
                                                       @Field("iduser") String iduser);


    // enviar lista de amigos con cual iniciare plan, se enviara
    // id solicitud

    @POST("app/comunidadplan/iniciar/plan/amigos")
    @FormUrlEncoded
    Observable<ModeloContedorComunidad> iniciarPlanAmigos(@Field("iduser") String iduser,
                                                           @Field("idplan") int idplan,
                                                           @Field("idiomaplan") int idioma,
                                                           @FieldMap Map<String, String> listado);



    @POST("app/comunidad/yoagregue/planeslistado")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoPlanesComunidadYoAdd(@Field("idiomaplan") int idiomaplan);

    @POST("app/comunidad/yoagregue/planeslistado/informacion")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoPlanesComunidadYoAddComoVan(@Field("idiomaplan") int idiomaplan,
                                                                          @Field("idplan") int idplan);



    // LISTADO DE AMIGOS QUE ME TIENEN AGREGADO A UN PLAN
    @POST("app/comunidad/mehanagregado/planes/amigos")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoAmigosMeAgregaronSuPlan(@Field("idiomaplan") int idiomaplan);

    @POST("app/comunidad/mehanagregado/planes/listado")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoMeHanAgregadoPlanes(@Field("idiomaplan") int idiomaplan,
                                                                  @Field("id") int id);



    // listado de planes que tiene ese amigo pero solo ver los que no tiene ocultos
    @POST("app/comunidad/informacion/planes")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoPlanesAmigoComunidad(@Field("idsolicitud") int idsolicitud,
                                                                   @Field("idiomaplan") int idiomaplan,
                                                                   @Field("iduser") String iduser);


    // listado de items de ese amigo, se envia el usuario de un solo a buscar
    @POST("app/comunidad/informacion/planes/items")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoPlanesItemsAmigoComunidad(@Field("idplan") int idplan,
                                                                   @Field("idiomaplan") int idiomaplan,
                                                                   @Field("idusuariobuscar") int idusuariobuscar,
                                                                   @Field("iduser") String iduser);


    // listado de preguntas y las respuestas de ese usuario
    @POST("app/comunidad/informacion/planes/itemspreguntas")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> listadoPlanesItemsPreguntasComunidad(@Field("idplanblockdetauser") int idplanblockdetauser,
                                                                        @Field("idiomaplan") int idiomaplan,
                                                                        @Field("idusuariobuscar") int idusuariobuscar);



    // listado de biblias
    @POST("app/listado/biblias")
    @FormUrlEncoded
    Observable<ModeloBibliaContenedor> listadoBiblias(@Field("iduser") String iduser);

    // listado de capitulos (libros) de la biblia
    @POST("app/listado/biblia/capitulos")
    @FormUrlEncoded
    Observable<ModeloCapituloContenedor> listadoBibliasCapitulos(@Field("iduser") String iduser,
                                                                 @Field("idiomaplan") int idioma,
                                                                 @Field("idbiblia") int idbiblia
    );



    // MODIFICADO: 18/06/2024
    // listado de Textos del capitulo
    @POST("app/listado/bibliacapitulo/textos")
    @FormUrlEncoded
    Observable<ModeloContenedorVersiculo> listadoTextosVersiculos(@Field("iduser") String iduser,
                                                            @Field("idiomaplan") int idioma,
                                                            @Field("idcapitulo") int idcapitulo
    );



    // MODIFICADO 18/06/2024
    // informacion textos devo capitulos
    @POST("app/listado/biblia/devocionales")
    @FormUrlEncoded
    Observable<ModeloDevoCapitulo> informacionTextoDevoCapitulo(@Field("iduser") String iduser,
                                                                @Field("idiomaplan") int idioma,
                                                                @Field("iddevobiblia") int iddevobiblia
    );


    // FIX 30/04/2024
    @POST("app/plan/listado/misplanes")
    @FormUrlEncoded
    Observable<ModeloContenedorPlanesV2> listadoMisPlanes(@Field("iduser") String iduser,
                                                          @Field("idiomaplan") int idiomaplan);



    @POST("app/buscar/planes/nuevos")
    @FormUrlEncoded
    Observable<ModeloContenedorPlanesV2> listadoNuevosPlanes(@Field("iduser") String iduser,
                                                          @Field("idiomaplan") int idiomaplan);



    // BORRAR NOTIFICACIONES
    @POST("app/notificacion/borrarlistado")
    @FormUrlEncoded
    Observable<ModeloUsuario> borrarListadoNotificaciones(@Field("idiomaplan") int idiomaplan);




}
