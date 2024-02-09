package com.tatanstudios.abbaappandroid.adaptadores.inicio.imagenes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.inicio.imagenes.ListadoImagenesActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioImagenes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorTodosImagenes extends RecyclerView.Adapter<AdaptadorTodosImagenes.ViewHolder> {

    private List<ModeloInicioImagenes> modeloInicioImagenes;
    private Context context;
    private ListadoImagenesActivity listadoImagenesActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorTodosImagenes(Context context, List<ModeloInicioImagenes> modeloInicioImagenes, ListadoImagenesActivity listadoImagenesActivity) {
        this.context = context;
        this.modeloInicioImagenes = modeloInicioImagenes;
        this.listadoImagenesActivity = listadoImagenesActivity;
    }

    @NonNull
    @Override
    public AdaptadorTodosImagenes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_listado_todos_imagenes, parent, false);
        return new AdaptadorTodosImagenes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTodosImagenes.ViewHolder holder, int position) {
        ModeloInicioImagenes m = modeloInicioImagenes.get(position);

        if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + m.getImagen())
                    .apply(opcionesGlide)
                    .into(holder.iconImageView);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.iconImageView);
        }

        holder.setListener((view, po) -> {
            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
                listadoImagenesActivity.abrirModalImagenes(m.getImagen());
            }
        });
    }

    @Override
    public int getItemCount() {

        return modeloInicioImagenes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iconImageView;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}