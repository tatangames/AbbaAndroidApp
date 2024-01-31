package com.tatanstudios.abbaappandroid.extras;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleManagerIdiomaAndroid {

    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    public static String obtenerIdioma(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        LocaleList locales = configuration.getLocales();
        if (locales.size() > 0) {
            return locales.get(0).getLanguage();
        }
        return "";
    }

}
