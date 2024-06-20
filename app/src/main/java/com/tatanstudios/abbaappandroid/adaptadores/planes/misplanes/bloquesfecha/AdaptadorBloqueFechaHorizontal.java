package com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.bloquesfecha;


import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tatanstudios.abbaappandroid.R;


import com.tatanstudios.abbaappandroid.activity.planes.MisPlanesBloquesFechaActivity;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFecha;

import java.util.List;

public class AdaptadorBloqueFechaHorizontal extends RecyclerView.Adapter<AdaptadorBloqueFechaHorizontal.MyViewHolder> {


    private List<ModeloBloqueFecha> modeloBloqueFechas;

    private Context context;

    private ColorStateList colorStateWhite, colorStateBlack;

    private boolean tema;


    private MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity;

    private boolean unaVez = true;

    public AdaptadorBloqueFechaHorizontal(Context context, List<ModeloBloqueFecha>
            modeloBloqueFechas, boolean tema,
                                          MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity) {
        this.context = context;
        this.modeloBloqueFechas = modeloBloqueFechas;

        this.misPlanesBloquesFechaActivity = misPlanesBloquesFechaActivity;
        this.tema = tema;

        int colorBlanco = ContextCompat.getColor(context, R.color.blanco);
        int colorNegro = ContextCompat.getColor(context, R.color.negro);

        colorStateWhite = ColorStateList.valueOf(colorBlanco);
        colorStateBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public AdaptadorBloqueFechaHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_bloquefecha_horizontal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBloqueFechaHorizontal.MyViewHolder holder, int position) {

        ModeloBloqueFecha m = modeloBloqueFechas.get(position);


        if(tema){ // negro
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
            holder.txtFecha.setTextColor(colorStateWhite);
        }else{
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
            holder.txtFecha.setTextColor(colorStateBlack);
        }

            if(unaVez) {
                unaVez = false;
                misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());
            }


       // SIEMPRE MOSTRAR EL TEXTO PERSONALIZADO

        if(m.getTxtPersonalizado() != null && !TextUtils.isEmpty(m.getTxtPersonalizado())){
            holder.txtFecha.setText(m.getTxtPersonalizado());
        }else{
            holder.txtFecha.setText("");
        }


        holder.itemView.setOnClickListener(v -> {
            int actualPosition = holder.getBindingAdapterPosition();

            for (int i = 0; i < modeloBloqueFechas.size(); i++) {
                ModeloBloqueFecha modelo = modeloBloqueFechas.get(i);
                modelo.setPrimerBloqueDrawable(false);
                modelo.setEstaPresionado(false);
            }

            misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

            ModeloBloqueFecha mimodelo = modeloBloqueFechas.get(actualPosition);
            mimodelo.setEstaPresionado(true);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {

        if(modeloBloqueFechas != null){
            return modeloBloqueFechas.size();
        }
        else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFecha;
        private ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView){
            super(itemView);

            txtFecha = itemView.findViewById(R.id.textviewFecha);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }


}