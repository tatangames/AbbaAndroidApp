package com.tatanstudios.abbaappandroid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tatanstudios.abbaappandroid.extras.ImageUtils;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;

public class EjemploActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private ImageView imagen;
    private Button boton;

    private boolean puedeDescargar = false;

    RequestOptions opcionesGlideOriginal = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Forzar la carga desde la memoria caché para mejorar la velocidad
            .placeholder(R.drawable.camaradefecto)
            .override(Target.SIZE_ORIGINAL); // Cargar la imagen con su resolución original





    // ******** PERMISOS *********
    private final String[] requiredPermission = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            // Manifest.permission.READ_MEDIA_VIDEO,
            // Manifest.permission.READ_MEDIA_AUDIO,
    };


    private boolean is_storage_image_permitted = false;





    private boolean allPermissionResultCheck(){

        //return is_storage_image_permitted && is_storage_video_permitted && is_storage_audio_permitted;
        return is_storage_image_permitted;
    }

    // IMAGE code for read storage media images starts
    private void requestPermissionStorageImage(){

        if(ContextCompat.checkSelfPermission(this, requiredPermission[0]) == PackageManager.PERMISSION_GRANTED){
            is_storage_image_permitted = true;

            // check for next permission, if you want only one stop it send to alert dialog
            //if(!allPermissionResultCheck()){
            // requestPermissionStorageVideo();
            //}
        } else{
            request_permission_launcher_storage_image.launch(requiredPermission[0]);
        }
    }


    private ActivityResultLauncher<String> request_permission_launcher_storage_image =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted-> {
                        if(isGranted){
                            is_storage_image_permitted = true;
                        }else{
                            is_storage_image_permitted = false;
                        }

                        // if we want to make hierarchy of permission another check over here

                       /* if(!allPermissionResultCheck()){
                            requestPermissionStorageVideo();
                        }*/

                    });


















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejemplo);

        imagen = findViewById(R.id.imagen);
        boton = findViewById(R.id.boton);

        boton.setOnClickListener(v ->{
            if(puedeDescargar){
                descargarImagen();
            }
        });

        cargarImagen();
    }

    private Bitmap bitmapGlobal;

    private void cargarImagen(){
        Glide.with(this)
                .load(RetrofitBuilder.urlImagenes + "aZScQ449CBGkYeC0.86526300_1706883139.png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Manejar el caso de carga fallida aquí
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // La imagen se ha cargado exitosamente, realizar acciones aquí

                        bitmapGlobal = ((BitmapDrawable) resource).getBitmap();
                        puedeDescargar = true;
                        return false;
                    }
                })
                .apply(opcionesGlideOriginal)
                .into(imagen);
    }


    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;


    private void descargarImagen(){



        int sdkVersion = Build.VERSION.SDK_INT;

        if (sdkVersion >= Build.VERSION_CODES.TIRAMISU) {
            // El dispositivo ejecuta Android 13 o superior.

            if(!allPermissionResultCheck()){

                requestPermissionStorageImage();

            }else{
                guardarImagen();
            }




        } else {
            // El dispositivo no ejecuta Android 13.
            String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

            if(EasyPermissions.hasPermissions(this,
                    perms)){
                // permiso autorizado

                guardarImagen();

            }else{
                // permiso denegado
                EasyPermissions.requestPermissions(this,
                        getString(R.string.permiso_almacenamiento_es_requerido),
                        MY_PERMISSION_STORAGE_101,
                        perms);
            }
        }
    }

    private boolean guardarImagen(){

        long currentTimeMillis = System.currentTimeMillis();

        String fileName = "imagen_" + currentTimeMillis + ".png"; // nombre del archivo

        Uri ur = ImageUtils.saveImageToGallery(bitmapGlobal, this, fileName);

        if(ur != null){
            Toasty.success(this, getString(R.string.guardado), Toasty.LENGTH_SHORT).show();
            return true;
        }else{
            Toasty.error(this, getString(R.string.error_al_guardar), Toasty.LENGTH_SHORT).show();
            return false;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Descargar la imagen y guardarla en el almacenamiento externo.

            } else {

                // Mostrar un mensaje al usuario indicando que no se pudo obtener el permiso.

            }

        }

    }


    private final int MY_PERMISSION_STORAGE_101 = 101;

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == MY_PERMISSION_STORAGE_101){
            Toasty.success(this, getString(R.string.permiso_autorizado), Toasty.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Permiso denegado, muestra un mensaje al usuario
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // El usuario ha seleccionado "No volver a mostrar"
            // Muestra un mensaje o guía al usuario para que active manualmente el permiso
            Toasty.error(this, getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        } else {
            // El usuario no ha seleccionado "No volver a mostrar", muestra un mensaje de solicitud de permiso estándar
            Toasty.error(this, getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        }
    }




}