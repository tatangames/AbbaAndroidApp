package com.tatanstudios.abbaappandroid.fragmentos.login;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.onesignal.OneSignal;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.principal.PrincipalActivity;
import com.tatanstudios.abbaappandroid.fragmentos.login.olvidepass.FragmentOlvidePassword;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentLoginDatos extends Fragment {

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputCorreo, inputContrasena;
    private TextInputEditText edtCorreo, edtContrasena;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private Button btnLogin;
    private TextView txtOlvide, txtToolbar;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;
    private boolean tema;

    private String oneSignalId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login_datos, container, false);

        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        btnLogin = vista.findViewById(R.id.btnLogin);

        inputCorreo = vista.findViewById(R.id.inputCorreo);
        inputContrasena = vista.findViewById(R.id.inputContrasena);

        edtCorreo = vista.findViewById(R.id.edtCorreo);
        edtContrasena = vista.findViewById(R.id.edtContrasena);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtOlvide = vista.findViewById(R.id.btnOlvide);
        txtToolbar = vista.findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.iniciar_sesion));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);


        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        colorGris = ContextCompat.getColor(requireContext(), R.color.gris616161);
        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);

        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        btnLogin.setBackgroundTintList(colorEstadoGris);
        btnLogin.setTextColor(colorBlanco);
        btnLogin.setEnabled(false);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnLogin.setOnClickListener(v -> {
            validar();
        });

        txtOlvide.setOnClickListener(v -> {
            vistaRecuperarPassword();
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                verificarEntrada();
            }
        };

        edtCorreo.addTextChangedListener(textWatcher);
        edtContrasena.addTextChangedListener(textWatcher);


        // obtener identificador id one signal
        oneSignalId = OneSignal.getUser().getPushSubscription().getId();

        return vista;
    }


    private void verificarEntrada(){
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();

        // Habilitar el botón si ambos campos tienen texto, de lo contrario, deshabilitarlo
        if(TextUtils.isEmpty(txtCorreo) || TextUtils.isEmpty(txtContrasena)){
            desactivarBoton();
        }else{
            activarBoton();
        }
    }

    private void activarBoton(){

        btnLogin.setEnabled(true);

        if(tema){ // Dark
            btnLogin.setBackgroundTintList(colorEstadoBlanco);
            btnLogin.setTextColor(colorBlack);
        }else{
            btnLogin.setBackgroundTintList(colorEstadoNegro);
            btnLogin.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnLogin.setEnabled(false);

        // aplica a los 2 temas
        btnLogin.setBackgroundTintList(colorEstadoGris);
        btnLogin.setTextColor(colorBlanco);
    }


    private void vistaRecuperarPassword(){
        FragmentOlvidePassword fragmentOlvidePassword = new FragmentOlvidePassword();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentOlvidePassword.getClass())) return;

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentOlvidePassword)
                .addToBackStack(null)
                .commit();
    }

    private void validar(){

        closeKeyboard();

        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();
        boolean boolCorreo, boolPassword;


        if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()){
            String valiCorreo = getString(R.string.direccion_correo_invalida);
            inputCorreo.setError(valiCorreo);
            boolCorreo = false;
        }else{
            boolCorreo = true;
            inputCorreo.setError(null);
        }

        if(txtContrasena.length() >= 6){
            inputContrasena.setError(null);
            boolPassword = true;
        }else{
            String valiContrasena = getString(R.string.contrasena_minimo_6);
            inputContrasena.setError(valiContrasena);
            boolPassword = false;
        }

        if(boolCorreo && boolPassword){
            enviarDatos();
        }
    }

    private void enviarDatos(){

        progressBar.setVisibility(View.VISIBLE);

        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();

        compositeDisposable.add(
                service.inicioSesion(txtCorreo, txtContrasena, oneSignalId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            tokenManager.guardarClienteTOKEN(apiRespuesta);
                                            tokenManager.guardarClienteID(apiRespuesta);
                                            redireccionVistaPrincipal();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            datosInvalidos();

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

    private void redireccionVistaPrincipal(){

        // Siguiente Actvity
        Intent intent = new Intent(getActivity(), PrincipalActivity.class);
        startActivity(intent);

        // Animación personalizada de entrada
        //getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        getActivity().finish();
    }

    private void datosInvalidos(){
        String valiDatos = getString(R.string.correo_o_contrasena_incorrectos);
        inputCorreo.setError(valiDatos);
        Toasty.info(getActivity(), getString(R.string.lo_siento_algo_salio_mal)).show();
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
