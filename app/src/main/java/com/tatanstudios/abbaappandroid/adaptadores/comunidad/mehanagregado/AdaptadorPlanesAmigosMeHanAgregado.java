package com.tatanstudios.abbaappandroid.adaptadores.comunidad.mehanagregado;

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
import com.tatanstudios.abbaappandroid.activity.comunidad.mehanagregado.PlanesMeHanAgregadoActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorPlanesAmigosMeHanAgregado extends RecyclerView.Adapter<AdaptadorPlanesAmigosMeHanAgregado.ViewHolder> {

    private List<ModeloMisPlanes> modeloMisPlanes;
    private Context context;

    private PlanesMeHanAgregadoActivity planesMeHanAgregadoActivity;



    public AdaptadorPlanesAmigosMeHanAgregado(Context context, List<ModeloMisPlanes> modeloMisPlanes, PlanesMeHanAgregadoActivity planesMeHanAgregadoActivity) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.planesMeHanAgregadoActivity = planesMeHanAgregadoActivity;
    }

    @NonNull
    @Override
    public AdaptadorPlanesAmigosMeHanAgregado.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planes_amigos_mehanagregado, parent, false);
        return new AdaptadorPlanesAmigosMeHanAgregado.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesAmigosMeHanAgregado.ViewHolder holder, int position) {

        ModeloMisPlanes currentItem = modeloMisPlanes.get(position);


        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtTitulo.setText(currentItem.getTitulo());
        }


        holder.itemView.setOnClickListener(view -> {

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

        private TextView txtTitulo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}