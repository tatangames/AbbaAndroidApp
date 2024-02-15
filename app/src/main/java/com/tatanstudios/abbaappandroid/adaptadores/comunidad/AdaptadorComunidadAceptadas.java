package com.tatanstudios.abbaappandroid.adaptadores.comunidad;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.preguntas.AdaptadorPreguntas;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabComunidad;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloComunidad;
import com.tatanstudios.abbaappandroid.modelos.comunidad.ModeloVistaComunidad;

import java.util.ArrayList;

public class AdaptadorComunidadAceptadas extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    public ArrayList<ModeloVistaComunidad> modeloVistaComunidads;
    private FragmentTabComunidad fragmentTabComunidad;
    private ColorStateList colorStateListBlack, colorStateListWhite;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    private int colorBlanco = 0;
    private int colorNegro = 0;
    private boolean tema;

    public AdaptadorComunidadAceptadas(Context context, ArrayList<ModeloVistaComunidad> modeloVistaComunidads,
                                       FragmentTabComunidad fragmentTabComunidad, boolean tema){
        this.context = context;
        this.fragmentTabComunidad = fragmentTabComunidad;
        this.modeloVistaComunidads = modeloVistaComunidads;
        this.tema = tema;

        colorBlanco = context.getColor(R.color.blanco);
        colorStateListWhite = ColorStateList.valueOf(colorBlanco);

        colorNegro = context.getColor(R.color.negro);
        colorStateListBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistaComunidad.TIPO_BOTONERA) {
            View view = inflater.inflate(R.layout.cardview_comunidad_botonera, parent, false);
            return new AdaptadorComunidadAceptadas.HolderVistaBotonera(view);

        } else if (viewType == ModeloVistaComunidad.TIPO_RECYCLER) {
            View view = inflater.inflate(R.layout.cardview_comunidad_amigos, parent, false);
            return new AdaptadorComunidadAceptadas.HolderVistaRecycler(view);
        }

        else if (viewType == ModeloVistaComunidad.TIPO_NOAMIGO) {
            View view = inflater.inflate(R.layout.cardview_comunidad_noamigos, parent, false);
            return new AdaptadorComunidadAceptadas.HolderVistaNoAmigos(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistaComunidad modelo2 = modeloVistaComunidads.get(position);

        // VISTA BOTONERA
        if (holder instanceof AdaptadorComunidadAceptadas.HolderVistaBotonera) {

            if(tema){ // dark
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnAgregar.setBackgroundTintList(colorStateListWhite);
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnAgregar.setTextColor(colorStateListBlack);

                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnPendiente.setBackgroundTintList(colorStateListWhite);
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnPendiente.setTextColor(colorStateListBlack);
            }else{
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnAgregar.setBackgroundTintList(colorStateListBlack);
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnAgregar.setTextColor(colorStateListWhite);

                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnPendiente.setBackgroundTintList(colorStateListBlack);
                ((AdaptadorComunidadAceptadas.HolderVistaBotonera) holder).btnPendiente.setTextColor(colorStateListWhite);
            }

            ((HolderVistaBotonera) holder).btnAgregar.setOnClickListener(v -> {
                fragmentTabComunidad.vistaEnviarSolicitud();
            });

            ((HolderVistaBotonera) holder).btnPendiente.setOnClickListener(v -> {


                // Crea un PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, ((HolderVistaBotonera) holder).btnPendiente);

                // Infla el menú en el PopupMenu
                popupMenu.inflate(R.menu.menu_opciones_pendientes_opcion);

                // Establece un listener para manejar los clics en los elementos del menú
                popupMenu.setOnMenuItemClickListener(item -> {
                    // Marcar que el menú está cerrado
                    // menuAbierto = false;

                    // Enviadas
                    if (item.getItemId() == R.id.opcion1) {

                        fragmentTabComunidad.vistaSolicitudPendientes(1);

                        return true;
                    }

                    // recibidas
                    else if (item.getItemId() == R.id.opcion2) {

                        fragmentTabComunidad.vistaSolicitudPendientes(2);

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

        // VISTA LISTADO ACEPTADO SOLICITUDES
        else if (holder instanceof AdaptadorComunidadAceptadas.HolderVistaRecycler) {

            ModeloComunidad m2 = modelo2.getModeloComunidad();

            if(m2.getNombre() != null && !TextUtils.isEmpty(m2.getNombre())){
                String nombre = context.getString(R.string.nombre_dos_puntos);
                ((HolderVistaRecycler) holder).txtNombre.setText(nombre + " " + m2.getNombre());
            }

            if(m2.getCorreo() != null && !TextUtils.isEmpty(m2.getCorreo())){
                String correo = context.getString(R.string.correo_dos_puntos);
                ((HolderVistaRecycler) holder).txtCorreo.setText(correo + " " + m2.getCorreo());
            }

            if(m2.getPais() != null && !TextUtils.isEmpty(m2.getPais())){
                String pais = context.getString(R.string.pais_dos_puntos);
                ((HolderVistaRecycler) holder).txtPais.setText(pais + " " + m2.getPais());
            }

            if(m2.getIdpais() == 1){ // el salvador
                int flagElsalvador = R.drawable.flag_elsalvador;
                Glide.with(context)
                        .load(flagElsalvador)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }else if(m2.getIdpais() == 2){ // guatemala
                int flagGuatemala = R.drawable.flag_guatemala;
                Glide.with(context)
                        .load(flagGuatemala)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }else if(m2.getIdpais() == 3){ // honduras
                int flagHonduras = R.drawable.flag_honduras;
                Glide.with(context)
                        .load(flagHonduras)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }else if(m2.getIdpais() == 4){ // nicaragua
                int flagNicaragua = R.drawable.flag_nicaragua;
                Glide.with(context)
                        .load(flagNicaragua)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }else if(m2.getIdpais() == 5){ // mexico
                int flagMexico = R.drawable.flag_mexico;
                Glide.with(context)
                        .load(flagMexico)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }else{
                int flagCamara = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(flagCamara)
                        .apply(opcionesGlide)
                        .into(((HolderVistaRecycler) holder).imgPais);
            }


            holder.itemView.setOnClickListener(v -> {




                // Crea un PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);

                // Infla el menú en el PopupMenu
                popupMenu.inflate(R.menu.menu_opciones_comunidad);

                // Establece un listener para manejar los clics en los elementos del menú
                popupMenu.setOnMenuItemClickListener(item -> {

                    // Planes
                    if (item.getItemId() == R.id.opcion1) {

                        //fragmentTabComunidad.planesComunidad(1);

                        return true;
                    }

                    // Insignias
                    else if (item.getItemId() == R.id.opcion2) {

                        //fragmentTabComunidad.insigniasComunidad(2);

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


        } else if (holder instanceof AdaptadorComunidadAceptadas.HolderVistaNoAmigos) {

           // no hay nada
        }
    }





    // HOLDER PARA BOTONERA
    private static class HolderVistaBotonera extends RecyclerView.ViewHolder{

        private Button btnAgregar;
        private Button btnPendiente;

        public HolderVistaBotonera(@NonNull View itemView) {
            super(itemView);

            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            btnPendiente = itemView.findViewById(R.id.btnPendientes);
        }
    }






    // HOLDER PARA LISTADO
    private static class HolderVistaRecycler extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private TextView txtNombre;
        private TextView txtCorreo;
        private TextView txtPais;
        private ImageView imgPais;

        public HolderVistaRecycler(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCorreo = itemView.findViewById(R.id.txtCorreo);
            txtPais = itemView.findViewById(R.id.txtPais);
            imgPais = itemView.findViewById(R.id.imgPais);

        }
    }




    // HOLDER PARA LISTADO
    private static class HolderVistaNoAmigos extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        public HolderVistaNoAmigos(@NonNull View itemView) {
            super(itemView);

        }
    }







    @Override
    public int getItemCount() {
        if(modeloVistaComunidads != null){
            return modeloVistaComunidads.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistaComunidads.get(position).getTipoVista();
    }

}