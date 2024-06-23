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
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.AdaptadorSolicitudPendientesEnviadas;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SolicitudPendienteEnviadaActivity extends AppCompatActivity {


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

    private AdaptadorSolicitudPendientesEnviadas adaptadorSolicitudPendientesEnviadas;

    private String textoEliminar = "";
    private String textoCorreo = "";
    private String textoFechaSoli = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_pendiente_enviada);
        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);
        txtSinDatos = findViewById(R.id.txtSinDatos);

        txtToolbar.setText(getString(R.string.pendientes));
        textoEliminar = getString(R.string.eliminar);
        textoCorreo = getString(R.string.correo);
        textoFechaSoli = getString(R.string.fecha_de_solicitud);

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

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
                service.listadoSolicitudPendienteEnviadas(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){
                                                adaptadorSolicitudPendientesEnviadas = new AdaptadorSolicitudPendientesEnviadas(getApplicationContext(), apiRespuesta.getModeloComunidads(), this,
                                                        textoEliminar, textoCorreo, textoFechaSoli);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorSolicitudPendientesEnviadas);
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



}