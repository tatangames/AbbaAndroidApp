package com.tatanstudios.abbaappandroid.fragmentos.login.registro;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.onesignal.OneSignal;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.principal.PrincipalActivity;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerDepartamento;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerGenero;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerIglesia;
import com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro.AdaptadorSpinnerPais;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloDepartamentos;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloGeneros;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloIglesias;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloPais;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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

    private int idSpinnerIglesia = 0, idSpinnerGenero = 0;

    private TextView txtFecha,txtToolbar;

    private static final String CERO = "0";
    private static final String BARRA = "-";

    private String fechaNacimiento = "";
    private boolean hayFecha = false;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;

    private DatePickerDialog datePickerDialog;

    private final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    private boolean tema = false;
    private boolean puedeAddIglesiaSpin =  false, boolDialogoEnviar = true;
    private boolean enviarDatosApi = true;

    private String oneSignalId = "";

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
        btnRegistro = vista.findViewById(R.id.btnRegistro);
        txtToolbar = vista.findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.crear_cuenta));

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

        if(tokenManager.getToken().getTema() == 1){
            inputNombre.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.blanco));
            inputNombre.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputApellido.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.blanco));
            inputApellido.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputCorreo.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.blanco));
            inputCorreo.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputContrasena.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.blanco));
            inputContrasena.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
        }

        colorGris = ContextCompat.getColor(requireContext(), R.color.gris616161);
        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);
        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        btnRegistro.setEnabled(false);
        btnRegistro.setBackgroundTintList(colorEstadoGris);
        btnRegistro.setTextColor(colorBlanco);

        txtFecha.setPaintFlags(txtFecha.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtFecha.setPadding(0, 8, 0, 8);

        txtFecha.setOnClickListener(v -> {
            elegirFecha();
        });

        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        btnRegistro.setOnClickListener(v -> {
            closeKeyboard();
            confirmarRegistro();
        });

        llenarSpinner();


        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {

                verificarEntradas();
            }
        });

        edtApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {

                verificarEntradas();
            }
        });

        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String textoIngresado = editable.toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(textoIngresado).matches()){
                    String valiCorreo = getString(R.string.direccion_correo_invalida);
                    inputCorreo.setError(valiCorreo);
                    boolCorreo = false;
                }else{
                    boolCorreo = true;
                    inputCorreo.setError(null);
                }

                verificarEntradas();
            }
        });

        edtContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto ha cambiado
                String textoIngresado = editable.toString();
                // Hacer algo con el texto ingresado
                if (!textoIngresado.isEmpty()) {

                    if(textoIngresado.length() >= 6){
                        inputContrasena.setError(null);
                        boolContrasena = true;
                    }else{
                        String valiContrasena = getString(R.string.contrasena_minimo_6);
                        inputContrasena.setError(valiContrasena);
                        boolContrasena = false;
                    }
                }else{
                    inputContrasena.setError(null);
                    boolContrasena = false;
                }

                verificarEntradas();
            }
        });


        // obtener identificador id one signal
        oneSignalId = OneSignal.getUser().getPushSubscription().getId();

        return vista;
    }

    private void verificarEntradas(){

        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();

        if(TextUtils.isEmpty(txtNombre) || TextUtils.isEmpty(txtApellido)
                || TextUtils.isEmpty(txtCorreo) || TextUtils.isEmpty(txtContrasena)){

            desactivarBtnRegistro();
        }else{

            if(boolCorreo && boolContrasena){

                activarBtnRegistro();
            }else{
                desactivarBtnRegistro();
            }
        }
    }

    private void activarBtnRegistro(){
        btnRegistro.setEnabled(true);

        if(tema){ // Dark
            btnRegistro.setBackgroundTintList(colorEstadoBlanco);
            btnRegistro.setTextColor(colorBlack);
        }else{
            btnRegistro.setBackgroundTintList(colorEstadoNegro);
            btnRegistro.setTextColor(colorBlanco);
        }
    }

    private void desactivarBtnRegistro(){
        btnRegistro.setEnabled(false);

        // igual para los 2 temas
        btnRegistro.setBackgroundTintList(colorEstadoGris);
        btnRegistro.setTextColor(colorBlanco);
    }


    private void llenarSpinner(){

        // *********** GENEROS ****************

        List<ModeloGeneros> modeloGeneros = new ArrayList<>();
        modeloGeneros.add(new ModeloGeneros(0, getString(R.string.seleccionar_genero)));
        modeloGeneros.add(new ModeloGeneros(1, getString(R.string.masculino)));
        modeloGeneros.add(new ModeloGeneros(2, getString(R.string.femenino)));


        AdaptadorSpinnerGenero adaptadorGenero = new AdaptadorSpinnerGenero(getContext(), android.R.layout.simple_spinner_item, modeloGeneros, tema);
        spinGenero.setAdapter(adaptadorGenero);


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

                    // para evitar llenado de spinner de iglesia si usuario mueve rapido de pais
                    // y aun esta cargando iglesia de un departamento
                    puedeAddIglesiaSpin = false;

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

                    if(depaSeleccionado.getId() == 0){
                        idSpinnerIglesia = 0;
                        limpiarSpinIglesia();
                        puedeAddIglesiaSpin = false;
                    }else{
                        puedeAddIglesiaSpin = true;
                    }

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

        spinGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ModeloGeneros generoSeleccionado = (ModeloGeneros) parentView.getItemAtPosition(position);

                if (generoSeleccionado != null) {

                    // no importa que venga id 0, ya que en la comprobacion fina se resulve
                    idSpinnerGenero = generoSeleccionado.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });

    }

    private void elegirFecha(){
        closeKeyboard();

        if (datePickerDialog == null || !datePickerDialog.isShowing()) {
            datePickerDialog = new DatePickerDialog(getContext(),  (view, year, month, dayOfMonth) -> {

                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                txtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);

                if(tema) { // dark
                    txtFecha.setTextColor(colorBlanco);
                }else{
                    txtFecha.setTextColor(colorBlack);
                }

                fechaNacimiento = year + BARRA + mesFormateado + BARRA + diaFormateado;
                hayFecha = true;
            },anio, mes, dia);
            //Muestro el widget
            datePickerDialog.show();
        }
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
                                                if(puedeAddIglesiaSpin){
                                                    llenarSpinnerIglesias(apiRespuesta.getModeloIglesias());
                                                }
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


    private void confirmarRegistro(){

        if(!hayFecha){
            Toasty.error(getContext(), getString(R.string.fecha_nacimiento_es_requerido)).show();
            return;
        }

        if(idSpinnerGenero == 0){
            Toasty.error(getContext(), getString(R.string.genero_es_requerido)).show();
            return;
        }


        if(idSpinnerIglesia == 0){
            Toasty.error(getContext(), getString(R.string.iglesia_es_requerido)).show();
            return;
        }
        if(boolDialogoEnviar){
            boolDialogoEnviar = false;

            int colorVerdeSuccess = ContextCompat.getColor(requireContext(), R.color.verdeSuccess);
            KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE, false);
            pDialog.getProgressHelper().setBarColor(colorVerdeSuccess);

            pDialog.setTitleText(getString(R.string.completar_registro));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText(getString(R.string.se_ve_genial));
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.enviar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogoEnviar = true;
                registrarUsuario();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.editar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogoEnviar = true;
            });
            pDialog.show();
        }
    }

    private void registrarUsuario(){

        if(enviarDatosApi){
            enviarDatosApi = false;
            progressBar.setVisibility(View.VISIBLE);

            String version = getString(R.string.version_app);
            String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
            String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
            String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
            String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();


            compositeDisposable.add(
                    service.registroUsuario(txtNombre, txtApellido, fechaNacimiento, idSpinnerGenero,
                                    idSpinnerIglesia, txtCorreo, txtContrasena, oneSignalId, version)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // NO RETRY
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        enviarDatosApi = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                                // correo ya esta registrado
                                                correoYaRegistrado(txtCorreo);
                                            }

                                            else if(apiRespuesta.getSuccess() == 2){

                                                tokenManager.guardarClienteTOKEN(apiRespuesta);
                                                tokenManager.guardarClienteID(apiRespuesta);

                                                finalizar();

                                            }else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        enviarDatosApi = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    void finalizar(){
        Toasty.success(getActivity(), getString(R.string.registrado_correctamente)).show();

        // Siguiente Actvity
        Intent intent = new Intent(getActivity(), PrincipalActivity.class);
        startActivity(intent);

        // Animación personalizada de entrada
        getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        getActivity().finish();
    }

    private void correoYaRegistrado(String correo){

        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);

        pDialog.setTitleText(getString(R.string.correo_ya_registrado));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentText(correo);
        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.aceptar), sDialog -> {
            sDialog.dismissWithAnimation();

        });
        pDialog.show();
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
