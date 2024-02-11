package com.tatanstudios.abbaappandroid.fragmentos.menuprincipal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abbaappandroid.R;

public class FragmentBiblia extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_biblia, container, false);

        return vista;
    }
}