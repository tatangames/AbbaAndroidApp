package com.tatanstudios.abbaappandroid.adaptadores.planes.completados.bloquefecha;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.MisPlanesBloquesFechaActivity;
import com.tatanstudios.abbaappandroid.activity.planes.completados.MisPlanesBloquesFechaCompletadosActivity;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFechaDetalle;

import java.util.List;

public class AdaptadorBloqueFechaVerticalCompletado extends RecyclerView.Adapter<AdaptadorBloqueFechaVerticalCompletado.ViewHolder> {
    private List<ModeloBloqueFechaDetalle> modeloMisPlanesBloqueDetalles;
    private Context context;
    private MisPlanesBloquesFechaCompletadosActivity misPlanesBloquesFechaCompletadosActivity;
    private boolean tema;
    private ColorStateList colorStateWhite, colorStateBlack;

    private int colorBlanco;
    private int colorNegro;

    public AdaptadorBloqueFechaVerticalCompletado(Context context, List<ModeloBloqueFechaDetalle> modeloMisPlanesBloqueDetalles,
                                                  MisPlanesBloquesFechaCompletadosActivity misPlanesBloquesFechaCompletadosActivity, boolean tema) {
        this.context = context;
        this.modeloMisPlanesBloqueDetalles = modeloMisPlanesBloqueDetalles;
        this.misPlanesBloquesFechaCompletadosActivity = misPlanesBloquesFechaCompletadosActivity;
        this.tema = tema;

        colorBlanco = ContextCompat.getColor(context, R.color.blanco);
        colorNegro = ContextCompat.getColor(context, R.color.negro);

        colorStateWhite = ColorStateList.valueOf(colorBlanco);
        colorStateBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_bloquefecha_item_vertical_completado, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloBloqueFechaDetalle m = modeloMisPlanesBloqueDetalles.get(position);

        if(tema){ // DARK
            holder.txtTitulo.setTextColor(colorStateWhite);
        }else{
            holder.txtTitulo.setTextColor(colorStateBlack);
        }

        if(m.getTienePreguntas() == 1){
            holder.imgCompartir.setVisibility(View.VISIBLE);
        }else{
            holder.imgCompartir.setVisibility(View.GONE);
        }

        holder.txtTitulo.setText(m.getTitulo());

        holder.txtTitulo.setOnClickListener(v -> {
            int idBlockDeta = m.getId();
            int tienePreguntas = m.getTienePreguntas();
            misPlanesBloquesFechaCompletadosActivity.redireccionarCuestionario(idBlockDeta, tienePreguntas);
        });

        holder.imgCompartir.setOnClickListener(v ->{
            misPlanesBloquesFechaCompletadosActivity.informacionCompartir(m.getId());
        });
    }



    @Override
    public int getItemCount() {
        if(modeloMisPlanesBloqueDetalles != null){
            return modeloMisPlanesBloqueDetalles.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private ImageView imgCompartir;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            imgCompartir = itemView.findViewById(R.id.imgCompartir);
        }
    }
}