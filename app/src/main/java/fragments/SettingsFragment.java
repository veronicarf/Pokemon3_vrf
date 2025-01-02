package fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.romerofernandez.pokemon_vrf.LoginActivity;
import com.romerofernandez.pokemon_vrf.MainActivity;
import com.romerofernandez.pokemon_vrf.R;

public class SettingsFragment extends Fragment {

    // Constantes para el nombre del archivo de preferencias y la clave específica del switch
    private static final String PREFS_NAME = "settings_preferences"; // Nombre del archivo SharedPreferences
    private static final String KEY_DELETE_POKEMON = "delete_pokemon"; // Clave para almacenar el estado del switch

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout correspondiente al fragmento
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializa FirebaseAuth para manejar el cierre de sesión
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Enlaza las vistas del diseño
        Switch deletePokemonSwitch = view.findViewById(R.id.switch_delete_pokemon); // Switch para activar o desactivar la eliminación de Pokémon
        Button changeLanguageButton = view.findViewById(R.id.button_change_language); // Botón para cambiar el idioma
        Button aboutButton = view.findViewById(R.id.button_about); // Botón "Acerca de"
        Button logoutButton = view.findViewById(R.id.button_logout); // Botón para cerrar sesión

        // Configuración de SharedPreferences para almacenar las preferencias del usuario
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Configuración inicial del Switch para eliminar Pokémon
        deletePokemonSwitch.setChecked(preferences.getBoolean(KEY_DELETE_POKEMON, false)); // Recupera el estado almacenado del switch
        deletePokemonSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guarda el estado del switch en SharedPreferences cuando cambia
            editor.putBoolean(KEY_DELETE_POKEMON, isChecked);
            editor.apply();

            if (isChecked) {
                deleteAllCapturedPokemon(); // Llama al método para eliminar los Pokémon
            }
        });

        // Configurar el botón para mostrar el diálogo de selección de idioma
        changeLanguageButton.setOnClickListener(v -> showLanguageSelectionDialog());

        // Configurar el botón "Acerca de"
        aboutButton.setOnClickListener(v -> showAboutDialog());

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Cierra la sesión del usuario en Firebase
            Intent intent = new Intent(getActivity(), LoginActivity.class); // Redirige a la pantalla de inicio de sesión
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el stack de actividades
            startActivity(intent);
        });

        return view; // Devuelve la vista inflada
    }

    /**
     * Método para eliminar todos los Pokémon capturados en Firebase Firestore.
     */
    private void deleteAllCapturedPokemon() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("captured_pokemons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    WriteBatch batch = db.batch(); // Usar un batch para operaciones en masa
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        batch.delete(document.getReference());
                    }
                    batch.commit().addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), R.string.pokemon_deleted, Toast.LENGTH_SHORT).show();
                        notifyCapturedPokemonFragment(); // Notifica al fragmento de CapturedPokemonFragment
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), R.string.error_deleting_pokemon, Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Notifica al CapturedPokemonFragment para que actualice su lista.
     */
    private void notifyCapturedPokemonFragment() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).notifyCapturedPokemonFragment();
        }
    }

    // Método para mostrar el diálogo de selección de idioma
    private void showLanguageSelectionDialog() {
        String[] languages = {"Español", "English"}; // Opciones de idiomas
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.cambiar_idioma) // Título del diálogo
                .setItems(languages, (dialog, which) -> {
                    // Cambia el idioma según la selección del usuario
                    String languageCode = (which == 0) ? "es" : "en"; // Código de idioma
                    setLocale(languageCode); // Aplica el idioma seleccionado
                })
                .show(); // Muestra el diálogo
    }

    // Método para cambiar el idioma de la aplicación
    private void setLocale(String languageCode) {
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(new java.util.Locale(languageCode)); // Cambia la configuración local
        getResources().updateConfiguration(config, getResources().getDisplayMetrics()); // Aplica los cambios

        // Reinicia la actividad para aplicar los cambios de idioma
        Intent refresh = new Intent(getActivity(), MainActivity.class); // Redirige a la actividad principal
        startActivity(refresh);
        requireActivity().finish(); // Finaliza la actividad actual
    }

    // Método para mostrar el diálogo "Acerca de"
    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.acerca_de) // Título del diálogo
                .setMessage(getString(R.string.desarrollador) + "\n" + getString(R.string.version)) // Mensaje con el nombre del desarrollador y versión
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss()) // Botón "OK" para cerrar el diálogo
                .setIcon(android.R.drawable.ic_dialog_info) // Ícono informativo
                .show(); // Muestra el diálogo
    }
}
