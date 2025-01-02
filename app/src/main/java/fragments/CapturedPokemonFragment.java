package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.romerofernandez.pokemon_vrf.R;

import java.util.ArrayList;
import java.util.List;

import adapters.CapturedPokemonAdapter;
import models.CapturedPokemon;

public class CapturedPokemonFragment extends Fragment {

    private RecyclerView recyclerView; // RecyclerView para mostrar la lista de Pokémon capturados
    private CapturedPokemonAdapter adapter; // Adaptador para manejar los elementos del RecyclerView
    private List<CapturedPokemon> capturedPokemonList = new ArrayList<>(); // Lista local de Pokémon capturados
    private FirebaseFirestore db; // Referencia a la base de datos Firestore

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_captured_pokemon, container, false);

        // Configura el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_captured_pokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configura el adaptador y lo enlaza al RecyclerView
        adapter = new CapturedPokemonAdapter(capturedPokemonList);
        recyclerView.setAdapter(adapter);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Configura el listener en tiempo real
        setupRealtimeListener();

        return view;
    }

    /**
     * Método para obtener los Pokémon capturados de Firebase Firestore y actualizar la lista local.
     */
    private void setupRealtimeListener() {
        db.collection("captured_pokemons")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        // Muestra un mensaje de error en caso de fallo
                        Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        // Actualiza la lista con los datos obtenidos
                        List<CapturedPokemon> updatedList = querySnapshot.toObjects(CapturedPokemon.class);
                        adapter.updateList(updatedList); // Asegura que tu adaptador tiene este método
                    }
                });
    }
    public void refreshCapturedPokemon() {
        db.collection("captured_pokemons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CapturedPokemon> updatedList = queryDocumentSnapshots.toObjects(CapturedPokemon.class);
                    adapter.updateList(updatedList); // Asegúrate de que el adaptador tiene este método
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show());
    }

}
