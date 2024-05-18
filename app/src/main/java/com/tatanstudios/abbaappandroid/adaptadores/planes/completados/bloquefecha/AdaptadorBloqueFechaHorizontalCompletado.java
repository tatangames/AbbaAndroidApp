package com.tatanstudios.abbaappandroid.adaptadores.planes.completados.bloquefecha;


import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.planes.completados.MisPlanesBloquesFechaCompletadosActivity;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.bloquefechas.ModeloBloqueFecha;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdaptadorBloqueFechaHorizontalCompletado extends RecyclerView.Adapter<AdaptadorBloqueFechaHorizontalCompletado.MyViewHolder> {
    private List<ModeloBloqueFecha> modeloBloqueFechas;
    private Context context;
    private RecyclerView recyclerView;

    private ColorStateList colorStateGrey, colorStateWhite, colorStateBlack;

    private boolean tema;
    private int hayDiaActual, idUltimoBloque;

    private MisPlanesBloquesFechaCompletadosActivity misPlanesBloquesFechaCompletadosActivity;

    private boolean unaVez = true;

    public AdaptadorBloqueFechaHorizontalCompletado(Context context, List<ModeloBloqueFecha>
            modeloBloqueFechas, RecyclerView recyclerView, boolean tema, int hayDiaActual, int idUltimoBloque,
                                                    MisPlanesBloquesFechaCompletadosActivity misPlanesBloquesFechaCompletadosActivity) {
        this.context = context;
        this.modeloBloqueFechas = modeloBloqueFechas;
        this.recyclerView = recyclerView;
        this.hayDiaActual = hayDiaActual;
        this.idUltimoBloque = idUltimoBloque;
        this.misPlanesBloquesFechaCompletadosActivity = misPlanesBloquesFechaCompletadosActivity;
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
    public AdaptadorBloqueFechaHorizontalCompletado.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_bloquefecha_horizontal_completado, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBloqueFechaHorizontalCompletado.MyViewHolder holder, int position) {
        ModeloBloqueFecha m = modeloBloqueFechas.get(position);


        if(tema){ // negro
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
            holder.txtFecha.setTextColor(colorStateWhite);
            holder.txtContador.setTextColor(colorStateWhite);
        }else{
            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
            holder.txtFecha.setTextColor(colorStateBlack);
            holder.txtContador.setTextColor(colorStateBlack);
        }

        if (m.getContador() == 1) {
            if(unaVez) {
                unaVez = false;
                misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());
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

            for (ModeloBloqueFecha modelo : modeloBloqueFechas) {
                modelo.setEstaPresionado(false);
                m.setPrimerBloqueDrawable(false);
            }

            misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());
            m.setEstaPresionado(true);
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
        recyclerView.scrollToPosition(posicion);
    }


    private void estaba(){


            /*if(m.getEstaPresionado()){

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
                                m.setPrimerBloqueDrawable(false);
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

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
                                m.setPrimerBloqueDrawable(false);
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_dark_white_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

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
                                m.setPrimerBloqueDrawable(false);
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

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
                                m.setPrimerBloqueDrawable(false);
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_bloquefecha_light_negro_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.codigo_boton_redondeado_v3));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                misPlanesBloquesFechaCompletadosActivity.llenarDatosAdapterVertical(modeloBloqueFechas.get(position).getModeloBloqueFechas());

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
            }*/


    }

}