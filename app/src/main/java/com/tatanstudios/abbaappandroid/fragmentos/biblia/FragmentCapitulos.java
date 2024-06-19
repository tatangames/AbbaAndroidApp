package com.tatanstudios.abbaappandroid.fragmentos.biblia;

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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.VersiculoTextoActivity;
import com.tatanstudios.abbaappandroid.activity.biblia.VersiculosListaActivity;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.AdaptadorCapitulos;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCapitulos extends Fragment {


    // CAPITULOS DE LA BIBLIA


    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;
    private ProgressBar progressBar;

    private TextView txtToolbar;

    private int idbiblia = 0;
    private RecyclerView recyclerView;
    private AdaptadorCapitulos adaptadorCapitulos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_capitulos, container, false);

        txtToolbar = vista.findViewById(R.id.txtToolbar);
        rootRelative = vista.findViewById(R.id.rootRelative);
        recyclerView = vista.findViewById(R.id.recyclerView);

        txtToolbar.setText(getString(R.string.referencias));


        Bundle bundle = getArguments();
        if(bundle != null) {
            idbiblia = bundle.getInt("IDBIBLIA");
        }

        int colorProgress = ContextCompat.getColor(getContext(), R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiBuscarCapitulos();

        return vista;
    }


    private void apiBuscarCapitulos(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.listadoBibliasCapitulos(iduser, idioma, idbiblia)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            adaptadorCapitulos = new AdaptadorCapitulos(getContext(), apiRespuesta.getModeloCapitulos(), FragmentCapitulos.this);
                                            recyclerView.setAdapter(adaptadorCapitulos);
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


    // 18/06/2024
    // SE VA DIRECTO AL TEXTO, YA NO ELIGE VERSICULO
    public void bloqueCapitulo(int idcapibloque) {


        Intent intent = new Intent(getContext(), VersiculoTextoActivity.class);
        intent.putExtra("IDCAPITULO", idcapibloque);
        startActivity(intent);
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
