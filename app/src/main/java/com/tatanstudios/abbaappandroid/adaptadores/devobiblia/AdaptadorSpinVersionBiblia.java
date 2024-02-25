package com.tatanstudios.abbaappandroid.adaptadores.devobiblia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.modelos.devocapitulos.ModeloVersiones;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloGeneros;

import java.util.List;

public class AdaptadorSpinVersionBiblia extends ArrayAdapter<ModeloVersiones> {

    private Context context;
    private boolean tipoTema;

    public AdaptadorSpinVersionBiblia(@NonNull Context context, @NonNull List<ModeloVersiones> modeloVersiones, boolean tipoTema) {
        super(context, 0, modeloVersiones);
        this.context = context;
        this.tipoTema = tipoTema;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_item_estilo_v2, parent, false);
        }

        ModeloVersiones modelo = getItem(position);

        if(modelo != null){
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(modelo.getTitulo());

            if(tipoTema){ // dark
                // Color del texto para el primer elemento
                int textColor = ContextCompat.getColor(context, R.color.blanco);
                textView.setTextColor(textColor);
            }else{
                // Color del texto para el primer elemento
                int textColor = ContextCompat.getColor(context, R.color.negro);
                textView.setTextColor(textColor);
            }
        }

        return convertView;
    }
}