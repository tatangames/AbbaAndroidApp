package com.tatanstudios.abbaappandroid.adaptadores.biblia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.CapitulosBibliaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapituloBloque;

import java.util.List;

public class AdaptadorSubCapitulos extends RecyclerView.Adapter<AdaptadorSubCapitulos.AdaptadorViewHolder> {

    private List<ModeloCapituloBloque> mList;
    private FragmentCapitulos fragmentCapitulos;

    public AdaptadorSubCapitulos(List<ModeloCapituloBloque> mList, FragmentCapitulos fragmentCapitulos){
        this.mList = mList;
        this.fragmentCapitulos = fragmentCapitulos;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sub_capitulo, parent, false);
        return new AdaptadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {

        ModeloCapituloBloque modelo = mList.get(position);

        holder.txtSubTitulo.setText(modelo.getTitulo());


        holder.setListener((view, po) -> {
            fragmentCapitulos.bloqueCapitulo(modelo.getId());
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtSubTitulo;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }
        public AdaptadorViewHolder(@NonNull View itemView){
            super(itemView);

            txtSubTitulo = itemView.findViewById(R.id.txtSubTitulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }


}
