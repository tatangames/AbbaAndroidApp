package com.tatanstudios.abbaappandroid.fragmentos.devocapitulo;

import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.comunidad.AdaptadorComunidadAceptadas;
import com.tatanstudios.abbaappandroid.adaptadores.devobiblia.AdaptadorSpinVersionBiblia;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.cuestionario.AdaptadorSpinnerTipoLetraCuestionario;
import com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario.FragmentCuestionarioPlanBloque;
import com.tatanstudios.abbaappandroid.modelos.devocapitulos.ModeloVersiones;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloIglesias;
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

public class FragmentDevoCapitulo extends Fragment {


    // MUESTRA CAPITULO DE LA BIBLIA CUANDO SE HACE CLICK AL TITULO DEL DEVOCIONAL



    private int iddevobiblia = 0;

    private ImageView imgFlechaAtras, imgTuerca;
    private WebView webViewTexto;


    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ColorStateList colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;

    private boolean bottomSheetShowing = false;

    private boolean tema = false;


    private int currentFontSize = 18;
    private static final int MIN_FONT_SIZE = 15;
    private static final int MAX_FONT_SIZE = 30;



    // necesito una lista de versiones de textos

    private List<ModeloVersiones> modeloSpinner = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_devo_capitulo, container, false);


        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        webViewTexto = vista.findViewById(R.id.webView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        imgTuerca = vista.findViewById(R.id.imgTuerca);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);


        Bundle bundle = getArguments();
        if(bundle != null) {
            iddevobiblia = bundle.getInt("IDDEVOBIBLIA");
        }

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        // TAMANO DE LETRA POR DEFECTO
        if(tokenManager.getToken().getTamanoLetra() > 0){
            currentFontSize = tokenManager.getToken().getTamanoLetra();
        }

        imgTuerca.setOnClickListener(v ->{
            opcionesWebView();
        });

        // detectar que toque el titulo para redireccinar biblia
        webViewTexto.getSettings().setJavaScriptEnabled(true);
        webViewTexto.setBackgroundColor(Color.TRANSPARENT);

        imgFlechaAtras.setOnClickListener(v -> {
            getActivity().finish();
        });

        apiBuscarTexto();

        return vista;
    }


    private void apiBuscarTexto(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionTextoDevoCapitulo(iduser, idiomaPlan, iddevobiblia)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            setearTexto(apiRespuesta.getContenido());
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




    private void setearTexto(String texto){

        webViewTexto.loadDataWithBaseURL(null, texto, "text/html", "UTF-8", null);

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




    /*private void verOpcionesTuerca(){

        if(unaVezTuerca){
            unaVezTuerca = false;

            new Handler().postDelayed(() -> {
                unaVezTuerca = true;
            }, 1000);


            // Crea un PopupMenu
            PopupMenu popupMenu = new PopupMenu(getContext(), imgTuerca);

            // Infla el menú en el PopupMenu
            popupMenu.inflate(R.menu.menu_opciones_devo_biblia);

            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                // Marcar que el menú está cerrado
                // menuAbierto = false;

                // modal opciones webview
                if (item.getItemId() == R.id.opcion1) {

                    opcionesWebView();

                    return true;
                }

                // mas versiones de biblia devocional
                else if (item.getItemId() == R.id.opcion2) {

                    opcionesBiblias();

                    return true;
                }
                else {
                    return false;
                }
            });

            // Agrega un listener para detectar cuando se cierra el menú
            popupMenu.setOnDismissListener(menu -> {
                // Marcar que el menú está cerrado
                // menuAbierto = false;
            });

            // Muestra el menú emergente
            popupMenu.show();
        }
    }*/


    private void opcionesWebView(){

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

    private boolean usuarioSeleccionoSpinner = false;

    private void opcionesBiblias(){

        usuarioSeleccionoSpinner = false;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.cardview_devo_biblias_spinner, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Spinner spinVersion = bottomSheetDialog.findViewById(R.id.estiloSpinner);

        AdaptadorSpinVersionBiblia adapterModelo = new AdaptadorSpinVersionBiblia(getContext(), modeloSpinner, tema);
        spinVersion.setAdapter(adapterModelo);


        // GUARDAR TEXTO SELECCIONADO
        spinVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(usuarioSeleccionoSpinner) {
                    // Aquí pones el código que quieres ejecutar cuando el usuario selecciona un elemento en el Spinner

                    ModeloVersiones modeloSeleccionado = (ModeloVersiones) parentView.getItemAtPosition(position);

                    if (modeloSeleccionado != null) {

                        bottomSheetDialog.dismiss();
                        recargarDatos(modeloSeleccionado.getId());
                    }
                } else {
                    usuarioSeleccionoSpinner = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });




                // CAMBIAR POSICION

        for (int i = 0; i < modeloSpinner.size(); i++) {
            ModeloVersiones modelo = modeloSpinner.get(i);
            if (modelo.getId() == iddevobiblia) {
                // Cuando encuentres el modelo con el id deseado, selecciona el elemento en el Spinner
                spinVersion.setSelection(i);
                break; // Rompe el bucle una vez que encuentres el modelo deseado
            }
        }


        // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
        bottomSheetDialog.setOnDismissListener(dialog -> {
            bottomSheetShowing = false;
        });

        bottomSheetDialog.show();
    }


    private void recargarDatos(int newIdDevoBiblia){

        webViewTexto.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        iddevobiblia = newIdDevoBiblia;
        apiBuscarTexto();
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
