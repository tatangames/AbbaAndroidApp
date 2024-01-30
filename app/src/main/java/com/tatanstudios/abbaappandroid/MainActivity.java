package com.tatanstudios.abbaappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        *
        * boolean hasNotificationsEnabled = OneSignal.getPermissionSubscriptionState().getPermissionStatus().getEnabled();

* String userId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
Log.d("OneSignal", "User ID: " + userId);
*
if (hasNotificationsEnabled) {
    // El usuario tiene permitido notificaciones
} else {
    // El usuario no tiene permitido notificaciones
}
        *
        * */
    }
}