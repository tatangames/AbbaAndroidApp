package com.tatanstudios.abbaappandroid.adaptadores.comunidad;

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
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.inicio.insignias.ListadoInsigniasActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorTodasInsigniasComunidad extends RecyclerView.Adapter<AdaptadorTodasInsigniasComunidad.ViewHolder> {

    private List<ModeloInicioInsignias> modeloInicioInsignias;
    private Context context;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorTodasInsigniasComunidad(Context context, List<ModeloInicioInsignias> modeloInicioInsignias) {
        this.context = context;
        this.modeloInicioInsignias = modeloInicioInsignias;
    }

    @NonNull
    @Override
    public AdaptadorTodasInsigniasComunidad.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_listado_todas_insignias_comunidad, parent, false);
        return new AdaptadorTodasInsigniasComunidad.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTodasInsigniasComunidad.ViewHolder holder, int position) {
        ModeloInicioInsignias m = modeloInicioInsignias.get(position);

        if(m.getImageninsignia() != null && !TextUtils.isEmpty(m.getImageninsignia())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + m.getImageninsignia())
                    .apply(opcionesGlide)
                    .into(holder.imgLogo);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.imgLogo);
        }

        holder.txtNivel.setText(String.valueOf(m.getNivelVoy()));
        holder.txtTitulo.setText(m.getTitulo());

    }

    @Override
    public int getItemCount() {

        return modeloInicioInsignias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgLogo;
        private TextView txtNivel;
        private TextView txtTitulo;


        ViewHolder(View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);

        }
    }
}