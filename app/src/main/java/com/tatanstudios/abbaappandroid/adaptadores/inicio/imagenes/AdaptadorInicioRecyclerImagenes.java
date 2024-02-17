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
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabInicio;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioImagenes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInicioRecyclerImagenes extends RecyclerView.Adapter<AdaptadorInicioRecyclerImagenes.ViewHolder> {

    private List<ModeloInicioImagenes> modeloInicioImagenes;

    private Context context;

    private FragmentTabInicio fragmentTabInicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInicioRecyclerImagenes(Context context, List<ModeloInicioImagenes> modeloInicioImagenes, FragmentTabInicio fragmentTabInicio) {
        this.context = context;
        this.modeloInicioImagenes = modeloInicioImagenes;
        this.fragmentTabInicio = fragmentTabInicio;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerImagenes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_imagenes, parent, false);
        return new AdaptadorInicioRecyclerImagenes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerImagenes.ViewHolder holder, int position) {
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

        holder.iconImageView.setOnClickListener(v -> {
            fragmentTabInicio.abrirModalImagenes(m.getImagen());
        });

    }

    @Override
    public int getItemCount() {
        if(modeloInicioImagenes != null){
            return modeloInicioImagenes.size();
        }else{
            return 0;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;

        ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}