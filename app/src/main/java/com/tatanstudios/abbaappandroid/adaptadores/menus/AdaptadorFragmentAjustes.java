package com.tatanstudios.abbaappandroid.adaptadores.menus;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentAjustes;
import com.tatanstudios.abbaappandroid.modelos.ajustes.ModeloFragmentConfiguracion;
import com.tatanstudios.abbaappandroid.modelos.ajustes.ModeloFragmentPerfil;
import com.tatanstudios.abbaappandroid.modelos.ajustes.ModeloVistaFragmentAjustes;

import java.util.ArrayList;

public class AdaptadorFragmentAjustes extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // adaptador para carrito de compras

    private Context context;
    public ArrayList<ModeloVistaFragmentAjustes> modeloVistaFragmentAjustes;
    private FragmentAjustes fragmentAjustes;

    ColorStateList colorStateListBlack, colorStateListWhite;


    public AdaptadorFragmentAjustes(Context context, ArrayList<ModeloVistaFragmentAjustes> modeloVistaFragmentAjustes, FragmentAjustes fragmentAjustes){
        this.context = context;
        this.fragmentAjustes = fragmentAjustes;
        this.modeloVistaFragmentAjustes = modeloVistaFragmentAjustes;

        int colorWhite = context.getColor(R.color.blanco);
        colorStateListWhite = ColorStateList.valueOf(colorWhite);

        int colorBlack = context.getColor(R.color.negro);
        colorStateListBlack = ColorStateList.valueOf(colorBlack);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistaFragmentAjustes.TIPO_PERFIL) {
            View view = inflater.inflate(R.layout.cardview_perfil_letra, parent, false);
            return new PerfilViewHolder(view);
        }
        else if (viewType == ModeloVistaFragmentAjustes.TIPO_ITEM_NORMAL) {
            View view = inflater.inflate(R.layout.cardview_perfil_opciones, parent, false);
            return new ItemNormalViewHolder(view);

        } else if (viewType == ModeloVistaFragmentAjustes.TIPO_LINEA_SEPARACION) {
            View view = inflater.inflate(R.layout.cardview_perfil_linea_separacion, parent, false);
            return new LineaSeparacionViewHolder(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistaFragmentAjustes modelo = modeloVistaFragmentAjustes.get(position);

        if (holder instanceof PerfilViewHolder) {
            // Configurar el ViewHolder para un ítem normal
            ModeloFragmentPerfil mPerfil = modelo.getModeloFragmentPerfil();

            ((PerfilViewHolder) holder).txtLetra.setText(mPerfil.getLetra());
            ((PerfilViewHolder) holder).txtNombre.setText(mPerfil.getNombrePerfil());

            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem
                fragmentAjustes.editarPerfil();
            });

        }
        else if (holder instanceof ItemNormalViewHolder) {

            ModeloFragmentConfiguracion mConfig = modelo.getModeloFragmentConfiguracion();

            ((ItemNormalViewHolder) holder).txtPerfil.setText(mConfig.getNombre());

            switch (mConfig.getIdentificador()){

                case 1:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_campana_vector);
                    break;
                case 2:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_candado_vector);
                    break;
                case 3:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_insignia_vector);
                    break;
                case 4:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_mundo);
                    break;
                case 5:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_lampara_vector);
                    break;
                case 6:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.ic_cerrar_sesion_vector);
                    break;
                default:

                    break;
            }


            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem normal
                handleItemClick(modelo);
            });



        } else if (holder instanceof LineaSeparacionViewHolder) {
            // Configurar el ViewHolder para una línea de separación

        }
    }



    private static class PerfilViewHolder extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        TextView txtLetra;
        TextView txtNombre;

        public PerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            txtLetra = itemView.findViewById(R.id.txtLetra);
            txtNombre = itemView.findViewById(R.id.txtNombre);

        }
    }




    // Definir ViewHolder para ítem normal
    private static class ItemNormalViewHolder extends RecyclerView.ViewHolder{
        // Definir los elementos de la interfaz gráfica según el layout de ítem normal

        TextView txtPerfil;
        ImageView imgPerfil;

        public ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            imgPerfil = itemView.findViewById(R.id.imgIcono);
            txtPerfil = itemView.findViewById(R.id.txtTexto);

        }

        // Agregar métodos para enlazar datos, si es necesario
    }

    // Definir ViewHolder para línea de separación
    private static class LineaSeparacionViewHolder extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación



        public LineaSeparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí


        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistaFragmentAjustes != null){
            return modeloVistaFragmentAjustes.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistaFragmentAjustes.get(position).getTipoVista();
    }




    // Método para manejar el clic en un ítem normal
    private void handleItemClick(ModeloVistaFragmentAjustes _modelo) {
        // Realizar acciones según el modelo del ítem normal
        // Por ejemplo, mostrar detalles, etc.

        ModeloFragmentConfiguracion m = _modelo.getModeloFragmentConfiguracion();
        fragmentAjustes.verPosicion(m.getIdentificador());
    }













}