package com.tatanstudios.abbaappandroid.activity.principal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tatanstudios.abbaappandroid.R;
import com.tatanstudios.abbaappandroid.extras.InterfaceActualizarTema;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentBiblia;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentInicio;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentAjustes;
import com.tatanstudios.abbaappandroid.fragmentos.menuprincipal.FragmentPlanes;

public class PrincipalActivity extends AppCompatActivity implements InterfaceActualizarTema {

    public BottomNavigationView bottomNavigationView;

    private MenuItem menuInicio;
    private MenuItem menuBiblia;
    private MenuItem menuPlanes;
    private MenuItem menuMas;


    private Fragment fragmentInicio = new FragmentInicio();
    private  Fragment fragmentBiblia = new FragmentBiblia();
    private  Fragment fragmentPlanes = new FragmentPlanes();

    private  Fragment fragmentAjustes = new FragmentAjustes();
    private FragmentManager fm = getSupportFragmentManager();

    private Fragment fragmentActivo = fragmentInicio;


    private boolean boolRecargarFragmentInicio;
    private boolean boolRecargarFragmentBiblia;
    private boolean boolRecargarFragmentPlanes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setBackground(null);

        Menu menu = bottomNavigationView.getMenu();

        menuInicio = menu.findItem(R.id.menu_inicio);
        menuBiblia = menu.findItem(R.id.menu_biblia);
        menuPlanes = menu.findItem(R.id.menu_planes);
        menuMas = menu.findItem(R.id.menu_mas);

        boolRecargarFragmentInicio = false;
        boolRecargarFragmentBiblia = false;
        boolRecargarFragmentPlanes = false;

