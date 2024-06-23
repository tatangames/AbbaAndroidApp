package com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.individual;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;

import java.util.List;

public class AdaptadorInicioRecyclerHitos extends RecyclerView.Adapter<AdaptadorInicioRecyclerHitos.ViewHolder> {

    private List<ModeloInsigniaHitos> modeloInsigniaHitos;
    private Context context;

    public AdaptadorInicioRecyclerHitos(Context context, List<ModeloInsigniaHitos> modeloInsigniaHitos) {
        this.context = context;
        this.modeloInsigniaHitos = modeloInsigniaHitos;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerHitos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_insignia_hito_recycler, parent, false);
        return new AdaptadorInicioRecyclerHitos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerHitos.ViewHolder holder, int position) {
        ModeloInsigniaHitos m = modeloInsigniaHitos.get(position);

        String formatoFinal = m.getTextoCompletado() + " " + m.getFechaFormat();
        holder.txtTitulo.setText(formatoFinal);
        holder.txtNivel.setText(String.valueOf(m.getNivel()));

    }

    @Override
    public int getItemCount() {
        if(modeloInsigniaHitos != null){
            return modeloInsigniaHitos.size();
        }else{
            return 0;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitulo;
        private TextView txtNivel;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
        }


    }
}