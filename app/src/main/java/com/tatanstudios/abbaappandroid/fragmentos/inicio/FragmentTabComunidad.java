package com.tatanstudios.abbaappandroid.fragmentos.inicio;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.AgregarAmigoComunidadActivity;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudesPendientesActivity;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.AdaptadorComunidadAceptadas;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.individual.AdaptadorInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloContedorComunidad;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloVistaComunidad;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentTabComunidad extends Fragment {


    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    int colorProgress = 0;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco = 0;
    private int colorBlack = 0;

    private boolean tema = false;

    private AdaptadorComunidadAceptadas adaptadorComunidadAceptadas;

    private ArrayList<ModeloVistaComunidad> elementos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_comunidad, container, false);


        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);
        int colorGris = ContextCompat.getColor(requireContext(), R.color.gris616161);

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        apiSolicitudesAceptadas();

        return vista;
    }


    private void apiSolicitudesAceptadas(){

        String iduser = tokenManager.getToken().getId();

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.listadoComunidadAceptado(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                           setearCampos(apiRespuesta);

                                        }
                                        else{
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


    private void setearCampos(ModeloContedorComunidad apiRespuesta){

        // botonera siempre ira
        elementos.add(new ModeloVistaComunidad( ModeloVistaComunidad.TIPO_BOTONERA,
                null
        ));


        if(apiRespuesta.getHayinfo() == 1){

            for (ModeloComunidad mm : apiRespuesta.getModeloComunidads()){
                elementos.add(new ModeloVistaComunidad( ModeloVistaComunidad.TIPO_RECYCLER,
                        new ModeloComunidad(mm.getId(), mm.getNombre(),
                                mm.getIglesia(), mm.getCorreo(), mm.getPais(),
                                mm.getIdpais())
                ));
            }

        }else{
            elementos.add(new ModeloVistaComunidad( ModeloVistaComunidad.TIPO_NOAMIGO,
                    null
            ));
        }

        adaptadorComunidadAceptadas = new AdaptadorComunidadAceptadas(getContext(), elementos, this, tema);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adaptadorComunidadAceptadas);
    }


    public void vistaEnviarSolicitud(){
        Intent intent = new Intent(getContext(), AgregarAmigoComunidadActivity.class);
        startActivity(intent);
    }

    public void vistaSolicitudPendientes(){
        Intent intent = new Intent(getContext(), SolicitudesPendientesActivity.class);
        startActivity(intent);
    }


    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getActivity(), getString(R.string.error_intentar_de_nuevo)).show();
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
