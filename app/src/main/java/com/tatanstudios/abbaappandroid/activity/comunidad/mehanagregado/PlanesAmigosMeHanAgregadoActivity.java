package com.tatanstudios.abbaappandroid.activity.comunidad.mehanagregado;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.mehanagregado.AdaptadorAmigosMeHanAgregado;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesAmigosMeHanAgregadoActivity extends AppCompatActivity {


    // PANTALLA LISTADO DE AMIGOS QUE ME HAN AGREGADO A SU PLAN

    private RelativeLayout rootRelative;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private ImageView imgFlechaAtras;
    private OnBackPressedDispatcher onBackPressedDispatcher;
    private RecyclerView recyclerView;
    private TextView txtToolbar;

    private AdaptadorAmigosMeHanAgregado adaptadorAmigosMeHanAgregado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_amigos_me_han_agregado);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        rootRelative = findViewById(R.id.rootRelative);
        txtToolbar = findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.amigos));


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

        apiBuscarInformacion();
    }



    private void apiBuscarInformacion(){

        int idioma = tokenManager.getToken().getIdiomaApp();

        compositeDisposable.add(
                service.listadoAmigosMeAgregaronSuPlan(idioma)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if (apiRespuesta.getSuccess() == 1) {

                                            int hayinfo = apiRespuesta.getHayinfo();

                                            if(hayinfo == 1){
                                                adaptadorAmigosMeHanAgregado = new AdaptadorAmigosMeHanAgregado(this, apiRespuesta.getModeloMisPlanes(),
                                                        this);
                                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                                                recyclerView.addItemDecoration(dividerItemDecoration);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorAmigosMeHanAgregado);
                                            }else{
                                                mensajeSinInfo();
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

    void mensajeSinInfo(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(this, getString(R.string.no_hay_informacion)).show();
    }


    public void vistaPlanes(int id){
        Intent i = new Intent(this, PlanesMeHanAgregadoActivity.class);
        i.putExtra("ID", id);
        startActivity(i);
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