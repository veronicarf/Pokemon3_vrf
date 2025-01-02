package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romerofernandez.pokemon_vrf.R;

import java.util.ArrayList;
import java.util.List;

import adapters.PokemonAdapter; // Asegúrate de tener un adaptador configurado
import models.Pokemon;
import models.PokemonResponse;
import network.ApiService;
import network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexFragment extends Fragment {

    // Variables para el RecyclerView, su adaptador y la lista de Pokémon
    private RecyclerView recyclerView; // Componente de la UI para mostrar la lista
    private PokemonAdapter adapter; // Adaptador para gestionar los datos de los Pokémon
    private List<Pokemon> pokemonList = new ArrayList<>(); // Lista que contiene los datos de los Pokémon

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el diseño del fragmento desde el archivo XML
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        // Configura el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPokedex); // Enlaza el RecyclerView del XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Establece un diseño lineal para la lista

        // Inicializa el adaptador con la lista de Pokémon vacía
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter); // Asigna el adaptador al RecyclerView

        // Llama a la función para cargar los Pokémon desde la API
        fetchPokemon();

        return view;
    }

    // Método para realizar la petición a la API de Pokémon
    private void fetchPokemon() {
        // Obtén la instancia de ApiService configurada por Retrofit
        ApiService apiService = RetrofitInstance.getApiService();

        // Realiza una petición GET a la API usando Retrofit
        apiService.getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                // Comprueba si la respuesta es exitosa y contiene datos
                if (response.isSuccessful() && response.body() != null) {
                    // Añade los Pokémon obtenidos a la lista
                    pokemonList.addAll(response.body().getResults());
                    // Notifica al adaptador que los datos han cambiado para actualizar el RecyclerView
                    adapter.notifyDataSetChanged();
                } else {
                    // Maneja el caso donde la respuesta no es exitosa (por ejemplo, error de servidor)
                    String errorMessage = getString(R.string.error_message, response.message());
                    System.err.println(errorMessage);                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                // Maneja los errores de conexión o problemas con la API
                t.printStackTrace();
            }
        });
    }
}

