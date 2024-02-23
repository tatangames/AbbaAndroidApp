package com.tatanstudios.abbaappandroid.activity.biblia;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.versiculo.AdaptadorVersiculos;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.cuestionario.AdaptadorSpinnerTipoLetraCuestionario;
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

public class VersiculoTextoActivity extends AppCompatActivity {

    private int idversiculo = 0;
    private RelativeLayout rootRelative;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private OnBackPressedDispatcher onBackPressedDispatcher;

    private WebView webView;

    private ImageView imgFlecha, imgTuerca;

    private boolean bottomSheetShowing = false;

    private ColorStateList colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;

    private int normalTextSize = 18;
    private int maxTextSize = 32;
    private int minTextSize = 15;

    private boolean tema = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculo_texto);

        rootRelative = findViewById(R.id.rootRelative);
        webView = findViewById(R.id.webView);
        imgFlecha = findViewById(R.id.imgFlechaAtras);
        imgTuerca = findViewById(R.id.imgTuerca);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idversiculo = bundle.getInt("IDVERSICULO");
        }

        onBackPressedDispatcher = getOnBackPressedDispatcher();

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        webView.getSettings().setJavaScriptEnabled(true);

        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);

        webView.setBackgroundColor(Color.TRANSPARENT);

        if(tokenManager.getToken().getTema() == 1){ // dark
            tema = true;
        }

        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        imgFlecha.setOnClickListener(v -> {
            onBackPressedDispatcher.onBackPressed();
        });

        imgTuerca.setOnClickListener(v -> {
            modalTuerca();
        });

        apiBuscarTextos();
    }

    private void apiBuscarTextos(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.listadoTextosVersiculos(iduser, idioma, idversiculo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            setearDatos(apiRespuesta.getContenido());

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

    private void setearDatos(String contenido){
        webView.loadDataWithBaseURL(null, contenido, "text/html", "UTF-8", null);
        String idpos = "verso" + idversiculo;

        String versoPosicion = "javascript:document.getElementById('" + idpos + "').scrollIntoView();";

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
                }
                else if(tipoLetra == 3){
                    // RECOLECTA MEDIUM
                    String fontName = "Fuente4";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else if(tipoLetra == 4){
                    // RECOLECTA REGULAR
                    String fontName = "Fuente5";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }
                else{
                    // NOTO SANS LIGHT
                    String fontName = "Fuente1";
                    webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", 18);
                webView.evaluateJavascript(javascript, null);

                if(tema){
                    String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                    webView.evaluateJavascript(javascriptColor, null);
                }else{
                    String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                    webView.evaluateJavascript(javascriptColor, null);
                }


                new Handler().postDelayed(() -> {
                    webView.loadUrl(versoPosicion);

                    webView.setVisibility(View.VISIBLE);
                }, 500);

            }
        });


    }


    private void modalTuerca() {

        if (!bottomSheetShowing) {
            bottomSheetShowing = true;

            List<ModeloTipoLetraCuestionario> estilosList = new ArrayList<>();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.cardview_bottonsheet_tipoletra_cuestionario, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            Button btnMenos = bottomSheetDialog.findViewById(R.id.btnMenos);
            Button btnMas = bottomSheetDialog.findViewById(R.id.btnMas);
            Spinner spinEstiloTexto = bottomSheetDialog.findViewById(R.id.estiloSpinner);

            AdaptadorSpinnerTipoLetraCuestionario adapterModelo = new AdaptadorSpinnerTipoLetraCuestionario(this, android.R.layout.simple_spinner_item, tema);

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
            } else {
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
                            webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        } else if (position == 1) {
                            // NOTO SANS MEDIUM
                            String fontName = "Fuente2";
                            tokenManager.guardarTipoLetraTexto(1);
                            webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        } else if (position == 2) {
                            // TIMES NEW
                            String fontName = "Fuente3";
                            tokenManager.guardarTipoLetraTexto(2);
                            webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        }
                        else if (position == 3) {
                            // TIMES NEW
                            String fontName = "Fuente4";
                            tokenManager.guardarTipoLetraTexto(3);
                            webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        }
                        else if (position == 4) {
                            // TIMES NEW
                            String fontName = "Fuente5";
                            tokenManager.guardarTipoLetraTexto(4);
                            webView.evaluateJavascript("document.body.style.fontFamily = '" + fontName + "';", null);
                        }
                        else {
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


            btnMas.setBackgroundTintList(colorStateTintBlack);
            btnMas.setTextColor(colorBlanco);

            btnMenos.setBackgroundTintList(colorStateTintBlack);
            btnMenos.setTextColor(colorBlanco);


            // TAMANOS DE TEXTOS

            btnMenos.setOnClickListener(v -> {
               // webView.evaluateJavascript("disminuirTamano();", null);

                normalTextSize -= 2;
                if (normalTextSize > maxTextSize) {
                    normalTextSize = maxTextSize;
                }

                String javascript = String.format("document.body.style.fontSize = '%dpx';", normalTextSize);
                webView.evaluateJavascript(javascript, null);
            });

            btnMas.setOnClickListener(v -> {
                //webView.evaluateJavascript("aumentarTamano();", null);

                normalTextSize += 2;
                if (normalTextSize < minTextSize) {
                    normalTextSize = minTextSize; // Asegurar que el tamaño mínimo sea 10
                }


                String javascript = String.format("document.body.style.fontSize = '%dpx';", normalTextSize);
                webView.evaluateJavascript(javascript, null);
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }




    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
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