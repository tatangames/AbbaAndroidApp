package com.tatanstudios.abbaappandroid.adaptadores.inicio.cuestionario;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatanstudios.abbaappandroid.fragmentos.inicio.cuestionario.FragmentCuestionarioInicioBloque;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.cuestionario.FragmentCuestionarioPreguntasInicioBloque;
import com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario.FragmentCuestionarioPlanBloque;
import com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;

public class MiPagerCuestionarioInicioAdapter extends FragmentStateAdapter {

    private int numberOfItems, idBloqueDeta;

    public MiPagerCuestionarioInicioAdapter(FragmentActivity fragmentActivity, int numberOfItems, int idBloqueDeta) {
        super(fragmentActivity);
        this.idBloqueDeta = idBloqueDeta;
        this.numberOfItems = numberOfItems;
    }

    @Override
    public Fragment createFragment(int position) {
        if(numberOfItems == 1){
            switch (position) {
                case 0:
                    return FragmentCuestionarioInicioBloque.newInstance(idBloqueDeta);
                default:
                    return null;
            }
        }else{
            switch (position) {
                case 0:
                    return FragmentCuestionarioInicioBloque.newInstance(idBloqueDeta);
                case 1:
                    return FragmentCuestionarioPreguntasInicioBloque.newInstance(idBloqueDeta);
                default:
                    return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return numberOfItems; // Número total de fragmentos
    }
}