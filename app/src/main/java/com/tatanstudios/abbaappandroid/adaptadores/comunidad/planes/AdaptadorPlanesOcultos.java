package com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.planes.PlanesOcultosActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.amigos.ModeloAmigos;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.ocultos.ModeloPlanesOcultos;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdaptadorPlanesOcultos extends RecyclerView.Adapter<AdaptadorPlanesOcultos.ViewHolder> {

    private List<ModeloPlanesOcultos> modeloPlanesOcultos;
    private Context context;

    private PlanesOcultosActivity planesOcultosActivity;

    public AdaptadorPlanesOcultos(Context context, List<ModeloPlanesOcultos> modeloPlanesOcultos, PlanesOcultosActivity planesOcultosActivity) {
        this.context = context;
        this.modeloPlanesOcultos = modeloPlanesOcultos;
        this.planesOcultosActivity = planesOcultosActivity;
    }

    @NonNull
    @Override
    public AdaptadorPlanesOcultos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planes_ocultos, parent, false);
        return new AdaptadorPlanesOcultos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesOcultos.ViewHolder holder, int position) {

        ModeloPlanesOcultos currentItem = modeloPlanesOcultos.get(position);


        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtTitulo.setText(currentItem.getTitulo());
        }

        holder.check.setChecked(modeloPlanesOcultos.get(position).getEstado());

        holder.check.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            int actualPosition = holder.getBindingAdapterPosition();
            holder.check.setChecked(isChecked1);
            modeloPlanesOcultos.get(actualPosition).setEstado(isChecked1);
        });
    }



    public List<ModeloPlanesOcultos> getCheckedModelIds() {
        List<ModeloPlanesOcultos> modelo = new ArrayList<>();

        for (ModeloPlanesOcultos item : modeloPlanesOcultos) {
            if (item.getEstado()) {
                // se envia id solicitud, y el id usuario quien dara los puntos
                modelo.add(new ModeloPlanesOcultos(item.getIdplanes(), item.getEstado()));
            }
        }
        return modelo;
    }


    @Override
    public int getItemCount() {
        return modeloPlanesOcultos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox check;
        private TextView txtTitulo;


        ViewHolder(View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }

    }
}