package com.tatanstudios.abbaappandroid.fragmentos.login.registro;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerDepartamento;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerIglesia;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerPais;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloDepartamentos;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloIglesias;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloPais;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentRegistro extends Fragment {

    private ImageView imgFlechaAtras;
    private TextInputLayout inputNombre, inputApellido, inputCorreo, inputContrasena;
    private TextInputEditText edtNombre, edtApellido, edtCorreo, edtContrasena;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar progressBar;
    private Button btnRegistro;
    private RelativeLayout rootRelative;
    private boolean boolCorreo = false, boolContrasena = false;
    private TokenManager tokenManager;
    private Spinner spinGenero, spinPais, spinEstado, spinIglesia;


    private int idSpinnerIglesia = 0;

    private TextView txtFecha;

    private static final String CERO = "0";
    private static final String BARRA = "-";

    private String fechaNacimiento = "";
    private boolean hayFecha = false;


    private int colorBlanco = 0, colorBlack = 0;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private DatePickerDialog datePickerDialog;

    private final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    private boolean tema = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registro, container, false);

        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);

        inputNombre = vista.findViewById(R.id.inputNombre);
        inputApellido = vista.findViewById(R.id.inputApellido);
        inputCorreo = vista.findViewById(R.id.inputCorreo);
        inputContrasena = vista.findViewById(R.id.inputContrasena);

        edtNombre = vista.findViewById(R.id.edtNombre);
        edtApellido = vista.findViewById(R.id.edtApellido);
        edtCorreo = vista.findViewById(R.id.edtCorreo);
        edtContrasena = vista.findViewById(R.id.edtContrasena);
        rootRelative = vista.findViewById(R.id.rootRelative);

        spinGenero = vista.findViewById(R.id.generoSpinner);
        spinPais = vista.findViewById(R.id.paisSpinner);
        spinEstado = vista.findViewById(R.id.estadoSpinner);
        spinIglesia = vista.findViewById(R.id.ciudadSpinner);
        txtFecha = vista.findViewById(R.id.txtCalendario);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        int colorProgressBar = ContextCompat.getColor(requireContext(), R.color.barraProgreso);
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgressBar, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        llenarSpinner();

        return vista;
    }

    private void llenarSpinner(){


        // *********** PAISES ****************
        List<ModeloPais> modeloPais = new ArrayList<>();
        modeloPais.add(new ModeloPais(0, getString(R.string.seleccionar_pais)));
        modeloPais.add(new ModeloPais(1, getString(R.string.el_salvador)));
        modeloPais.add(new ModeloPais(2, getString(R.string.guatemala)));
        modeloPais.add(new ModeloPais(3, getString(R.string.honduras)));
        modeloPais.add(new ModeloPais(4, getString(R.string.nicaragua)));
        modeloPais.add(new ModeloPais(5, getString(R.string.mexico)));

        AdaptadorSpinnerPais paisAdapter = new AdaptadorSpinnerPais(getContext(), android.R.layout.simple_spinner_item, modeloPais, tema);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPais.setAdapter(paisAdapter);

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();

        // ** Adaptador Departamentos


        spinPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ModeloPais paisSeleccionado = (ModeloPais) parentView.getItemAtPosition(position);

                if (paisSeleccionado != null) {

                    limpiarSpinIglesia();
                    limpiarSpinEstados();
                    closeKeyboard();
                    idSpinnerIglesia = 0;

                    updateAdapterEstado(paisSeleccionado.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });

        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ModeloDepartamentos depaSeleccionado = (ModeloDepartamentos) parentView.getItemAtPosition(position);


                if (depaSeleccionado != null) {

                    idSpinnerIglesia = 0;
                    updateAdapterIglesias(depaSeleccionado.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });

        spinIglesia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ModeloIglesias iglesiaSeleccionado = (ModeloIglesias) parentView.getItemAtPosition(position);


                if (iglesiaSeleccionado != null) {

                    // no importa que venga id 0, ya que en la comprobacion fina se resulve
                    idSpinnerIglesia = iglesiaSeleccionado.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });

    }


    private void updateAdapterEstado(int idPais) {

        switch (idPais) {
            case 1: // pais el salvador -> estados
                AdaptadorSpinnerDepartamento adaptadorElSalvador = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloDepartamentosElSalvador(), tema);
                spinEstado.setAdapter(adaptadorElSalvador);
                break;
            case 2: // pais guatemala -> estados
                AdaptadorSpinnerDepartamento adaptadorGuatemala = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloDepartamentosGuatemala(), tema);
                spinEstado.setAdapter(adaptadorGuatemala);
                break;
            case 3: // pais honduras -> estados
                AdaptadorSpinnerDepartamento adaptadorHonduras = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloDepartamentosHonduras(), tema);
                spinEstado.setAdapter(adaptadorHonduras);
                break;
            case 4: // pais nicaragua -> estados
                AdaptadorSpinnerDepartamento adaptadorNicaragua = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloDepartamentosNicaragua(), tema);
                spinEstado.setAdapter(adaptadorNicaragua);
                break;
            case 5: // pais mexico -> estados
                AdaptadorSpinnerDepartamento adaptadorMexico = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloDepartamentosMexico(), tema);
                spinEstado.setAdapter(adaptadorMexico);
                break;
            // Agrega más casos según la jerarquía deseada
        }
    }


    // solicitar ciudades
    private void updateAdapterIglesias(int ideDepa){

        // evitar cuando dice: seleccionar opcion
        if(ideDepa != 0){

            progressBar.setVisibility(View.VISIBLE);
            limpiarSpinIglesia();

            compositeDisposable.add(
                    service.solicitarListadoIglesias(ideDepa)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .retry()
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {

                                                llenarSpinnerIglesias(apiRespuesta.getModeloIglesias());
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
    }

    private void limpiarSpinIglesia(){
        List<ModeloIglesias> modeloBorrar = new ArrayList<>();
        AdaptadorSpinnerIglesia adaptadorLimpiar = new AdaptadorSpinnerIglesia(getContext(), android.R.layout.simple_spinner_item, modeloBorrar, tema);
        spinIglesia.setAdapter(adaptadorLimpiar);
    }

    private void limpiarSpinEstados(){
        List<ModeloDepartamentos> modeloBorrar = new ArrayList<>();
        AdaptadorSpinnerDepartamento adaptadorLimpiar = new AdaptadorSpinnerDepartamento(getContext(), android.R.layout.simple_spinner_item, modeloBorrar, tema);
        spinEstado.setAdapter(adaptadorLimpiar);
    }

    private void llenarSpinnerIglesias(List<ModeloIglesias> modeloIglesias){

        List<ModeloIglesias> modeloSet = new ArrayList<>();
        modeloSet.add(new ModeloIglesias(0, getString(R.string.seleccionar_iglesia)));

        for (ModeloIglesias m : modeloIglesias){
            modeloSet.add(new ModeloIglesias(m.getId(), m.getNombre()));
        }

        AdaptadorSpinnerIglesia adaptadorIglesia = new AdaptadorSpinnerIglesia(getContext(), android.R.layout.simple_spinner_item, modeloSet, tema);
        spinIglesia.setAdapter(adaptadorIglesia);
    }


    // llenar con modelo pais El Salvador
    private List<ModeloDepartamentos> modeloDepartamentosElSalvador(){

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();
        modeloDepartamento.add(new ModeloDepartamentos(0, getString(R.string.seleccionar_opcion)));
        modeloDepartamento.add(new ModeloDepartamentos(1, getString(R.string.santa_ana)));
        modeloDepartamento.add(new ModeloDepartamentos(2, getString(R.string.chalatengando)));
        modeloDepartamento.add(new ModeloDepartamentos(3, getString(R.string.sonsonate)));
        modeloDepartamento.add(new ModeloDepartamentos(4, getString(R.string.la_libertad)));
        modeloDepartamento.add(new ModeloDepartamentos(5, getString(R.string.ahuachapan)));
        return modeloDepartamento;
    }

    // llenar con modelo pais Guatemala
    private List<ModeloDepartamentos> modeloDepartamentosGuatemala(){

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();
        modeloDepartamento.add(new ModeloDepartamentos(0, getString(R.string.seleccionar_opcion)));
        modeloDepartamento.add(new ModeloDepartamentos(6, getString(R.string.san_marcos)));
        modeloDepartamento.add(new ModeloDepartamentos(7, getString(R.string.quetzaltenango)));
        modeloDepartamento.add(new ModeloDepartamentos(8, getString(R.string.retalhuleu)));
        modeloDepartamento.add(new ModeloDepartamentos(9, getString(R.string.suchitepequez)));
        modeloDepartamento.add(new ModeloDepartamentos(10, getString(R.string.solola)));
        modeloDepartamento.add(new ModeloDepartamentos(11, getString(R.string.sacatepequez)));
        modeloDepartamento.add(new ModeloDepartamentos(12, getString(R.string.chimaltenango)));
        modeloDepartamento.add(new ModeloDepartamentos(13, getString(R.string.guatemala)));
        modeloDepartamento.add(new ModeloDepartamentos(14, getString(R.string.escuintla)));
        modeloDepartamento.add(new ModeloDepartamentos(15, getString(R.string.santa_rosa)));
        modeloDepartamento.add(new ModeloDepartamentos(16, getString(R.string.jalapa)));
        modeloDepartamento.add(new ModeloDepartamentos(17, getString(R.string.jutiapa)));
        modeloDepartamento.add(new ModeloDepartamentos(18, getString(R.string.chiquimula)));
        modeloDepartamento.add(new ModeloDepartamentos(19, getString(R.string.zacapa)));
        return modeloDepartamento;
    }


    // llenar con modelo pais Honduras
    private List<ModeloDepartamentos> modeloDepartamentosHonduras(){

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();
        modeloDepartamento.add(new ModeloDepartamentos(0, getString(R.string.seleccionar_opcion)));
        modeloDepartamento.add(new ModeloDepartamentos(20, getString(R.string.francisco_morazan)));
        modeloDepartamento.add(new ModeloDepartamentos(21, getString(R.string.olancho)));
        modeloDepartamento.add(new ModeloDepartamentos(22, getString(R.string.el_paraiso)));
        return modeloDepartamento;
    }

    // llenar con modelo pais Nicaragua
    private List<ModeloDepartamentos> modeloDepartamentosNicaragua(){

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();
        modeloDepartamento.add(new ModeloDepartamentos(0, getString(R.string.seleccionar_opcion)));
        modeloDepartamento.add(new ModeloDepartamentos(23, getString(R.string.esteli)));
        modeloDepartamento.add(new ModeloDepartamentos(24, getString(R.string.madriz)));
        modeloDepartamento.add(new ModeloDepartamentos(25, getString(R.string.nueva_segovia)));
        return modeloDepartamento;
    }

    // llenar con modelo pais Mexico
    private List<ModeloDepartamentos> modeloDepartamentosMexico(){

        List<ModeloDepartamentos> modeloDepartamento = new ArrayList<>();
        modeloDepartamento.add(new ModeloDepartamentos(0, getString(R.string.seleccionar_opcion)));
        modeloDepartamento.add(new ModeloDepartamentos(26, getString(R.string.hidalgo)));
        modeloDepartamento.add(new ModeloDepartamentos(27, getString(R.string.chiapas)));
        return modeloDepartamento;
    }



    void mensajeSinConexion(){
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

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
