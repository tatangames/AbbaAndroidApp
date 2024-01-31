package com.tatanstudios.abbaappandroid.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.login.FragmentLogin;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentLogin())
                .commit();
    }
}