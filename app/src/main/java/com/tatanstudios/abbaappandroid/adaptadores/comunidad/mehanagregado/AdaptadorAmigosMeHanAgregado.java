package com.tatanstudios.abbaappandroid.adaptadores.comunidad.mehanagregado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.agregados.PlanesListadoAmigoAgregadoActivity;
import com.tatanstudios.abbaappandroid.activity.comunidad.mehanagregado.PlanesAmigosMeHanAgregadoActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;

import java.util.List;

public class AdaptadorAmigosMeHanAgregado extends RecyclerView.Adapter<AdaptadorAmigosMeHanAgregado.ViewHolder> {

    private List<ModeloMisPlanes> modeloMisPlanes;
    private Context context;

    private PlanesAmigosMeHanAgregadoActivity planesAmigosMeHanAgregadoActivity;



    public AdaptadorAmigosMeHanAgregado(Context context, List<ModeloMisPlanes> modeloMisPlanes,
                                        PlanesAmigosMeHanAgregadoActivity planesAmigosMeHanAgregadoActivity
                                        ) {
        this.context = context;
        this.modeloMisPlanes = modeloMisPlanes;
        this.planesAmigosMeHanAgregadoActivity = planesAmigosMeHanAgregadoActivity;
    }

    @NonNull
    @Override
    public AdaptadorAmigosMeHanAgregado.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_amigos_mehanagregado_plan, parent, false);
        return new AdaptadorAmigosMeHanAgregado.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAmigosMeHanAgregado.ViewHolder holder, int position) {

        ModeloMisPlanes currentItem = modeloMisPlanes.get(position);

        String textoNombre= context.getString(R.string.nombre_dos_puntos) + " " + currentItem.getNombrecompleto();
        String textoCorreo = context.getString(R.string.correo_dos_puntos) + " " + currentItem.getCorreo();

        holder.txtNombre.setText(textoNombre);
        holder.txtCorreo.setText(textoCorreo);

        holder.itemView.setOnClickListener(view -> {
            planesAmigosMeHanAgregadoActivity.vistaPlanes(currentItem.getId());
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

        private TextView txtNombre, txtCorreo;
        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCorreo = itemView.findViewById(R.id.txtCorreo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}