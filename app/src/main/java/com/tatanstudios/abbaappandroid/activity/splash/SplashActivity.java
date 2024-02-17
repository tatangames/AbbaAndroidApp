package com.tatanstudios.abbaappandroid.activity.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.login.LoginActivity;
import com.tatanstudios.abbaappandroid.activity.principal.PrincipalActivity;
import com.tatanstudios.abbaappandroid.extras.LocaleManagerIdiomaAndroid;
import com.tatanstudios.abbaappandroid.fragmentos.login.FragmentLogin;
import com.tatanstudios.abbaappandroid.network.TokenManager;

public class SplashActivity extends AppCompatActivity {


    // tiempo para cambiar de activity
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    TokenManager tokenManager;


    // para comparar el idioma del sistema android del usuario
    private static final String APP_INGLES = "en";
    private static final String APP_ESPANOL = "es";

    private ConstraintLayout constraintLayout;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        constraintLayout = findViewById(R.id.linearConstraint);
        frameLayout = findViewById(R.id.fragmentContenedor);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


        // buscando idioma del telefono por defecto
        if(tokenManager.getToken().getIdiomaCel() == 0){
            String valor = LocaleManagerIdiomaAndroid.obtenerIdioma(this);


            if(valor.equals(APP_INGLES)){ // ingles
                tokenManager.guardarIdiomaApp(2);
                tokenManager.guardarIdiomaTexto(2);
                LocaleManagerIdiomaAndroid.setLocale(this, APP_INGLES);
            }else if(valor.equals(APP_ESPANOL)){ // espanol
                tokenManager.guardarIdiomaApp(1);
                tokenManager.guardarIdiomaTexto(1);
                LocaleManagerIdiomaAndroid.setLocale(this, APP_ESPANOL);
            }else{
                // defecto sera espanol
                tokenManager.guardarIdiomaApp(1);
                tokenManager.guardarIdiomaTexto(1);
                LocaleManagerIdiomaAndroid.setLocale(this, APP_ESPANOL);
            }

            tokenManager.guardarIdiomaTelefono(1);
        }else{
            // espanol
            if(tokenManager.getToken().getIdiomaApp() == 1){
                LocaleManagerIdiomaAndroid.setLocale(this, APP_ESPANOL);
            }
            // ingles
            else if(tokenManager.getToken().getIdiomaApp() == 2){
                LocaleManagerIdiomaAndroid.setLocale(this, APP_INGLES);
            }
            else{
                LocaleManagerIdiomaAndroid.setLocale(this, APP_ESPANOL);
            }
        }


        new Handler().postDelayed(() -> {

            // inicio automatico con token que iria en el SPLASH
            if(!TextUtils.isEmpty(tokenManager.getToken().getId())){

                // Siguiente Actvity
                Intent intent = new Intent(this, PrincipalActivity.class);
                startActivity(intent);
                finish();

            }else {

                constraintLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContenedor, new FragmentLogin())
                        .commit();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



}