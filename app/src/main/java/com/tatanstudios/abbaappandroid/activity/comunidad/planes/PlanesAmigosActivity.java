package com.tatanstudios.abbaappandroid.activity.comunidad.planes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.tatanstudios.abbaappandroid.R;

import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesAmigos;

public class PlanesAmigosActivity extends AppCompatActivity {


    private int idsolicitud = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_amigos);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idsolicitud = bundle.getInt("IDSOLICITUD");
        }

        FragmentPlanesAmigos fragmentPlanesAmigos = new FragmentPlanesAmigos();

        Bundle bundle = new Bundle();
        bundle.putInt("IDSOLICITUD", idsolicitud);
        fragmentPlanesAmigos.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentPlanesAmigos)
                .commit();
    }




}