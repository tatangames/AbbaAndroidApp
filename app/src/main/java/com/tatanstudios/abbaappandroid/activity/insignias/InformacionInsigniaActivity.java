package com.tatanstudios.abbaappandroid.activity.insignias;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.individual.AdaptadorInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloContenedorInsignias;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InformacionInsigniaActivity extends AppCompatActivity {


    private int idTipoInsignia = 0;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private ImageView imgFlechaAtras, imgTuerca;

    private OnBackPressedDispatcher onBackPressedDispatcher;
    private RecyclerView recyclerView;

    private AdaptadorInsigniaHitos adaptadorInsigniaHitos;

    private ArrayList<ModeloVistaHitos> elementos = new ArrayList<>();

    private boolean yaPuedeTuerca = false;

    private int contadorActual = 0;

    private String textoNivel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_insignia);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        imgTuerca = findViewById(R.id.imgTuerca);
        rootRelative = findViewById(R.id.rootRelative);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idTipoInsignia = bundle.getInt("IDINSIGNIA");
        }

        textoNivel = getString(R.string.contador_actual);


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

        imgTuerca.setOnClickListener(v -> {
            informacionNiveles();
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAtras();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarInfoInsignia();
    }


    private void apiBuscarInfoInsignia(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaApp();

        compositeDisposable.add(
                service.informacionInsigniaSeleccionada(iduser, idioma, idTipoInsignia)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if (apiRespuesta.getSuccess() == 1) {

                                            textoNivel = textoNivel + ": " + apiRespuesta.getContador();

                                            setearCampos(apiRespuesta);

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


    private void setearCampos(ModeloContenedorInsignias apiRespuesta){

        elementos.add(new ModeloVistaHitos( ModeloVistaHitos.TIPO_IMAGEN,
                new ModeloDescripcionHitos(apiRespuesta.getImagen(), apiRespuesta.getTitulo(),
                        apiRespuesta.getDescripcion(), apiRespuesta.getNiveltexto()),
                null
        ));


        List<ModeloInsigniaHitos> mm = new ArrayList<>();


        for (ModeloInsigniaHitos m : apiRespuesta.getModeloInsigniaHitos()){
            mm.add(new ModeloInsigniaHitos(m.getFechaFormat(), 0, 0,m.getNivelTexto(), 0,
                    m.getTextoCompletado()));
        }


        elementos.add(new ModeloVistaHitos( ModeloVistaHitos.TIPO_RECYCLER,
                null,
                mm
        ));


        // llenar adaptador de los hitos, y le estoy pasando textoFalta o remaining
        adaptadorInsigniaHitos = new AdaptadorInsigniaHitos(getApplicationContext(), elementos, textoNivel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adaptadorInsigniaHitos);


        yaPuedeTuerca = true;
    }



    private void informacionNiveles(){

        if(yaPuedeTuerca){

            Intent i = new Intent(this, NivelesInsigniasActivity.class);
            i.putExtra("IDTIPOINSIGNIA", idTipoInsignia);
            startActivity(i);
        }

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