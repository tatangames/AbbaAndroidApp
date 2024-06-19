package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.AdaptadorCapitulos;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.versiculo.AdaptadorVersiculos;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;
import com.tatanstudios.abbaappandroid.modelos.biblia.versiculo.ModeloVersiculo;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VersiculosListaActivity extends AppCompatActivity {


    // YA NO SE USARA ESTA PANTALLA
    // 18/06/2024

    private int idcapibloque = 0;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private TextView txtToolbar;
    private ImageView imgFlechaAtras;
    private RecyclerView recyclerView;

    private OnBackPressedDispatcher onBackPressedDispatcher;
    private AdaptadorVersiculos adaptadorVersiculos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculos_lista);

        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);
        recyclerView = findViewById(R.id.recyclerView);
        txtToolbar.setText(getString(R.string.versiculos));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idcapibloque = bundle.getInt("IDCAPIBLOQUE");
        }

        onBackPressedDispatcher = getOnBackPressedDispatcher();

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });

        apiBuscarVersiculos();
    }

    private void apiBuscarVersiculos(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.listadoVersiculos(iduser, idioma, idcapibloque)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                           llenarDatos(apiRespuesta.getModeloVersiculos());
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

    private void llenarDatos(List<ModeloVersiculo> modeloVersiculos){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adaptadorVersiculos = new AdaptadorVersiculos(this, modeloVersiculos, VersiculosListaActivity.this);
        recyclerView.setAdapter(adaptadorVersiculos);
    }

    public void verTextoVersiculo(int idversiculo){

        Intent intent = new Intent(this, VersiculoTextoActivity.class);
        intent.putExtra("IDVERSICULO", idversiculo);
        startActivity(intent);
    }

    private void volverAtras(){
        onBackPressedDispatcher.onBackPressed();
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