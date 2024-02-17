package com.tatanstudios.abbaappandroid.adaptadores.comunidad.planes;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.adaptadores.inicio.cuestionario.preguntas.AdaptadorPreguntasInicio;
import com.tatanstudios.abbaappandroid.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesItemsAmigos;
import com.tatanstudios.abbaappandroid.fragmentos.comunidad.FragmentPlanesItemsPreAmigos;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.misplanes.ModeloMisPlanes;

import java.util.List;

public class AdaptadorPlanesItemsPreAmigos extends RecyclerView.Adapter<AdaptadorPlanesItemsPreAmigos.ViewHolder> {

    private List<ModeloPreguntas> modeloPreguntas;
    private Context context;

    private FragmentPlanesItemsPreAmigos fragmentPlanesItemsPreAmigos;

    private boolean temaActual;

    public AdaptadorPlanesItemsPreAmigos(Context context, List<ModeloPreguntas> modeloPreguntas,
                                         FragmentPlanesItemsPreAmigos fragmentPlanesItemsPreAmigos, boolean temaActual) {
        this.context = context;
        this.modeloPreguntas = modeloPreguntas;
        this.fragmentPlanesItemsPreAmigos = fragmentPlanesItemsPreAmigos;
        this.temaActual = temaActual;
    }

    @NonNull
    @Override
    public AdaptadorPlanesItemsPreAmigos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_planesitems_pre_amigos, parent, false);
        return new AdaptadorPlanesItemsPreAmigos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesItemsPreAmigos.ViewHolder holder, int position) {

        ModeloPreguntas currentItem = modeloPreguntas.get(position);



        holder.webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = holder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        holder.webView.loadDataWithBaseURL(null, currentItem.getTitulo(), "text/html", "UTF-8", null);

        holder.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Sera fondo transparente
                holder.webView.setBackgroundColor(Color.TRANSPARENT);

                if(temaActual){
                    String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                    holder.webView.evaluateJavascript(javascriptColor, null);
                }else{
                    String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                    holder.webView.evaluateJavascript(javascriptColor, null);
                }
            }
        });





        if(currentItem.getRespuesta() != null && !TextUtils.isEmpty(currentItem.getRespuesta())){
            holder.txtRespuesta.setText(currentItem.getRespuesta());
        }


    }




    @Override
    public int getItemCount() {
        if(modeloPreguntas != null){
            return modeloPreguntas.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtRespuesta;
        private WebView webView;

        ViewHolder(View itemView) {
            super(itemView);
            txtRespuesta = itemView.findViewById(R.id.txtRespuesta);
            webView = itemView.findViewById(R.id.webView);
        }

    }
}