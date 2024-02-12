package com.tatanstudios.abbaappandroid.adaptadores.comunidad;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudesPendientesActivity;
import com.tatanstudios.abbaappandroid.activity.inicio.videos.ListadoVideosActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioVideos;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdaptadorSolicitudesPendientes extends RecyclerView.Adapter<AdaptadorSolicitudesPendientes.ViewHolder> {

    private List<ModeloComunidad> modeloComunidad;
    private Context context;

    private SolicitudesPendientesActivity solicitudesPendientesActivity;

    public AdaptadorSolicitudesPendientes(Context context, List<ModeloComunidad> modeloComunidad, SolicitudesPendientesActivity solicitudesPendientesActivity) {
        this.context = context;
        this.modeloComunidad = modeloComunidad;
        this.solicitudesPendientesActivity = solicitudesPendientesActivity;
    }

    @NonNull
    @Override
    public AdaptadorSolicitudesPendientes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_solicitudes_pendientes, parent, false);
        return new AdaptadorSolicitudesPendientes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSolicitudesPendientes.ViewHolder holder, int position) {
        ModeloComunidad m = modeloComunidad.get(position);

        if(m.getCorreo() != null && !TextUtils.isEmpty(m.getCorreo())){
            String correo = context.getString(R.string.correo) + ": " + m.getCorreo();
            holder.txtCorreo.setText(correo);
        }

        if(m.getFecha() != null && !TextUtils.isEmpty(m.getFecha())){
            String fechaSolicitud = context.getString(R.string.fecha_de_solicitud) + ": " + m.getFecha();
            holder.txtFecha.setText(fechaSolicitud);
        }

        holder.setListener((view, po) -> {
            //solicitudesPendientesActivity.abrirPanelMenu(holder.txtFecha);


            // Crea un PopupMenu
            PopupMenu popupMenu = new PopupMenu(context, holder.txtFecha);

            // Infla el menú en el PopupMenu
            popupMenu.inflate(R.menu.menu_opciones_pendientes);

            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                // Marcar que el menú está cerrado
               // menuAbierto = false;

                if (item.getItemId() == R.id.opcion1) {

                    solicitudesPendientesActivity.borrarSolicitud(m.getId());

                    return true;
                } else {
                    return false;
                }
            });

            // Agrega un listener para detectar cuando se cierra el menú
            popupMenu.setOnDismissListener(menu -> {
                // Marcar que el menú está cerrado
               // menuAbierto = false;
            });

            // Muestra el menú emergente
            popupMenu.show();

        });
    }

    @Override
    public int getItemCount() {


        return modeloComunidad.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtCorreo;
        private TextView txtFecha;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(View itemView) {
            super(itemView);
            txtCorreo = itemView.findViewById(R.id.txtCorreo);
            txtFecha = itemView.findViewById(R.id.txtFecha);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}