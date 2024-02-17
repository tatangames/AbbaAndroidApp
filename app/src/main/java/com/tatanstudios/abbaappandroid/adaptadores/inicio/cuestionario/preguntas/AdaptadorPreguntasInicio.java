package com.tatanstudios.abbaappandroid.adaptadores.inicio.cuestionario.preguntas;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.fragmentos.inicio.cuestionario.FragmentCuestionarioPreguntasInicioBloque;
import com.tatanstudios.abbaappandroid.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloPreguntas;
import com.tatanstudios.abbaappandroid.modelos.planes.cuestionario.preguntas.ModeloVistasPreguntas;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorPreguntasInicio extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context context;
    public ArrayList<ModeloVistasPreguntas> modeloVistasPreguntas;
    private FragmentCuestionarioPreguntasInicioBloque fragmentCuestionarioPreguntasInicioBloque;

    private String descripcionP = "";

    private ColorStateList colorStateListBlack, colorStateListWhite;
    private boolean temaActual;



    // obtiene la referencia del Text Input Layout
    private Map<Integer, TextInputLayout> txtInputMap = new HashMap<>();

    // obtiene la referencia de ID identificador
    private Map<Integer, Integer> txtInputMapRequerido = new HashMap<>();


    // obtener el texto de la pregunta para COMPARTIR
    private Map<Integer, String> hashMapTextoPregunta = new HashMap<>();

    private RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    int colorBlanco = 0;
    int colorNegro = 0;


    public AdaptadorPreguntasInicio(Context context, ArrayList<ModeloVistasPreguntas> modeloVistasPreguntas,
                                    FragmentCuestionarioPreguntasInicioBloque fragmentCuestionarioPreguntasInicioBloque, String descripcionP, boolean temaActual){
        this.context = context;
        this.fragmentCuestionarioPreguntasInicioBloque = fragmentCuestionarioPreguntasInicioBloque;
        this.modeloVistasPreguntas = modeloVistasPreguntas;
        this.temaActual = temaActual;
        this.descripcionP = descripcionP;

        colorBlanco = context.getColor(R.color.blanco);
        colorStateListWhite = ColorStateList.valueOf(colorBlanco);

        colorNegro = context.getColor(R.color.negro);
        colorStateListBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistasPreguntas.TIPO_IMAGEN) {
            View view = inflater.inflate(R.layout.cardview_bloque_portada_preguntas, parent, false);
            return new HolderVistaImagen(view);

        }
        else if (viewType == ModeloVistasPreguntas.TIPO_TITULOP) {
            View view = inflater.inflate(R.layout.cardview_titulo_principal_pregunta, parent, false);
            return new HolderVistaTitular(view);

        } else if (viewType == ModeloVistasPreguntas.TIPO_PREGUNTA) {
            View view = inflater.inflate(R.layout.cardview_bloque_pregunta, parent, false);
            return new HolderVistaBloquePregunta(view);
        } else if (viewType == ModeloVistasPreguntas.TIPO_BOTON) {
            View view = inflater.inflate(R.layout.cardview_btn_guardar_preguntas, parent, false);
            return new HolderVistaBotonGuardar(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistasPreguntas modelo = modeloVistasPreguntas.get(position);

        // TITULAR
        if (holder instanceof HolderVistaTitular) {


            if(descripcionP != null && !TextUtils.isEmpty(descripcionP)) {
                ((HolderVistaTitular) holder).webViewDescripcion.setWebViewClient(new WebViewClient());
                WebSettings webSettings = ((HolderVistaTitular) holder).webViewDescripcion.getSettings();
                webSettings.setJavaScriptEnabled(true);

                ((HolderVistaTitular) holder).webViewDescripcion.loadDataWithBaseURL(null, descripcionP, "text/html", "UTF-8", null);

                ((HolderVistaTitular) holder).webViewDescripcion.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        // sera color trasparente
                        ((HolderVistaTitular) holder).webViewDescripcion.setBackgroundColor(Color.TRANSPARENT);

                        if(temaActual){

                            String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                            ((HolderVistaTitular) holder).webViewDescripcion.evaluateJavascript(javascriptColor, null);
                        }else{
                            String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                            ((HolderVistaTitular) holder).webViewDescripcion.evaluateJavascript(javascriptColor, null);
                        }

                    }
                });
            }else{
                ((HolderVistaTitular) holder).webViewDescripcion.setVisibility(View.GONE);
            }

            ((HolderVistaTitular) holder).webViewDescripcion.setOnClickListener(v -> {
                fragmentCuestionarioPreguntasInicioBloque.redireccionarBiblia();
            });
        }

        // BLOQUE PREGUNTAS
        else if (holder instanceof HolderVistaBloquePregunta) {

            ModeloPreguntas m = modelo.getModeloPreguntas();

            ((HolderVistaBloquePregunta) holder).webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = ((HolderVistaBloquePregunta) holder).webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            ((HolderVistaBloquePregunta) holder).webView.loadDataWithBaseURL(null, m.getTitulo(), "text/html", "UTF-8", null);

            ((HolderVistaBloquePregunta) holder).webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    // Sera fondo transparente
                    ((HolderVistaBloquePregunta) holder).webView.setBackgroundColor(Color.TRANSPARENT);

                    if(temaActual){
                        String javascriptColor = String.format("document.body.style.color = '%s';", "white");
                        ((HolderVistaBloquePregunta) holder).webView.evaluateJavascript(javascriptColor, null);
                    }else{
                        String javascriptColor = String.format("document.body.style.color = '%s';", "black");
                        ((HolderVistaBloquePregunta) holder).webView.evaluateJavascript(javascriptColor, null);
                    }
                }
            });


            // EL INPUT EDIT TEXT TENDRA 5 MIL CARACTERES


            String textoSinHTML = HtmlCompat.fromHtml(m.getTitulo(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();


            hashMapTextoPregunta.put(m.getId(), textoSinHTML);

            txtInputMap.put(m.getId(), ((HolderVistaBloquePregunta) holder).txtInput);

            txtInputMapRequerido.put(m.getId(), m.getRequerido());

            // recupera y setea el texto de la pregunta
            if(m.getTexto() != null && !TextUtils.isEmpty(m.getTexto())){
                ((HolderVistaBloquePregunta) holder).txtEdt.setText(m.getTexto());
            }


            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + m.getImagen())
                        .apply(opcionesGlide)
                        .into(((HolderVistaBloquePregunta) holder).iconImageView);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaBloquePregunta) holder).iconImageView);
            }


            ((HolderVistaBloquePregunta) holder).txtEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(TextUtils.isEmpty(s)){
                        if(m.getRequerido() == 1){
                            String texto = context.getString(R.string.campo_requerido);
                            ((HolderVistaBloquePregunta) holder).txtInput.setError(texto);
                        }
                    }else{
                        ((HolderVistaBloquePregunta) holder).txtInput.setError(null);
                    }

                }
            });
        }

        // BOTONES GUARDAR Y COMPARTIR
        else if (holder instanceof HolderVistaBotonGuardar) {

            if(temaActual){ // dark
                ((HolderVistaBotonGuardar) holder).btnGuardar.setBackgroundTintList(colorStateListWhite);
                ((HolderVistaBotonGuardar) holder).btnGuardar.setTextColor(colorStateListBlack);

                ((HolderVistaBotonGuardar) holder).btnCompartir.setBackgroundTintList(colorStateListWhite);
                ((HolderVistaBotonGuardar) holder).btnCompartir.setTextColor(colorStateListBlack);
            }else{
                ((HolderVistaBotonGuardar) holder).btnGuardar.setBackgroundTintList(colorStateListBlack);
                ((HolderVistaBotonGuardar) holder).btnGuardar.setTextColor(colorStateListWhite);

                ((HolderVistaBotonGuardar) holder).btnCompartir.setBackgroundTintList(colorStateListBlack);
                ((HolderVistaBotonGuardar) holder).btnCompartir.setTextColor(colorStateListWhite);
            }

            if(fragmentCuestionarioPreguntasInicioBloque.isYaHabiaGuardado()){
                ((HolderVistaBotonGuardar) holder).btnGuardar.setText(context.getString(R.string.actualizar));
            }else{
                ((HolderVistaBotonGuardar) holder).btnGuardar.setText(context.getString(R.string.guardar));
            }

            ((HolderVistaBotonGuardar) holder).btnGuardar.setOnClickListener(v -> {
                fragmentCuestionarioPreguntasInicioBloque.verificarDatosActualizar();
            });

            ((HolderVistaBotonGuardar) holder).btnCompartir.setOnClickListener(v -> {
                fragmentCuestionarioPreguntasInicioBloque.compartirDatosPreguntas();
            });

        }
    }


    private static class HolderVistaImagen extends RecyclerView.ViewHolder{

        public HolderVistaImagen(@NonNull View itemView) {
            super(itemView);

        }
    }


    // HOLDER PARA VISTA TITULO
    private static class HolderVistaTitular extends RecyclerView.ViewHolder{

        private WebView webViewDescripcion;

        public HolderVistaTitular(@NonNull View itemView) {
            super(itemView);

            webViewDescripcion = itemView.findViewById(R.id.webView);
        }
    }


    private static class HolderVistaBloquePregunta extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private WebView webView;
        private TextInputLayout txtInput;
        private TextInputEditText txtEdt;

        private ImageView iconImageView;

        public HolderVistaBloquePregunta(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            webView = itemView.findViewById(R.id.webView);
            txtInput = itemView.findViewById(R.id.inputNombre);
            txtEdt = itemView.findViewById(R.id.edtNombre);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }


    private static class HolderVistaBotonGuardar extends RecyclerView.ViewHolder{

        private Button btnGuardar;
        private Button btnCompartir;

        public HolderVistaBotonGuardar(@NonNull View itemView) {
            super(itemView);

            btnGuardar = itemView.findViewById(R.id.btnGuardar);
            btnCompartir = itemView.findViewById(R.id.btnCompartir);
        }
    }


    @Override
    public int getItemCount() {
        if(modeloVistasPreguntas != null){
            return modeloVistasPreguntas.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistasPreguntas.get(position).getTipoVista();
    }


    public boolean getBoolFromEditText(int uniqueId) {

        if (txtInputMap.containsKey(uniqueId)) {

            TextInputLayout editTextLayout = txtInputMap.get(uniqueId);
            TextInputEditText editText = (TextInputEditText) editTextLayout.getEditText();

            if(editText != null){
                if(TextUtils.isEmpty(editText.getText().toString())){
                    // buscar clave
                    if (txtInputMapRequerido.containsKey(uniqueId)) {

                        // obtener el valor
                        if(txtInputMapRequerido.get(uniqueId) == 1){
                            editTextLayout.setError(context.getString(R.string.campo_requerido));
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }


    public String getTextoFromEditText(int uniqueId) {

        if (txtInputMap.containsKey(uniqueId)) {

            TextInputLayout editTextLayout = txtInputMap.get(uniqueId);
            TextInputEditText editText = (TextInputEditText) editTextLayout.getEditText();

            if(editText != null){
                return editText.getText().toString();
            }

            return "";
        } else {
            return "";
        }
    }

    public String getTextoPregunta(int uniqueId) {

        if (hashMapTextoPregunta.containsKey(uniqueId)) {

            String texto = hashMapTextoPregunta.get(uniqueId);
            if(texto != null && !TextUtils.isEmpty(texto)){
                return texto;
            }else{
                return "";
            }
        } else {
            return "";
        }
    }

}