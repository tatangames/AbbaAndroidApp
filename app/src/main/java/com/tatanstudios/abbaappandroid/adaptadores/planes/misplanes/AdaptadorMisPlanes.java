package com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes;

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
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarmisplanes.FragmentMisPlanes;
import com.tatanstudios.abbaappandroid.fragmentos.planes.buscarplanes.FragmentBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.buscarplanes.ModeloBuscarPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesBloque1;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanesBloque2;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloVistaMisPlanes;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorMisPlanes extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    public ArrayList<ModeloVistaMisPlanes> modeloVistaMisPlanes;
    private FragmentMisPlanes fragmentMisPlanes;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorMisPlanes(Context context, ArrayList<ModeloVistaMisPlanes> modeloVistaMisPlanes, FragmentMisPlanes fragmentMisPlanes){
        this.context = context;
        this.fragmentMisPlanes = fragmentMisPlanes;
        this.modeloVistaMisPlanes = modeloVistaMisPlanes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistaMisPlanes.TIPO_CONTINUAR) {
            View view = inflater.inflate(R.layout.cardview_mis_planes_continuar, parent, false);
            return new HolderVistaPlanesContinuar(view);

        } else if (viewType == ModeloVistaMisPlanes.TIPO_PLANES) {
            View view = inflater.inflate(R.layout.cardview_mis_planes, parent, false);
            return new HolderVistaPlanes(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistaMisPlanes modelo = modeloVistaMisPlanes.get(position);

        if (holder instanceof HolderVistaPlanesContinuar) {
            // Configurar el ViewHolder para un ítem normal
            ModeloMisPlanesBloque1 bloque1 = modelo.getModeloMisPlanesBloque1();

            // La imagen es la portada, pero se obtiene por imagen
            if(bloque1.getImagenPortada() != null && !TextUtils.isEmpty(bloque1.getImagenPortada())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + bloque1.getImagenPortada())
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanesContinuar) holder).imgPlan);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanesContinuar) holder).imgPlan);
            }

            ((HolderVistaPlanesContinuar) holder).txtTitulo.setText(bloque1.getTitulo());

            holder.itemView.setOnClickListener(v -> {
                fragmentMisPlanes.planesBloquesFecha(bloque1.getIdPlanes());
            });

        }
        else if (holder instanceof HolderVistaPlanes) {

            ModeloMisPlanesBloque2 bloque2 = modelo.getModeloMisPlanesBloque2();

            if(bloque2.getImagen() != null && !TextUtils.isEmpty(bloque2.getImagen())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + bloque2.getImagen())
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanes) holder).imgPlan);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanes) holder).imgPlan);
            }

            ((HolderVistaPlanes) holder).txtTitulo.setText(String.valueOf(bloque2.getIdPlanes()));

            holder.itemView.setOnClickListener(v -> {
                fragmentMisPlanes.planesBloquesFecha(bloque2.getIdPlanes());
            });
        }
    }



    // HOLDER PARA VISTA TITULO
    private static class HolderVistaPlanesContinuar extends RecyclerView.ViewHolder{

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;

        public HolderVistaPlanesContinuar(@NonNull View itemView) {
            super(itemView);

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }
    }



    private static class HolderVistaPlanes extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;

        public HolderVistaPlanes(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);

        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistaMisPlanes != null){
            return modeloVistaMisPlanes.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistaMisPlanes.get(position).getTipoVista();
    }

}