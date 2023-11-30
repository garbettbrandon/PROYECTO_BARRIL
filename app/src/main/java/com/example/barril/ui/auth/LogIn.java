package com.example.barril.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LogIn extends AppCompatActivity {

    private static String ERROR = "Error";
    private static String ACEPTAR = "Aceptar";
    private static String MENSAJE_NO_AUTORIZADO = "Error en usuario o contraseña";
    private static final String EMAIL = "email";
    private static final String PROVIDER = "provider";



    TextView idUsuario, idContrasenia, idOlvidado;
    Button idBotonEntrar, idRegistro, idEntrarGoogle;
    String userEmail, comprobacionEmail, comprobacionProviderString;
    MainActivity.ProviderType comprobacionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_log_in);
        session();

        idUsuario = findViewById(R.id.idUsuario);
        idContrasenia = findViewById(R.id.idContrasenia);
        idBotonEntrar = findViewById(R.id.idBotonEntrar);
        idRegistro = findViewById(R.id.idRegistro);
        idEntrarGoogle = findViewById(R.id.idEntrarGoogle);

        idEntrarGoogle.setVisibility(View.INVISIBLE);

        //Comprobar sesión activa

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
                            userEmail = task.getResult().getUser().getEmail();
                            showHome(userEmail, MainActivity.ProviderType.BASIC);
                            finish();
                        } else {
                            // El inicio de sesión fallo
                            showAlert();
                        }
                    }
                });
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
    //enviar mas extras al home si es necesario
    private void showHome(String email, MainActivity.ProviderType pT){
        Intent i = new Intent(LogIn.this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("provider", pT.toString());
        startActivity(i);
        finish();
    }

    //comprobamos que hay sesion iniciada
    private void session(){
        //con esto recogemos los tipos de inicio de sesion que hay. Se podrá utilizar para diferentes tipos de cuentas
        SharedPreferences sP = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        comprobacionEmail = sP.getString(EMAIL, null);
        comprobacionProviderString = sP.getString(PROVIDER, null);

        if(comprobacionEmail != null && comprobacionProviderString != null){
            comprobacionProvider = MainActivity.ProviderType.valueOf(comprobacionProviderString);
            showHome(comprobacionEmail, comprobacionProvider);
        }
    }


}