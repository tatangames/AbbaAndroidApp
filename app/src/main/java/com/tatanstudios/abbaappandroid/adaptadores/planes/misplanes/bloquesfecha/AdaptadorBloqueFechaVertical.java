package com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.bloquesfecha;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.MisPlanesBloquesFechaActivity;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFechaDetalle;

import java.util.List;

public class AdaptadorBloqueFechaVertical extends RecyclerView.Adapter<AdaptadorBloqueFechaVertical.ViewHolder> {
    private List<ModeloBloqueFechaDetalle> modeloMisPlanesBloqueDetalles;
    private Context context;
    private MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity;
    private boolean tema;
    private ColorStateList colorStateWhite, colorStateBlack;

    private int colorBlanco;
    private int colorNegro;

    public AdaptadorBloqueFechaVertical(Context context, List<ModeloBloqueFechaDetalle> modeloMisPlanesBloqueDetalles,
                                        MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity, boolean tema) {
        this.context = context;
        this.modeloMisPlanesBloqueDetalles = modeloMisPlanesBloqueDetalles;
        this.misPlanesBloquesFechaActivity = misPlanesBloquesFechaActivity;
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
        View itemView = inflater.inflate(R.layout.cardview_bloquefecha_item_vertical, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloBloqueFechaDetalle m = modeloMisPlanesBloqueDetalles.get(position);

        if(tema){ // DARK
            holder.txtTitulo.setTextColor(colorStateWhite);
            CompoundButtonCompat.setButtonTintList(holder.idCheck, ColorStateList.valueOf(colorBlanco));
        }else{
            holder.txtTitulo.setTextColor(colorStateBlack);
            CompoundButtonCompat.setButtonTintList(holder.idCheck, ColorStateList.valueOf(colorNegro));
        }

        if(m.getCompletado() == 1){
            holder.idCheck.setChecked(true);
        }else{
            holder.idCheck.setChecked(false);
        }

        if(m.getTienePreguntas() == 1){
            holder.imgCompartir.setVisibility(View.VISIBLE);
        }else{
            holder.imgCompartir.setVisibility(View.GONE);
        }

        holder.txtTitulo.setText(m.getTitulo());

        holder.idCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {

            int valor = 0;
            if(isChecked){ valor = 1; }
            m.setCompletado(valor);

            misPlanesBloquesFechaActivity.actualizarCheck(m.getId(), valor);
        });


        holder.txtTitulo.setOnClickListener(v -> {
            int idBlockDeta = m.getId();
            int tienePreguntas = m.getTienePreguntas();
            misPlanesBloquesFechaActivity.redireccionarCuestionario(idBlockDeta, tienePreguntas);
        });

        holder.imgCompartir.setOnClickListener(v ->{
            misPlanesBloquesFechaActivity.informacionCompartir(m.getId());
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
        private CheckBox idCheck;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            imgCompartir = itemView.findViewById(R.id.imgCompartir);
            idCheck = itemView.findViewById(R.id.idcheck);
        }
    }
}