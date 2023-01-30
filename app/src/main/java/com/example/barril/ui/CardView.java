package com.example.barril.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.barril.R;

public class CardView extends AppCompatActivity {

    ImageView botellaCerveza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        botellaCerveza = findViewById(R.id.botellaCerveza);

        botellaCerveza.bringToFront();
        botellaCerveza.setElevation(50);
    }
}