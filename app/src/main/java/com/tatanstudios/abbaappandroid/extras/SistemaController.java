package com.tatanstudios.abbaappandroid.extras;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.tatanstudios.abbaappandroid.network.TokenManager;

public class SistemaController extends Application {


    // PRIMEROS AJUSTES DE APLICACION CUANDO SE INICIA


    // ID DE API ABBA APP
    private static final String ONESIGNAL_APP_ID = "cd253220-f1b1-4b46-9307-37cf176768a9";
    private TokenManager tokenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken().getTema() == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(tokenManager.getToken().getTema() == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);


        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
            if (r.isSuccess()) {
                if (r.getData()) {
                    // `requestPermission` completed successfully and the user has accepted permission
                }
                else {
                    // `requestPermission` completed successfully but the user has rejected permission
                }
            }
            else {
                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
            }
        }));
    }


}
