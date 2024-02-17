package com.tatanstudios.abbaappandroid.adaptadores.menus;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentPlanes;
import com.tatanstudios.abbaappandroid.modelos.menus.ModeloFragmentPlanBotonera;

import java.util.List;

public class AdaptadorFragmentPlanesBotonera extends RecyclerView.Adapter<AdaptadorFragmentPlanesBotonera.MyViewHolder> {

    // para agregar lista de direcciones

    private Context context;
    private List<ModeloFragmentPlanBotonera> modeloFragmentPlanBotoneras;

    private FragmentPlanes fragmentPlanes;

    private int botonSeleccionado = 0;

    private ColorStateList colorEstadoGris, colorEstadoBlanco, colorEstadoNegro;

    private boolean tema;

    // constructor vacio
    public AdaptadorFragmentPlanesBotonera() {}

    public AdaptadorFragmentPlanesBotonera(Context context,
                                           List<ModeloFragmentPlanBotonera> modeloFragmentPlanBotoneras,
                                           FragmentPlanes fragmentPlanes, boolean tema) {
        this.context = context;
        this.modeloFragmentPlanBotoneras = modeloFragmentPlanBotoneras;
        this.fragmentPlanes = fragmentPlanes;
        this.tema = tema;

        int colorGris = ContextCompat.getColor(context, R.color.gris616161);
        int colorBlanco = ContextCompat.getColor(context, R.color.blanco);
        int colorNegro = ContextCompat.getColor(context, R.color.negro);

        colorEstadoGris = ColorStateList.valueOf(colorGris);
        colorEstadoBlanco = ColorStateList.valueOf(colorBlanco);
        colorEstadoNegro = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public AdaptadorFragmentPlanesBotonera.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_fragment_planes_botonera, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFragmentPlanesBotonera.MyViewHolder holder, int position) {

        holder.btnPlanes.setText(modeloFragmentPlanBotoneras.get(position).getTexto());



        if (position == botonSeleccionado) {
            // boton seleccionado

            if(tema){ // dark
                holder.btnPlanes.setBackgroundTintList(colorEstadoBlanco);
                holder.btnPlanes.setTextColor(colorEstadoNegro);
            }else{
                holder.btnPlanes.setBackgroundTintList(colorEstadoNegro);
                holder.btnPlanes.setTextColor(colorEstadoBlanco);
            }

        } else {
            // boton no seleccionado

            // aplica a los 2 temas
            holder.btnPlanes.setBackgroundTintList(colorEstadoGris);
            holder.btnPlanes.setTextColor(colorEstadoBlanco);
        }

        holder.btnPlanes.setOnClickListener(v ->{
            int actualPosition = holder.getBindingAdapterPosition();
            botonSeleccionado = actualPosition;
            notifyDataSetChanged();

            int id = modeloFragmentPlanBotoneras.get(actualPosition).getId();
            fragmentPlanes.tipoPlan(id);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloFragmentPlanBotoneras != null){
            return modeloFragmentPlanBotoneras.size();
        }else{
            return 0;
        }
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {

        private TextView btnPlanes;

        public MyViewHolder(View itemView){
            super(itemView);

            btnPlanes = itemView.findViewById(R.id.btnItem);
        }
    }



}