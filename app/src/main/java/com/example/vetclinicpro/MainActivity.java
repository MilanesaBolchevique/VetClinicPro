package com.example.vetclinicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    Inicio inicio;
    Empleados empleados;
    Clientes clientes;
    Citaciones citaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        inicio = new Inicio();
        empleados = new Empleados();
        clientes = new Clientes();
        citaciones = new Citaciones();

        //Cargamos siempre el fragment de inicio para que no aparezca el main activity vacio
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, inicio).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.inicio) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, inicio).commit();
                } else if (item.getItemId() == R.id.citaciones) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, citaciones).commit();
                } else if (item.getItemId() == R.id.clientes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, clientes).commit();
                } else if (item.getItemId() == R.id.trabajadores) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, empleados).commit();
                }
                return true;
            }
        });
    }
}