package com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.LecturaDevoCapituloActivity;
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

    private boolean tema = false;

    private ColorStateList colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;

    private WebView webViewTexto;

    public static FragmentCuestionarioPlanBloque newInstance(int dato) {
        FragmentCuestionarioPlanBloque fragment = new FragmentCuestionarioPlanBloque();
        Bundle args = new Bundle();
        args.putInt(ARG_DATO, dato);
        fragment.setArguments(args);
        return fragment;
    }


    private int currentFontSize = 18;
    private static final int MIN_FONT_SIZE = 15;
    private static final int MAX_FONT_SIZE = 30;



    private boolean redireccionarTextoBiblico = false;
    private int iddevobiblia = 0;


    private boolean redireccionarPaginaWeb = false;
    private String urlWeb = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cuestionario_plan_bloque, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        fabButton = vista.findViewById(R.id.fabButton);
        linear = vista.findViewById(R.id.linear);
        webViewTexto = vista.findViewById(R.id.webViewTexto);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

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
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        // TAMANO DE LETRA POR DEFECTO
        if(tokenManager.getToken().getTamanoLetra() > 0){
            currentFontSize = tokenManager.getToken().getTamanoLetra();
        }

        // detectar que toque el titulo para redireccinar biblia
        webViewTexto.getSettings().setJavaScriptEnabled(true);
        webViewTexto.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        webViewTexto.setBackgroundColor(Color.TRANSPARENT);


        if(tokenManager.getToken().getTema() == 1){ // dark
            tema = true;
        }

        apiBuscarCuestionario();

        return vista;
    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Método llamado desde el código JavaScript */
        @JavascriptInterface
        public void notifyClickToJava() {

            if(redireccionarPaginaWeb){

                if(!TextUtils.isEmpty(urlWeb)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWeb));
                    startActivity(intent);
                }
            }else{
                if(redireccionarTextoBiblico){
                    Intent intent = new Intent(getContext(), LecturaDevoCapituloActivity.class);
                    intent.putExtra("IDDEVOBIBLIA", iddevobiblia);
                    startActivity(intent);
                }
            }

        }
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

                                            String devocional = apiRespuesta.getDevocional();

                                            if(apiRespuesta.getRedireccionar() == 1){
                                                iddevobiblia = apiRespuesta.getIddevobiblia();
                                                redireccionarTextoBiblico = true;
                                            }

                                            if(apiRespuesta.getRedirecweb() == 1){
                                                redireccionarPaginaWeb = true;
                                                if(apiRespuesta.getUrllink() != null){
                                                    urlWeb = apiRespuesta.getUrllink();
                                                }
                                            }

                                            setearTexto(devocional);
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


    private void setearTexto(String devocional){

        webViewTexto.loadDataWithBaseURL(null, devocional, "text/html", "UTF-8", null);

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
                }
                else if(tipoLetra == 3){
                    // RECOLECTA MEDIUM
                    String fontName = "Fuente4";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 4){
                    // RECOLECTA REGULAR
                    String fontName = "Fuente5";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else{
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", currentFontSize);
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
            estilosList.add(new ModeloTipoLetraCuestionario(3, "Recolecta Medium"));
            estilosList.add(new ModeloTipoLetraCuestionario(4, "Recolecta Regular"));

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



                    if (position == 0) {
                        // NOTO SANS LIGHT
                        String fontName = "Fuente1";
                        tokenManager.guardarTipoLetraTexto(0);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    } else if (position == 1) {
                        // NOTO SANS MEDIUM
                        String fontName = "Fuente2";
                        tokenManager.guardarTipoLetraTexto(1);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    } else if (position == 2) {
                        // TIMES NEW
                        String fontName = "Fuente3";
                        tokenManager.guardarTipoLetraTexto(2);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if (position == 3) {
                        // TIMES NEW
                        String fontName = "Fuente4";
                        tokenManager.guardarTipoLetraTexto(3);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else if (position == 4) {
                        // TIMES NEW
                        String fontName = "Fuente5";
                        tokenManager.guardarTipoLetraTexto(4);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }
                    else {
                        // NOTO SANS LIGHT
                        String fontName = "Fuente1";
                        tokenManager.guardarTipoLetraTexto(0);
                        webViewTexto.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                    }



                    if(tema){
                        String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                        webViewTexto.evaluateJavascript(javascriptColor, null);
                    }else{
                        String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                        webViewTexto.evaluateJavascript(javascriptColor, null);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Implementa según sea necesario
                }
            });

            // CAMBIAR ESTILO BOTONES


            btnMas.setBackgroundTintList(colorStateTintBlack);
            btnMas.setTextColor(colorBlanco);

            btnMenos.setBackgroundTintList(colorStateTintBlack);
            btnMenos.setTextColor(colorBlanco);


            // TAMANOS DE TEXTOS

            btnMenos.setOnClickListener(v ->{


                if (currentFontSize > MIN_FONT_SIZE) {
                    currentFontSize--; // Disminuir el tamaño de letra
                    tokenManager.guardarTamanoLetraCuestionario(currentFontSize);

                    String javascript = String.format("document.body.style.fontSize = '%dpx';", currentFontSize);
                    webViewTexto.evaluateJavascript(javascript, null);
                }


            });

            btnMas.setOnClickListener(v -> {

                if (currentFontSize  < MAX_FONT_SIZE) {
                    currentFontSize++; // Aumentar el tamaño de letra
                    tokenManager.guardarTamanoLetraCuestionario(currentFontSize);

                    String javascript = String.format("document.body.style.fontSize = '%dpx';", currentFontSize);
                    webViewTexto.evaluateJavascript(javascript, null);
                }

            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
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

