package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.AdaptadorCapitulos;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesAmigos;
import com.tatanstudios.abbaappandroid.fragmentos.login.FragmentLogin;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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