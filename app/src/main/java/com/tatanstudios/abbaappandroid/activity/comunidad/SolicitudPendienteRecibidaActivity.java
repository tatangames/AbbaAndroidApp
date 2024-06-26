package com.tatanstudios.abbaappandroid.activity.comunidad;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.AdaptadorSolicitudPendientesRecibidas;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SolicitudPendienteRecibidaActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private TextView txtSinDatos;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private AdaptadorSolicitudPendientesRecibidas adaptadorSolicitudPendientesRecibidas;

    private String textoCorreo = "";
    private String textoFecha = "";

    private String textoAceptarSoli = "";
    private String textoBorrarSoli = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_pendiente_recibida);
        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);
        txtSinDatos = findViewById(R.id.txtSinDatos);

        txtToolbar.setText(getString(R.string.recibidas));

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        textoCorreo = getString(R.string.correo);
        textoFecha = getString(R.string.fecha_de_solicitud);
        textoAceptarSoli = getString(R.string.aceptar_solicitud);
        textoBorrarSoli = getString(R.string.borrar_solicitud);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        onBackPressedDispatcher = getOnBackPressedDispatcher();

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAtras();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarSolicitudesPendientes();
    }


    private void apiBuscarSolicitudesPendientes(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoSolicitudPendienteRecibidas(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){
                                                adaptadorSolicitudPendientesRecibidas = new AdaptadorSolicitudPendientesRecibidas(getApplicationContext(), apiRespuesta.getModeloComunidads(), this,
                                                        textoCorreo, textoFecha, textoAceptarSoli, textoBorrarSoli);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorSolicitudPendientesRecibidas);
                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinDatos.setVisibility(View.VISIBLE);
                                            }

                                        }else{

                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {

                                    mensajeSinConexion();
                                })
        );
    }

    private void volverAtras(){
        finish();
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
    }


    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }






    public void borrarSolicitud(int idsolicitud) {

        String id = tokenManager.getToken().getId();

        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.borrarSolicitudPendiente(id, idsolicitud)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        // SOLO DECIR QUE FUE ELIMINADO, SEA CUAL SEA EL ESTADO

                                        if(apiRespuesta.getSuccess() == 1) {
                                            Toasty.success(this, getString(R.string.eliminado), Toasty.LENGTH_SHORT).show();
                                            apiBuscarSolicitudesPendientes();
                                        }else{

                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {

                                    mensajeSinConexion();
                                })
        );
    }

    public void aceptarSolicitud(int idsolicitud) {

        String id = tokenManager.getToken().getId();

        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.aceptarSolicitudRecibida(id, idsolicitud)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        // SOLO SE DIRA ACTUALIZADO

                                        if(apiRespuesta.getSuccess() == 1) {
                                            Toasty.success(this, getString(R.string.actualizado), Toasty.LENGTH_SHORT).show();
                                            apiBuscarSolicitudesPendientes();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {

                                            // SOLICITUD NO EXISTE
                                            Toasty.error(this, getString(R.string.error_intentar_de_nuevo), Toasty.LENGTH_SHORT).show();
                                            apiBuscarSolicitudesPendientes();

                                        }
                                        else{

                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {

                                    mensajeSinConexion();
                                })
        );
    }

}