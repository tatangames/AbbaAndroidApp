package com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro;

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
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloGeneros;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloIglesias;

import java.util.List;

public class AdaptadorSpinnerGenero extends ArrayAdapter<ModeloGeneros> {

    private Context context;
    private boolean tipoTema;

    public AdaptadorSpinnerGenero(@NonNull Context context, int resource, @NonNull List<ModeloGeneros> modeloGeneros, boolean tipoTema) {
        super(context, 0, modeloGeneros);
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

        ModeloGeneros modeloGeneros = getItem(position);

        if(modeloGeneros != null){
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(modeloGeneros.getNombre());

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