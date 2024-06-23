package com.tatanstudios.abbaappandroid.activity.comunidad.agregados;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.yoagregue.AdaptadorPlanesAmigosYoAgregue;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.yoagregue.AdaptadorPlanesAmigosYoAgregueComoVan;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesListadoAmigoAgregadoActivity extends AppCompatActivity {

    // LISTADO DE AMIGOS QUE YO AGREGUE A UN PLAN, SE MUESTRA EL TOTDAL DEVOCIONALES HECHO
    // FALTANES

    private int idPlan = 0;

    private RelativeLayout rootRelative;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private ImageView imgFlechaAtras;
    private OnBackPressedDispatcher onBackPressedDispatcher;
    private RecyclerView recyclerView;
    private TextView txtToolbar;

    private AdaptadorPlanesAmigosYoAgregueComoVan adaptadorPlanesAmigosYoAgregueComoVan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_listado_amigo_agregado);


        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        rootRelative = findViewById(R.id.rootRelative);
        txtToolbar = findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.planes));


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idPlan = bundle.getInt("IDPLAN");
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
                service.listadoPlanesComunidadYoAddComoVan(idioma, idPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if (apiRespuesta.getSuccess() == 1) {

                                            int itemTotal = apiRespuesta.getItemtotal();

                                            adaptadorPlanesAmigosYoAgregueComoVan = new AdaptadorPlanesAmigosYoAgregueComoVan(this, apiRespuesta.getModeloMisPlanes(),
                                                    this, itemTotal);
                                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                                                recyclerView.addItemDecoration(dividerItemDecoration);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorPlanesAmigosYoAgregueComoVan);


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
}