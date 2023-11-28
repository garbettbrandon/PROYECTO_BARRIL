package com.example.barril.ui.cards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barril.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class CardViewGrande extends AppCompatActivity {

    ImageView idBotellaGrande, idLogoGrande, idRadarGrande, idMaridaje;
    TextView idModeloGrande, idMarcaGrande, idDescripcionGrande, idCantidadGrande, idPrecioGrande, idGradosGrande;
    Button idVolverBotonGrande;
    View idColorCabeceraGrande;

    String cantidad;
    String color;
    String descripcion;
    String grados;
    String marca;
    String modelo;
    String precio;
    String tipo;
    String urlBotella;
    String urlLogo;
    String urlMaridaje;
    String urlRadar;

    FirebaseFirestore db;
    DocumentReference cervezasRef;
    FirebaseStorage storage;
    StorageReference storageRefBotella, storageRefLogo, storageRefRadar, storageRefMaridaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_card_view_grande);

        idBotellaGrande = findViewById(R.id.idBotellaGrande);
        idLogoGrande = findViewById(R.id.idLogoGrande);
        idRadarGrande = findViewById(R.id.idRadarGrande);
        idMaridaje = findViewById(R.id.idMaridaje);

        idModeloGrande = findViewById(R.id.idModeloGrande);
        idMarcaGrande = findViewById(R.id.idMarcaGrande);
        idDescripcionGrande = findViewById(R.id.idDescripcionGrande);
        idCantidadGrande = findViewById(R.id.idCantidadGrande);
        idPrecioGrande = findViewById(R.id.idPrecioGrande);
        idGradosGrande = findViewById(R.id.idGradosGrande);

        idVolverBotonGrande = findViewById(R.id.idVolverBotonGrande);

        idColorCabeceraGrande = findViewById(R.id.idColorCabeceraGrande);

        Intent intent = getIntent();
        String idDocumento = intent.getStringExtra("idDocumento");

        recogerDatos(idDocumento);

        idVolverBotonGrande.setOnClickListener(view->{
            finish();
        });

    }

    public void recogerDatos( String idDocumento){
        db = FirebaseFirestore.getInstance();
        cervezasRef = db.collection("cervezas").document(idDocumento);
        cervezasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Documento encontrado, ahora puedes obtener los datos
                    Map<String, Object> datos = document.getData();
                    // Utiliza los datos según sea necesario
                    cantidad = (String) datos.get("cantidad");
                    color = (String) datos.get("color");
                    descripcion = (String) datos.get("descripcion");
                    grados = (String) datos.get("grados");
                    marca = (String) datos.get("marca");
                    modelo = (String) datos.get("modelo");
                    precio = (String) datos.get("precio");
                    tipo = (String) datos.get("tipo");
                    urlBotella = (String) datos.get("urlBotella");
                    urlLogo = (String) datos.get("urlLogo");
                    urlMaridaje = (String) datos.get("urlMaridaje");
                    urlRadar = (String) datos.get("urlRadar");

                    colocarDatos(cantidad, color, descripcion, grados, marca, modelo, precio, tipo, urlBotella, urlMaridaje, urlLogo, urlRadar);
                } else {
                    // El documento no existe
                    Toast.makeText(this,idDocumento.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error al intentar obtener el documento
                Toast.makeText(this,"Error al buscar en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void colocarDatos(String cantidad, String color, String descripcion, String grados, String marca,
                              String modelo, String precio, String tipo, String urlBotella, String urlMaridaje,
                              String urlLogo, String urlRadar) {
        storage = FirebaseStorage.getInstance();
        storageRefBotella = storage.getReference().child(urlBotella);
        storageRefLogo = storage.getReference().child(urlLogo);
        storageRefRadar = storage.getReference().child(urlRadar);
        storageRefMaridaje = storage.getReference().child(urlMaridaje);


        // Recuperar la URL de la imagen
        storageRefBotella.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(idBotellaGrande);
        });
        storageRefLogo.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(idLogoGrande);
        });
        storageRefRadar.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(idRadarGrande);
        });
        storageRefMaridaje.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(idMaridaje);
        });

        //Parsear el color y setear
        idColorCabeceraGrande.setBackgroundColor(Color.parseColor(color));

        //Setear textview
        idMarcaGrande.setText(marca);
        idModeloGrande.setText(modelo);
        idDescripcionGrande.setText(descripcion);
        idPrecioGrande.setText(precio);
        idCantidadGrande.setText(cantidad);
        idGradosGrande.setText(grados);
    }


}