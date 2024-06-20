package com.tatanstudios.abbaappandroid.adaptadores.comunidad.yoagregue;

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
import com.tatanstudios.abbaappandroid.activity.comunidad.agregados.PlanesAgregueComunidadActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesAmigos;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorPlanesAmigosYoAgregue extends RecyclerView.Adapter<AdaptadorPlanesAmigosYoAgregue.ViewHolder> {

    private List<ModeloMisPlanes> modeloMisPlanes;
    private Context context;

    private PlanesAgregueComunidadActivity planesAgregueComunidadActivity;


    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorPlanesAmigosYoAgregue(Context context, List<ModeloMisPlanes> modeloMisPlanes, PlanesAgregueComunidadActivity planesAgregueComunidadActivity) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.planesAgregueComunidadActivity = planesAgregueComunidadActivity;
    }

    @NonNull
    @Override
    public AdaptadorPlanesAmigosYoAgregue.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planes_amigos_yoagregue, parent, false);
        return new AdaptadorPlanesAmigosYoAgregue.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesAmigosYoAgregue.ViewHolder holder, int position) {

        ModeloMisPlanes currentItem = modeloMisPlanes.get(position);


        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtTitulo.setText(currentItem.getTitulo());
        }

        if(currentItem.getImagen() != null && !TextUtils.isEmpty(currentItem.getImagen())){


            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + currentItem.getImagen())
                    .apply(opcionesGlide)
                    .into(holder.imageView);
        }else{
            int resourceId = R.drawable.ic_campana_vector;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(view -> {
            planesAgregueComunidadActivity.cambioVista(currentItem.getIdPlanes()); // id_planes
        });
    }




    @Override
    public int getItemCount() {
        if(modeloMisPlanes != null){
            return modeloMisPlanes.size();
        }else{
            return 0;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ShapeableImageView imageView;
        private TextView txtTitulo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}