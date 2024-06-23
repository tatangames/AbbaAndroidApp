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
import com.tatanstudios.abbaappandroid.activity.comunidad.SolicitudPendienteEnviadaActivity;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;

import java.util.List;

public class AdaptadorSolicitudPendientesEnviadas extends RecyclerView.Adapter<AdaptadorSolicitudPendientesEnviadas.ViewHolder> {

    private List<ModeloComunidad> modeloComunidad;
    private Context context;

    private SolicitudPendienteEnviadaActivity solicitudPendienteEnviadaActivity;

    private String textoEliminar = "";
    private String textoCorreo = "";
    private String textoFechaSoli = "";


    public AdaptadorSolicitudPendientesEnviadas(Context context, List<ModeloComunidad> modeloComunidad, SolicitudPendienteEnviadaActivity solicitudPendienteEnviadaActivity,
                                                String textoEliminar, String textoCorreo, String textoFechaSoli) {
        this.context = context;
        this.modeloComunidad = modeloComunidad;
        this.solicitudPendienteEnviadaActivity = solicitudPendienteEnviadaActivity;
        this.textoEliminar = textoEliminar;
        this.textoCorreo = textoCorreo;
        this.textoFechaSoli = textoFechaSoli;
    }

    @NonNull
    @Override
    public AdaptadorSolicitudPendientesEnviadas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_solicitudes_pendientes, parent, false);
        return new AdaptadorSolicitudPendientesEnviadas.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSolicitudPendientesEnviadas.ViewHolder holder, int position) {
        ModeloComunidad m = modeloComunidad.get(position);

        if(m.getCorreo() != null && !TextUtils.isEmpty(m.getCorreo())){
            String correo = textoCorreo + ": " + m.getCorreo();
            holder.txtCorreo.setText(correo);
        }

        if(m.getFecha() != null && !TextUtils.isEmpty(m.getFecha())){
            String fechaSolicitud = textoFechaSoli + ": " + m.getFecha();
            holder.txtFecha.setText(fechaSolicitud);
        }

        holder.setListener((view, po) -> {

            // Crea un PopupMenu
            PopupMenu popupMenu = new PopupMenu(context, holder.txtFecha);

            // Infla el menú en el PopupMenu
            popupMenu.inflate(R.menu.menu_opciones_pendientes_borrar);

            MenuItem opcion1 = popupMenu.getMenu().findItem(R.id.opcion2);
            opcion1.setTitle(textoEliminar);

            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                // Marcar que el menú está cerrado
               // menuAbierto = false;

                if (item.getItemId() == R.id.opcion1) {

                    solicitudPendienteEnviadaActivity.borrarSolicitud(m.getId());

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