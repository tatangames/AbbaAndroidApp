package com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.cuestionario.AdaptadorSpinnerTipoLetraCuestionario;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.preguntas.AdaptadorPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.ModeloTipoLetraCuestionario;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCuestionarioPlanBloque extends Fragment {

    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private FloatingActionButton fabButton;

    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private static final String ARG_DATO = "IDBLOQUE";

    private FrameLayout linear;

    private int idBloqueDeta = 0;

    private boolean bottomSheetShowing = false;

    private int textSize = 19; // Tamaño de fuente inicial
    private static final int MIN_TEXT_SIZE = 18;
    private static final int MAX_TEXT_SIZE = 28;

    private boolean tema = false;

    private ColorStateList colorStateTintWhite, colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;
    private WebView webViewTitulo, webViewTexto;
    public static FragmentCuestionarioPlanBloque newInstance(int dato) {
        FragmentCuestionarioPlanBloque fragment = new FragmentCuestionarioPlanBloque();
        Bundle args = new Bundle();
        args.putInt(ARG_DATO, dato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cuestionario_plan_bloque, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        fabButton = vista.findViewById(R.id.fabButton);
        linear = vista.findViewById(R.id.linear);
        webViewTitulo = vista.findViewById(R.id.webViewTitulo);
        webViewTexto = vista.findViewById(R.id.webViewTexto);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        webViewTitulo.setWebViewClient(new WebViewClient());
        WebSettings webSettingsTitulo = webViewTitulo.getSettings();
        webSettingsTitulo.setJavaScriptEnabled(true);

        webViewTexto.setWebViewClient(new WebViewClient());
        WebSettings webSettingsTexto = webViewTexto.getSettings();
        webSettingsTexto.setJavaScriptEnabled(true);




        idBloqueDeta = getArguments().getInt(ARG_DATO, 0);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        fabButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.blanco)));

        fabButton.setOnClickListener(v -> {
            verOpciones();
        });

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);


        // verificar tamano de letra, es cuando es primera vez
        if(tokenManager.getToken().getTamanoLetra() == 0){
            tokenManager.guardarTamanoLetraCuestionario(textSize);
        }else{
            textSize = tokenManager.getToken().getTamanoLetra();
        }


        // transparente fondo
        webViewTitulo.setBackgroundColor(Color.TRANSPARENT);
        webViewTexto.setBackgroundColor(Color.TRANSPARENT);

        if(tokenManager.getToken().getTema() == 1){ // dark
            tema = true;
        }

        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        apiBuscarCuestionario();


        // DETECTA SI SE TOCO EL WEBVIEW, Y AQUI SE OBTIENE A CUAL VERSICULO REDIRECCIONAR.

        webViewTitulo.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Obtener las coordenadas del evento de clic
                    float x = event.getX();
                    float y = event.getY();

                    // Verificar si el clic se encuentra dentro del WebView
                    if (x >= 0 && x <= v.getWidth() && y >= 0 && y <= v.getHeight()) {
                        redireccionarBiblia();
                    }
                    break;
            }
            return false;
        });

        return vista;
    }

    private void redireccionarBiblia(){

    }

    private void apiBuscarCuestionario(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionCuestionarioBloqueDetalle(iduser, idBloqueDeta, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            // siempre vendran
                                            String titulo = apiRespuesta.getTitulo();
                                            String texto = apiRespuesta.getTexto();

                                            setearTexto(titulo, texto);
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {
                                            // BLOQUE NO TIENE CUESTIONARIO
                                            Toasty.info(getContext(), getString(R.string.cuestionario_no_encontrado), Toasty.LENGTH_SHORT).show();
                                            linear.setVisibility(View.INVISIBLE);
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


    // carga opciones para el texto html
    private void verOpciones(){

        if (!bottomSheetShowing) {
            bottomSheetShowing = true;

            List<ModeloTipoLetraCuestionario> estilosList = new ArrayList<>();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.cardview_bottonsheet_tipoletra_cuestionario, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            Button btnMenos = bottomSheetDialog.findViewById(R.id.btnMenos);
            Button btnMas = bottomSheetDialog.findViewById(R.id.btnMas);
            Spinner spinEstiloTexto = bottomSheetDialog.findViewById(R.id.estiloSpinner);

            AdaptadorSpinnerTipoLetraCuestionario adapterModelo = new AdaptadorSpinnerTipoLetraCuestionario(getContext(), android.R.layout.simple_spinner_item, tema);

            estilosList.add(new ModeloTipoLetraCuestionario(0, "Noto Sans Light"));
            estilosList.add(new ModeloTipoLetraCuestionario(1, "Noto Sans Medium"));
            estilosList.add(new ModeloTipoLetraCuestionario(2, "Time New Roman"));

            for (ModeloTipoLetraCuestionario listado : estilosList) {
                adapterModelo.add(listado.getNombre());
            }

            spinEstiloTexto.setAdapter(adapterModelo);

            // CAMBIAR POSICION
            int posTexto = tokenManager.getToken().getTipoLetra();

            if (posTexto >= 0 && posTexto < spinEstiloTexto.getAdapter().getCount()) {
                spinEstiloTexto.setSelection(posTexto);
            }else{
                spinEstiloTexto.setSelection(0);
            }

            // GUARDAR TEXTO SELECCIONADO
            spinEstiloTexto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(position == 0){
                        // NOTO SANS LIGHT
                        String fontName = "Fuente1";
                        tokenManager.guardarTipoLetraTexto(0);
                        webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if(position == 1){
                        // NOTO SANS MEDIUM
                        String fontName = "Fuente2";
                        tokenManager.guardarTipoLetraTexto(1);
                        webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if(position == 2){
                        // TIMES NEW
                        String fontName = "Fuente3";
                        tokenManager.guardarTipoLetraTexto(2);
                        webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }else{
                        // NOTO SANS LIGHT
                        String fontName = "Fuente1";
                        tokenManager.guardarTipoLetraTexto(0);
                        webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Implementa según sea necesario
                }
            });

            // CAMBIAR ESTILO BOTONES


            if(tema){ // dark
                btnMas.setBackgroundTintList(colorStateTintWhite);
                btnMas.setTextColor(colorBlack);

                btnMenos.setBackgroundTintList(colorStateTintWhite);
                btnMenos.setTextColor(colorBlack);
            }else{
                btnMas.setBackgroundTintList(colorStateTintBlack);
                btnMas.setTextColor(colorBlanco);

                btnMenos.setBackgroundTintList(colorStateTintBlack);
                btnMenos.setTextColor(colorBlanco);
            }

            // TAMANOS DE TEXTOS

            btnMenos.setOnClickListener(v ->{

                if (textSize > MIN_TEXT_SIZE) {
                    textSize--;
                    disminuirTamañoTexto();
                }
            });

            btnMas.setOnClickListener(v -> {
                if (textSize < MAX_TEXT_SIZE) {
                    textSize++;
                    aumentarTamañoTexto();
                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }


    private void aumentarTamañoTexto() {
        webViewTitulo.evaluateJavascript("aumentarTamaño();", null);
        webViewTexto.evaluateJavascript("aumentarTamaño();", null);
    }

    private void disminuirTamañoTexto() {
        webViewTitulo.evaluateJavascript("disminuirTamaño();", null);
        webViewTexto.evaluateJavascript("disminuirTamaño();", null);
    }


    private void setearTexto(String titulo, String texto){

        webViewTitulo.loadDataWithBaseURL(null, titulo, "text/html", "UTF-8", null);
        webViewTexto.loadDataWithBaseURL(null, texto, "text/html", "UTF-8", null);

        webViewTitulo.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Realiza el cambio de letra aquí
                // TIPOS DE LETRA POR DEFECTO
                int tipoLetra = tokenManager.getToken().getTipoLetra();

                if(tipoLetra == 0){
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 1){
                    // NOTO SANS MEDIUM
                    String fontName = "Fuente2";
                    webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 2){
                    // TIMES NEW
                    String fontName = "Fuente3";
                    webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }else{
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webViewTitulo.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", textSize);
                webViewTitulo.evaluateJavascript(javascript, null);

                if(tema){
                    String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                    webViewTitulo.evaluateJavascript(javascriptColor, null);
                }else{
                    String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                    webViewTitulo.evaluateJavascript(javascriptColor, null);
                }

                webViewTitulo.setVisibility(View.VISIBLE);
            }
        });




        webViewTexto.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Realiza el cambio de letra aquí
                // TIPOS DE LETRA POR DEFECTO
                int tipoLetra = tokenManager.getToken().getTipoLetra();

                if(tipoLetra == 0){
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 1){
                    // NOTO SANS MEDIUM
                    String fontName = "Fuente2";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 2){
                    // TIMES NEW
                    String fontName = "Fuente3";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }else{
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", textSize);
                webViewTexto.evaluateJavascript(javascript, null);

                if(tema){
                    String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                    webViewTexto.evaluateJavascript(javascriptColor, null);
                }else{
                    String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                    webViewTexto.evaluateJavascript(javascriptColor, null);
                }

                webViewTexto.setVisibility(View.VISIBLE);
            }
        });

        new Handler().postDelayed(() -> {
            webViewTitulo.setVisibility(View.VISIBLE);
            webViewTexto.setVisibility(View.VISIBLE);
        }, 1000);

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

