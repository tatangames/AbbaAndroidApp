package com.tatanstudios.abbaappandroid.fragmentos.planes.completados;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.VerPlanParaSeleccionarActivity;
import com.tatanstudios.abbaappandroid.adaptadores.planes.buscarplanes.AdaptadorBuscarPlanes;
import com.tatanstudios.abbaappandroid.adaptadores.planes.completados.AdaptadorPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanesPaginateRequest;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentPlanesCompletados extends Fragment {

    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView txtSinPlanes;
    private boolean unaVezVisibilidad = true;
    private AdaptadorPlanesCompletados adapter;
    private int RETORNO_ACTUALIZAR_100 = 100;


    // AJUSTES DE PAGINACION

    private boolean estaCargandoApi = false;
    private boolean puedeCargarYaPaginacion = false;
    private int currentPage = 1;
    private int lastPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes_completados, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPlanes = vista.findViewById(R.id.txtSinPlanes);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        // Configura el LinearLayoutManager y el ScrollListener para manejar la paginación
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // PAGINACION
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (puedeCargarYaPaginacion && !isLastPage()) {

                    // Verificar si no se está cargando y si ha llegado al final de la lista
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && dy > 0) {
                        apiBuscarPlanesNuevosPaginacion();
                    }
                }
            }
        });

        apiBuscarPlanesNuevos();

        return vista;
    }


    // verificar estado de carga para paginacion
    private boolean isLastPage(){
        if(currentPage > lastPage){
            return true;
        }else{
            return false;
        }
    }


    private void apiBuscarPlanesNuevos(){

        progressBar.setVisibility(View.VISIBLE);

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        ModeloBuscarPlanesPaginateRequest paginationRequest = new ModeloBuscarPlanesPaginateRequest();
        paginationRequest.setPage(currentPage);
        paginationRequest.setLimit(10);
        paginationRequest.setIdiomaplan(idiomaPlan);
        paginationRequest.setIduser(iduser);

        compositeDisposable.add(
                service.listadoPlanesCompletados(paginationRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(
                                apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta.getSuccess() == 1){

                                        if(apiRespuesta.getHayinfo() == 1){
                                            if (!apiRespuesta.getData().getData().isEmpty()) {
                                                List<ModeloBuscarPlanes> newData = apiRespuesta.getData().getData();

                                                lastPage = apiRespuesta.getData().getLastPage();
                                                currentPage++;

                                                setearAdapter(newData);

                                                unaVezVisibilidad = false;
                                                puedeCargarYaPaginacion = true;
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }
                                        }else{
                                            if(unaVezVisibilidad){
                                                unaVezVisibilidad = false;
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                }
                        ));
    }





    // UTILIZADO PARA PAGINACION
    private void apiBuscarPlanesNuevosPaginacion(){

        if(!estaCargandoApi){
            estaCargandoApi = true;

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

            ModeloBuscarPlanesPaginateRequest paginationRequest = new ModeloBuscarPlanesPaginateRequest();
            paginationRequest.setPage(currentPage);
            paginationRequest.setLimit(10);
            paginationRequest.setIdiomaplan(idiomaPlan);
            paginationRequest.setIduser(iduser);

            compositeDisposable.add(
                    service.listadoNuevosPlanes(paginationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(
                                    apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);

                                        if(apiRespuesta.getSuccess() == 1){

                                            if(apiRespuesta.getHayinfo() == 1){
                                                if (!apiRespuesta.getData().getData().isEmpty()) {
                                                    List<ModeloBuscarPlanes> newData = apiRespuesta.getData().getData();

                                                    lastPage = apiRespuesta.getData().getLastPage();
                                                    currentPage++;

                                                    adapter.addData(newData);

                                                    estaCargandoApi = false;
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                }
                                            }

                                        }else{
                                            mensajeSinConexion();
                                            estaCargandoApi = false;
                                        }
                                    },
                                    throwable -> {
                                        mensajeSinConexion();
                                        estaCargandoApi = false;
                                        //String errorMessage = throwable.getMessage();
                                    }
                            ));
        }
    }


    private void setearAdapter(List<ModeloBuscarPlanes> modeloBuscarPlanes) {
        adapter = new AdaptadorBuscarPlanes(getContext(), modeloBuscarPlanes, this);
        recyclerView.setAdapter(adapter);
    }


    // Vista para ver todos completados
    public void verBloquePlanesVista(int idplanes){
        /*Intent intent = new Intent(getActivity(), VerPlanParaSeleccionarActivity.class);
        intent.putExtra("ID", idplanes);
        someActivityResultLauncher.launch(intent);*/
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY VerPlanParaSeleccionar.class
                if(result.getResultCode() == RETORNO_ACTUALIZAR_100){

                    currentPage = 1;
                    unaVezVisibilidad = true;
                    recyclerView.setVisibility(View.INVISIBLE);
                    apiBuscarPlanesNuevos();
                }
            });


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
