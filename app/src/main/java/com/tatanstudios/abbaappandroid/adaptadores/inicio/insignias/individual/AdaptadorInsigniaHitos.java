package com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.individual;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abbaappandroid.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import org.w3c.dom.Text;

import java.util.List;

public class AdaptadorInsigniaHitos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModeloVistaHitos> modeloVistaHitos;
    private Context context;


    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    private String textoNivel = "";


    public AdaptadorInsigniaHitos(Context context, List<ModeloVistaHitos> modeloVistaHitos,  String textoNivel) {
        this.context = context;
        this.modeloVistaHitos = modeloVistaHitos;
        this.textoNivel = textoNivel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        switch (viewType) {
            case ModeloVistaHitos.TIPO_IMAGEN:
                itemView = inflater.inflate(R.layout.cardview_insignia_hito_descripcion, parent, false);
                return new AdaptadorInsigniaHitos.DescripcionViewHolder(itemView);
            case ModeloVistaHitos.TIPO_RECYCLER:
                itemView = inflater.inflate(R.layout.cardview_recyclerview_hitos, parent, false);
                return new AdaptadorInsigniaHitos.RecyclerHitosViewHolder(itemView);

            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModeloVistaHitos mVista = modeloVistaHitos.get(position);

        switch (mVista.getTipoVista()) {
            case ModeloVistaHitos.TIPO_IMAGEN:

                ModeloDescripcionHitos mDescripcion = mVista.getModeloDescripcionHitos();

                AdaptadorInsigniaHitos.DescripcionViewHolder viewHolderDescripcion = (AdaptadorInsigniaHitos.DescripcionViewHolder) holder;

                if(mDescripcion.getTitulo() != null && !TextUtils.isEmpty(mDescripcion.getTitulo())){
                    viewHolderDescripcion.txtTitulo.setText(mDescripcion.getTitulo());
                }

                if(mDescripcion.getDescripcion() != null && !TextUtils.isEmpty(mDescripcion.getDescripcion())){
                    viewHolderDescripcion.txtDescripcion.setText(mDescripcion.getDescripcion());
                    viewHolderDescripcion.txtDescripcion.setVisibility(View.VISIBLE);
                }else{
                    viewHolderDescripcion.txtDescripcion.setVisibility(View.GONE);
                }

                if(mDescripcion.getImagen() != null && !TextUtils.isEmpty(mDescripcion.getImagen())){
                    Glide.with(context)
                            .load(RetrofitBuilder.urlImagenes + mDescripcion.getImagen())
                            .apply(opcionesGlide)
                            .into(viewHolderDescripcion.imgLogo);
                }else{
                    int resourceId = R.drawable.camaradefecto;
                    Glide.with(context)
                            .load(resourceId)
                            .apply(opcionesGlide)
                            .into(viewHolderDescripcion.imgLogo);
                }

                viewHolderDescripcion.txtNivel.setText(String.valueOf(mDescripcion.getNivelvoy()));

                viewHolderDescripcion.txtContador.setText(textoNivel);


                break;
            case ModeloVistaHitos.TIPO_RECYCLER:

                AdaptadorInsigniaHitos.RecyclerHitosViewHolder viewHolderRecycler = (AdaptadorInsigniaHitos.RecyclerHitosViewHolder) holder;

                configurarRecyclerHitos(viewHolderRecycler.recyclerView, mVista.getModeloInsigniaHitos());
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistaHitos != null){
            return modeloVistaHitos.size();
        }else{
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return modeloVistaHitos.get(position).getTipoVista();
    }


    private void configurarRecyclerHitos(RecyclerView recyclerView, List<ModeloInsigniaHitos> modeloInsigniaHitos) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerHitos(context, modeloInsigniaHitos);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }


    // BLOQUE DEVOCIONAL
    private static class DescripcionViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgLogo;
        private TextView txtNivel;
        private TextView txtTitulo;
        private TextView txtDescripcion;

        private TextView txtContador;

        DescripcionViewHolder(View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtContador = itemView.findViewById(R.id.txtContador);
        }
    }


    private static class RecyclerHitosViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        RecyclerHitosViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }


}