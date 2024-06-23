package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.devocapitulo.FragmentDevoCapitulo;

public class LecturaDevoCapituloActivity extends AppCompatActivity {

    private int iddevobiblia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_devo_capitulo);


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            iddevobiblia = bundle.getInt("IDDEVOBIBLIA");
        }

        FragmentDevoCapitulo fragmentDevoCapitulo = new FragmentDevoCapitulo();

        Bundle bundle = new Bundle();
        bundle.putInt("IDDEVOBIBLIA", iddevobiblia);
        fragmentDevoCapitulo.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentDevoCapitulo)
                .commit();
    }





}