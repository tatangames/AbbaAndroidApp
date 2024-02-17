package com.tatanstudios.abbaappandroid.extras;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.OutputStream;

public class ImageUtils {



    // GUARDAR IMAGEN EN DIRECTORIO IMAGENES

    public static Uri saveImageToGallery(Bitmap bitmap, Context context, String fileName) {

        // Define los detalles de la imagen para la inserción en MediaStore
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // Obtiene el ContentResolver
        ContentResolver contentResolver = context.getContentResolver();

        // Inserta la imagen en MediaStore
        try {
            // Inserta la imagen y obtiene la URI del archivo insertado
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            // Si la URI no es nula, la inserción fue exitosa
            if (imageUri != null) {
                // Abre un OutputStream para escribir los datos de la imagen en el archivo
                OutputStream outputStream = contentResolver.openOutputStream(imageUri);
                if (outputStream != null) {
                    // Comprime y guarda el bitmap en el OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                    // Cierra el OutputStream
                    outputStream.close();

                   return imageUri;
                }
            } else {
                // La inserción falló, muestra un mensaje de error
               return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores
            return null;
        }

        return null;
    }

}
