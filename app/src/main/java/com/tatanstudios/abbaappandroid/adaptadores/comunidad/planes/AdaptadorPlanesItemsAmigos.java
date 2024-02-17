package com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes;

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
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesAmigos;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesItemsAmigos;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorPlanesItemsAmigos extends RecyclerView.Adapter<AdaptadorPlanesItemsAmigos.ViewHolder> {

    private List<ModeloMisPlanes> modeloMisPlanes;
    private Context context;

    private FragmentPlanesItemsAmigos fragmentPlanesItemsAmigos;



    public AdaptadorPlanesItemsAmigos(Context context, List<ModeloMisPlanes> modeloMisPlanes, FragmentPlanesItemsAmigos fragmentPlanesItemsAmigos) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.fragmentPlanesItemsAmigos = fragmentPlanesItemsAmigos;
    }

    @NonNull
    @Override
    public AdaptadorPlanesItemsAmigos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planesitems_amigos, parent, false);
        return new AdaptadorPlanesItemsAmigos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesItemsAmigos.ViewHolder holder, int position) {

        ModeloMisPlanes currentItem = modeloMisPlanes.get(position);

        if(currentItem.getFecha() != null && !TextUtils.isEmpty(currentItem.getFecha())){
            holder.txtFecha.setText(currentItem.getFecha());
        }

        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtTitulo.setText(currentItem.getTitulo());
        }


        holder.itemView.setOnClickListener(view -> {

            // id: PlanesBlockDetaUsuario
           fragmentPlanesItemsAmigos.verItemsPlanPregunta(currentItem.getId());
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

        private TextView txtFecha;
        private TextView txtTitulo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}