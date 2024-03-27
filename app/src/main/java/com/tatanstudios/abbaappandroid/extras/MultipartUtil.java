package com.tatanstudios.abbaappandroid.extras;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
public class MultipartUtil {

    public static MultipartBody createMultipart(byte[] imageData, String iduser, String nombre, String apellido, String fechaNac, String correo) {

        if(imageData != null){
            RequestBody imagenBody = RequestBody.create(MediaType.parse("image/*"), imageData);

            // Crear el MultipartBody que contiene la imagen, el nombre y el apellido
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("imagen", "image.jpg", imagenBody)
                    .addFormDataPart("iduser", iduser)
                    .addFormDataPart("nombre", nombre)
                    .addFormDataPart("apellido", apellido)
                    .addFormDataPart("fechanac", fechaNac)
                    .addFormDataPart("correo", correo)
                    .build();
        }else{

            // Crear el MultipartBody que contiene la imagen, el nombre y el apellido
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("iduser", iduser)
                    .addFormDataPart("nombre", nombre)
                    .addFormDataPart("apellido", apellido)
                    .addFormDataPart("fechanac", fechaNac)
                    .addFormDataPart("correo", correo)
                    .build();
        }

    }
}
