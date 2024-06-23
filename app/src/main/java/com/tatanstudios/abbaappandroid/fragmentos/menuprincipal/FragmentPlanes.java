package com.tatanstudios.abbaappandroid.fragmentos.menuprincipal;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.menus.AdaptadorFragmentPlanesBotonera;
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarmisplanes.FragmentMisPlanes;
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarplanes.FragmentBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.menus.ModeloFragmentPlanBotonera;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentPlanes extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorFragmentPlanesBotonera adaptadorFragmentPlanesBotonera;
    private TokenManager tokenManager;

    private boolean tema = false;
    private TextView txtToolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        txtToolbar = vista.findViewById(R.id.txtToolbar);

        txtToolbar.setText(getString(R.string.devocional));

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adaptadorFragmentPlanesBotonera = new AdaptadorFragmentPlanesBotonera();
        recyclerView.setAdapter(adaptadorFragmentPlanesBotonera);

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        modeloDatos();
        return vista;
    }

    private void modeloDatos(){

        List<ModeloFragmentPlanBotonera> modeloBotoneraPlanes = new ArrayList<>();
        modeloBotoneraPlanes.add(new ModeloFragmentPlanBotonera(1, getString(R.string.mis_planes)));
        modeloBotoneraPlanes.add(new ModeloFragmentPlanBotonera(2, getString(R.string.buscar_planes)));

        adaptadorFragmentPlanesBotonera = new AdaptadorFragmentPlanesBotonera(getContext(), modeloBotoneraPlanes, this, tema);
        recyclerView.setAdapter(adaptadorFragmentPlanesBotonera);

        // automaticamente cargar el Fragment Mis Planes

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new FragmentMisPlanes())
                .addToBackStack(null)
                .commit();
    }


    public void tipoPlan(int identificador){

        if(identificador == 1) { // MIS PLANES
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentMisPlanes())
                    .addToBackStack(null)
                    .commit();
        }
        else if(identificador == 2){ // NUEVOS PLANES
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentBuscarPlanes())
                    .addToBackStack(null)
                    .commit();
        }
    }

}