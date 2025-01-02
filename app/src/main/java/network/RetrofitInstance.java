package network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    private static Retrofit retrofit;

    // Devuelve una instancia única de Retrofit
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Devuelve el ApiService que usaremos para las peticiones
    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
