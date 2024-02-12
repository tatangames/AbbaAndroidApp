package com.tatanstudios.abbaappandroid.fragmentos.menuprincipal;

import static android.content.Context.MODE_PRIVATE;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.TabPagerInicioAdapter;
import com.tatanstudios.abbaappandroid.extras.OnDataUpdateListenerRachas;
import com.tatanstudios.abbaappandroid.modelos.rachas.ModeloRachas;
import com.tatanstudios.abbaappandroid.network.TokenManager;

public class FragmentInicio extends Fragment implements OnDataUpdateListenerRachas {

    private TokenManager tokenManager;
    private int tabStrokeColor, tabTextColor, colorPrimary;
    private ImageView imgNoti;

    private TextView txtRacha;


    private boolean hayInfoRachas = false;

    private ModeloRachas modeloRachas = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);

        ViewPager viewPager = vista.findViewById(R.id.viewPager);
        TabLayout tabLayout = vista.findViewById(R.id.tabLayout);
        txtRacha = vista.findViewById(R.id.txtRacha);
        imgNoti = vista.findViewById(R.id.imgNoti);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken().getTema() == 1) {
            colorPrimary = ContextCompat.getColor(getContext(), R.color.gris616161); // Obtén el color para tema dark
            tabTextColor = ContextCompat.getColor(getContext(), R.color.negro); // Obtén el color para tema dark
            tabStrokeColor = ContextCompat.getColor(getContext(), R.color.blanco); // Obtén el color para tema dark


        } else {
            colorPrimary = ContextCompat.getColor(getContext(), R.color.blanco); // Obtén el color para tema light
            tabTextColor = ContextCompat.getColor(getContext(), R.color.gris616161); // Obtén el color para tema light
            tabStrokeColor = ContextCompat.getColor(getContext(), R.color.negro); // Obtén el color para tema light

        }

        tabLayout.setBackgroundColor(colorPrimary);
        tabLayout.setTabTextColors(tabTextColor, tabStrokeColor);


        txtRacha.setOnClickListener(v -> {
            if(hayInfoRachas){
                informacionRacha();
            }
        });

        imgNoti.setOnClickListener(v -> {

        });

        // Configura el adaptador del ViewPager y agrega pestañas al TabLayout
        TabPagerInicioAdapter adapter = new TabPagerInicioAdapter(getContext(), getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return vista;
    }


    @Override
    public void updateData(ModeloRachas modeloR) {

        if(modeloR != null){
            hayInfoRachas = true;
            modeloRachas = modeloR;

            txtRacha.setText(String.valueOf(modeloR.getNivelrachaalta()));
            txtRacha.setVisibility(View.VISIBLE);
        }
    }


    private void informacionRacha(){

        BottomSheetDialog bottomSheetRacha = new BottomSheetDialog(requireContext());
        View bottomSheetViewLayout = getLayoutInflater().inflate(R.layout.cardview_bottonsheet_racha, null);
        bottomSheetRacha.setContentView(bottomSheetViewLayout);

        TextView txtRachaAlta = bottomSheetRacha.findViewById(R.id.textViewLeft);
        TextView txtDiasSeguidos = bottomSheetRacha.findViewById(R.id.textViewRight);
        TextView txtDiasEsteAnio = bottomSheetRacha.findViewById(R.id.txtDiasEsteAnio);

        ImageView imgLunes = bottomSheetRacha.findViewById(R.id.imgLunes);
        ImageView imgMartes = bottomSheetRacha.findViewById(R.id.imgMartes);
        ImageView imgMiercoles = bottomSheetRacha.findViewById(R.id.imgMiercoles);
        ImageView imgJueves = bottomSheetRacha.findViewById(R.id.imgJueves);
        ImageView imgViernes = bottomSheetRacha.findViewById(R.id.imgViernes);
        ImageView imgSabado = bottomSheetRacha.findViewById(R.id.imgSabado);
        ImageView imgDomingo = bottomSheetRacha.findViewById(R.id.imgDomingo);

        txtRachaAlta.setText(String.valueOf(modeloRachas.getNivelrachaalta()));
        txtDiasSeguidos.setText(String.valueOf(modeloRachas.getDiasconcecutivos()));

        String texto = modeloRachas.getDiasesteanio() + " " + getString(R.string.dias_app_este_anio);
        txtDiasEsteAnio.setText(texto);

        if(modeloRachas.getLunes() == 1){
            imgLunes.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getMartes() == 1){
            imgMartes.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getMiercoles() == 1){
            imgMiercoles.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getJueves() == 1){
            imgJueves.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getViernes() == 1){
            imgViernes.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getSabado() == 1){
            imgSabado.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        if(modeloRachas.getDomingo() == 1){
            imgDomingo.setImageResource(R.drawable.ic_circulo_blanco_cronometro);
        }

        bottomSheetRacha.show();
    }



}