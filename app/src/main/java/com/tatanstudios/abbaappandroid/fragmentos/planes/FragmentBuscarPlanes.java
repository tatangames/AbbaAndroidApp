package com.tatanstudios.abbaappandroid.fragmentos.planes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentPlanes;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentBuscarPlanes extends FragmentPlanes {

    private ProgressBar progressBar;
    private TokenManage tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloVistasBuscarPlanes> elementos;
    private final int ID_INTENT_RETORNO_10 = 10;
    private final int ID_INTENT_RETORNO_11 = 11;

    private TextView txtSinPlanes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_buscar_planes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPlanes = vista.findViewById(R.id.txtSinPlanes);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        apiBuscarPlanesNuevos();
        return vista;
    }

    private void apiBuscarPlanesNuevos(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.buscarPlanesNuevos(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){

                                                for (ModeloPlanesTitulo arrayTitulo : apiRespuesta.getModeloPlanesTitulos()) {
                                                    elementos.add(new ModeloVistasBuscarPlanes( ModeloVistasBuscarPlanes.TIPO_TITULO, new ModeloPlanesTitulo(arrayTitulo.getId(), arrayTitulo.getTitulo()), null));

                                                    for (ModeloPlanes arrayPlanes : arrayTitulo.getModeloPlanes()){

                                                        elementos.add(new ModeloVistasBuscarPlanes( ModeloVistasBuscarPlanes.TIPO_PLANES, null, new ModeloPlanes(
                                                                arrayPlanes.getId(),
                                                                arrayPlanes.getImagen(),
                                                                arrayPlanes.getTitulo(),
                                                                arrayPlanes.getSubtitulo()
                                                        )));
                                                    }
                                                }

                                                completarAdapter();


                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
                                            }
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


    private void completarAdapter(){
        AdaptadorBuscarNuevosPlanes adapter = new AdaptadorBuscarNuevosPlanes(getContext(), elementos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getActivity(), getString(R.string.error_intentar_de_nuevo)).show();
    }




    public void verPlanSeleccionado(int id){
        Intent intent = new Intent(getActivity(), VerPlanParaSeleccionarActivity.class);
        intent.putExtra("ID", id);
        someActivityResultLauncher.launch(intent);
    }

    public void verTodoPlanesContenedor(int id, String titulo){
        Intent intent = new Intent(getActivity(), PlanesContenedorActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("TITULO", titulo);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY VerPlanParaSeleccionar.class
                if(result.getResultCode() == ID_INTENT_RETORNO_10){
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    apiBuscarPlanesNuevos();
                }

                // DE ACTIVITY PlanesContenedorActivity.class
                else if(result.getResultCode() == ID_INTENT_RETORNO_11){
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    apiBuscarPlanesNuevos();
                }
            });


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