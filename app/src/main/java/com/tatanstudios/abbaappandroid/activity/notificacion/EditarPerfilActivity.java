package com.tatanstudios.abbaappandroid.activity.notificacion;

import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.extras.ImageUtils;
import com.tatanstudios.abbaappandroid.extras.MultipartUtil;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class EditarPerfilActivity extends AppCompatActivity {

    private TextView txtToolbar, txtFechaNac;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private TextInputLayout inputCorreo;
    private TextInputEditText edtNombre, edtApellido, edtCorreo;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ProgressBar progressBar;
    private Button btnActualizar;
    private RelativeLayout rootRelative;

    private boolean boolCorreo = true;
    private String fechaNacimiento = "";

    private TokenManager tokenManager;

    private static final String CERO = "0";
    private static final String BARRA = "-";
    private final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    private LinearLayout linearContenedor;

    private DatePickerDialog recogerFecha;

    private int colorBlanco = 0, colorBlack = 0, colorGris = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;

    private boolean tema = false;


    private ImageView imgPerfil;





    // ******** PERMISOS ANDROID 13 O SUPERIOR *********

    private final int MY_PERMISSION_STORAGE_101 = 101;


    private int sdkVersion = Build.VERSION.SDK_INT;

    private final String[] requiredPermission = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
    };


    private boolean is_storage_image_permitted = false;



    private boolean allPermissionResultCheck(){
        return is_storage_image_permitted;
    }

    // IMAGE code for read storage media images starts
    private void requestPermissionStorageImage(){

        if(ContextCompat.checkSelfPermission(this, requiredPermission[0]) == PackageManager.PERMISSION_GRANTED){
            is_storage_image_permitted = true;
        } else{
            request_permission_launcher_storage_image.launch(requiredPermission[0]);
        }
    }


    private final ActivityResultLauncher<String> request_permission_launcher_storage_image =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted-> {
                        if(isGranted){
                            is_storage_image_permitted = true;
                        }else{
                            is_storage_image_permitted = false;
                        }
                    });




    // ******** END PERMISOS ANDROID 13 O SUPERIOR *********



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtToolbar = findViewById(R.id.txtToolbar);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnActualizar = findViewById(R.id.btnActualizar);
        rootRelative = findViewById(R.id.rootRelative);
        txtFechaNac = findViewById(R.id.txtCalendario);
        inputCorreo = findViewById(R.id.inputCorreo);
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtCorreo = findViewById(R.id.edtCorreo);
        linearContenedor = findViewById(R.id.linearContenedor);

        imgPerfil = findViewById(R.id.imgPerfil);

        txtToolbar.setText(getString(R.string.editar_perfil));

        int colorProgress = ContextCompat.getColor(this, R.color.barraProgreso);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        colorGris = ContextCompat.getColor(this, R.color.gris616161);
        colorBlanco = ContextCompat.getColor(this, R.color.blanco);
        colorBlack = ContextCompat.getColor(this, R.color.negro);
        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorBlack);

        btnActualizar.setBackgroundTintList(colorEstadoGris);
        btnActualizar.setTextColor(colorBlanco);

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });
        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        txtFechaNac.setOnClickListener(v -> {
            elegirFecha();
        });

        btnActualizar.setOnClickListener(v -> {
            try {
                actualizarPerfil();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

        imgPerfil.setOnClickListener(v -> {
            verPermisos();
        });

        solicitarPerfil();
    }

    private void verPermisos(){


        if (sdkVersion >= Build.VERSION_CODES.TIRAMISU) {
            // El dispositivo ejecuta Android 13 o superior.

            if(!allPermissionResultCheck()){

                requestPermissionStorageImage();

            }else{
                // aqui ya puede abrir galeria
                abrirGaleria();
            }

        } else {
            // El dispositivo no ejecuta Android 13.
            String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

            if(EasyPermissions.hasPermissions(this,
                    perms)){
                // permiso autorizado

               // aqui ya puede abrir galeria
                abrirGaleria();

            }else{
                // permiso denegado
                EasyPermissions.requestPermissions(this,
                        getString(R.string.permiso_almacenamiento_es_requerido),
                        MY_PERMISSION_STORAGE_101,
                        perms);
            }
        }
    }


    private void abrirGaleria(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    private boolean hayUriNueva = false;
    private Uri uriImagen = null;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // Handle successful image selection
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            hayUriNueva = true;
                            uriImagen = data.getData();
                            cargar(imageUri);
                        }
                    }
                }
            }
    );


    private void cargar(Uri uri){
        Glide.with(this)
                .load(uri)
                .apply(opcionesGlide)
                .circleCrop()
                .into(imgPerfil);
    }



    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);



    private void solicitarPerfil(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.informacionPerfil(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            edtNombre.setText(apiRespuesta.getNombre());
                                            edtApellido.setText(apiRespuesta.getApellido());
                                            txtFechaNac.setText(apiRespuesta.getFechaNacimiento());
                                            edtCorreo.setText(apiRespuesta.getCorreo());
                                            fechaNacimiento = apiRespuesta.getFechaNacimientoRaw();

                                            if(apiRespuesta.getHayImagen() == 1){
                                                cargarFoto(apiRespuesta.getImagen());
                                            }

                                            linearContenedor.setVisibility(View.VISIBLE);
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

    private void cargarFoto(String imagen){

        Glide.with(this)
                .load(RetrofitBuilder.urlImagenes + imagen)
                .apply(opcionesGlide)
                .circleCrop()
                .into(imgPerfil);
    }


    private void elegirFecha(){
        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual

        if (recogerFecha == null || !recogerFecha.isShowing()) {
            recogerFecha = new DatePickerDialog(this,  (view, year, month, dayOfMonth) -> {

                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                txtFechaNac.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);

                fechaNacimiento = year + BARRA + mesFormateado + BARRA + diaFormateado;
            },anio, mes, dia);
            //Muestro el widget
            recogerFecha.show();
        }
    }


    private void verificarEntradas(){

        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        if(TextUtils.isEmpty(txtNombre) || TextUtils.isEmpty(txtApellido)
                || TextUtils.isEmpty(txtCorreo)){

            desactivarBtnActualizar();
        }else{

            if(boolCorreo){
                activarBtnActualizar();
            }else{
                desactivarBtnActualizar();
            }
        }
    }


    private void activarBtnActualizar(){
        btnActualizar.setEnabled(true);

        if(tema){ // Dark
            btnActualizar.setBackgroundTintList(colorEstadoBlanco);
            btnActualizar.setTextColor(colorBlack);
        }else{
            btnActualizar.setBackgroundTintList(colorEstadoNegro);
            btnActualizar.setTextColor(colorBlanco);
        }
    }

    private void desactivarBtnActualizar(){
        btnActualizar.setEnabled(false);

        btnActualizar.setBackgroundTintList(colorEstadoGris);
        btnActualizar.setTextColor(colorBlanco);
    }


    private void actualizarPerfil() throws IOException {

        closeKeyboard();

        String iduser = tokenManager.getToken().getId();

        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        progressBar.setVisibility(View.VISIBLE);

        byte[] bytes = null;
        if(hayUriNueva){
            ContentResolver resolver = getContentResolver();
            InputStream inputStream = resolver.openInputStream(uriImagen);
            bytes = ImageUtils.inputStreamToByteArray(inputStream);
        }

        MultipartBody multipartBody = MultipartUtil.createMultipart(bytes, iduser, txtNombre, txtApellido, fechaNacimiento, txtCorreo);


        compositeDisposable.add(
                service.actualizarPerfilUsuario(multipartBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            correoYaRegistrado(txtCorreo);
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            volverAtrasActualizado();
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






    private void volverAtrasActualizado(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);

        Toasty.success(this, getString(R.string.actualizado)).show();
        onBackPressedDispatcher.onBackPressed();
    }


    private void correoYaRegistrado(String correo){

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.NORMAL_TYPE, false);

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
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
    }

    private void volverAtras(){
        onBackPressedDispatcher.onBackPressed();
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
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}