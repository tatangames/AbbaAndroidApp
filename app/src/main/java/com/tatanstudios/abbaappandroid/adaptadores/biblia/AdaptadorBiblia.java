package com.tatanstudios.abbaappandroid.adaptadores.biblia;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudPendienteEnviadaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentBiblia;
import com.tatanstudios.abbaappandroid.modelos.biblia.ModeloBiblia;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorBiblia extends RecyclerView.Adapter<AdaptadorBiblia.ViewHolder> {

    private List<ModeloBiblia> modeloBiblias;
    private Context context;
    private FragmentBiblia fragmentBiblia;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorBiblia(Context context, List<ModeloBiblia> modeloBiblias, FragmentBiblia fragmentBiblia) {
        this.context = context;
        this.modeloBiblias = modeloBiblias;
        this.fragmentBiblia = fragmentBiblia;
    }

    @NonNull
    @Override
    public AdaptadorBiblia.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_biblias, parent, false);
        return new AdaptadorBiblia.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBiblia.ViewHolder holder, int position) {
        ModeloBiblia currentItem = modeloBiblias.get(position);

        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtBiblia.setText(currentItem.getTitulo());
        }

        if(currentItem.getImagen() != null && !TextUtils.isEmpty(currentItem.getImagen())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + currentItem.getImagen())
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
            fragmentBiblia.vistaCapitulos(currentItem.getId());
        });
    }

    @Override
    public int getItemCount() {

        if(modeloBiblias != null){
            return modeloBiblias.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtBiblia;
        private ImageView imgLogo;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtBiblia = itemView.findViewById(R.id.txtBiblia);
            imgLogo = itemView.findViewById(R.id.imgLogo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}