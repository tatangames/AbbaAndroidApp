package com.tatanstudios.abbaappandroid.adaptadores.inicio;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.imagenes.AdaptadorInicioRecyclerImagenes;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.insignias.AdaptadorInicioRecyclerInsignias;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.videos.AdaptadorInicioRecyclerVideos;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.FragmentTabInicio;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioComparteApp;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioDevocional;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioImagenes;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioInsignias;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloInicioVideos;
import com.tatanstudios.abbaappandroid.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abbaappandroid.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

public class AdaptadorInicio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModeloVistasInicio> modeloVistasInicios;
    private Context context;
    private FragmentTabInicio fragmentTabInicio;
    private boolean tema;
    private boolean menuAbierto = false;
    private ModeloInicioSeparador modeloInicioSeparador;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    private String textoCompartir = "";

    public AdaptadorInicio(Context context, List<ModeloVistasInicio> modeloVistasInicios, FragmentTabInicio fragmentTabInicio,
                           boolean tema, ModeloInicioSeparador modeloInicioSeparador) {
        this.context = context;
        this.modeloVistasInicios = modeloVistasInicios;
        this.fragmentTabInicio = fragmentTabInicio;
        this.tema = tema;
        this.modeloInicioSeparador = modeloInicioSeparador;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        switch (viewType) {
            case ModeloVistasInicio.TIPO_DEVOCIONAL:
                itemView = inflater.inflate(R.layout.cardview_inicio_devocional, parent, false);
                return new DevocionalViewHolder(itemView);
            case ModeloVistasInicio.TIPO_VIDEOS:
                itemView = inflater.inflate(R.layout.cardview_inicio_videos_recycler, parent, false);
                return new RecyclerVideoViewHolder(itemView);

            case ModeloVistasInicio.TIPO_IMAGENES:
                itemView = inflater.inflate(R.layout.cardview_inicio_imagenes_recycler, parent, false);
                return new RecyclerImagenesViewHolder(itemView);

            case ModeloVistasInicio.TIPO_COMPARTEAPP:
                itemView = inflater.inflate(R.layout.cardview_inicio_comparteapp, parent, false);
                return new ComparteAppViewHolder(itemView);

            case ModeloVistasInicio.TIPO_INSIGNIAS:
                itemView = inflater.inflate(R.layout.cardview_inicio_insignias_recycler, parent, false);
                return new RecyclerInsigniasViewHolder(itemView);

            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModeloVistasInicio mVista = modeloVistasInicios.get(position);

        switch (mVista.getTipoVista()) {

            case ModeloVistasInicio.TIPO_DEVOCIONAL:

                ModeloInicioDevocional m = mVista.getModeloInicioDevocional();

                DevocionalViewHolder viewHolderDevocional = (DevocionalViewHolder) holder;

                Document document = Jsoup.parse(m.getDevocuestionario());
                document.outputSettings().prettyPrint(true);
                document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);

                // 40 CARACTERES MAXIMO
                String limitedHtmlText = document.html().substring(0, Math.min(document.html().length(), 400));
                limitedHtmlText += " ...";

                viewHolderDevocional.webView.loadDataWithBaseURL(null, limitedHtmlText , "text/html", "UTF-8", null);

                viewHolderDevocional.webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        String javascript = String.format("document.body.style.fontSize = '%dpx';", 19);
                        viewHolderDevocional.webView.evaluateJavascript(javascript, null);
                    }
                });


                viewHolderDevocional.imgCompartir.setOnClickListener(v -> {
                    String mitexto = HtmlCompat.fromHtml(m.getDevocuestionario(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                    fragmentTabInicio.compartirTextoDevocionalDia(mitexto);
                });

                viewHolderDevocional.webView.setOnClickListener(v -> {
                    fragmentTabInicio.redireccionarCuestionario(m.getDevoidblockdeta(), m.getDevopreguntas());
                });


                viewHolderDevocional.imgOpciones.setOnClickListener(v -> {

                    if (!menuAbierto) {
                        // Marcar que el menú está abierto
                        menuAbierto = true;

                        // Crea un PopupMenu
                        PopupMenu popupMenu = new PopupMenu(context, viewHolderDevocional.imgOpciones);

                        // Infla el menú en el PopupMenu
                        popupMenu.inflate(R.menu.menu_opciones_devocionales);

                        // Establece un listener para manejar los clics en los elementos del menú
                        popupMenu.setOnMenuItemClickListener(item -> {
                            // Marcar que el menú está cerrado
                            menuAbierto = false;

                            if (item.getItemId() == R.id.opcion1) {

                                // IR A CUESTIONARIO Y PREGUNTAS
                                fragmentTabInicio.redireccionarCuestionario(m.getDevoidblockdeta(), m.getDevopreguntas());

                                return true;
                            } else {
                                return false;
                            }
                        });

                        // Agrega un listener para detectar cuando se cierra el menú
                        popupMenu.setOnDismissListener(menu -> {
                            // Marcar que el menú está cerrado
                            menuAbierto = false;
                        });

                        // Muestra el menú emergente
                        popupMenu.show();
                    }
                });

                break;
            case ModeloVistasInicio.TIPO_VIDEOS:

                RecyclerVideoViewHolder viewHolderVideo = (RecyclerVideoViewHolder) holder;
                viewHolderVideo.txtToolbar.setText(context.getString(R.string.videos));

                if(modeloInicioSeparador.getHayMasDe5Videos() == 1){
                    viewHolderVideo.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderVideo.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderVideo.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Videos() == 1){

                        fragmentTabInicio.vistaTodosLosVideos();
                    }
                });

                configurarRecyclerVideos(viewHolderVideo.recyclerViewVideos, mVista.getModeloInicioVideos());
                break;

            case ModeloVistasInicio.TIPO_IMAGENES:
                RecyclerImagenesViewHolder viewHolderImagenes = (RecyclerImagenesViewHolder) holder;

                viewHolderImagenes.txtToolbar.setText(context.getString(R.string.imagenes_del_dia));

                if(modeloInicioSeparador.getHayMasDe5Imagenes() == 1){
                    viewHolderImagenes.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderImagenes.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderImagenes.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Imagenes() == 1){
                        fragmentTabInicio.vistaTodosLasImagenes();
                    }
                });


                configurarRecyclerImagenes(viewHolderImagenes.recyclerViewImagenes, mVista.getModeloInicioImagenes());
                break;
            case ModeloVistasInicio.TIPO_COMPARTEAPP:
                ComparteAppViewHolder viewHolderComparteApp = (ComparteAppViewHolder) holder;

                ModeloInicioComparteApp mComparte = mVista.getModeloInicioComparteApp();

                if(mComparte.getTitulo() != null && !TextUtils.isEmpty(mComparte.getTitulo())){
                    viewHolderComparteApp.txtTitulo.setText(mComparte.getTitulo());
                    viewHolderComparteApp.txtTitulo.setVisibility(View.VISIBLE);
                }else{
                    viewHolderComparteApp.txtTitulo.setVisibility(View.GONE);
                }

                if(mComparte.getDescripcion() != null && !TextUtils.isEmpty(mComparte.getDescripcion())){
                    viewHolderComparteApp.txtDescripcion.setText(mComparte.getDescripcion());
                    viewHolderComparteApp.txtDescripcion.setVisibility(View.VISIBLE);
                }else{
                    viewHolderComparteApp.txtDescripcion.setVisibility(View.GONE);
                }

                if(mComparte.getImagen() != null && !TextUtils.isEmpty(mComparte.getImagen())){
                    Glide.with(context)
                            .load(RetrofitBuilder.urlImagenes + mComparte.getImagen())
                            .apply(opcionesGlide)
                            .into(viewHolderComparteApp.imgPortada);
                }else{
                    int resourceId = R.drawable.camaradefecto;
                    Glide.with(context)
                            .load(resourceId)
                            .apply(opcionesGlide)
                            .into(viewHolderComparteApp.imgPortada);
                }

                viewHolderComparteApp.setListener((view, position1) -> {
                    fragmentTabInicio.compartirAplicacion();
                });

                break;

            case ModeloVistasInicio.TIPO_INSIGNIAS:

                RecyclerInsigniasViewHolder viewHolderInsignias = (RecyclerInsigniasViewHolder) holder;

                viewHolderInsignias.txtToolbar.setText(context.getString(R.string.insignias));


                if(modeloInicioSeparador.getHayMasDe5Insignias() == 1){
                    viewHolderInsignias.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderInsignias.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderInsignias.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Insignias() == 1){
                        fragmentTabInicio.vistaTodosLasInsignias();
                    }
                });

                configurarRecyclerInsignias(viewHolderInsignias.recyclerViewInsignias, mVista.getModeloInicioInsignias());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modeloVistasInicios.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modeloVistasInicios.get(position).getTipoVista();
    }


    private void configurarRecyclerVideos(RecyclerView recyclerView, List<ModeloInicioVideos> modeloInicioVideos) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerVideos(context, modeloInicioVideos, fragmentTabInicio);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    private void configurarRecyclerImagenes(RecyclerView recyclerView, List<ModeloInicioImagenes> modeloInicioImagenes) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerImagenes(context, modeloInicioImagenes, fragmentTabInicio);

        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    private void configurarRecyclerInsignias(RecyclerView recyclerView, List<ModeloInicioInsignias> modeloInicioInsignias) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerInsignias(context, modeloInicioInsignias, fragmentTabInicio);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    // BLOQUE DEVOCIONAL
    private static class DevocionalViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCompartir;
        private ImageView imgOpciones;

        private WebView webView;

        DevocionalViewHolder(View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            imgCompartir = itemView.findViewById(R.id.imgShare);
            imgOpciones = itemView.findViewById(R.id.imgOpciones);
        }
    }


    // BLOQUE VIDEOS
    private static class RecyclerVideoViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewVideos;
        private TextView txtToolbar;

        private ImageView imgFlechaDerecha;

        RecyclerVideoViewHolder(View itemView) {
            super(itemView);
            recyclerViewVideos = itemView.findViewById(R.id.recyclerViewVideos);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
        }
    }


    // BLOQUE IMAGENES
    private static class RecyclerImagenesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewImagenes;
        private TextView txtToolbar;
        private ImageView imgFlechaDerecha;


        RecyclerImagenesViewHolder(View itemView) {
            super(itemView);
            recyclerViewImagenes = itemView.findViewById(R.id.recyclerViewImagenes);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
        }
    }


    // BLOQUE COMPARTE APP
    private static class ComparteAppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ShapeableImageView imgPortada;
        private TextView txtTitulo;
        private TextView txtDescripcion;

        private ImageView imgShare;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
            itemView.setOnClickListener(this);
        }


        ComparteAppViewHolder(View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.iconImageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            imgShare = itemView.findViewById(R.id.imgShare);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }


    // BLOQUE INSIGNIAS
    private static class RecyclerInsigniasViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewInsignias;
        private TextView txtToolbar;
        private ImageView imgFlechaDerecha;

        RecyclerInsigniasViewHolder(View itemView) {
            super(itemView);
            recyclerViewInsignias = itemView.findViewById(R.id.recyclerViewInsignias);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
        }
    }

}