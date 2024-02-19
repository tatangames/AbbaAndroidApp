package com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes;

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
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarmisplanes.FragmentMisPlanes;
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarplanes.FragmentBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;


public class AdaptadorMisPlanes extends RecyclerView.Adapter<AdaptadorMisPlanes.MyViewHolder> {

    // UTILIZADO CON PAGINACION
    private Context context;
    private List<ModeloMisPlanes> modeloMisPlanes;
    private FragmentMisPlanes fragmentMisPlanes;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    public AdaptadorMisPlanes(Context context, List<ModeloMisPlanes> modeloMisPlanes, FragmentMisPlanes fragmentMisPlanes) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.fragmentMisPlanes = fragmentMisPlanes;
    }

    @NonNull
    @Override
    public AdaptadorMisPlanes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_mis_planes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorMisPlanes.MyViewHolder holder, int position) {

        ModeloMisPlanes m = modeloMisPlanes.get(position);

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
            fragmentMisPlanes.redireccionarBloqueFechas(m.getIdPlanes());
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
    public void addData(List<ModeloMisPlanes> newData) {

        int startPosition = modeloMisPlanes.size();

        // Agrega nuevos elementos a la lista existente
        modeloMisPlanes.addAll(newData);

        // Notifica al RecyclerView sobre los cambios en los datos
        notifyItemRangeInserted(startPosition, newData.size());
    }

}