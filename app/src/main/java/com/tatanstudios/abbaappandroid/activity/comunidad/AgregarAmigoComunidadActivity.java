package com.tatanstudios.abbaappandroid.activity.comunidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.login.olvidepass.FragmentCodigoPassword;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AgregarAmigoComunidadActivity extends AppCompatActivity {


    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputCorreo;
    private TextInputEditText edtCorreo;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private Button btnEnviarCorreo;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;
    private boolean tema = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo_comunidad);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnEnviarCorreo = findViewById(R.id.btnEnviar);
        inputCorreo = findViewById(R.id.inputCorreo);
        edtCorreo = findViewById(R.id.edtCorreo);
        rootRelative = findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        colorGris = ContextCompat.getColor(this, R.color.gris616161);
        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);

        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        btnEnviarCorreo.setEnabled(false);

        btnEnviarCorreo.setBackgroundTintList(colorEstadoGris);
        btnEnviarCorreo.setTextColor(colorBlanco);

        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            finish();
        });

        btnEnviarCorreo.setOnClickListener(v -> {
            closeKeyboard();
            verificarDatos();
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                verificarEntrada();
            }
        };

        edtCorreo.addTextChangedListener(textWatcher);
    }


    private void verificarDatos(){
        closeKeyboard();
        apiEnviarCorreoCodigo();
    }


    private void apiEnviarCorreoCodigo(){

        progressBar.setVisibility(View.VISIBLE);

        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.enviarSolicitudComunidad(iduser, txtCorreo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()) // NO RETRY
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        // 1:segun estado

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getEstado() == 0){
                                                // pendiente aceptacion
                                                Toasty.info(this, getString(R.string.solicitud_pendiente_aceptacion), Toasty.LENGTH_SHORT).show();
                                            }else if(apiRespuesta.getEstado() == 1){
                                                // solicitud ya fue aceptada
                                                Toasty.info(this, getString(R.string.solicitud_ya_esta_aceptada), Toasty.LENGTH_SHORT).show();
                                            }
                                        }

                                        else if(apiRespuesta.getSuccess() == 2){
                                            Toasty.success(this, getString(R.string.solicitud_enviada), Toasty.LENGTH_SHORT).show();
                                            edtCorreo.setText("");
                                            inputCorreo.setError(null);
                                        }
                                        else if(apiRespuesta.getSuccess() == 3){
                                            alertaMensaje(getString(R.string.correo_no_encontrado));
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


    private void alertaMensaje(String texto){
        Toasty.info(this, texto, Toasty.LENGTH_SHORT).show();
    }


    private void verificarEntrada(){
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()){
            String valiCorreo = getString(R.string.direccion_correo_invalida);
            inputCorreo.setError(valiCorreo);
            desactivarBoton();
        }else{
            inputCorreo.setError(null);
            activarBoton();
        }
    }

    private void activarBoton(){

        btnEnviarCorreo.setEnabled(true);

        if(tema){ // Dark
            btnEnviarCorreo.setBackgroundTintList(colorEstadoBlanco);
            btnEnviarCorreo.setTextColor(colorBlack);
        }else{
            btnEnviarCorreo.setBackgroundTintList(colorEstadoNegro);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviarCorreo.setEnabled(false);

        // lo mismo para los 2 temas
        btnEnviarCorreo.setBackgroundTintList(colorEstadoGris);
        btnEnviarCorreo.setTextColor(colorBlanco);
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

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}