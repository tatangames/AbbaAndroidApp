package com.tatanstudios.abbaappandroid.fragmentos.comunidad;

import static android.content.Context.MODE_PRIVATE;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesAmigos;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesItemsAmigos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentPlanesItemsAmigos extends Fragment {


    private TextView txtSinDevocionales;

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private ImageView imgFlechaAtras;

    private AdaptadorPlanesItemsAmigos adaptadorPlanesItemsAmigos;

    private TextView txtToolbar;


    private int idplan = 0;

    private int usuariobuscado = 0;

    private OnBackPressedDispatcher onBackPressedDispatcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planesitems_amigos, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinDevocionales = vista.findViewById(R.id.txtSinDevocionales);
        txtToolbar = vista.findViewById(R.id.txtToolbar);


        txtToolbar.setText(getString(R.string.devocional));

        Bundle bundle = getArguments();
        if(bundle != null) {
            idplan = bundle.getInt("IDPLAN");
            usuariobuscado = bundle.getInt("IDUSER");
        }

        int colorProgress = ContextCompat.getColor(getContext(), R.color.barraProgreso);

        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getActivity().getOnBackPressedDispatcher();

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        imgFlechaAtras.setOnClickListener(v -> {
            onBackPressedDispatcher.onBackPressed();
        });


        apiBuscarPlanes();


        return vista;
    }


    private void apiBuscarPlanes(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoPlanesItemsAmigoComunidad(idplan, idiomaPlan, usuariobuscado, iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){

                                                adaptadorPlanesItemsAmigos = new AdaptadorPlanesItemsAmigos(getContext(), apiRespuesta.getModeloMisPlanes(), this);
                                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                                                recyclerView.addItemDecoration(dividerItemDecoration);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorPlanesItemsAmigos);
                                            }else{

                                                recyclerView.setVisibility(View.GONE);
                                                txtSinDevocionales.setVisibility(View.VISIBLE);
                                            }

                                        }else{

                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }



    public void verItemsPlanPregunta(int idplanblockdetauser){

        // cargar esa pregunt aque ya esta guardada
        FragmentPlanesItemsPreAmigos fragmentInfo = new FragmentPlanesItemsPreAmigos();

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if (currentFragment.getClass().equals(fragmentInfo.getClass())) return;

        Bundle bundle = new Bundle();
        bundle.putInt("IDPLANBLOCKDETAUSER", idplanblockdetauser);
        bundle.putInt("IDUSER", usuariobuscado);

        fragmentInfo.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentInfo)
                .addToBackStack(null)
                .commit();

    }


    private void volverAtras(){

    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getContext(), getString(R.string.error_intentar_de_nuevo)).show();
    }


    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }



}
