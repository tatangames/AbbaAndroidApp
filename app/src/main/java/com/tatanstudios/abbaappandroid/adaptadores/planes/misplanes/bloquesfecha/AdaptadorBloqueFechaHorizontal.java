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
    private RecyclerView recyclerView;

    private ColorStateList colorStateGrey, colorStateWhite, colorStateBlack;

    private boolean tema;
    private int hayDiaActual, idUltimoBloque;

    private MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity;


    public AdaptadorBloqueFechaHorizontal(Context context, List<ModeloBloqueFecha>
            modeloBloqueFechas, RecyclerView recyclerView, boolean tema, int hayDiaActual, int idUltimoBloque,
                                          MisPlanesBloquesFechaActivity misPlanesBloquesFechaActivity) {
        this.context = context;
        this.modeloBloqueFechas = modeloBloqueFechas;
        this.recyclerView = recyclerView;
        this.hayDiaActual = hayDiaActual;
        this.idUltimoBloque = idUltimoBloque;
        this.misPlanesBloquesFechaActivity = misPlanesBloquesFechaActivity;
        this.tema = tema;

        int colorGris = ContextCompat.getColor(context, R.color.gris616161);
        int colorBlanco = ContextCompat.getColor(context, R.color.blanco);
        int colorNegro = ContextCompat.getColor(context, R.color.negro);

        colorStateGrey = ColorStateList.valueOf(colorGris);
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

        if(m.getEstaPresionado()){

            if(tema){

                // TEMA DARK

                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                holder.txtFecha.setTextColor(colorStateWhite);

            }else{

                // TEMA LIGHT

                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                holder.txtFecha.setTextColor(colorStateWhite);
            }




        }else{

            if(tema){

                // TEMA DARK

                if(hayDiaActual == 1){
                    if(m.getMismoDia() == 1){
                        if(m.getPrimerBloqueDrawable()){
                            //m.setPrimerBloqueDrawable(false);
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                            holder.txtFecha.setTextColor(colorStateWhite);

                            misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_gris_off));
                            holder.txtFecha.setTextColor(colorStateWhite);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                        }

                    }else{
                        holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_gris_off));
                        holder.txtFecha.setTextColor(colorStateWhite);
                        holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                    }


                }else{
                    if(idUltimoBloque == m.getId()){
                        if(m.getPrimerBloqueDrawable()){
                            //m.setPrimerBloqueDrawable(false);
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                            holder.txtFecha.setTextColor(colorStateWhite);

                            misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_gris_off));
                            holder.txtFecha.setTextColor(colorStateWhite);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                        }
                    }else{
                        holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_gris_off));
                        holder.txtFecha.setTextColor(colorStateWhite);
                        holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                    }
                }


            }else{

                // TEMA LIGHT

                if(hayDiaActual == 1){
                    if(m.getMismoDia() == 1){
                        if(m.getPrimerBloqueDrawable()){
                            //m.setPrimerBloqueDrawable(false);
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                            holder.txtFecha.setTextColor(colorStateWhite);

                            misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_gris_off));
                            holder.txtFecha.setTextColor(colorStateBlack);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                        }

                    }else{
                        holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_gris_off));
                        holder.txtFecha.setTextColor(colorStateBlack);
                        holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                    }
                }else{
                    if(idUltimoBloque == m.getId()){
                        if(m.getPrimerBloqueDrawable()){
                            //m.setPrimerBloqueDrawable(false);
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                            holder.txtFecha.setTextColor(colorStateWhite);

                            misPlanesBloquesFechaActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_gris_off));
                            holder.txtFecha.setTextColor(colorStateBlack);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                        }
                    }else{
                        holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_gris_off));
                        holder.txtFecha.setTextColor(colorStateBlack);
                        holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_redondeado_vacia));
                    }
                }
            }
        }


        if(m.getTextoPersonalizado() == 1){

            if(m.getTxtPersonalizado() != null && !TextUtils.isEmpty(m.getTxtPersonalizado())){
                holder.txtFecha.setText(m.getTxtPersonalizado());
                holder.txtContador.setText("");
            }else{
                holder.txtFecha.setText("");
                holder.txtContador.setText("");
            }

        }else{
            holder.txtContador.setText(String.valueOf(m.getContador()));
            holder.txtFecha.setText(m.getAbreviatura());
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

            //modeloBloqueFechas.get(actualPosition).setEstaPresionado(true);
            //m.setEstaPresionado(true);
            notifyDataSetChanged();
            //notifyItemChanged(actualPosition);
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

        private TextView txtContador;
        private TextView txtFecha;
        private ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView){
            super(itemView);

            txtContador = itemView.findViewById(R.id.textviewContador);
            txtFecha = itemView.findViewById(R.id.textviewFecha);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }


    // MOVER DE POSICION AL RECYCLER
    public void moverPosicionRecycler(int posicion) {
        recyclerView.smoothScrollToPosition(posicion);
    }


}