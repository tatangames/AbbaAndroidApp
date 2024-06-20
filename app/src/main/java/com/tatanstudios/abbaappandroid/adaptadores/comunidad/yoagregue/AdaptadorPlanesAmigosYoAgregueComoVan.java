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
import com.tatanstudios.abbaappandroid.activity.comunidad.agregados.PlanesListadoAmigoAgregadoActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorPlanesAmigosYoAgregueComoVan extends RecyclerView.Adapter<AdaptadorPlanesAmigosYoAgregueComoVan.ViewHolder> {

    private List<ModeloMisPlanes> modeloMisPlanes;
    private Context context;

    private PlanesListadoAmigoAgregadoActivity planesAgregueComunidadActivity;

    private int itemTotal = 0;


    public AdaptadorPlanesAmigosYoAgregueComoVan(Context context, List<ModeloMisPlanes> modeloMisPlanes,
                                                 PlanesListadoAmigoAgregadoActivity planesAgregueComunidadActivity,
                                                 int itemTotal) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.planesAgregueComunidadActivity = planesAgregueComunidadActivity;
        this.itemTotal = itemTotal;
    }

    @NonNull
    @Override
    public AdaptadorPlanesAmigosYoAgregueComoVan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planes_amigos_yoagregue_comovan, parent, false);
        return new AdaptadorPlanesAmigosYoAgregueComoVan.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesAmigosYoAgregueComoVan.ViewHolder holder, int position) {

        ModeloMisPlanes currentItem = modeloMisPlanes.get(position);

        String textoNombre = context.getString(R.string.nombre_dos_puntos) + " " + currentItem.getNombre();
        String textoCorreo = context.getString(R.string.correo_dos_puntos) + " " + currentItem.getCorreo();

        String devoTotal = context.getString(R.string.devocional_total) + ": " + itemTotal;
        String devoRealizado = context.getString(R.string.devocional_realizados) + ": " + currentItem.getConteo();

        holder.txtNombre.setText(textoNombre);
        holder.txtCorreo.setText(textoCorreo);
        holder.txtItemTotal.setText(devoTotal);
        holder.txtItemLLevo.setText(devoRealizado);


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

        private TextView txtNombre, txtCorreo, txtItemTotal, txtItemLLevo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCorreo = itemView.findViewById(R.id.txtCorreo);
            txtItemTotal = itemView.findViewById(R.id.txtItemTotal);
            txtItemLLevo = itemView.findViewById(R.id.txtItemLlevo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}