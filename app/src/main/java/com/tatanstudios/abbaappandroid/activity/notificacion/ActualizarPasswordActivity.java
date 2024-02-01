package com.tatanstudios.abbaappandroid.activity.notificacion;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ActualizarPasswordActivity extends AppCompatActivity {


    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputContrasena;
    private TextInputEditText edtContrasena;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;

    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private Button btnEnviar;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;

    private boolean tema;

    private boolean boolDialogEnviar;

    private TextView txtToolbar;

    private OnBackPressedDispatcher onBackPressedDispatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_password);

        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtToolbar = findViewById(R.id.txtToolbar);

        inputContrasena = findViewById(R.id.inputContrasena);

        edtContrasena = findViewById(R.id.edtContrasena);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.editar));

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        boolDialogEnviar = true;

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        colorGris = ContextCompat.getColor(this, R.color.gris616161);
        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);
        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        btnEnviar.setEnabled(false);
        btnEnviar.setBackgroundTintList(colorEstadoGris);
        btnEnviar.setTextColor(colorBlanco);


        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            vistaAtras();
        });

        btnEnviar.setOnClickListener(v -> {
            closeKeyboard();
            verificarDatos();
        });

        String valiContrasena = getString(R.string.contrasena_minimo_6);

        edtContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto ha cambiado
                String textoIngresado = editable.toString();
                // Hacer algo con el texto ingresado
                if (!textoIngresado.isEmpty()) {

                    if(textoIngresado.length() >= 6){
                        inputContrasena.setError(null);
                        activarBoton();
                    }else{
                        inputContrasena.setError(valiContrasena);
                        desactivarBoton();
                    }
                }else{
                    inputContrasena.setError(null);
                }
            }
        });


    }


    private void activarBoton(){

        btnEnviar.setEnabled(true);

        if(tema){ // Dark
            btnEnviar.setBackgroundTintList(colorEstadoBlanco);
            btnEnviar.setTextColor(colorBlack);
        }else{
            btnEnviar.setBackgroundTintList(colorEstadoNegro);
            btnEnviar.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviar.setEnabled(false);

        btnEnviar.setBackgroundTintList(colorEstadoGris);
        btnEnviar.setTextColor(colorBlanco);
    }


    private void verificarDatos(){

        if(boolDialogEnviar){
            boolDialogEnviar = false;

            progressBar.setVisibility(View.VISIBLE);

            String txtPassword = Objects.requireNonNull(edtContrasena.getText()).toString();
            String iduser = tokenManager.getToken().getId();

            compositeDisposable.add(
                    service.actualizarPassword(iduser, txtPassword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolDialogEnviar = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                                // actualizado
                                                vistaAtrasActualizado();
                                            }
                                            else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolDialogEnviar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }


    private void vistaAtras(){
        finish();
    }

    private void vistaAtrasActualizado(){
        Toasty.success(this, getString(R.string.actualizado), Toasty.LENGTH_SHORT).show();
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

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}