        fm.beginTransaction().add(R.id.main_container, fragmentAjustes, "4").hide(fragmentAjustes).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentBiblia, "3").hide(fragmentBiblia).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentPlanes, "2").hide(fragmentPlanes).commit();
        fm.beginTransaction().add(R.id.main_container,fragmentInicio, "1").commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.menu_inicio) {

                cambioMenuInicio();
                if(boolRecargarFragmentInicio){
                    recargarFragmentInicio();
                }

            } else if (itemId == R.id.menu_biblia) {
                cambioMenuBiblia();
                if(boolRecargarFragmentBiblia){
                    recargarFragmentBiblia();
                }

            } else if (itemId == R.id.menu_planes) {
                cambioMenuPlanes();
                //if(boolRecargarFragmentPlanes){
                    recargarFragmentPlanes();
                //}

            }
            else if (itemId == R.id.menu_mas) {
                cambioMenuMas();


            }

            return true;
        });
    }



    private void cambioMenuInicio(){



        menuInicio.setIcon(R.drawable.vector_iglesia_lleno);

        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);

        fm.beginTransaction().hide(fragmentActivo).show(fragmentInicio).commit();
        fragmentActivo = fragmentInicio;

    }

    private void cambioMenuBiblia(){


        menuBiblia.setIcon(R.drawable.vector_biblia_lleno);

        menuInicio.setIcon(R.drawable.vector_iglesia_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);

        fm.beginTransaction().hide(fragmentActivo).show(fragmentBiblia).commit();
        fragmentActivo = fragmentBiblia;

    }

    private void cambioMenuPlanes(){


        menuPlanes.setIcon(R.drawable.vector_planes_lleno);

        menuInicio.setIcon(R.drawable.vector_iglesia_linea);
        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);

        fm.beginTransaction().hide(fragmentActivo).show(fragmentPlanes).commit();
        fragmentActivo = fragmentPlanes;

    }

    private void cambioMenuMas(){

        menuMas.setIcon(R.drawable.vector_tuerca_lleno);

        menuInicio.setIcon(R.drawable.vector_iglesia_linea);
        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);

        fm.beginTransaction().hide(fragmentActivo).show(fragmentAjustes).commit();
        fragmentActivo = fragmentAjustes;
    }



    @Override
    public void onFragmentInteraction(int tipoTema) {
        if(tipoTema == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        boolRecargarFragmentInicio = true;
        boolRecargarFragmentBiblia = true;
        boolRecargarFragmentPlanes = true;
        recargarFragmentAjustes();
    }

    private void recargarFragmentAjustes(){

        // Obtén el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Comienza una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Si el fragmento ya existe, elimínalo
        if (fragmentAjustes != null) {
            fragmentTransaction.remove(fragmentAjustes);
        }

        // Crea una nueva instancia del fragmento
        fragmentAjustes = new FragmentAjustes();

        // Añade el fragmento al contenedor
        fragmentTransaction.add(R.id.main_container, fragmentAjustes);

        // Oculta el fragmento anterior si es necesario
        if (fragmentActivo != null) {
            fragmentTransaction.hide(fragmentActivo);
        }

        // Muestra el nuevo fragmento
        fragmentTransaction.show(fragmentAjustes);

        // Realiza la transacción
        fragmentTransaction.commit();

        // Actualiza el fragmento activo
        fragmentActivo = fragmentAjustes;
    }

    private void recargarFragmentInicio(){

        boolRecargarFragmentInicio = false;

        // Obtén el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Comienza una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Si el fragmento ya existe, elimínalo
        if (fragmentInicio != null) {
            fragmentTransaction.remove(fragmentInicio);
        }

        // Crea una nueva instancia del fragmento
        fragmentInicio = new FragmentInicio();

        // Añade el fragmento al contenedor
        fragmentTransaction.add(R.id.main_container, fragmentInicio);

        // Oculta el fragmento anterior si es necesario
        if (fragmentActivo != null) {
            fragmentTransaction.hide(fragmentActivo);
        }

        // Muestra el nuevo fragmento
        fragmentTransaction.show(fragmentInicio);

        // Realiza la transacción
        fragmentTransaction.commit();

        // Actualiza el fragmento activo
        fragmentActivo = fragmentInicio;
    }


    private void recargarFragmentBiblia(){

        boolRecargarFragmentBiblia = false;

        // Obtén el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Comienza una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Si el fragmento ya existe, elimínalo
        if (fragmentBiblia != null) {
            fragmentTransaction.remove(fragmentBiblia);
        }

        // Crea una nueva instancia del fragmento
        fragmentBiblia = new FragmentBiblia();

        // Añade el fragmento al contenedor
        fragmentTransaction.add(R.id.main_container, fragmentBiblia);

        // Oculta el fragmento anterior si es necesario
        if (fragmentActivo != null) {
            fragmentTransaction.hide(fragmentBiblia);
        }

        // Muestra el nuevo fragmento
        fragmentTransaction.show(fragmentBiblia);

        // Realiza la transacción
        fragmentTransaction.commit();

        // Actualiza el fragmento activo
        fragmentActivo = fragmentBiblia;
    }


    private void recargarFragmentPlanes(){

        boolRecargarFragmentPlanes = false;

        // Obtén el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Comienza una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Si el fragmento ya existe, elimínalo
        if (fragmentPlanes != null) {
            fragmentTransaction.remove(fragmentPlanes);
        }

        // Crea una nueva instancia del fragmento
        fragmentPlanes = new FragmentPlanes();

        // Añade el fragmento al contenedor
        fragmentTransaction.add(R.id.main_container, fragmentPlanes);

        // Oculta el fragmento anterior si es necesario
        if (fragmentActivo != null) {
            fragmentTransaction.hide(fragmentPlanes);
        }

        // Muestra el nuevo fragmento
        fragmentTransaction.show(fragmentPlanes);

        // Realiza la transacción
        fragmentTransaction.commit();

        // Actualiza el fragmento activo
        fragmentActivo = fragmentPlanes;
    }


    private static final int REQUEST_CODE_COMPARTIR = 123;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMPARTIR) {
            // Pasa el resultado de vuelta al fragmento
            fragmentInicio.onActivityResult(requestCode, resultCode, data);
        }
    }





}