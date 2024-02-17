package com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.listado;

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
import com.tatanstudios.abbaappandroid.activity.insignias.InsigniasPorGanarActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInsigniasFaltantes extends RecyclerView.Adapter<AdaptadorInsigniasFaltantes.ViewHolder> {

    private List<ModeloInicioInsignias> modeloInicioInsignias;
    private Context context;
    private InsigniasPorGanarActivity insigniasPorGanarActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInsigniasFaltantes(Context context, List<ModeloInicioInsignias> modeloInicioInsignias, InsigniasPorGanarActivity insigniasPorGanarActivity) {
        this.context = context;
        this.modeloInicioInsignias = modeloInicioInsignias;
        this.insigniasPorGanarActivity = insigniasPorGanarActivity;
    }

    @NonNull
    @Override
    public AdaptadorInsigniasFaltantes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_insignias_por_ganar, parent, false);
        return new AdaptadorInsigniasFaltantes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInsigniasFaltantes.ViewHolder holder, int position) {
        ModeloInicioInsignias m = modeloInicioInsignias.get(position);

        if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + m.getImagen())
                    .apply(opcionesGlide)
                    .into(holder.imgLogo);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.imgLogo);
        }

        holder.txtTitulo.setText(m.getTitulo());

        if(m.getDescripcion() != null && !TextUtils.isEmpty(m.getDescripcion())){
            holder.txtDescripcion.setText(m.getDescripcion());
        }

    }

    @Override
    public int getItemCount() {

        if(modeloInicioInsignias != null) {
            return modeloInicioInsignias.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgLogo;
        private TextView txtTitulo;

        private TextView txtDescripcion;

        ViewHolder(View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }


    }
}