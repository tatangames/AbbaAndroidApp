package com.tatanstudios.abbaappandroid.activity.planes.completados.cuestionario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.cuestionario.AdaptadorViewPagerCuestionario;
import com.tatanstudios.abbaappandroid.network.TokenManager;

public class CuestionarioPlanCompletadoActivity extends AppCompatActivity {


    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private TokenManager tokenManager;
    private int tabStrokeColor, tabTextColor, colorPrimary;
    private int idBloqueDeta = 0;
    private int idTienePreguntas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_plan_completado);

        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idBloqueDeta = bundle.getInt("IDBLOQUE");
            idTienePreguntas = bundle.getInt("PREGUNTAS");
        }

        int cantidadFragment = 1;

        if(idTienePreguntas == 1){
            // si hay preguntas
            cantidadFragment = 2;
        }

        if (tokenManager.getToken().getTema() == 1) {
            colorPrimary = ContextCompat.getColor(this, R.color.blanco); // Obtén el color para tema dark
            tabTextColor = ContextCompat.getColor(this, R.color.gris616161); // Obtén el color para tema dark
            tabStrokeColor = ContextCompat.getColor(this, R.color.negro); // Obtén el color para tema dark
        } else {
            colorPrimary = ContextCompat.getColor(this, R.color.blanco); // Obtén el color para tema light
            tabTextColor = ContextCompat.getColor(this, R.color.gris616161); // Obtén el color para tema light
            tabStrokeColor = ContextCompat.getColor(this, R.color.negro); // Obtén el color para tema light
        }


        // Configura el ViewPager2 con el adaptador
        AdaptadorViewPagerCuestionario pagerAdapter = new AdaptadorViewPagerCuestionario(this, cantidadFragment, idBloqueDeta);
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.setBackgroundColor(colorPrimary);
        tabLayout.setTabTextColors(tabTextColor, tabStrokeColor);

        // Conecta el ViewPager2 al TabLayout
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    // Establece el texto de la pestaña según la posición
                    if (position == 0) {
                        tab.setText(getString(R.string.devocional));
                    } else {
                        tab.setText(getString(R.string.meditacion));
                    }
                }
        ).attach();
    }

}