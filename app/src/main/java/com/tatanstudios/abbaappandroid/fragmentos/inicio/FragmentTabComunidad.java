package com.tatanstudios.abbaappandroid.fragmentos.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abbaappandroid.R;

public class FragmentTabComunidad extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_comunidad, container, false);


        return vista;
    }
}