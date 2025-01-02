package com.romerofernandez.pokemon_vrf;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fragments.CapturedPokemonFragment;

public class MainActivity extends AppCompatActivity {

    // Instancia de FirebaseAuth para manejar la autenticación del usuario
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verifica si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Si no hay un usuario autenticado, redirige a la pantalla de inicio de sesión (LoginActivity)
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finaliza esta actividad para que no quede en el stack
            return; // Salir de la función para evitar ejecutar el resto del código
        }

        // Configura el diseño de la actividad principal
        setContentView(R.layout.activity_main);

        // Configura las barras del sistema (superior e inferior) para evitar superposición con el contenido
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Ajusta los márgenes según las barras del sistema
            return insets;
        });

        // Vincula el TabLayout (pestañas) y el ViewPager2 (contenedor de vistas)
        TabLayout tabLayout = findViewById(R.id.tab_layout); // TabLayout para mostrar las pestañas
        ViewPager2 viewPager = findViewById(R.id.view_pager); // ViewPager2 para manejar las vistas en cada pestaña

        // Configura el adaptador para el ViewPager2
        TabsPagerAdapter adapter = new TabsPagerAdapter(this); // Crea un adaptador para manejar las vistas
        viewPager.setAdapter(adapter); // Asigna el adaptador al ViewPager2

        // Vincula el TabLayout con el ViewPager2 para que trabajen juntos
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Configura cada pestaña según su posición
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.mis_pokemon)); // Título de la pestaña
                    tab.setIcon(R.drawable.ic_captured_pokemon); // Icono para la pestaña
                    break;
                case 1:
                    tab.setText(getString(R.string.pokedex)); // Título de la pestaña
                    tab.setIcon(R.drawable.ic_pokedex); // Icono para la pestaña
                    break;
                case 2:
                    tab.setText(getString(R.string.ajustes)); // Título de la pestaña
                    tab.setIcon(R.drawable.ic_settings); // Icono para la pestaña
                    break;
            }
        }).attach(); // Adjunta el TabLayout al ViewPager2
    }
    public void notifyCapturedPokemonFragment() {
        // Busca el fragmento CapturedPokemonFragment y llama a su método refreshCapturedPokemon()
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main); // Reemplaza 'your_fragment_container_id' con el ID del contenedor del fragmento
        if (fragment instanceof CapturedPokemonFragment) {
            ((CapturedPokemonFragment) fragment).refreshCapturedPokemon();
        }
    }

}
