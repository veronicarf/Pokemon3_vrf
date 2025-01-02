package models;

public class Pokemon {

    // Nombre del Pokémon
    private String name;

    // URL asociada al Pokémon (proporcionada por la API)
    private String url;

    // Getter para obtener el nombre del Pokémon
    public String getName() {
        return name;
    }

    // Setter para establecer el nombre del Pokémon
    public void setName(String name) {
        this.name = name;
    }

    // Getter para obtener la URL del Pokémon
    public String getUrl() {
        return url;
    }

    // Setter para establecer la URL del Pokémon
    public void setUrl(String url) {
        this.url = url;
    }
}
