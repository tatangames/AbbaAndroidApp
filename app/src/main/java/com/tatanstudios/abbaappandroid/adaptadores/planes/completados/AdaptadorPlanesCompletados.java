package com.tatanstudios.abbaappandroid.adaptadores.planes.completados;

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
import com.tatanstudios.abbaappandroid.fragmentos.planes.completados.FragmentPlanesCompletados;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.completados.ModeloPlanesCompletados;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorPlanesCompletados extends RecyclerView.Adapter<AdaptadorPlanesCompletados.MyViewHolder> {

    // UTILIZADO CON PAGINACION
    private Context context;
    private List<ModeloPlanesCompletados> modeloPlanesCompletados;
    private FragmentPlanesCompletados fragmentPlanesCompletados;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorPlanesCompletados(Context context, List<ModeloPlanesCompletados> modeloPlanesCompletados, FragmentPlanesCompletados fragmentPlanesCompletados) {
        this.context = context;
        this.modeloPlanesCompletados = modeloPlanesCompletados;
        this.fragmentPlanesCompletados = fragmentPlanesCompletados;
    }

    @NonNull
    @Override
    public AdaptadorPlanesCompletados.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_planes_completados, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesCompletados.MyViewHolder holder, int position) {

        ModeloPlanesCompletados m = modeloPlanesCompletados.get(position);

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

        // visualizar, pasar id plan
        holder.itemView.setOnClickListener(v -> {
            fragmentPlanesCompletados.verBloquePlanesVista(m.getIdPlan());
        });
    }

    @Override
    public int getItemCount() {
        if(modeloPlanesCompletados != null){
            return modeloPlanesCompletados.size();
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
    public void addData(List<ModeloPlanesCompletados> newData) {

        int startPosition = modeloPlanesCompletados.size();

        // Agrega nuevos elementos a la lista existente
        modeloPlanesCompletados.addAll(newData);

        // Notifica al RecyclerView sobre los cambios en los datos
        notifyItemRangeInserted(startPosition, newData.size());
    }

}