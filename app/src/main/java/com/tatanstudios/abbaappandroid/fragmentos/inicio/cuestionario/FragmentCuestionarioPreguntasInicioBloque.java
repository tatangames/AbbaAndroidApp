package com.tatanstudios.abbaappandroid.fragmentos.inicio.cuestionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.cuestionario.preguntas.AdaptadorPreguntasInicio;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.preguntas.AdaptadorPreguntas;
import com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloVistasPreguntas;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCuestionarioPreguntasInicioBloque extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private static final String ARG_DATO = "IDBLOQUE";

    private int idBloqueDeta = 0;

    private boolean temaActual = false;

    private Map<String,String> hashMapPreguntas = new HashMap<>();

    private TextView txtSinPreguntas;

    public static FragmentPreguntasPlanBloque newInstance(int dato) {
        FragmentPreguntasPlanBloque fragment = new FragmentPreguntasPlanBloque();
        Bundle args = new Bundle();
        args.putInt(ARG_DATO, dato);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<ModeloVistasPreguntas> elementos;

    private AdaptadorPreguntasInicio adapter;

    private List<ModeloPreguntas> modeloPreguntas;

    private boolean yaHabiaGuardado = false;
    private boolean boolApiActualizar = true;
    private boolean modalCompartir = false;

    private String tituloPreguntaBloque = "";

    public boolean isYaHabiaGuardado() {
        return yaHabiaGuardado;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_preguntas_plan_bloque, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPreguntas = vista.findViewById(R.id.txtSinPreguntas);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        idBloqueDeta = getArguments().getInt(ARG_DATO, 0);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


        if(tokenManager.getToken().getTema() == 1){ // dark
            temaActual = true;
        }

        elementos = new ArrayList<>();
        modeloPreguntas = new ArrayList<>();

        apiBuscarPreguntas();

        return vista;
    }

    private void apiBuscarPreguntas(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionPreguntasBloqueDetalle(iduser, idBloqueDeta, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            // colocado en un webview
                                            tituloPreguntaBloque = apiRespuesta.getDescripcion();

                                            if(apiRespuesta.getHayRespuesta() == 1){
                                                yaHabiaGuardado = true;
                                            }

                                            // Imagen
                                            elementos.add(new ModeloVistasPreguntas(ModeloVistasPreguntas.TIPO_IMAGEN, null));

                                            // Titulo
                                            elementos.add(new ModeloVistasPreguntas(ModeloVistasPreguntas.TIPO_TITULOP, null));

                                            // Preguntas
                                            for (ModeloPreguntas arrayPreguntas : apiRespuesta.getModeloPreguntas()) {
                                                elementos.add(new ModeloVistasPreguntas( ModeloVistasPreguntas.TIPO_PREGUNTA, new ModeloPreguntas(
                                                        arrayPreguntas.getId(),
                                                        arrayPreguntas.getRequerido(),
                                                        arrayPreguntas.getTitulo(),
                                                        arrayPreguntas.getTexto(),
                                                        arrayPreguntas.getIdImagenPregunta(),
                                                        arrayPreguntas.getImagen())
                                                ));
                                            }

                                            // Boton Guardar
                                            elementos.add(new ModeloVistasPreguntas(ModeloVistasPreguntas.TIPO_BOTON, null));

                                            modeloPreguntas = apiRespuesta.getModeloPreguntas();

                                            setearAdaptador();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {
                                            // BLOQUE NO TIENE CUESTIONARIO
                                            recyclerView.setVisibility(View.GONE);
                                            txtSinPreguntas.setVisibility(View.VISIBLE);
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


    private void setearAdaptador(){

        adapter = new AdaptadorPreguntasInicio(getContext(), elementos, this, tituloPreguntaBloque, temaActual);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void verificarDatosActualizar(){

        hashMapPreguntas.clear();

        boolean valor = false;
        for (ModeloPreguntas m : modeloPreguntas){
            if(adapter.getBoolFromEditText(m.getId())){
                valor = true;
            }
        }

        if(valor){
            Toasty.error(getContext(), getString(R.string.completar_campos), Toasty.LENGTH_SHORT).show();
            return;
        }

        // Obtener textos de los input text

        for (ModeloPreguntas m : modeloPreguntas){

            String texto = adapter.getTextoFromEditText(m.getId());

            hashMapPreguntas.put("idpregunta["+String.valueOf(m.getId())+"][txtpregunta]", texto);
        }

        apiActualizarPreguntas();
    }


    private void apiActualizarPreguntas(){

        if(boolApiActualizar){
            boolApiActualizar = false;

            progressBar.setVisibility(View.VISIBLE);

            String iduser = tokenManager.getToken().getId();

            compositeDisposable.add(
                    service.guardarPreguntasCuestionarioInicio(iduser, idBloqueDeta, hashMapPreguntas)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // NO RETRY
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolApiActualizar = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                                Toasty.success(getContext(), getString(R.string.actualizado), Toasty.LENGTH_SHORT).show();
                                            }
                                            else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolApiActualizar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    public void compartirDatosPreguntas(){

        String textoGlobal = "";

        boolean valor = false;
        for (ModeloPreguntas m : modeloPreguntas){
            if(adapter.getBoolFromEditText(m.getId())){

                valor = true;
            }
        }

        if(valor){
            Toasty.error(getContext(), getString(R.string.completar_campos), Toasty.LENGTH_SHORT).show();
            return;
        }

        if (!modalCompartir) {
            modalCompartir = true;

            new Handler().postDelayed(() -> {
                modalCompartir = false;
            }, 1000);


            // se ignora el primer cuadro
            boolean vuelta1 = false;

            for (ModeloPreguntas m : modeloPreguntas){

                if(vuelta1){
                    String textoPregunta = adapter.getTextoPregunta(m.getId());
                    String textoEdt = adapter.getTextoFromEditText(m.getId());

                    String linea = textoPregunta + "\n" + "R// " + textoEdt + "\n" + "\n";
                    textoGlobal += linea;
                }

                vuelta1 = true;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, textoGlobal);

            try {
                startActivity(Intent.createChooser(intent, getString(R.string.compartir)));
            } catch (Exception e) {

            }
        }
    }


    public void redireccionarBiblia(){


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