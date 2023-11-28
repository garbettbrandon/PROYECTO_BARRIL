package com.example.barril.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.barril.R;
import com.example.barril.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {
    private static String ERROR = "Error";
    private static String ACEPTAR = "Aceptar";
    private static String MENSAJE_NO_AUTORIZADO = "El inicio de sesión no está autorizado";


    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    TextView idNombre, idApellido, idMail, idRegistroContrasenia, idErrorContrasenia, idReRegistroContrasenia;
    Button idAcceder, idIniciar;
    String userEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_registrarse);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        idNombre = findViewById(R.id.idNombre);
        idApellido = findViewById(R.id.idApellido);
        idMail = findViewById(R.id.idMail);
        idRegistroContrasenia= findViewById(R.id.idRegistroContrasenia);

        idAcceder= findViewById(R.id.idAcceder);
        idIniciar= findViewById(R.id.idIniciar);

        idReRegistroContrasenia = findViewById(R.id.idRegistroReContrasenia);
        idErrorContrasenia = findViewById(R.id.errorContrasenia);

        idErrorContrasenia.setVisibility(View.INVISIBLE);


        idIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registrarse.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });

        setTitle("Registrarse");
        idAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contrasenia = idRegistroContrasenia.getText().toString();
                String confirmacionContrasenia = idReRegistroContrasenia.getText().toString();

                if (!contrasenia.equals(confirmacionContrasenia)) {
                    // Mostrar mensaje de error si las contraseñas no coinciden
                    idErrorContrasenia.setVisibility(View.VISIBLE);

                    // Deshabilitar el botón de acceso si las contraseñas no coinciden

                } else {
                    // Ocultar el mensaje de error si las contraseñas coinciden
                    idErrorContrasenia.setVisibility(View.INVISIBLE);

                    if(!idMail.getText().toString().isEmpty() && !idRegistroContrasenia.getText().toString().isEmpty()){
                        registrarUsuario();
                    }
                }
            }
        });
    }
    //este metodo se encarga de registrar el usuario en firebase Auth
    private void registrarUsuario(){
        mAuth.getInstance().createUserWithEmailAndPassword(idMail.getText().toString(), idRegistroContrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // El inicio de sesión fue exitoso
                    userEmail = task.getResult().getUser().getEmail();
                    crearNuevoUsuarioEnDB(idNombre.getText().toString(), idApellido.getText().toString(), userEmail);
                } else {
                    // El inicio de sesión falló
                    showAlert();
                }

            }
        });
    }
    //este metodo se encarga de crear un usuario en la base de datos
    private void crearNuevoUsuarioEnDB(String nombre, String apellido, String email) {


        // Obtener la referencia del nuevo usuario en Firestore
        DocumentReference nuevoUsuarioRef = firestore.collection("usuarios")
                .document(mAuth.getCurrentUser().getUid());

        // Crear un objeto Usuario con la información del nuevo usuario
        Map<String, Object> nuevoUsuario = new HashMap<>();
        nuevoUsuario.put("nombre", nombre);
        nuevoUsuario.put("apellido", apellido);
        nuevoUsuario.put("email", email);
        nuevoUsuario.put("favoritos", new ArrayList<>());

        // Configurar la referencia del nuevo usuario con la información
        nuevoUsuarioRef.set(nuevoUsuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Éxito al agregar el nuevo usuario a la colección "usuarios"
                            showHome(email, MainActivity.ProviderType.BASIC);
                            finish();
                        } else {
                            // Error al agregar el nuevo usuario a la colección "usuarios"
                            showAlert();
                        }
                    }
                });
    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_NO_AUTORIZADO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showHome(String email, MainActivity.ProviderType pT){
        Intent i = new Intent(Registrarse.this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("provider", pT);
        startActivity(i);
        finish();
    }



}