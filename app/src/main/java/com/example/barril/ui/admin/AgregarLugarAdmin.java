package com.example.barril.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barril.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarLugarAdmin extends AppCompatActivity {
    private static String ERROR = "Error";
    private static String ACEPTAR = "Aceptar";
    private static String MENSAJE_NO_AUTORIZADO = "Coordenadas inválidas";
    private static String MENSAJE_VACIOS = "No deben quedar campos vacíos";
    private static String MENSAJE_FORMATO_INCORRECTO = "Formato incorrecto deben ser asi Ej: 90.00000 -80.00000";


    private GoogleMap mMap;
    FirebaseFirestore db;
    FirebaseAuth auth;


    TextView idTituloMapa, idLatitudMapa, idLongitudMapa, idDescaripcionMapa;
    Button idVolverMapa, idAgregarMapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_agregar_lugar_admin);

        idTituloMapa = findViewById(R.id.idTituloMapa);
        idLatitudMapa = findViewById(R.id.idLatitudMapa);
        idLongitudMapa = findViewById(R.id.idLongitudMapa);
        idDescaripcionMapa = findViewById(R.id.idDescripcionMapa);

        idVolverMapa = findViewById(R.id.idVolverMapa);
        idAgregarMapa = findViewById(R.id.idAgregarMapa);

        idAgregarMapa.setOnClickListener(view ->{
            agregarAlMapa(idTituloMapa.getText().toString(), idLatitudMapa.getText().toString(), idLongitudMapa.getText().toString(), idDescaripcionMapa.getText().toString());
        });

        idVolverMapa.setOnClickListener(view ->{
            finish();
        });
    }

    private void agregarAlMapa(String titulo, String latitud, String longitud, String descripcion) {
        db = FirebaseFirestore.getInstance();

        if (titulo.isEmpty() || latitud.isEmpty() || longitud.isEmpty() || descripcion.isEmpty()) {
            showAlertVacios();
            return;
        }
        if (comprobarCoordenadas(Double.parseDouble(latitud), Double.parseDouble(longitud))) {
            Map<String, Object> lugar = new HashMap<>();
            lugar.put("titulo", titulo);
            lugar.put("latitud", latitud);
            lugar.put("longitud", longitud);
            lugar.put("descripcion", descripcion);
            db.collection("mapas")
                    .add(lugar)
                    .addOnSuccessListener(documentReference -> {
                        showToast("Nuevo punto en el mapa");
                    })
                    .addOnFailureListener(e -> {
                        showToast("Error al subir punto en el mapa");
                    });
        }else{
            showAlert();
        }

    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_NO_AUTORIZADO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private boolean comprobarCoordenadas(double latitude, double longitude) {
        // Verifica si las coordenadas están dentro de rangos válidos
        String latitudStr = String.valueOf(latitude);
        String longitudStr = String.valueOf(longitude);

        // Verifica si las coordenadas están dentro de rangos válidos
        boolean formatoCorrecto = latitudStr.matches("^-?\\d{1,2}(\\.\\d+)?$") &&
                longitudStr.matches("^-?\\d{1,3}(\\.\\d+)?$");

        if (!formatoCorrecto) {
            showAlertFormatoIncorrecto();
        }
        return (latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180);
    }
    private void showAlertVacios(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_VACIOS);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showAlertFormatoIncorrecto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_FORMATO_INCORRECTO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}