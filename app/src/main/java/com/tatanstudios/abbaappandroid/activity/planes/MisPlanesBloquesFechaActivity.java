package com.tatanstudios.abbaappandroid.activity.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloVistasPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFecha;
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

    // si plan fue completado, al regresar a tras a mis planes, que se recargue
    private final int RETORNO_ACTUALIZAR_100 = 100;

    private boolean tema = false;

    private boolean boolActualizarVistaAtras = false;
    private boolean boolApiCompartir = true;
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

                                            adapterHorizontal = new AdaptadorBloqueFechaHorizontal(this, apiRespuesta.getModeloBloqueFechas(),  tema,
                                                    this);
                                            recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                            recyclerViewHorizontal.setAdapter(adapterHorizontal);

                                            setearPortada(apiRespuesta.getPortada());

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


    private void setearPortada(String urlPortada){

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


    public void actualizarCheck(int blockDeta, int valor, int posFila){

            progressBar.setVisibility(View.VISIBLE);


            String iduser = tokenManager.getToken().getId();
            int idioma = tokenManager.getToken().getIdiomaTextos();

            compositeDisposable.add(
                    service.actualizarBloqueFechaCheckbox(iduser, blockDeta, valor, idPlan, idioma)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);


                                        if(apiRespuesta != null) {

                                            if (apiRespuesta.getSuccess() == 1) {
                                                // no ha respondido preguntas y hay activas y requeridas

                                                Toasty.info(this, getString(R.string.completar_devocional),Toasty.LENGTH_SHORT).show();
                                                setearFila(posFila, false, valor);
                                            }
                                            else if (apiRespuesta.getSuccess() == 2) {
                                                Toasty.success(this, getString(R.string.actualizado),Toasty.LENGTH_SHORT).show();

                                                if(apiRespuesta.getPlanCompletado() == 1){
                                                    boolActualizarVistaAtras = true;
                                                }

                                                setearFila(posFila, true, valor);
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

    private void setearFila(int posFila, boolean valor, int valorTenia){
        adapterVertical.retornoRespuesta(posFila, valor, valorTenia);
    }


    public void informacionCompartir(int idblockdeta){

        if(boolApiCompartir){
            boolApiCompartir = false;

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

            compositeDisposable.add(
                    service.infoPreguntasTextosParaCompartir(iduser, idblockdeta, idiomaPlan)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolApiCompartir = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                                // falta responder preguntas
                                                Toasty.info(this, getString(R.string.completar_devocional),Toasty.LENGTH_SHORT).show();
                                            }
                                            else if(apiRespuesta.getSuccess() == 2) {

                                                String textoGlobal = "";


                                                if(apiRespuesta.getDescripcion() != null && !TextUtils.isEmpty(apiRespuesta.getDescripcion())){
                                                    textoGlobal += apiRespuesta.getDescripcion() + "\n";
                                                }

                                                // Preguntas
                                                for (ModeloPreguntas arrayPreguntas : apiRespuesta.getModeloPreguntas()) {

                                                    if(arrayPreguntas.getTitulo() != null && !TextUtils.isEmpty(arrayPreguntas.getTitulo())){
                                                        String textoSinHTMLTitulo = HtmlCompat.fromHtml(arrayPreguntas.getTitulo(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();

                                                        textoGlobal += textoSinHTMLTitulo + "\n";
                                                    }

                                                    if(arrayPreguntas.getTexto() != null && !TextUtils.isEmpty(arrayPreguntas.getTexto())){
                                                        textoGlobal += arrayPreguntas.getTexto() + "\n";
                                                    }
                                                }

                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("text/plain");
                                                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                                                intent.putExtra(Intent.EXTRA_TEXT, textoGlobal);

                                                try {
                                                    startActivity(Intent.createChooser(intent, getString(R.string.compartir)));
                                                } catch (Exception e) {

                                                }
                                            }

                                            else if(apiRespuesta.getSuccess() == 3) {

                                                // no hay preguntas disponibles, es que estan inactivas
                                                Toasty.info(this, getString(R.string.devocional_no_disponible), Toasty.LENGTH_SHORT).show();
                                            }
                                            else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolApiCompartir = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }


    public void redireccionarCuestionario(int idBlockDeta){

        Intent intent = new Intent(this, CuestionarioPlanActivity.class);
        intent.putExtra("IDBLOQUE", idBlockDeta);
        startActivity(intent);
    }

    private void volverAtrasActualizar(){
        if(boolActualizarVistaAtras){
            Intent returnIntent = new Intent();
            setResult(RETORNO_ACTUALIZAR_100, returnIntent);
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