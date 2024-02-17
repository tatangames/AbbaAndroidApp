package com.tatanstudios.abbaappandroid.adaptadores.inicio.videos;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabInicio;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioVideos;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInicioRecyclerVideos extends RecyclerView.Adapter<AdaptadorInicioRecyclerVideos.ViewHolder> {

    private List<ModeloInicioVideos> modeloInicioVideos;
    private Context context;
    private FragmentTabInicio fragmentTabInicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInicioRecyclerVideos(Context context, List<ModeloInicioVideos> modeloInicioVideos, FragmentTabInicio fragmentTabInicio) {
        this.context = context;
        this.modeloInicioVideos = modeloInicioVideos;
        this.fragmentTabInicio = fragmentTabInicio;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerVideos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_video, parent, false);
        return new AdaptadorInicioRecyclerVideos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerVideos.ViewHolder holder, int position) {
        ModeloInicioVideos m = modeloInicioVideos.get(position);

        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
            holder.txtTitulo.setVisibility(View.VISIBLE);
        }else{
            holder.txtTitulo.setVisibility(View.GONE);
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
            fragmentTabInicio.redireccionamientoVideo(m.getId_tipo_video(), m.getUrl_video());
        });
    }

    @Override
    public int getItemCount() {

        if(modeloInicioVideos != null){
            return modeloInicioVideos.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtTitulo;
        private ShapeableImageView iconImageView;
        private IOnRecyclerViewClickListener listener;

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