package models;

import java.util.List;

public class PokemonResponse {

    // Lista que contendrá los resultados de Pokémon obtenidos de la API
    private List<Pokemon> results;

    // Método getter para obtener la lista de resultados
    public List<Pokemon> getResults() {
        return results; // Devuelve la lista de Pokémon
    }

    // Método setter para establecer los resultados
    public void setResults(List<Pokemon> results) {
        this.results = results; // Asigna la lista de Pokémon al atributo 'results'
    }
}
