package com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias;

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
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabInicio;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInicioRecyclerInsignias extends RecyclerView.Adapter<AdaptadorInicioRecyclerInsignias.ViewHolder> {

    private List<ModeloInicioInsignias> modeloInicioInsignias;
    private Context context;

    private FragmentTabInicio fragmentTabInicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorInicioRecyclerInsignias(Context context, List<ModeloInicioInsignias> modeloInicioInsignias, FragmentTabInicio fragmentTabInicio) {
        this.context = context;
        this.modeloInicioInsignias = modeloInicioInsignias;
        this.fragmentTabInicio = fragmentTabInicio;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerInsignias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_insignias, parent, false);
        return new AdaptadorInicioRecyclerInsignias.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerInsignias.ViewHolder holder, int position) {
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

        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
            holder.txtTitulo.setVisibility(View.VISIBLE);
        }else{
            holder.txtTitulo.setVisibility(View.GONE);
        }

        holder.txtNivel.setText(String.valueOf(m.getNivelVoy()));

        holder.setListener((view, po) -> {
            fragmentTabInicio.vistaInformacionInsignia(m.getIdTipoInsignia());
        });
    }

    @Override
    public int getItemCount() {


        return modeloInicioInsignias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imgLogo;
        private TextView txtNivel;

        private TextView txtTitulo;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}