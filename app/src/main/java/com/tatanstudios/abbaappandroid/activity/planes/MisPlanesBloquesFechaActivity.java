package com.tatanstudios.abbaappandroid.activity.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.cuestionarios.CuestionarioPlanActivity;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.bloquesfecha.AdaptadorBloqueFechaHorizontal;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.bloquesfecha.AdaptadorBloqueFechaVertical;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFechaDetalle;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MisPlanesBloquesFechaActivity extends AppCompatActivity {

    private int idPlan = 0;
    private RelativeLayout rootRelative;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras, imgPortada;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private final int ID_INTENT_RETORNO_10 = 10;
    private final int ID_INTENT_RETORNO_11 = 11;

    private boolean tema = false;

    private boolean puedeActualizarCheck = true;
    private boolean boolActualizarVistaAtras = false;
    private RecyclerView recyclerViewHorizontal, recyclerViewVertical;
    private AdaptadorBloqueFechaHorizontal adapterHorizontal;
    private AdaptadorBloqueFechaVertical adapterVertical;

    private LinearLayout linearContenedor;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_planes_bloques_fecha);

        recyclerViewHorizontal = findViewById(R.id.recyclerViewHorizontal);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);
        recyclerViewVertical = findViewById(R.id.recyclerViewVertical);
        imgPortada = findViewById(R.id.imgPortada);
        linearContenedor = findViewById(R.id.linear);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idPlan = bundle.getInt("IDPLAN");
        }

        txtToolbar.setText(getString(R.string.informacion));

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        onBackPressedDispatcher = getOnBackPressedDispatcher();

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtrasActualizar();
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAtrasActualizar();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);
        apiBuscarPlanesbloques();
    }

    private void apiBuscarPlanesbloques(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.informacionPlanBloque(iduser, idiomaPlan, idPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            int hayDiaActual = apiRespuesta.getHayDiaActual();
                                            int idUltimoBloque = apiRespuesta.getIdUltimoBloque();

                                            adapterHorizontal = new AdaptadorBloqueFechaHorizontal(this, apiRespuesta.getModeloBloqueFechas(), recyclerViewHorizontal, tema,
                                                    hayDiaActual, idUltimoBloque, this);
                                            recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                            recyclerViewHorizontal.setAdapter(adapterHorizontal);

                                            setearAdapter(apiRespuesta.getPortada());
                                            linearContenedor.setVisibility(View.VISIBLE);
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

    private void setearAdapter(String urlPortada){

        if(urlPortada != null && !TextUtils.isEmpty(urlPortada)){
            Glide.with(this)
                    .load(RetrofitBuilder.urlImagenes + urlPortada)
                    .apply(opcionesGlide)
                    .into(imgPortada);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(this)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(imgPortada);
        }
    }


    public void llenarDatosAdapterVertical(List<
            ModeloBloqueFechaDetalle> modeloMisPlanesBloqueDetalles){

        adapterVertical = new AdaptadorBloqueFechaVertical(this, modeloMisPlanesBloqueDetalles, this, tema);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewVertical.setAdapter(adapterVertical);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY CuestionarioPlanActivity
                //
                if(result.getResultCode() == ID_INTENT_RETORNO_11){

                }

            });

    public void actualizarCheck(int blockDeta, int valor){

        if(puedeActualizarCheck){
            puedeActualizarCheck = false;

            String iduser = tokenManager.getToken().getId();

            // NO TENDRA RETRY
            compositeDisposable.add(
                    service.actualizarBloqueFechaCheckbox(iduser, blockDeta, valor, idPlan)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(apiRespuesta -> {

                                        puedeActualizarCheck = true;

                                        if(apiRespuesta != null) {

                                            if (apiRespuesta.getSuccess() == 1) {

                                                if(apiRespuesta.getPlanCompletado() == 1){
                                                    // ejecutar confeti
                                                    boolActualizarVistaAtras = true;
                                                }

                                            }else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        puedeActualizarCheck = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }


    public void redireccionarCuestionario(int idBlockDeta, int tienePreguntas){

        Intent intent = new Intent(this, CuestionarioPlanActivity.class);
        intent.putExtra("IDBLOQUE", idBlockDeta);
        intent.putExtra("PREGUNTAS", tienePreguntas);
        startActivity(intent);
    }

    private void volverAtrasActualizar(){
        if(boolActualizarVistaAtras){
            Intent returnIntent = new Intent();
            setResult(ID_INTENT_RETORNO_10, returnIntent);
        }
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