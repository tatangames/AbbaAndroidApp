package com.tatanstudios.abbaappandroid.adaptadores.comunidad;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudPendienteRecibidaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;

import java.util.List;

public class AdaptadorSolicitudPendientesRecibidas extends RecyclerView.Adapter<AdaptadorSolicitudPendientesRecibidas.ViewHolder> {

    private List<ModeloComunidad> modeloComunidad;
    private Context context;

    private String textoCorreo = "";
    private String textoFecha = "";

    private String textoAceptarSolic = "";
    private String textoBorrarSolic = "";

    private SolicitudPendienteRecibidaActivity solicitudPendienteRecibidaActivity;

    public AdaptadorSolicitudPendientesRecibidas(Context context, List<ModeloComunidad> modeloComunidad,
                                                 SolicitudPendienteRecibidaActivity solicitudPendienteRecibidaActivity,
                                                 String textoCorreo, String textoFecha,
                                                 String textoAceptarSoli, String textoBorrarSolic) {
        this.context = context;
        this.modeloComunidad = modeloComunidad;
        this.solicitudPendienteRecibidaActivity = solicitudPendienteRecibidaActivity;
        this.textoCorreo = textoCorreo;
        this.textoFecha = textoFecha;
        this.textoAceptarSolic = textoAceptarSoli;
        this.textoBorrarSolic = textoBorrarSolic;
    }

    @NonNull
    @Override
    public AdaptadorSolicitudPendientesRecibidas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_solicitudes_pendientes, parent, false);
        return new AdaptadorSolicitudPendientesRecibidas.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSolicitudPendientesRecibidas.ViewHolder holder, int position) {
        ModeloComunidad m = modeloComunidad.get(position);

        if(m.getCorreo() != null && !TextUtils.isEmpty(m.getCorreo())){
            String correo = textoCorreo + ": " + m.getCorreo();
            holder.txtCorreo.setText(correo);
        }

        if(m.getFecha() != null && !TextUtils.isEmpty(m.getFecha())){
            String fechaSolicitud = textoFecha + ": " + m.getFecha();
            holder.txtFecha.setText(fechaSolicitud);
        }

        holder.setListener((view, po) -> {

            // Crea un PopupMenu
            PopupMenu popupMenu = new PopupMenu(context, holder.txtFecha);

            // Infla el menú en el PopupMenu
            popupMenu.inflate(R.menu.menu_opciones_pendientes_opciones);


            MenuItem opcion1 = popupMenu.getMenu().findItem(R.id.opcion1);
            opcion1.setTitle(textoAceptarSolic);

            MenuItem opcion2 = popupMenu.getMenu().findItem(R.id.opcion2);
            opcion2.setTitle(textoBorrarSolic);


            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                // Marcar que el menú está cerrado
               // menuAbierto = false;

                if (item.getItemId() == R.id.opcion1) {
                    solicitudPendienteRecibidaActivity.aceptarSolicitud(m.getId());
                    return true;
                }
                else if (item.getItemId() == R.id.opcion2) {
                    solicitudPendienteRecibidaActivity.borrarSolicitud(m.getId());
                    return true;
                }
                else {
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

        if(modeloComunidad != null){
            return modeloComunidad.size();
        }else{
            return 0;
        }

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