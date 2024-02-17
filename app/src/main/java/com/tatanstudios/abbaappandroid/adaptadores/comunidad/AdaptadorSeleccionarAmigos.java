package com.tatanstudios.abbaappandroid.adaptadores.comunidad;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SeleccionarAmigosPlanActivity;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudPendienteEnviadaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.amigos.ModeloAmigos;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdaptadorSeleccionarAmigos extends RecyclerView.Adapter<AdaptadorSeleccionarAmigos.ViewHolder> {
    private List<ModeloComunidad> modeloComunidad;
    private Context context;

    private SeleccionarAmigosPlanActivity seleccionarAmigosPlanActivity;


    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorSeleccionarAmigos(Context context, List<ModeloComunidad> modeloComunidad, SeleccionarAmigosPlanActivity seleccionarAmigosPlanActivity) {
        this.context = context;
        this.modeloComunidad = modeloComunidad;
        this.seleccionarAmigosPlanActivity = seleccionarAmigosPlanActivity;


    }

    @NonNull
    @Override
    public AdaptadorSeleccionarAmigos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_seleccionar_amigos, parent, false);
        return new AdaptadorSeleccionarAmigos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSeleccionarAmigos.ViewHolder holder, int position) {
        ModeloComunidad m2 = modeloComunidad.get(position);

        if(m2.getPais() != null && !TextUtils.isEmpty(m2.getPais())){
            holder.txtPais.setText(m2.getPais());
        }

        if(m2.getNombre() != null && !TextUtils.isEmpty(m2.getNombre())){
            String nombre = context.getString(R.string.nombre_dos_puntos) + m2.getNombre();
            holder.txtNombre.setText(nombre);
        }

        if(m2.getCorreo() != null && !TextUtils.isEmpty(m2.getCorreo())){
            String correo = context.getString(R.string.correo_dos_puntos) + m2.getCorreo();
            holder.txtCorreo.setText(correo);
        }


        if(m2.getIdpais() == 1){ // el salvador
            int flagElsalvador = R.drawable.flag_elsalvador;
            Glide.with(context)
                    .load(flagElsalvador)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }else if(m2.getIdpais() == 2){ // guatemala
            int flagGuatemala = R.drawable.flag_guatemala;
            Glide.with(context)
                    .load(flagGuatemala)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }else if(m2.getIdpais() == 3){ // honduras
            int flagHonduras = R.drawable.flag_honduras;
            Glide.with(context)
                    .load(flagHonduras)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }else if(m2.getIdpais() == 4){ // nicaragua
            int flagNicaragua = R.drawable.flag_nicaragua;
            Glide.with(context)
                    .load(flagNicaragua)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }else if(m2.getIdpais() == 5){ // mexico
            int flagMexico = R.drawable.flag_mexico;
            Glide.with(context)
                    .load(flagMexico)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }else{
            int flagCamara = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(flagCamara)
                    .apply(opcionesGlide)
                    .into(holder.imgPais);
        }


        holder.check.setChecked(modeloComunidad.get(position).getCheckValor());


        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {


            int actualPosition = holder.getBindingAdapterPosition();
            holder.check.setChecked(isChecked);
            modeloComunidad.get(actualPosition).setCheckValor(isChecked);

            int conteo = countCheckedItems();
            if(conteo > 5){
                Toasty.info(context, context.getString(R.string.maximo_5_amigos), Toast.LENGTH_SHORT).show();
            }
        });

        holder.setListener((view, po) -> {


        });
    }


    // evitar mas de 5
    public int countCheckedItems() {
        int count = 0;
        for (ModeloComunidad item : modeloComunidad) {
            if (item.getCheckValor()) {
                count++;
            }
        }
        return count;
    }


    public List<ModeloAmigos> getCheckedModelIds() {
        List<ModeloAmigos> modeloAmigos = new ArrayList<>();

        for (ModeloComunidad item : modeloComunidad) {
            if (item.getCheckValor()) {
                // se envia id solicitud, y el id usuario quien dara los puntos
                modeloAmigos.add(new ModeloAmigos(item.getId(), item.getIdusuario()));
            }
        }
        return modeloAmigos;
    }

    @Override
    public int getItemCount() {
        return modeloComunidad.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CheckBox check;
        private ImageView imgPais;
        private TextView txtPais;
        private TextView txtNombre;
        private TextView txtCorreo;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
            imgPais = itemView.findViewById(R.id.imgPais);
            txtPais = itemView.findViewById(R.id.txtPais);
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