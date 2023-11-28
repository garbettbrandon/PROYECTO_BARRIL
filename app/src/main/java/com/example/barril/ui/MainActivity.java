package com.example.barril.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {


    public enum ProviderType{
        BASIC,
        GOOGLE
    }

    ActivityMainBinding binding;

    String comprobacionEmail, comprobacionProvider;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FloatingActionButton cameraBtn;


    //FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //desactiva modo noche
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recibirDatos();
        fragments();

        cameraBtn = findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(view -> {

        });
        /*FloatingActionButton cameraBtn = binding.cameraButton;
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Escanea tu cerveza");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });*/




    }



   /*protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);

        if (result != null){
            if (result.getContents()==null){
                Toast.makeText(this,"Escaneo Cancelado", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }*/



    private void fragments() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.location:
                    replaceFragment(new LocationFragment());
                    break;
                case R.id.account:
                    replaceFragment(new AccountFragment());
                    break;
            }
            return true;
        });
    }


    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void recibirDatos() {
        //db = FirebaseFirestore.getInstance();
        //-----------------------------AUTH FIREBASE RECIBE DATOS DE LOG IN--------------------------------------------------
        //recibir datos de autenticacion
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        //no recibe bien el provider auqnue se envie un BASIC desde el registrarse.class
        String pT = intent.getStringExtra("provider");
        pT = "BASIC";
        ProviderType providerType = ProviderType.valueOf(pT);
        //Guardado de datos
        SharedPreferences.Editor sP = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        sP.putString("email", email);
        sP.putString("provider", pT);
        sP.apply();


        //----------------------------------FIN-------------------------------------------------------------------------------
    }

}