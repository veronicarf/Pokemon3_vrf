package com.romerofernandez.pokemon_vrf;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123; // Código de solicitud para Google Sign-In
    private FirebaseAuth mAuth;

    // Variables para Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;

    // Vistas para correo/contraseña y Google Sign-In
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton, googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ID del cliente web de Firebase
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Enlazar vistas para correo/contraseña
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.signUpButton);

        // Enlazar vista para Google Sign-In
        googleSignInButton = findViewById(R.id._google);

        // Listener para iniciar sesión con correo/contraseña
        loginButton.setOnClickListener(v -> loginUser());

        // Listener para redirigir al registro
        registerButton.setOnClickListener(v -> registerUser());

        // Listener para iniciar sesión con Google
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    // Iniciar sesión con correo/contraseña
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showErrorDialog(getString(R.string.toast_enter_email));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showErrorDialog(getString(R.string.toast_enter_password));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso con correo
                        Toast.makeText(this, getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        // Error de inicio de sesión
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : getString(R.string.toast_login_failed_generic);
                        showErrorDialog(errorMessage);
                    }
                });
    }

    // Registrar un nuevo usuario con correo/contraseña
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showErrorDialog(getString(R.string.toast_enter_email));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showErrorDialog(getString(R.string.toast_enter_password));
            return;
        }

        if (password.length() < 6) {
            showErrorDialog(getString(R.string.password_too_short));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : getString(R.string.toast_login_failed_generic);
                        showErrorDialog(errorMessage);
                    }
                });
    }

    // Iniciar sesión con Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Manejar resultado de Google Sign-In
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                showErrorDialog("Google Sign-In failed: " + e.getMessage());
            }
        }
    }

    // Autenticar en Firebase usando cuenta de Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión con Google exitoso
                        Toast.makeText(this, getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        // Error al autenticar con Firebase
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : getString(R.string.toast_login_failed_generic);
                        showErrorDialog(errorMessage);
                    }
                });
    }

    // Navegar a MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Mostrar un diálogo de error
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.error_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
