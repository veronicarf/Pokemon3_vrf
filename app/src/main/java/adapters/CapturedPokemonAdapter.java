package adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.romerofernandez.pokemon_vrf.R;

import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;

import models.CapturedPokemon;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedPokemonViewHolder> {

    private List<CapturedPokemon> capturedPokemonList;

    // Constructor del adaptador
    public CapturedPokemonAdapter(List<CapturedPokemon> capturedPokemonList) {
        this.capturedPokemonList = capturedPokemonList;
    }

    @NonNull
    @Override
    public CapturedPokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del elemento para el RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_captured_pokemon, parent, false);
        return new CapturedPokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturedPokemonViewHolder holder, int position) {
        CapturedPokemon pokemon = capturedPokemonList.get(position);

        // Configura los datos del Pokémon capturado en las vistas
        holder.nameTextView.setText(pokemon.getName());
        holder.indexTextView.setText(holder.itemView.getContext().getString(R.string.index_placeholder, pokemon.getIndice()));
        holder.typesTextView.setText(holder.itemView.getContext().getString(R.string.types_placeholder, TextUtils.join(", ", pokemon.getTipo())));
        holder.heightTextView.setText(holder.itemView.getContext().getString(R.string.height_placeholder, pokemon.getAltura()));
        holder.weightTextView.setText(holder.itemView.getContext().getString(R.string.weight_placeholder, pokemon.getPeso()));

        // Carga la imagen del Pokémon usando Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getFoto())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }

    /**
     * Método para actualizar la lista del adaptador.
     * Este método se llamará cada vez que haya un cambio en la base de datos.
     */
    public void updateList(List<CapturedPokemon> newList) {
        capturedPokemonList.clear(); // Limpia la lista actual
        capturedPokemonList.addAll(newList); // Añade los nuevos datos
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
    }

    // Clase interna para representar los elementos del RecyclerView
    static class CapturedPokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, indexTextView, typesTextView, heightTextView, weightTextView;
        ImageView imageView;

        public CapturedPokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vincula las vistas del diseño con sus IDs
            nameTextView = itemView.findViewById(R.id.textViewPokemonName);
            indexTextView = itemView.findViewById(R.id.textViewPokemonIndex);
            typesTextView = itemView.findViewById(R.id.textViewPokemonTypes);
            heightTextView = itemView.findViewById(R.id.textViewPokemonHeight);
            weightTextView = itemView.findViewById(R.id.textViewPokemonWeight);
            imageView = itemView.findViewById(R.id.imageViewPokemon);
        }
    }
}
