package com.example.barril.ui;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barril.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class CardView extends AppCompatActivity {

    ImageView botellaCerveza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        botellaCerveza = findViewById(R.id.botellaCerveza);

        botellaCerveza.setElevation(5f);

        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("botellas/abadia_botella.png");



        // Recuperar la URL de la imagen
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(botellaCerveza);
        }).addOnFailureListener(exception -> {
            // Manejar el caso en que no se puede recuperar la URL de la imagen
            //Toast.makeText(requireContext(), "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show();
        });*/

    }

}