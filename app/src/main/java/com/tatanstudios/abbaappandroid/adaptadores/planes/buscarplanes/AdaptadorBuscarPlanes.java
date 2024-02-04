package com.tatanstudios.abbaappandroid.adaptadores.planes.buscarplanes;

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
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarplanes.FragmentBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;


public class AdaptadorBuscarPlanes extends RecyclerView.Adapter<AdaptadorBuscarPlanes.MyViewHolder> {

    // UTILIZADO CON PAGINACION
    private Context context;
    private List<ModeloBuscarPlanes> modeloBuscarPlanes;
    private FragmentBuscarPlanes fragmentBuscarPlanes;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorBuscarPlanes(Context context, List<ModeloBuscarPlanes> modeloBuscarPlanes, FragmentBuscarPlanes fragmentBuscarPlanes) {
        this.context = context;
        this.modeloBuscarPlanes = modeloBuscarPlanes;
        this.fragmentBuscarPlanes = fragmentBuscarPlanes;
    }

    @NonNull
    @Override
    public AdaptadorBuscarPlanes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_buscar_planes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBuscarPlanes.MyViewHolder holder, int position) {

        ModeloBuscarPlanes m = modeloBuscarPlanes.get(position);

        if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + m.getImagen())
                    .apply(opcionesGlide)
                    .into(holder.imgPlan);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.imgPlan);
        }

        if(m.getSubtitulo() != null && !TextUtils.isEmpty(m.getSubtitulo())){
            holder.txtSubtitulo.setText(m.getSubtitulo());
        }else{
            holder.txtSubtitulo.setVisibility(View.GONE);
        }

        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
        }else{
            holder.txtTitulo.setText("");
        }

        // visualizar
        holder.itemView.setOnClickListener(v -> {
            fragmentBuscarPlanes.verBloquePlanesVista(m.getId());
        });
    }

    @Override
    public int getItemCount() {
        if(modeloBuscarPlanes != null){
            return modeloBuscarPlanes.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;

        private TextView txtSubtitulo;

        public MyViewHolder(View itemView){
            super(itemView);

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtSubtitulo = itemView.findViewById(R.id.txtSubtitulo);
        }
    }

    // ACTUALIZAR SOLO LAS POSICIONES NUEVAS QUE SE AGREGARAN
    public void addData(List<ModeloBuscarPlanes> newData) {

        int startPosition = modeloBuscarPlanes.size();

        // Agrega nuevos elementos a la lista existente
        modeloBuscarPlanes.addAll(newData);

        // Notifica al RecyclerView sobre los cambios en los datos
        notifyItemRangeInserted(startPosition, newData.size());
    }

}