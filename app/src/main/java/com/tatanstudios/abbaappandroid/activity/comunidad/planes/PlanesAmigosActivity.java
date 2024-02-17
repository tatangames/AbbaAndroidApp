package com.tatanstudios.abbaappandroid.activity.comunidad.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesAmigos;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesOcultos;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesAmigos;
import com.tatanstudios.abbaappandroid.fragmentos.login.FragmentLogin;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesOcultos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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