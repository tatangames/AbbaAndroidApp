package com.tatanstudios.abbaappandroid.activity.insignias;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.completados.MisPlanesBloquesFechaCompletadosActivity;
import com.tatanstudios.abbaappandroid.adaptadores.notificacion.AdaptadorListaNotificaciones;
import com.tatanstudios.abbaappandroid.adaptadores.planes.completados.AdaptadorPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacion;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacionPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletadosPaginateRequest;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListadoNotificacionActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView txtSinNotificacion;
    private boolean unaVezVisibilidad = true;
    private AdaptadorListaNotificaciones adapter;


    // AJUSTES DE PAGINACION

    private boolean estaCargandoApi = false;
    private boolean puedeCargarYaPaginacion = false;
    private int currentPage = 1;
    private int lastPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_notificacion);


        recyclerView = findViewById(R.id.recyclerView);
        rootRelative = findViewById(R.id.rootRelative);
        txtSinNotificacion = findViewById(R.id.txtSinNotificacion);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        // Configura el LinearLayoutManager y el ScrollListener para manejar la paginación
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // PAGINACION
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (puedeCargarYaPaginacion && !isLastPage()) {

                    if (!recyclerView.canScrollVertically(1)) {
                        // Estamos en el fondo, carga más elementos
                        apiBuscarNotificacionesPaginacion();
                    }
                }
            }
        });

        apiBuscarNotificaciones();
    }



    // verificar estado de carga para paginacion
    private boolean isLastPage(){
        if(currentPage > lastPage){
            return true;
        }else{
            return false;
        }
    }


    private void apiBuscarNotificaciones(){

        progressBar.setVisibility(View.VISIBLE);

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        ModeloListaNotificacionPaginateRequest paginationRequest = new ModeloListaNotificacionPaginateRequest();
        paginationRequest.setPage(currentPage);
        paginationRequest.setLimit(75);
        paginationRequest.setIdiomaplan(idiomaPlan);
        paginationRequest.setIduser(iduser);

        compositeDisposable.add(
                service.listadoNotificaciones(paginationRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(
                                apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta.getSuccess() == 1){

                                        if(apiRespuesta.getHayinfo() == 1){
                                            if (!apiRespuesta.getData().getData().isEmpty()) {
                                                List<ModeloListaNotificacion> newData = apiRespuesta.getData().getData();

                                                lastPage = apiRespuesta.getData().getLastPage();
                                                currentPage++;

                                                setearAdapter(newData);

                                                unaVezVisibilidad = false;
                                                puedeCargarYaPaginacion = true;
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }
                                        }else{

                                            if(unaVezVisibilidad){
                                                unaVezVisibilidad = false;
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinNotificacion.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                }
                        ));
    }



    // UTILIZADO PARA PAGINACION
    private void apiBuscarNotificacionesPaginacion(){

        if(!estaCargandoApi){
            estaCargandoApi = true;

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idiomaPlan = tokenManager.getToken().getIdiomaTextos();


            ModeloListaNotificacionPaginateRequest paginationRequest = new ModeloListaNotificacionPaginateRequest();
            paginationRequest.setPage(currentPage);
            paginationRequest.setLimit(75);
            paginationRequest.setIdiomaplan(idiomaPlan);
            paginationRequest.setIduser(iduser);

            compositeDisposable.add(
                    service.listadoNotificaciones(paginationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(
                                    apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);

                                        if(apiRespuesta.getSuccess() == 1){

                                            if(apiRespuesta.getHayinfo() == 1){
                                                if (!apiRespuesta.getData().getData().isEmpty()) {
                                                    List<ModeloListaNotificacion> newData = apiRespuesta.getData().getData();

                                                    lastPage = apiRespuesta.getData().getLastPage();
                                                    currentPage++;

                                                    adapter.addData(newData);

                                                    estaCargandoApi = false;
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                }
                                            }

                                        }else{
                                            mensajeSinConexion();
                                            estaCargandoApi = false;
                                        }
                                    },
                                    throwable -> {
                                        mensajeSinConexion();
                                        estaCargandoApi = false;
                                        //String errorMessage = throwable.getMessage();
                                    }
                            ));
        }
    }


    private void setearAdapter(List<ModeloListaNotificacion> modeloListaNotificacions) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new AdaptadorListaNotificaciones(this, modeloListaNotificacions);
        recyclerView.setAdapter(adapter);
    }



    private void mensajeSinConexion(){
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