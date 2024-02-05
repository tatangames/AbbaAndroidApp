package com.tatanstudios.abbaappandroid.adaptadores.planes.misplanes.cuestionario;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.tatanstudios.abbaappandroid.R;

public class AdaptadorSpinnerTipoLetraCuestionario extends ArrayAdapter<String> {

    private Context context;
    private boolean tema;

    public AdaptadorSpinnerTipoLetraCuestionario(Context context, int resource, boolean tema) {
        super(context, resource);
        this.context = context;
        this.tema = tema;
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
            convertView = inflater.inflate(R.layout.cardview_spinner_cuestionario_tipo_letra, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        if(tema){ // dark
            // Color del texto para el primer elemento
            int textColor = ContextCompat.getColor(context, R.color.blanco);
            textView.setTextColor(textColor);

        }else{
            // Color del texto para el primer elemento
            int textColor = ContextCompat.getColor(context, R.color.negro);
            textView.setTextColor(textColor);
        }


        // TIPOS DE LETRA

        switch (position){
            case 0:
                // Sin Formato
                textView.setTypeface(null , Typeface.NORMAL);
                break;
            case 1:
                // Noto sans condensed medium
                final Typeface typeface2 = ResourcesCompat.getFont(context, R.font.notosans_condensed_medium);
                textView.setTypeface(typeface2);
                break;
            case 2:
                // Noto sans light
                final Typeface typeface3 = ResourcesCompat.getFont(context, R.font.notosans_light);
                textView.setTypeface(typeface3);
                break;
            case 3:
                // Time new roman
                final Typeface typeface4 = ResourcesCompat.getFont(context, R.font.times_new_normal_regular);
                textView.setTypeface(typeface4);
                break;
            default:
                textView.setTypeface(null , Typeface.NORMAL);
                break;
        }

        return convertView;
    }
}