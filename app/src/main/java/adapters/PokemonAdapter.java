package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.romerofernandez.pokemon_vrf.R;
import java.util.ArrayList;
import java.util.List;
import models.CapturedPokemon;
import models.Pokemon;
import models.PokemonDetailsResponse;
import network.ApiService;
import network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Clase adaptador para el RecyclerView que muestra la lista de Pokémon
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    // Lista de Pokémon que se pasará al adaptador
    private List<Pokemon> pokemonList;

    // Constructor para inicializar la lista de Pokémon
    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    // Crea nuevos ViewHolders cuando el RecyclerView lo necesite
    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada elemento del RecyclerView (item_pokemon.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view); // Devuelve el ViewHolder asociado al diseño
    }

    // Vincula los datos de un Pokémon a un ViewHolder en una posición específica
    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        // Configura el TextView con el nombre del Pokémon
        holder.nameTextView.setText(pokemon.getName());

        // Construye la URL de la imagen del Pokémon
        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + (position + 1) + ".png";

        // Usa Glide para cargar la imagen en el ImageView
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageView);

        // Configura el clic en el elemento para capturar Pokémon
        holder.itemView.setOnClickListener(v -> {
            // Realiza una petición a la API para obtener detalles del Pokémon
            ApiService apiService = RetrofitInstance.getApiService();
            apiService.getPokemonDetails(position + 1).enqueue(new Callback<PokemonDetailsResponse>() {
                @Override
                public void onResponse(Call<PokemonDetailsResponse> call, Response<PokemonDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Obtiene los detalles del Pokémon de la respuesta
                        PokemonDetailsResponse details = response.body();

                        // Convierte los tipos del Pokémon a una lista de strings
                        List<String> types = new ArrayList<>();
                        for (PokemonDetailsResponse.TypeWrapper type : details.getTypes()) {
                            types.add(type.getType().getName());
                        }

                        // Crear un objeto CapturedPokemon con los datos reales
                        CapturedPokemon capturedPokemon = new CapturedPokemon(
                                pokemon.getName(),
                                position + 1, // Índice en la Pokédex
                                imageUrl,
                                types,
                                details.getHeight() / 10.0, // Altura en metros
                                details.getWeight() / 10.0  // Peso en kilogramos
                        );

                        // Guardar el Pokémon capturado en Firebase Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("captured_pokemons")
                                .add(capturedPokemon)
                                .addOnSuccessListener(documentReference ->
                                        Toast.makeText(holder.itemView.getContext(), R.string.pokemon_captured, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(holder.itemView.getContext(), R.string.error_capturing_pokemon, Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onFailure(Call<PokemonDetailsResponse> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), R.string.error_fetching_pokemon_details, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    // Devuelve el número total de elementos en la lista
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // Clase interna para representar cada elemento (ViewHolder) en el RecyclerView
    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        // Referencias a las vistas del diseño (item_pokemon.xml)
        TextView nameTextView; // TextView para el nombre del Pokémon
        ImageView imageView;  // ImageView para la imagen del Pokémon

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vincula las vistas del diseño con sus ID
            nameTextView = itemView.findViewById(R.id.textViewPokemonName);
            imageView = itemView.findViewById(R.id.imageViewPokemon);
        }
    }
}
