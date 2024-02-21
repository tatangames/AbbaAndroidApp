package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.modelos.biblia.grupos.ModeloGrupo;
import com.tatanstudios.abbaappandroid.modelos.biblia.grupos.ModeloSubGrupo;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CapitulosBibliaActivity extends AppCompatActivity {


    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private int idbiblia = 0;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulos_biblia);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.referencias));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idbiblia = bundle.getInt("IDBIBLIA");
        }


        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        onBackPressedDispatcher = getOnBackPressedDispatcher();





        // Llenar el modelo de datos con marcas y modelos
        List<ModeloGrupo> listaDeGrupos = llenarDatos();






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

       // apiBuscarCapitulos();




    }






    private void apiBuscarCapitulos(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.listadoBibliasCapitulos(iduser, idioma, idbiblia)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            Toasty.info(this, "entra", Toasty.LENGTH_SHORT).show();

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


    private List<ModeloGrupo> llenarDatos(){

        List<ModeloGrupo> listaDeGrupos = new ArrayList<>();

        List<ModeloSubGrupo> modeloSamsung = new ArrayList<>();
        modeloSamsung.add(new ModeloSubGrupo(1, "A1"));
        modeloSamsung.add(new ModeloSubGrupo(2, "A2"));
        modeloSamsung.add(new ModeloSubGrupo(3, "A3"));
        modeloSamsung.add(new ModeloSubGrupo(4, "A4"));
        modeloSamsung.add(new ModeloSubGrupo(5, "A5"));

        ModeloGrupo samsung = new ModeloGrupo("Samsung", modeloSamsung);
        listaDeGrupos.add(samsung);

        List<ModeloSubGrupo> modeloApple = new ArrayList<>();
        modeloApple.add(new ModeloSubGrupo(6, "P1"));
        modeloApple.add(new ModeloSubGrupo(7, "P2"));
        modeloApple.add(new ModeloSubGrupo(8, "P3"));
        modeloApple.add(new ModeloSubGrupo(9, "P4"));
        modeloApple.add(new ModeloSubGrupo(10, "P5"));

        ModeloGrupo apple = new ModeloGrupo("Apple", modeloApple);
        listaDeGrupos.add(apple);

        List<ModeloSubGrupo> modeloMotorola = new ArrayList<>();
        modeloMotorola.add(new ModeloSubGrupo(11, "M1"));
        modeloMotorola.add(new ModeloSubGrupo(12, "M2"));
        modeloMotorola.add(new ModeloSubGrupo(13, "M3"));
        modeloMotorola.add(new ModeloSubGrupo(14, "M4"));
        modeloMotorola.add(new ModeloSubGrupo(15, "M5"));

        ModeloGrupo motorola = new ModeloGrupo("Motorola", modeloMotorola);
        listaDeGrupos.add(motorola);

        return listaDeGrupos;
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