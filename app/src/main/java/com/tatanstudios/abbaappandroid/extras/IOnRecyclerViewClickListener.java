package com.tatanstudios.abbaappandroid.extras;

import android.view.View;

public interface IOnRecyclerViewClickListener {

    // ESCUCHADOR DE CUANDO SE PRESIONA UN ITEM DENTRO DEL RECYCLERVIEW
    void onClick(View view, int position);

}
