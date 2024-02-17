package com.tatanstudios.abbaappandroid.activity.comunidad.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesOcultos;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.videos.AdaptadorTodosVideos;
import com.tatanstudios.abbaappandroid.modelos.amigos.ModeloAmigos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesOcultos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesOcultosActivity extends AppCompatActivity {

    private TextView txtSinPlanes;

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private AdaptadorPlanesOcultos adaptadorPlanesOcultos;

    private TextView txtBotonOcultar;

    private Map<String,Integer> hashMapOcultos = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_ocultos);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        rootRelative = findViewById(R.id.rootRelative);
        txtSinPlanes = findViewById(R.id.txtSinPlanes);
        txtBotonOcultar = findViewById(R.id.txtOcultar);


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

        txtBotonOcultar.setOnClickListener(v -> {
            revisarDatos();
        });

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarPlanes();
    }


    private void apiBuscarPlanes(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoPlanesParaOcultar(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){

                                                List<ModeloPlanesOcultos> lista = new ArrayList<>();

                                                for(ModeloPlanesOcultos mm : apiRespuesta.getModeloPlanesOcultos()){
                                                    lista.add(new ModeloPlanesOcultos(mm.getIdplanes(), mm.getTitulo(), mm.getEstado()));
                                                }


                                                adaptadorPlanesOcultos = new AdaptadorPlanesOcultos(getApplicationContext(), lista, this);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorPlanesOcultos);

                                                txtBotonOcultar.setVisibility(View.VISIBLE);
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }else{

                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
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

    private boolean boolRevisar = true;

    private void revisarDatos(){

        if(boolRevisar){
            boolRevisar = false;

            int colorVerdeSuccess = ContextCompat.getColor(this, R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.actualizar));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText(getString(R.string.los_amigos_que_tienes_agregados));
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.enviar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolRevisar = true;
                apiActualizar();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolRevisar = true;

            });
            pDialog.show();
        }
    }

    private void apiActualizar(){

        hashMapOcultos.clear();

        List<ModeloPlanesOcultos> model = adaptadorPlanesOcultos.getCheckedModelIds();

        for (ModeloPlanesOcultos mm : model){
            int estado = 0;
            if(mm.getEstado()){
                estado = 1;
            }

            hashMapOcultos.put("idplan["+String.valueOf(mm.getIdplanes())+"][estado]", estado);
        }


        progressBar.setVisibility(View.VISIBLE);

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.actualizarPlanesOcultos(iduser, hashMapOcultos)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if (apiRespuesta != null) {

                                        if (apiRespuesta.getSuccess() == 1) {
                                            Toasty.success(this, getString(R.string.actualizado),Toasty.LENGTH_SHORT).show();
                                        }
                                        else {
                                            mensajeSinConexion();
                                        }
                                    } else {
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




}