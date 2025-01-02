package models;

import java.util.List;

public class CapturedPokemon {
    private String name;       // Nombre del Pokémon
    private int indice;        // Índice en la Pokédex
    private String foto;       // URL de la foto
    private List<String> tipo; // Lista de tipos del Pokémon
    private double altura;     // Altura del Pokémon
    private double peso;       // Peso del Pokémon

    public CapturedPokemon() {
        // Constructor vacío necesario para Firebase
    }

    public CapturedPokemon(String name, int indice, String foto, List<String> tipo, double altura, double peso) {
        this.name = name;
        this.indice = indice;
        this.foto = foto;
        this.tipo = tipo;
        this.altura = altura;
        this.peso = peso;
    }

    // Getters y setters necesarios para Firestore
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getIndice() { return indice; }
    public void setIndice(int indice) { this.indice = indice; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public List<String> getTipo() { return tipo; }
    public void setTipo(List<String> tipo) { this.tipo = tipo; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
}

