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
import com.tatanstudios.abbaappandroid.activity.insignias.InsigniasPorGanarActivity;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInsigniasNiveles extends RecyclerView.Adapter<AdaptadorInsigniasNiveles.ViewHolder> {

    private List<ModeloInicioInsignias> modeloInicioInsignias;
    private Context context;

    public AdaptadorInsigniasNiveles(Context context, List<ModeloInicioInsignias> modeloInicioInsignias) {
        this.context = context;
        this.modeloInicioInsignias = modeloInicioInsignias;
    }

    @NonNull
    @Override
    public AdaptadorInsigniasNiveles.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_insignias_niveles, parent, false);
        return new AdaptadorInsigniasNiveles.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInsigniasNiveles.ViewHolder holder, int position) {
        ModeloInicioInsignias m = modeloInicioInsignias.get(position);

        String texto = context.getString(R.string.nivel) + " " + m.getNivel();

        holder.txtTitulo.setText(texto);
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

        private TextView txtTitulo;


        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }


    }
}