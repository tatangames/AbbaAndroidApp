package com.tatanstudios.abbaappandroid.extras;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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



    private static final int MAX_IMAGE_SIZE = 1024; // Tamaño máximo de la imagen en píxeles

    public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        // Decodificar el inputStream en un Bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Redimensionar el Bitmap si es necesario
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > MAX_IMAGE_SIZE || height > MAX_IMAGE_SIZE) {
            float aspectRatio = (float) width / (float) height;
            if (width > height) {
                width = MAX_IMAGE_SIZE;
                height = (int) (width / aspectRatio);
            } else {
                height = MAX_IMAGE_SIZE;
                width = (int) (height * aspectRatio);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        // Comprimir el Bitmap en un ByteArrayOutputStream
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, buffer); // 80 es la calidad de compresión (de 0 a 100)

        // Convertir el ByteArrayOutputStream en un arreglo de bytes y devolverlo
        return buffer.toByteArray();
    }

}
