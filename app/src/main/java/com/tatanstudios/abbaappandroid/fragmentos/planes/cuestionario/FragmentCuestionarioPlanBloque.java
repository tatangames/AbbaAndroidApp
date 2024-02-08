package com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
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

    WebView webView;
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
        //txtHtml = vista.findViewById(R.id.txtCuestionario);
        fabButton = vista.findViewById(R.id.fabButton);
        linear = vista.findViewById(R.id.linear);
        //txtTitulo = vista.findViewById(R.id.txtTitulo);
        webView = vista.findViewById(R.id.webView);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);


        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

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


        // transparente fondo
        webView.setBackgroundColor(Color.TRANSPARENT);

        if(tokenManager.getToken().getTema() == 1){ // dark
            tema = true;
        }

        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        apiBuscarCuestionario();

        return vista;
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

                                            String texto = apiRespuesta.getTexto();

                                            setearTexto(texto);
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
                        webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if(position == 1){
                        // NOTO SANS MEDIUM
                        String fontName = "Fuente2";
                        tokenManager.guardarTipoLetraTexto(1);
                        webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if(position == 2){
                        // TIMES NEW
                        String fontName = "Fuente3";
                        tokenManager.guardarTipoLetraTexto(2);
                        webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }else{
                        // NOTO SANS LIGHT
                        String fontName = "Fuente1";
                        tokenManager.guardarTipoLetraTexto(0);
                        webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
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
        webView.evaluateJavascript("aumentarTamaño();", null);
    }

    private void disminuirTamañoTexto() {
        webView.evaluateJavascript("disminuirTamaño();", null);
    }


    private void setearTexto( String texto){

        webView.loadDataWithBaseURL(null, texto, "text/html", "UTF-8", null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Realiza el cambio de letra aquí
                // TIPOS DE LETRA POR DEFECTO
                int tipoLetra = tokenManager.getToken().getTipoLetra();

                if(tipoLetra == 0){
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 1){
                    // NOTO SANS MEDIUM
                    String fontName = "Fuente2";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 2){
                    // TIMES NEW
                    String fontName = "Fuente3";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }else{
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", textSize);
                webView.evaluateJavascript(javascript, null);

                if(tema){
                    String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                    webView.evaluateJavascript(javascriptColor, null);
                }else{
                    String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                    webView.evaluateJavascript(javascriptColor, null);
                }

                webView.setVisibility(View.VISIBLE);
            }
        });
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

