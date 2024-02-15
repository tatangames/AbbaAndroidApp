package com.tatanstudios.abbaappandroid.activity.planes;

import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SeleccionarAmigosPlanActivity;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VerPlanParaSeleccionarActivity extends AppCompatActivity {

    private ImageView imgFlechaAtras, imgPlan;
    private TextView txtTitulo, txtSubtitulo, txtToolbar, txtDescripcion;
    private Button btnComenzar, btnComunidad;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private int colorBlanco = 0, colorBlack = 0;

    private ColorStateList colorEstadoBlanco, colorEstadoNegro;

    private int idPlan = 0;

    private NestedScrollView nestedScrollView;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private boolean tema = false;

    private final int RETORNO_ACTUALIZAR_100 = 100;

    private final int RETORNO_ACTUALIZAR_111 = 111;

    private boolean boolIniciarSolo = true;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_plan_para_seleccionar);

        imgPlan = findViewById(R.id.imgPlan);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtSubtitulo = findViewById(R.id.txtSubtitulo);
        rootRelative = findViewById(R.id.rootRelative);
        btnComenzar = findViewById(R.id.botonComenzar);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        nestedScrollView = findViewById(R.id.nested);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnComunidad = findViewById(R.id.botonComunidad);

        txtToolbar.setText(getString(R.string.informacion_plan));

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);

        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        if (tokenManager.getToken().getTema() == 1) {
            tema = true;
        }

        if (tokenManager.getToken().getTema() == 1) {
            btnComenzar.setBackgroundTintList(colorEstadoBlanco);
            btnComenzar.setTextColor(colorBlack);

            btnComunidad.setBackgroundTintList(colorEstadoBlanco);
            btnComunidad.setTextColor(colorBlack);
        } else {
            btnComenzar.setBackgroundTintList(colorEstadoNegro);
            btnComenzar.setTextColor(colorBlanco);

            btnComunidad.setBackgroundTintList(colorEstadoNegro);
            btnComunidad.setTextColor(colorBlanco);
        }

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idPlan = bundle.getInt("ID");
        }

        btnComenzar.setOnClickListener(v -> {
            preguntarIniciarSolo();
        });

        btnComunidad.setOnClickListener(v -> {
            iniciarConAmigos();
        });

        imgFlechaAtras.setOnClickListener(v -> {
            onBackPressedDispatcher.onBackPressed();
        });

        apiBuscarDatos();
    }


    private void iniciarConAmigos(){
        Intent intent = new Intent(this, SeleccionarAmigosPlanActivity.class);
        intent.putExtra("IDPLAN", idPlan);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY VerPlanParaSeleccionar.class
                if(result.getResultCode() == RETORNO_ACTUALIZAR_111){
                    volverAtrasActualizado();
                }
            });




    private void preguntarIniciarSolo(){

        if(boolIniciarSolo) {
            boolIniciarSolo = false;

            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);

            pDialog.setTitleText(getString(R.string.iniciar_plan));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText("");
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
                sDialog.dismissWithAnimation();
                boolIniciarSolo = true;
                apiSeleccionarPlan();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.no), sDialog -> {
                sDialog.dismissWithAnimation();
                boolIniciarSolo = true;
            });

            pDialog.show();
        }
    }


    private void apiBuscarDatos(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionPlanSeleccionado(idPlan, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            setearCamposNuevosDatos(apiRespuesta);
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


    private void setearCamposNuevosDatos(ModeloBuscarPlanes api){

        if(api.getImagen() != null && !TextUtils.isEmpty(api.getImagen())){

            Glide.with(this)
                    .load(RetrofitBuilder.urlImagenes + api.getImagen())
                    .apply(opcionesGlide)
                    .into(imgPlan);
        }

        txtTitulo.setText(api.getTitulo());

        if(api.getSubtitulo() != null && !TextUtils.isEmpty(api.getSubtitulo())){
            txtSubtitulo.setText(api.getSubtitulo());
        }else{
            txtSubtitulo.setVisibility(View.GONE);
        }

        if(api.getDescripcion() != null && !TextUtils.isEmpty(api.getDescripcion())){

            txtDescripcion.setText(HtmlCompat.fromHtml(api.getDescripcion(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            txtDescripcion.setVisibility(View.VISIBLE);
        }

        nestedScrollView.setVisibility(View.VISIBLE);
    }


    private void apiSeleccionarPlan(){

        progressBar.setVisibility(View.VISIBLE);

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.seleccionarPlanNuevo(idPlan, iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            // plan ya estaba seleccionado.
                                            volverAtrasActualizado();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){
                                            // plan seleccionado
                                            volverAtrasActualizado();
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


    private void volverAtrasActualizado(){
        Intent returnIntent = new Intent();
        setResult(RETORNO_ACTUALIZAR_100, returnIntent);

        Toasty.success(this, getString(R.string.plan_agregado)).show();
        onBackPressedDispatcher.onBackPressed();
    }

}