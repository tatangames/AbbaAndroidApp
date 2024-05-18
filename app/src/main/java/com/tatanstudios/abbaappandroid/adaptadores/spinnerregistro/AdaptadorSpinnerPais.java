package com.tatanstudios.abbaappandroid.adaptadores.spinnerregistro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.modelos.iglesias.ModeloPais;

import java.util.List;

public class AdaptadorSpinnerPais extends ArrayAdapter<ModeloPais> {


    private Context context;
    private boolean tipoTema;

    public AdaptadorSpinnerPais(@NonNull Context context, int resource, @NonNull List<ModeloPais> modeloPais, boolean tipoTema) {
        super(context, 0, modeloPais);
        this.context = context;
        this.tipoTema = tipoTema;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, position == 0);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent, boolean isFirstItem) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_item_estilo, parent, false);
        }

        ModeloPais modeloPais = getItem(position);

        ImageView imgBandera = convertView.findViewById(R.id.imageView);

        switch (modeloPais.getId()) {
            case 0:
                imgBandera.setVisibility(View.GONE);
                break;
            case 1:
                imgBandera.setImageResource(R.drawable.flag_elsalvador);
                break;
            case 2:
                imgBandera.setImageResource(R.drawable.flag_guatemala);
                break;
            case 3:
                imgBandera.setImageResource(R.drawable.flag_honduras);
                break;
            case 4:
                imgBandera.setImageResource(R.drawable.flag_nicaragua);
                break;
            case 5:
                imgBandera.setImageResource(R.drawable.flag_mexico);
                break;
            case 6:
                imgBandera.setImageResource(R.drawable.localizacion);
                break;
            default:
                imgBandera.setVisibility(View.INVISIBLE);
                break;
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(modeloPais.getNombre());

        if(tipoTema){ // dark
            // Color del texto para el primer elemento
            int textColor = ContextCompat.getColor(context, R.color.blanco);
            textView.setTextColor(textColor);

        }else{
            // Color del texto para el primer elemento
            int textColor = isFirstItem ? ContextCompat.getColor(context, R.color.gris616161) : ContextCompat.getColor(context, R.color.negro);
            textView.setTextColor(textColor);
        }



        return convertView;
    }
}