package com.example.barril.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registrarse extends AppCompatActivity {



    private FirebaseAuth mAuth;
    TextView idNombre, idApellido, idMail, idRegistroContrasenia;
    Button idAcceder, idIniciar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);



        idNombre = findViewById(R.id.idNombre);
        idApellido = findViewById(R.id.idApellido);
        idMail = findViewById(R.id.idMail);
        idRegistroContrasenia= findViewById(R.id.idRegistroContrasenia);

        idAcceder= findViewById(R.id.idAcceder);
        idIniciar= findViewById(R.id.idIniciar);

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

                if(!idMail.getText().toString().isEmpty() && !idRegistroContrasenia.getText().toString().isEmpty()){
                    //esta linea realiza la comprobacion de no estar vacio y crea un usuario nuevo
                    mAuth.getInstance().createUserWithEmailAndPassword(idMail.getText().toString(), idRegistroContrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // El inicio de sesión fue exitoso
                                Intent i = new Intent(Registrarse.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // El inicio de sesión falló
                                Toast.makeText(Registrarse.this, "Inicio de sesión no autorizado", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }
}