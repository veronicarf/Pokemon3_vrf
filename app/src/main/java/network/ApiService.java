package network;

import models.PokemonDetailsResponse;
import models.PokemonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Método existente para obtener la lista de Pokémon
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    // Método para obtener los detalles de un Pokémon específico
    @GET("pokemon/{id}")
    Call<PokemonDetailsResponse> getPokemonDetails(
            @Path("id") int id
    );
}
