package com.tatanstudios.abbaappandroid.adaptadores.inicio.redes;

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
import com.tatanstudios.abbaappandroid.modelos.redes.ModeloRedesSociales;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInicioRecyclerRedes extends RecyclerView.Adapter<AdaptadorInicioRecyclerRedes.ViewHolder> {

    private List<ModeloRedesSociales> modeloRedesSociales;
    private Context context;
    private FragmentTabInicio fragmentTabInicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInicioRecyclerRedes(Context context, List<ModeloRedesSociales> modeloRedesSociales, FragmentTabInicio fragmentTabInicio) {
        this.context = context;
        this.modeloRedesSociales = modeloRedesSociales;
        this.fragmentTabInicio = fragmentTabInicio;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerRedes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_redes, parent, false);
        return new AdaptadorInicioRecyclerRedes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerRedes.ViewHolder holder, int position) {
        ModeloRedesSociales m = modeloRedesSociales.get(position);

        if(m.getNombre() != null && !TextUtils.isEmpty(m.getNombre())){
            holder.txtTitulo.setText(m.getNombre());
            holder.txtTitulo.setVisibility(View.VISIBLE);
        }else{
            holder.txtTitulo.setVisibility(View.GONE);
        }


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

        holder.setListener((view, po) -> {


            fragmentTabInicio.redireccionarRedSocial(m.getLink());
        });
    }

    @Override
    public int getItemCount() {

        if(modeloRedesSociales != null){
            return modeloRedesSociales.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtTitulo;
        private ImageView imgLogo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            imgLogo = itemView.findViewById(R.id.imgLogo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}