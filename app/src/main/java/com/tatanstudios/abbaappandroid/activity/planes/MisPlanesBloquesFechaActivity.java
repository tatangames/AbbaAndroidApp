package com.tatanstudios.abbaappandroid.activity.planes;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.tatanstudios.abbaappandroid.R;

import es.dmoral.toasty.Toasty;

public class MisPlanesBloquesFechaActivity extends AppCompatActivity {


    private ImageView flecha;
    private OnBackPressedDispatcher onBackPressedDispatcher;
    private final int RETORNO_ACTUALIZAR_100 = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_planes_bloques_fecha);

        flecha = findViewById(R.id.imgFlechaAtras);
        onBackPressedDispatcher = getOnBackPressedDispatcher();


        flecha.setOnClickListener(v -> {
            volverAtrasActualizado();
        });


    }


    private void volverAtrasActualizado(){
        Intent returnIntent = new Intent();
        setResult(RETORNO_ACTUALIZAR_100, returnIntent);

        Toasty.success(this, getString(R.string.plan_agregado)).show();
        onBackPressedDispatcher.onBackPressed();
    }
}