package models;

import java.util.List;

public class PokemonDetailsResponse {
    private int height;
    private int weight;
    private List<TypeWrapper> types;

    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public List<TypeWrapper> getTypes() { return types; }

    public static class TypeWrapper {
        private Type type;

        public Type getType() { return type; }

        public static class Type {
            private String name;

            public String getName() { return name; }
        }
    }
}
