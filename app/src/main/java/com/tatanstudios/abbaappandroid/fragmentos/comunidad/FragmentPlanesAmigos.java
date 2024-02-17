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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes.AdaptadorPlanesAmigos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentPlanesAmigos extends Fragment {

    private TextView txtSinPlanes;

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private ImageView imgFlechaAtras;

    private AdaptadorPlanesAmigos adaptadorPlanesAmigos;

    private TextView txtToolbar;


    private int idsolicitud = 0;

    private int usuariobuscado = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes_amigos, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPlanes = vista.findViewById(R.id.txtSinPlanes);
        txtToolbar = vista.findViewById(R.id.txtToolbar);


        txtToolbar.setText(getString(R.string.planes));

        Bundle bundle = getArguments();
        if(bundle != null) {
            idsolicitud = bundle.getInt("IDSOLICITUD");
        }

        int colorProgress = ContextCompat.getColor(getContext(), R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });


        apiBuscarPlanes();


        return vista;
    }


    private void apiBuscarPlanes(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoPlanesAmigoComunidad(idsolicitud, idiomaPlan, iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){

                                                usuariobuscado = apiRespuesta.getUsuariobuscado();

                                                adaptadorPlanesAmigos = new AdaptadorPlanesAmigos(getContext(), apiRespuesta.getModeloMisPlanes(), this);
                                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                                                recyclerView.addItemDecoration(dividerItemDecoration);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorPlanesAmigos);
                                            }else{

                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
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



    public void verItemsPlan(int idplan){

        FragmentPlanesItemsAmigos fragmentInfo = new FragmentPlanesItemsAmigos();

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if (currentFragment.getClass().equals(fragmentInfo.getClass())) return;

        Bundle bundle = new Bundle();
        bundle.putInt("IDPLAN", idplan);
        bundle.putInt("IDUSER", usuariobuscado);

        fragmentInfo.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentInfo)
                .addToBackStack(null)
                .commit();
    }


    private void volverAtras(){
       getActivity().finish();
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
