package com.tatanstudios.abbaappandroid.adaptadores.inicio.videos;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.inicio.videos.ListadoVideosActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioVideos;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorTodosVideos extends RecyclerView.Adapter<AdaptadorTodosVideos.ViewHolder> {

    private List<ModeloInicioVideos> modeloInicioVideos;
    private Context context;

    private ListadoVideosActivity listadoVideosActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorTodosVideos(Context context, List<ModeloInicioVideos> modeloInicioVideos, ListadoVideosActivity listadoVideosActivity) {
        this.context = context;
        this.modeloInicioVideos = modeloInicioVideos;
        this.listadoVideosActivity = listadoVideosActivity;
    }

    @NonNull
    @Override
    public AdaptadorTodosVideos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_listado_todos_videos, parent, false);
        return new AdaptadorTodosVideos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTodosVideos.ViewHolder holder, int position) {
        ModeloInicioVideos m = modeloInicioVideos.get(position);

        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
        }


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
            listadoVideosActivity.redireccionamientoVideo(m.getId_tipo_video(), m.getUrl_video());
        });
    }

    @Override
    public int getItemCount() {


        return modeloInicioVideos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtTitulo;
        private ShapeableImageView iconImageView;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            iconImageView = itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}