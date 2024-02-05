package com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

    private TextView txtHtml;

    private static final String ARG_DATO = "IDBLOQUE";

    private int idBloqueDeta = 0;

    private boolean bottomSheetShowing = false;

    private int tamanoTextoHtml = 18;
    private final int minTextSize = 14;
    private final int maxTextSize = 28;

    private boolean tema = false;


    private Typeface faceNotoSansCondensesMedium;
    private Typeface faceNotoSansLight;
    private Typeface faceTimeNewRoman;

    private ColorStateList colorStateTintWhite, colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;


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
        txtHtml = vista.findViewById(R.id.txtCuestionario);
        fabButton = vista.findViewById(R.id.fabButton);

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

        faceNotoSansCondensesMedium = ResourcesCompat.getFont(getContext(), R.font.notosans_condensed_medium);
        faceNotoSansLight = ResourcesCompat.getFont(getContext(), R.font.notosans_light);
        faceTimeNewRoman = ResourcesCompat.getFont(getContext(), R.font.times_new_normal_regular);

        if(tokenManager.getToken().getTema() == 1){ // dark
            txtHtml.setTextColor(colorBlanco);
            tema = true;
        }else{
            txtHtml.setTextColor(colorBlack);
        }


        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        // TIPOS DE LETRA POR DEFECTO
        int tipoLetra = tokenManager.getToken().getTipoLetra();
        switch (tipoLetra){
            case 0:
                // POR DEFECTO SERA NORMAL
                txtHtml.setTypeface(null, Typeface.NORMAL);
                break;
            case 1:
                // Noto Sans Condensed Medium
                txtHtml.setTypeface(faceNotoSansCondensesMedium);
                break;
            case 2:
                // Noto Sans Light
                txtHtml.setTypeface(faceNotoSansLight);
                break;
            case 3:
                // Time new roman
                txtHtml.setTypeface(faceTimeNewRoman);
                break;
            default:
                txtHtml.setTypeface(null, Typeface.NORMAL);
                break;
        }


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


                                            setearTexto(apiRespuesta.getTitulo());
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {
                                            // BLOQUE NO TIENE CUESTIONARIO

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

            estilosList.add(new ModeloTipoLetraCuestionario(0, "Sin Formato"));
            estilosList.add(new ModeloTipoLetraCuestionario(1, "Noto Sans Medium"));
            estilosList.add(new ModeloTipoLetraCuestionario(2, "Noto Sans Light"));
            estilosList.add(new ModeloTipoLetraCuestionario(3, "Time New Roman"));

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
                        tokenManager.guardarTipoLetraTexto(0);
                        txtHtml.setTypeface(null ,Typeface.NORMAL);
                    }
                    else if(position == 1){
                        tokenManager.guardarTipoLetraTexto(1);
                        txtHtml.setTypeface(faceNotoSansCondensesMedium);
                    }
                    else if(position == 2){
                        tokenManager.guardarTipoLetraTexto(2);
                        txtHtml.setTypeface(faceNotoSansLight);
                    }
                    else if(position == 3){
                        tokenManager.guardarTipoLetraTexto(3);
                        txtHtml.setTypeface(faceTimeNewRoman);
                    }
                    else{
                        tokenManager.guardarTipoLetraTexto(0);
                        txtHtml.setTypeface(null ,Typeface.NORMAL);
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
                if (tamanoTextoHtml > minTextSize) {
                    tamanoTextoHtml--;
                    updateTextSize();
                }
            });

            btnMas.setOnClickListener(v -> {
                if (tamanoTextoHtml < maxTextSize) {
                    tamanoTextoHtml++;
                    updateTextSize();
                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }

    private void updateTextSize() {
        txtHtml.setTextSize(tamanoTextoHtml);
    }

    private void setearTexto(String texto){

        if(texto != null){
            if(!TextUtils.isEmpty(texto)){
                txtHtml.setText(HtmlCompat.fromHtml(texto, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
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

