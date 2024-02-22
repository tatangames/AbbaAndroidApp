package com.tatanstudios.abbaappandroid.adaptadores.biblia.versiculo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.VersiculosListaActivity;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.AdaptadorSubCapitulos;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapitulo;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapituloBloque;
import com.tatanstudios.abbaappandroid.modelos.biblia.versiculo.ModeloVersiculo;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorVersiculos extends RecyclerView.Adapter<AdaptadorVersiculos.ItemViewHolder> {

    private List<ModeloVersiculo> modeloVersiculos;
    private Context context;

    private VersiculosListaActivity versiculosListaActivity;

    private List<ModeloCapituloBloque> list = new ArrayList<>();

    public AdaptadorVersiculos(Context context, List<ModeloVersiculo> modeloVersiculos, VersiculosListaActivity versiculosListaActivity){
        this.context = context;
        this.modeloVersiculos = modeloVersiculos;
        this.versiculosListaActivity = versiculosListaActivity;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_versiculo, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ModeloVersiculo model = modeloVersiculos.get(position);

        if(model.getTitulo() != null && !TextUtils.isEmpty(model.getTitulo())){
            holder.txtTitulo.setText(model.getTitulo());
        }

        holder.setListener((view, po) -> {


        });
    }



    @Override
    public int getItemCount() {
        if(modeloVersiculos != null){
            return modeloVersiculos.size();
        }else{
            return 0;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtTitulo;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public ItemViewHolder(@NonNull View itemView){
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
