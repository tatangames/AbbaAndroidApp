package com.tatanstudios.abbaappandroid.activity.comunidad;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.AdaptadorSeleccionarAmigos;
import com.tatanstudios.abbaappandroid.modelos.amigos.ModeloAmigos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SeleccionarAmigosPlanActivity extends AppCompatActivity {



    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ImageView imgFlechaAtras;
    int colorProgress = 0;


    private AdaptadorSeleccionarAmigos adaptadorSeleccionarAmigos;
    private int idplan = 0;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private final int RETORNO_ACTUALIZAR_111 = 111;

    private boolean boolIniciarPlan = true;
    private boolean boolApiActualizar = true;

    private TextView txtNoAmigos, txtIniciar;

    private Map<String,String> hashMapAmigos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_amigos_plan);

        recyclerView = findViewById(R.id.recyclerView);
        rootRelative = findViewById(R.id.rootRelative);
        txtNoAmigos = findViewById(R.id.txtNoAmigo);
        txtIniciar = findViewById(R.id.txtIniciar);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);

        onBackPressedDispatcher = getOnBackPressedDispatcher();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idplan = bundle.getInt("IDPLAN");
        }

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);



        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);




        txtIniciar.setOnClickListener(v -> {
            revisarDatos();
        });

        imgFlechaAtras.setOnClickListener(v -> {
            onBackPressedDispatcher.onBackPressed();
        });

       apiSolicitudesAceptadas();
    }


    // AL ENTRAR A ESTA ACTIVITY SIEMPRE HABRA AMIGOS
    private void apiSolicitudesAceptadas(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoComunidadAceptado(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta.getSuccess() == 1){

                                        if(apiRespuesta.getHayinfo() == 1){
                                            txtIniciar.setVisibility(View.VISIBLE);

                                            adaptadorSeleccionarAmigos = new AdaptadorSeleccionarAmigos(getApplicationContext(), apiRespuesta.getModeloComunidads(), this);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                            recyclerView.setAdapter(adaptadorSeleccionarAmigos);
                                        }else{
                                            recyclerView.setVisibility(View.GONE);

                                            txtIniciar.setVisibility(View.VISIBLE);
                                            txtNoAmigos.setVisibility(View.VISIBLE);
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

    private void revisarDatos(){

        int conteo = adaptadorSeleccionarAmigos.countCheckedItems();

        if(conteo > 5){
            Toasty.info(this, getString(R.string.maximo_5_amigos), Toast.LENGTH_SHORT).show();
        }else{
            // verificar que haya al menos 1 amigo

            if(conteo <= 0){
                Toasty.info(this, getString(R.string.minimo_1_amigo), Toast.LENGTH_SHORT).show();
                return;
            }

            // preguntar iniciar plan amigos ya
            preguntaIniciarPlanAmigos();
        }
    }


    private void preguntaIniciarPlanAmigos(){

        if(boolIniciarPlan) {
            boolIniciarPlan = false;

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
                boolIniciarPlan = true;
                apiSeleccionarPlan();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.no), sDialog -> {
                sDialog.dismissWithAnimation();
                boolIniciarPlan = true;
            });

            pDialog.show();
        }
    }

    private void apiSeleccionarPlan(){
        hashMapAmigos.clear();

        List<ModeloAmigos> modeloAmigos = adaptadorSeleccionarAmigos.getCheckedModelIds();

        for (ModeloAmigos mm : modeloAmigos){
            hashMapAmigos.put("idsolicitud["+String.valueOf(mm.getIdsolicitud())+"][idusuario]", String.valueOf( mm.getIdusuario()));
        }

        if(boolApiActualizar) {
            boolApiActualizar = false;

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idioma = tokenManager.getToken().getIdiomaApp();

            compositeDisposable.add(
                    service.iniciarPlanAmigos(iduser, idplan, idioma, hashMapAmigos)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolApiActualizar = true;

                                        if (apiRespuesta != null) {

                                            if (apiRespuesta.getSuccess() == 1) {
                                               planYaRegistrado();
                                            }
                                            else if (apiRespuesta.getSuccess() == 2) {
                                               volverAtrasActualizado();
                                            }

                                            else {
                                                mensajeSinConexion();
                                            }
                                        } else {
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolApiActualizar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    private void planYaRegistrado(){
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE, false);

        pDialog.setTitleText(getString(R.string.error));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentText(getString(R.string.plan_ya_estaba_iniciado));
        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();
            volverAtras();
        });

        pDialog.show();
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

    private void volverAtras(){
        Intent returnIntent = new Intent();
        setResult(RETORNO_ACTUALIZAR_111, returnIntent);
        onBackPressedDispatcher.onBackPressed();
    }

    private void volverAtrasActualizado(){
        Intent returnIntent = new Intent();
        setResult(RETORNO_ACTUALIZAR_111, returnIntent);

        Toasty.success(this, getString(R.string.plan_agregado)).show();
        onBackPressedDispatcher.onBackPressed();
    }
}