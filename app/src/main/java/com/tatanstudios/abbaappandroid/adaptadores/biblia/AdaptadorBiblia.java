package com.tatanstudios.abbaappandroid.adaptadores.biblia;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudPendienteEnviadaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentBiblia;
import com.tatanstudios.abbaappandroid.modelos.biblia.ModeloBiblia;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;

import java.util.List;

public class AdaptadorBiblia extends RecyclerView.Adapter<AdaptadorBiblia.ViewHolder> {

    private List<ModeloBiblia> modeloBiblias;
    private Context context;
    private FragmentBiblia fragmentBiblia;

    public AdaptadorBiblia(Context context, List<ModeloBiblia> modeloBiblias, FragmentBiblia fragmentBiblia) {
        this.context = context;
        this.modeloBiblias = modeloBiblias;
        this.fragmentBiblia = fragmentBiblia;
    }

    @NonNull
    @Override
    public AdaptadorBiblia.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_biblias, parent, false);
        return new AdaptadorBiblia.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBiblia.ViewHolder holder, int position) {
        ModeloBiblia currentItem = modeloBiblias.get(position);

        if(currentItem.getTitulo() != null && !TextUtils.isEmpty(currentItem.getTitulo())){
            holder.txtBiblia.setText(currentItem.getTitulo());
        }

        holder.setListener((view, po) -> {
            fragmentBiblia.vistaCapitulos(currentItem.getId());
        });
    }

    @Override
    public int getItemCount() {

        if(modeloBiblias != null){
            return modeloBiblias.size();
        }else{
            return 0;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtBiblia;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtBiblia = itemView.findViewById(R.id.txtBiblia);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}