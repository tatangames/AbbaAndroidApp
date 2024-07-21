package com.tatanstudios.abbaappandroid.fragmentos.menuprincipal;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.activity.biblia.CapitulosBibliaActivity;
import com.tatanstudios.abbaappandroid.adaptadores.biblia.AdaptadorBiblia;
import com.tatanstudios.abbaappandroid.network.ApiService;
import com.tatanstudios.abbaappandroid.network.RetrofitBuilder;
import com.tatanstudios.abbaappandroid.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentBiblia extends Fragment {

    private RecyclerView recyclerView;
    private RelativeLayout rootRelative;
    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtSinBiblias, txtToolbar;

    private AdaptadorBiblia adaptadorBiblia;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_biblia, container, false);

        //recyclerView = vista.findViewById(R.id.recyclerView);
        //txtSinBiblias = vista.findViewById(R.id.txtSinBiblias);
        rootRelative = vista.findViewById(R.id.rootRelative);
        //txtToolbar = vista.findViewById(R.id.txtToolbar);

       // txtToolbar.setText(getString(R.string.biblia));

        int colorProgress = ContextCompat.getColor(getContext(), R.color.barraProgreso);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        progressBar.setVisibility(View.GONE);

        //apiBuscarBiblias();



        /*


         <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <include
                android:id="@+id/include"
                layout="@layout/toolbar_titulo_v1"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"/>


            <ImageView
                android:layout_width="290dp"
                android:layout_height="311dp"
                android:layout_gravity="center"
                android:contentDescription="@string/logo"
                android:src="@drawable/obra" />

            <TextView
                android:id="@+id/txtSinBiblias"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:layout_marginEnd="16dp"
                android:gravity="center"
                android:layout_marginTop="30dp"
                android:text="@string/no_hay_biblias_disponibles"
                android:textColor="@color/fondo_textos_negro_blanco_v1"
                android:textSize="17sp"
                android:visibility="gone" />

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:visibility="gone"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:layout_constraintTop_toBottomOf="@+id/include" />

        </LinearLayout>

        * */




        return vista;
    }

    private void apiBuscarBiblias(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoBiblias(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            if(apiRespuesta.getHayinfo() == 1){

                                                adaptadorBiblia = new AdaptadorBiblia(getContext(), apiRespuesta.getModeloBiblias(), this);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(adaptadorBiblia);
                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinBiblias.setVisibility(View.VISIBLE);
                                            }

                                        }else{

                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {

                                    mensajeSinConexion();
                                })
        );
    }

    public void vistaCapitulos(int idbiblia){
        Intent intent = new Intent(getContext(), CapitulosBibliaActivity.class);
        intent.putExtra("IDBIBLIA", idbiblia);
        startActivity(intent);
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getContext(), getString(R.string.error_intentar_de_nuevo)).show();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }


}