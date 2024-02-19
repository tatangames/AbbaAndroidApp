package com.tatanstudios.abbaappandroid.activity.notificacion;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.splash.SplashActivity;
import com.tatanstudios.abbaappandroid.network.TokenManager;

public class ActualizarIdiomasActivity extends AppCompatActivity {



    private static final int APP_ESPANOL = 1;
    private static final int APP_INGLES = 2;


    private ImageView imgFlechaAtras;
    private TextView txtIdiomaApp, txtIdiomaPlanesBiblia, txtToolbar;
    private TokenManager tokenManager;
    private boolean bottomDialogIdiomaApp, unaVezRadioIdioma, bottomDialogIdiomaTextos;

    private OnBackPressedDispatcher onBackPressedDispatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_idiomas);

        txtIdiomaApp = findViewById(R.id.txtIdiomaApp);
        txtIdiomaPlanesBiblia = findViewById(R.id.txtIdiomaPlanesBiblia);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        bottomDialogIdiomaApp = true;
        bottomDialogIdiomaTextos = true;
        unaVezRadioIdioma = true;

        txtToolbar.setText(getString(R.string.idioma));

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });

        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        setTextosIdiomas();

        txtIdiomaApp.setOnClickListener(v -> {
            cambioIdiomaApp();
        });

        txtIdiomaPlanesBiblia.setOnClickListener(v -> {
            cambioIdiomaTextos();
        });
    }

    private void volverAtras(){
        onBackPressedDispatcher.onBackPressed();
    }

    private void setTextosIdiomas(){

        // IDIOMA APP

        if(tokenManager.getToken().getIdiomaApp() == 1){ // espanol
            txtIdiomaApp.setText(getString(R.string.espanol));
        }
        else if(tokenManager.getToken().getIdiomaApp() == 2){ // ingles
            txtIdiomaApp.setText(getString(R.string.ingles));
        }else{
            // defecto espanol
            txtIdiomaApp.setText(getString(R.string.espanol));
        }

        if(tokenManager.getToken().getIdiomaTextos() == 1){ // espanol
            txtIdiomaPlanesBiblia.setText(getString(R.string.espanol));
        }
        else if(tokenManager.getToken().getIdiomaTextos() == 2){ // ingles
            txtIdiomaPlanesBiblia.setText(getString(R.string.ingles));
        }else{
            // defecto espanol
            txtIdiomaPlanesBiblia.setText(getString(R.string.espanol));
        }
    }



    private void cambioIdiomaApp(){
        if (bottomDialogIdiomaApp) {
            bottomDialogIdiomaApp = false;

            BottomSheetDialog bottomSheetDialogIdioma = new BottomSheetDialog(this);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.cardview_opcion_cambiar_idioma_app, null);
            bottomSheetDialogIdioma.setContentView(bottomSheetView);

            RadioButton radioIngles = bottomSheetDialogIdioma.findViewById(R.id.radio_button_english);
            RadioButton radioEspanol = bottomSheetDialogIdioma.findViewById(R.id.radio_button_spanish);

            if(tokenManager.getToken().getIdiomaApp() == 1){ // espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }
            else if(tokenManager.getToken().getIdiomaApp() == 2){ // ingles
                radioIngles.setChecked(true);
                radioEspanol.setChecked(false);
            }else{
                // defecto espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }

            radioIngles.setOnClickListener(v -> {
                if(unaVezRadioIdioma){
                    unaVezRadioIdioma = false;
                    radioEspanol.setEnabled(false);
                    tokenManager.guardarIdiomaApp(APP_INGLES);
                    changeLanguage();
                }

            });

            radioEspanol.setOnClickListener(v -> {
                if(unaVezRadioIdioma){
                    unaVezRadioIdioma = false;
                    radioEspanol.setEnabled(false);
                    tokenManager.guardarIdiomaApp(APP_ESPANOL);
                    changeLanguage();

                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialogIdioma.setOnDismissListener(dialog -> {
                bottomDialogIdiomaApp = true;
            });

            bottomSheetDialogIdioma.show();
        }
    }

    private void cambioIdiomaTextos(){
        if (bottomDialogIdiomaTextos) {
            bottomDialogIdiomaTextos = false;

            BottomSheetDialog bottomSheetDialogIdiomaTexto = new BottomSheetDialog(this);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.cardview_opcion_cambiar_idioma_app, null);
            bottomSheetDialogIdiomaTexto.setContentView(bottomSheetView);

            RadioButton radioIngles = bottomSheetDialogIdiomaTexto.findViewById(R.id.radio_button_english);
            RadioButton radioEspanol = bottomSheetDialogIdiomaTexto.findViewById(R.id.radio_button_spanish);

            if(tokenManager.getToken().getIdiomaTextos() == 1){ // espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }
            else if(tokenManager.getToken().getIdiomaTextos() == 2){ // ingles
                radioIngles.setChecked(true);
                radioEspanol.setChecked(false);
            }else{
                // defecto espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }

            radioIngles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtIdiomaPlanesBiblia.setText(getString(R.string.ingles));
                    tokenManager.guardarIdiomaTexto(APP_INGLES);
                    bottomSheetDialogIdiomaTexto.dismiss();
                }
            });

            radioEspanol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtIdiomaPlanesBiblia.setText(getString(R.string.espanol));
                    tokenManager.guardarIdiomaTexto(APP_ESPANOL);
                    bottomSheetDialogIdiomaTexto.dismiss();
                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialogIdiomaTexto.setOnDismissListener(dialog -> {
                bottomDialogIdiomaTextos = true;
            });

            bottomSheetDialogIdiomaTexto.show();
        }
    }


    private void changeLanguage() {

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE, false);

        pDialog.setTitleText(getString(R.string.idioma_actualizado));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentText(getString(R.string.para_aplicar_efectos_se_debe_reiniciar));
        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.reiniciar), sDialog -> {
            sDialog.dismissWithAnimation();
            reiniciarApp();
        });

        pDialog.show();
    }


    private void reiniciarApp(){
        Intent intentReload = new Intent(this, SplashActivity.class);
        intentReload.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentReload);
    }


}