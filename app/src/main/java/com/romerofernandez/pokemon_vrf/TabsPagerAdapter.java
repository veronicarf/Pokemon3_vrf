package com.romerofernandez.pokemon_vrf;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fragments.CapturedPokemonFragment;
import fragments.PokedexFragment;
import fragments.SettingsFragment;

public class TabsPagerAdapter extends FragmentStateAdapter {

    // Constructor: Se inicializa el adaptador con el contexto de la actividad principal que gestiona los fragmentos
    public TabsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Retorna el fragmento correspondiente según la posición de la pestaña
        switch (position) {
            case 0:
                // Fragmento de Pokémon capturados
                return new CapturedPokemonFragment();
            case 1:
                // Fragmento de la Pokédex
                return new PokedexFragment();
            case 2:
                // Fragmento de configuración (ajustes)
                return new SettingsFragment();
            default:
                // Por defecto, retorna el fragmento de Pokémon capturados
                return new CapturedPokemonFragment();
        }
    }

    @Override
    public int getItemCount() {
        // Número total de pestañas que se mostrarán
        return 3; // En este caso, tres pestañas
    }
}
