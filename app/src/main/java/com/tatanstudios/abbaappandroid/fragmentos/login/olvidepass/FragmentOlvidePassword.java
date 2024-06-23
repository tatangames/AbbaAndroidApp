package com.tatanstudios.abbaappandroid.fragmentos.login.olvidepass;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
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

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentOlvidePassword extends Fragment {

    // vista para ingresar correo y solicitar codigo correo

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputCorreo;
    private TextInputEditText edtCorreo;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private Button btnEnviarCorreo;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;
    private boolean tema;
    private boolean boolDialogEnviar;

    private TextView txtToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_olvide_password, container, false);

        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        btnEnviarCorreo = vista.findViewById(R.id.btnEnviar);
        inputCorreo = vista.findViewById(R.id.inputCorreo);
        edtCorreo = vista.findViewById(R.id.edtCorreo);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtToolbar = vista.findViewById(R.id.txtToolbar);


        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        boolDialogEnviar = true;

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.barraProgreso);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        colorGris = ContextCompat.getColor(requireContext(), R.color.gris616161);
        colorBlanco = ContextCompat.getColor(requireContext(), R.color.blanco);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.negro);

        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        btnEnviarCorreo.setEnabled(false);

        btnEnviarCorreo.setBackgroundTintList(colorEstadoGris);
        btnEnviarCorreo.setTextColor(colorBlanco);

        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnEnviarCorreo.setOnClickListener(v -> {
            closeKeyboard();
            verificarDatos();
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

        return vista;
    }


    private void verificarDatos(){

        if(boolDialogEnviar){
            boolDialogEnviar = false;
            String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
            KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.NORMAL_TYPE, false);

            pDialog.setTitleText(getString(R.string.solicitar_codigo));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText(""+txtCorreo);
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.codigo_kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.enviar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogEnviar = true;
                apiEnviarCorreoCodigo();
            });

            pDialog.cancelButtonColor(R.drawable.codigo_kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogEnviar = true;
            });
            pDialog.show();
        }
    }


    private void apiEnviarCorreoCodigo(){

        progressBar.setVisibility(View.VISIBLE);

        int idioma = tokenManager.getToken().getIdiomaApp();

        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        compositeDisposable.add(
                service.solicitarCodigoPassword(txtCorreo, idioma)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()) // NO RETRY
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            correoNoEncontrado();
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){
                                            pantallaIngresarCodigo(txtCorreo);
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

    private void correoNoEncontrado(){
        Toasty.error(getContext(), getString(R.string.correo_no_encontrado), Toasty.LENGTH_SHORT).show();
    }

    private void pantallaIngresarCodigo(String correo){
        FragmentCodigoPassword fragmentCodigoPassword = new FragmentCodigoPassword();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentCodigoPassword.getClass())) return;

        Bundle bundle = new Bundle();
        bundle.putString("CORREO", correo);
        fragmentCodigoPassword.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentCodigoPassword)
                .addToBackStack(null)
                .commit();
    }


    private void verificarEntrada(){
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()){
            String valiCorreo = getString(R.string.direccion_correo_invalida);
            inputCorreo.setError(valiCorreo);
            desactivarBoton();
        }else{
            inputCorreo.setError(null);
            activarBoton();
        }
    }

    private void activarBoton(){

        btnEnviarCorreo.setEnabled(true);

        if(tema){ // Dark
            btnEnviarCorreo.setBackgroundTintList(colorEstadoBlanco);
            btnEnviarCorreo.setTextColor(colorBlack);
        }else{
            btnEnviarCorreo.setBackgroundTintList(colorEstadoNegro);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviarCorreo.setEnabled(false);

        // lo mismo para los 2 temas
        btnEnviarCorreo.setBackgroundTintList(colorEstadoGris);
        btnEnviarCorreo.setTextColor(colorBlanco);
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
