package com.example.barril.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.ui.MainActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {



    TextView idUsuario, idContrasenia;
    Button idBotonEntrar, idRegistro, idEntrarGoogle;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        idUsuario = findViewById(R.id.idUsuario);
        idContrasenia = findViewById(R.id.idContrasenia);
        idBotonEntrar = findViewById(R.id.idBotonEntrar);
        idRegistro = findViewById(R.id.idRegistro);
        idEntrarGoogle = findViewById(R.id.idEntrarGoogle);

        //Comprobar sesion activa


        idEntrarGoogle.setOnClickListener(view -> {
        });

        idRegistro.setOnClickListener(view -> {
            Intent i = new Intent(LogIn.this, Registrarse.class);
            startActivity(i);
            finish();
        });

        idBotonEntrar.setOnClickListener(view -> {
            if(!idUsuario.getText().toString().isEmpty() && !idContrasenia.getText().toString().isEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(idUsuario.getText().toString(),idContrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El inicio de sesión fue exitoso

                            showHome("robert", MainActivity.ProviderType.BASIC);
                            finish();
                        } else {
                            // El inicio de sesión falló
                            Toast.makeText(LogIn.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                            showAlert();
                        }
                    }
                });
            }

        });

    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Error en usuario o contraseña");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //enviar mas extras al home si es necesario
    private void showHome(String email, MainActivity.ProviderType pT){
        Intent i = new Intent(LogIn.this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("provider", pT);
        startActivity(i);
    }
}