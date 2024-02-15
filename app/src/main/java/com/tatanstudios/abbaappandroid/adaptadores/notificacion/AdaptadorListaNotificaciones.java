package com.tatanstudios.abbaappandroid.adaptadores.notificacion;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.cuestionario.preguntas.AdaptadorPreguntasInicio;
import com.tatanstudios.abbaappandroid.fragmentos.planes.completados.FragmentPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.notificacion.ModeloListaNotificacion;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletados;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorListaNotificaciones extends RecyclerView.Adapter<AdaptadorListaNotificaciones.MyViewHolder> {

    // UTILIZADO CON PAGINACION
    private Context context;
    private List<ModeloListaNotificacion> modeloListaNotificacions;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorListaNotificaciones(Context context, List<ModeloListaNotificacion> modeloListaNotificacions) {
        this.context = context;
        this.modeloListaNotificacions = modeloListaNotificacions;
    }

    @NonNull
    @Override
    public AdaptadorListaNotificaciones.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_lista_notificaciones, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaNotificaciones.MyViewHolder holder, int position) {

        ModeloListaNotificacion m = modeloListaNotificacions.get(position);

        if(m.getHayimagen() == 1){
            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){


                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + m.getImagen())
                        .apply(opcionesGlide)
                        .into(holder.imgImagen);
            }else{
                int resourceId = R.drawable.ic_campana_vector;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(holder.imgImagen);
            }
        }


        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
        }else{
            holder.txtTitulo.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if(modeloListaNotificacions != null){
            return modeloListaNotificacions.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitulo;
        private ImageView imgImagen;

        public MyViewHolder(View itemView){
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            imgImagen = itemView.findViewById(R.id.imgImagen);
        }
    }

    // ACTUALIZAR SOLO LAS POSICIONES NUEVAS QUE SE AGREGARAN
    public void addData(List<ModeloListaNotificacion> newData) {

        int startPosition = modeloListaNotificacions.size();

        // Agrega nuevos elementos a la lista existente
        modeloListaNotificacions.addAll(newData);

        // Notifica al RecyclerView sobre los cambios en los datos
        notifyItemRangeInserted(startPosition, newData.size());
    }

}