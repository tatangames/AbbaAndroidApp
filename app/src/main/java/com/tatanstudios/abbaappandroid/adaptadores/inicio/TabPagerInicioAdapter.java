package com.tatanstudios.abbaappandroid.adaptadores.inicio;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabComunidad;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabInicio;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentInicio;

public class TabPagerInicioAdapter extends FragmentPagerAdapter {

    private FragmentInicio fragmentInicio;
    private Context context;

    public TabPagerInicioAdapter(Context context, FragmentManager fm, FragmentInicio fragmentInicio) {
        super(fm);
        this.context = context;
        this.fragmentInicio = fragmentInicio;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentTabInicio.newInstance(fragmentInicio);
            case 1:
                return new FragmentTabComunidad();
            // Agrega más fragmentos según sea necesario
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Número total de fragmentos
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.inicio);
            case 1:
                return context.getString(R.string.comunidad);
            // Agrega más títulos según sea necesario
            default:
                return null;
        }
    }
}