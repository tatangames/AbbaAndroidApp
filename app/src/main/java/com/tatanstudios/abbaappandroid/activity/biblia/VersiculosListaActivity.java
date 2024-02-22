package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.abbaappandroid.R;

public class VersiculosListaActivity extends AppCompatActivity {

    private int idcapibloque = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculos_lista);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idcapibloque = bundle.getInt("IDCAPIBLOQUE");
        }
    }




}