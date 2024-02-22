package com.tatanstudios.abbaappandroid.adaptadores.biblia;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.CapitulosBibliaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.biblia.grupos.ModeloGrupo;
import com.tatanstudios.abbaappandroid.modelos.biblia.grupos.ModeloSubGrupo;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCapitulos extends RecyclerView.Adapter<AdaptadorCapitulos.ItemViewHolder> {

    private List<ModeloGrupo> mList;
    private Context context;

    private CapitulosBibliaActivity capitulosBibliaActivity;

    private List<ModeloSubGrupo> list = new ArrayList<>();

    public AdaptadorCapitulos(Context context, List<ModeloGrupo> mList, CapitulosBibliaActivity capitulosBibliaActivity){
        this.context = context;
        this.mList = mList;
        this.capitulosBibliaActivity = capitulosBibliaActivity;
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_capitulo, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ModeloGrupo model = mList.get(position);
        holder.txtTitulo.setText(model.getTitulo());

        boolean isExpandable = model.isExpandlabe();
        holder.expandlabeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if(isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.arrow_up);
        }else{
            holder.mArrowImage.setImageResource(R.drawable.arrow_down);
        }


        AdaptadorSubCapitulos adaptador = new AdaptadorSubCapitulos(list, capitulosBibliaActivity);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
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
            list = model.getModeloSubGrupos();
            notifyItemChanged(holder.getBindingAdapterPosition());

            deseleccionarTodos(model.getId());
        });
    }


    private void deseleccionarTodos(int idno) {
        for (ModeloGrupo modelo : mList) {
            if(modelo.getId() != idno){
                modelo.setExpandlabe(false);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
