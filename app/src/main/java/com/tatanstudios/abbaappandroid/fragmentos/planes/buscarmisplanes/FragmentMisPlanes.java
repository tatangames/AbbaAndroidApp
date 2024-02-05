package com.tatanstudios.abbaappandroid.fragmentos.planes.buscarmisplanes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.MisPlanesBloquesFechaActivity;
import com.tatanstudios.abbaappandroid.activity.planes.VerPlanParaSeleccionarActivity;
import com.tatanstudios.abbaappandroid.adaptadores.planes.buscarplanes.AdaptadorBuscarPlanes;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.AdaptadorMisPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanesPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesBloque1;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesBloque2;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesPaginateRequest;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloVistaMisPlanes;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMisPlanes extends Fragment {

    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView txtSinPlanes;

    private int RETORNO_ACTUALIZAR_100 = 100;

    private ArrayList<ModeloVistaMisPlanes> elementos;

    // para saver si hay mas de 1 elemento en la primera vez que se pide datos,
    // como es paginacion puede venir una pagina sin datos

    private boolean unaVezRecyclerOcultar = true;


    // AJUSTES DE PAGINACION

    private boolean estaCargandoApi = false;
    private boolean puedeCargarYaPaginacion = false;
    private int currentPage = 1;
    private int lastPage = 1;

    private AdaptadorMisPlanes adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mis_planes, container, false);

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

        elementos = new ArrayList<>();

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
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        apiBuscarMisPlanesPaginate();
                    }
                }
            }
        });

        apiBuscarMisPlanes();

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


    private void apiBuscarMisPlanes(){

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
            unaVezRecyclerOcultar = true;

            ModeloMisPlanesPaginateRequest paginationRequest = new ModeloMisPlanesPaginateRequest();
            paginationRequest.setPage(currentPage);
            paginationRequest.setLimit(10);
            paginationRequest.setIdiomaplan(idiomaPlan);
            paginationRequest.setIduser(iduser);

            compositeDisposable.add(
                    service.listadoMisPlanes(paginationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(
                                    apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta.getSuccess() == 1){

                                        if(apiRespuesta.getHaycontinuar() == 1){
                                            unaVezRecyclerOcultar = false;
                                            setearDatosContinuar(apiRespuesta.getModeloMisPlanesBloque1());
                                        }

                                        if(apiRespuesta.getHayinfo() == 1){
                                            unaVezRecyclerOcultar = false;

                                            if (!apiRespuesta.getData().getData().isEmpty()) {
                                                List<ModeloMisPlanes> newData = apiRespuesta.getData().getData();

                                                lastPage = apiRespuesta.getData().getLastPage();
                                                currentPage++;

                                                setearDatosLista(newData);
                                            }
                                        }

                                        llenarAdaptador();

                                        if(unaVezRecyclerOcultar){
                                            recyclerView.setVisibility(View.GONE);
                                            txtSinPlanes.setVisibility(View.VISIBLE);
                                        }else{
                                            recyclerView.setVisibility(View.VISIBLE);
                                            puedeCargarYaPaginacion = true;
                                        }

                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                    String errorMessage = throwable.getMessage();
                                }
                        ));

    }

    // 1 vez sera realizado
    private void setearDatosContinuar(List<ModeloMisPlanesBloque1> modeloMisPlanesBloque1) {

        // viene en array pero solo sera 1 vuelta
        for (ModeloMisPlanesBloque1 m : modeloMisPlanesBloque1){
            elementos.add(new ModeloVistaMisPlanes( ModeloVistaMisPlanes.TIPO_CONTINUAR, new ModeloMisPlanesBloque1(
                    m.getIdPlanes(),
                    m.getTitulo(),
                    m.getImagenPortada()),
                    null
            ));
        }
    }


    private void setearDatosLista(List<ModeloMisPlanes> modeloMisPlanes) {


        // meter listado mis planes
        for (ModeloMisPlanes m : modeloMisPlanes){
            elementos.add(new ModeloVistaMisPlanes( ModeloVistaMisPlanes.TIPO_PLANES,
                 null,
                    new ModeloMisPlanesBloque2(
                            m.getIdPlanes(), // debe ser id plan
                            m.getTitulo(),
                            m.getImagen(),
                            m.getSubtitulo()
                    )
            ));
        }
    }

    private void llenarAdaptador(){
        adapter = new AdaptadorMisPlanes(getContext(), elementos, this);
        recyclerView.setAdapter(adapter);
    }



    //*********************************
    private void apiBuscarMisPlanesPaginate(){

        if(!estaCargandoApi){
            estaCargandoApi = true;
            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();
            int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

            ModeloMisPlanesPaginateRequest paginationRequest = new ModeloMisPlanesPaginateRequest();
            paginationRequest.setPage(currentPage);
            paginationRequest.setLimit(10);
            paginationRequest.setIdiomaplan(idiomaPlan);
            paginationRequest.setIduser(iduser);

            compositeDisposable.add(
                    service.listadoMisPlanes(paginationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(
                                    apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);

                                        if(apiRespuesta.getSuccess() == 1){

                                            // solo si hay datos nuevos de paginate
                                            if(apiRespuesta.getHayinfo() == 1){
                                                if (!apiRespuesta.getData().getData().isEmpty()) {
                                                    List<ModeloMisPlanes> newData = apiRespuesta.getData().getData();

                                                    currentPage++;

                                                    adapterPaginado(newData);

                                                    estaCargandoApi = false;
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

    private void adapterPaginado(List<ModeloMisPlanes> modeloMisPlanes){

        int startPosition = elementos.size();

        for (ModeloMisPlanes m : modeloMisPlanes){
            elementos.add(new ModeloVistaMisPlanes( ModeloVistaMisPlanes.TIPO_PLANES,
                    null,
                    new ModeloMisPlanesBloque2(
                            m.getIdPlanes(),
                            m.getTitulo(),
                            m.getImagen(),
                            m.getSubtitulo()
                    )
            ));
        }

        adapter.notifyItemRangeInserted(startPosition, elementos.size());
    }


    public void planesBloquesFecha(int idplan){
        Intent intent = new Intent(getActivity(), MisPlanesBloquesFechaActivity.class);
        intent.putExtra("IDPLAN", idplan);
        someActivityResultLauncher.launch(intent);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY VerPlanParaSeleccionar.class
                if(result.getResultCode() == RETORNO_ACTUALIZAR_100){

                    recyclerView.setVisibility(View.GONE);
                    elementos.clear();
                    currentPage = 1;

                    apiBuscarMisPlanes();
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
