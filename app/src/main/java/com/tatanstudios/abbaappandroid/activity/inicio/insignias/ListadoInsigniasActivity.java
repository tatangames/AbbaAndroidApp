package com.tatanstudios.abbaappandroid.activity.inicio.insignias;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.insignias.InformacionInsigniaActivity;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.listado.AdaptadorTodasInsignias;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListadoInsigniasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private AdaptadorTodasInsignias adaptadorTodasInsignias;

    private  int colorProgress = 0;

    private boolean temaActual = false;


    private ColorStateList colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco = 0;
    private int colorBlack = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_insignias);
        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.insignias));

        colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);

        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        if(tokenManager.getToken().getTema() == 1){
            temaActual = true;
        }


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

        apiBuscarTodosLasInsignias();
    }


    private void apiBuscarTodosLasInsignias(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaApp();

        compositeDisposable.add(
                service.obtenerTodosLasInsignias(iduser, idioma)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            adaptadorTodasInsignias = new AdaptadorTodasInsignias(getApplicationContext(), apiRespuesta.getModeloInicioInsignias(), this);
                                            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
                                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                                            recyclerView.setLayoutManager(layoutManager);
                                            recyclerView.setAdapter(adaptadorTodasInsignias);

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


    public void verInformacionInsignia(int idtipoinsign){
        Intent intent = new Intent(this, InformacionInsigniaActivity.class);
        intent.putExtra("IDINSIGNIA", idtipoinsign);
        startActivity(intent);
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