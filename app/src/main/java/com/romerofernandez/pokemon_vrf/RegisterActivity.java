package com.romerofernandez.pokemon_vrf;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    // Declaramos la instancia de FirebaseAuth para manejar el registro de usuarios
    private FirebaseAuth mAuth;

    // Vistas de la interfaz
    private EditText emailEditText, passwordEditText;
    private Button signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); // Enlaza el archivo XML correspondiente a la actividad

        // Inicializa FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Enlaza las vistas definidas en el archivo XML con las variables en la clase
        emailEditText = findViewById(R.id.emailEditText); // Campo de texto para el email
        passwordEditText = findViewById(R.id.passwordEditText); // Campo de texto para la contraseña
        signUpButton = findViewById(R.id.signUpButton); // Botón para registrarse
        loginButton = findViewById(R.id.loginButton); // Botón para ir a la pantalla de inicio de sesión

        // Configura el botón de registro
        signUpButton.setOnClickListener(v -> registerUser()); // Llama al método `registerUser` al hacer clic

        // Configura el botón para redirigir al login
        loginButton.setOnClickListener(v -> {
            // Cambia a la actividad de inicio de sesión (`LoginActivity`)
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent); // Inicia la actividad
        });
    }

    // Método para registrar al usuario
    private void registerUser() {
        // Obtiene el email y la contraseña de los campos de texto
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Verifica si el campo de email está vacío
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Introduce un email", Toast.LENGTH_SHORT).show(); // Muestra un mensaje al usuario
            return; // Sale del método
        }

        // Verifica si el campo de contraseña está vacío
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Introduce una contraseña", Toast.LENGTH_SHORT).show(); // Muestra un mensaje al usuario
            return; // Sale del método
        }

        // Verifica que la contraseña tenga al menos 6 caracteres
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show(); // Mensaje de validación
            return; // Sale del método
        }

        // Llama a Firebase Authentication para crear un nuevo usuario con email y contraseña
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el registro es exitoso, muestra un mensaje y redirige al usuario a `MainActivity`
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent); // Inicia `MainActivity`
                        finish(); // Finaliza esta actividad para que no quede en el stack
                    } else {
                        // Si ocurre un error, muestra el mensaje correspondiente
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
