package com.tatanstudios.abbaappandroid.activity.biblia;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;

public class CapitulosBibliaActivity extends AppCompatActivity {

    private int idbiblia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulos_biblia);

        // trae id biblia


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idbiblia = bundle.getInt("IDBIBLIA");
        }

        FragmentCapitulos fragmentCapitulos = new FragmentCapitulos();

        Bundle bundle = new Bundle();
        bundle.putInt("IDBIBLIA", idbiblia);
        fragmentCapitulos.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentCapitulos)
                .commit();
    }


}