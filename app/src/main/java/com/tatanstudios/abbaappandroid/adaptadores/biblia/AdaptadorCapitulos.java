package com.tatanstudios.abbaappandroid.adaptadores.biblia;

import android.content.Context;
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
import com.tatanstudios.abbaappandroid.activity.biblia.CapitulosBibliaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.biblia.FragmentCapitulos;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapitulo;
import com.tatanstudios.abbaappandroid.modelos.biblia.capitulo.ModeloCapituloBloque;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCapitulos extends RecyclerView.Adapter<AdaptadorCapitulos.ItemViewHolder> {

    private List<ModeloCapitulo> modeloCapitulos;
    private Context context;

    private FragmentCapitulos fragmentCapitulos;

    private List<ModeloCapituloBloque> list = new ArrayList<>();

    public AdaptadorCapitulos(Context context, List<ModeloCapitulo> modeloCapitulos, FragmentCapitulos fragmentCapitulos){
        this.context = context;
        this.modeloCapitulos = modeloCapitulos;
        this.fragmentCapitulos = fragmentCapitulos;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_capitulo, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ModeloCapitulo model = modeloCapitulos.get(position);
        holder.txtTitulo.setText(model.getTitulo());

        boolean isExpandable = model.isExpandlabe();
        holder.expandlabeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if(isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down);
        }

        AdaptadorSubCapitulos adaptador = new AdaptadorSubCapitulos(list, fragmentCapitulos);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 6);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.nestedRecycler.setLayoutManager(layoutManager);

        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        LayoutAnimationController controller = new LayoutAnimationController(animation, 0.4f);
        holder.nestedRecycler.setHasFixedSize(true);
        holder.nestedRecycler.setLayoutAnimation(controller);
        holder.nestedRecycler.setLayoutManager(layoutManager);
        holder.nestedRecycler.setAdapter(adaptador);



        holder.setListener((view, po) -> {

            model.setExpandlabe(!model.isExpandlabe());
            list = model.getModeloCapituloBloques();
            notifyItemChanged(holder.getBindingAdapterPosition());

            deseleccionarTodos(model.getId());
        });
    }


    private void deseleccionarTodos(int idno) {
        for (ModeloCapitulo modelo : modeloCapitulos) {
            if(modelo.getId() != idno){
                modelo.setExpandlabe(false);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(modeloCapitulos != null){
            return modeloCapitulos.size();
        }else{
            return 0;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtTitulo;
        private ImageView mArrowImage;
        private RelativeLayout expandlabeLayout;

        private RecyclerView nestedRecycler;

        private IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            expandlabeLayout = itemView.findViewById(R.id.expandable_layout);
            nestedRecycler = itemView.findViewById(R.id.child_rv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }

    }


}
