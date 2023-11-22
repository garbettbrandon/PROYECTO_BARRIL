package com.example.barril.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public enum ProviderType{
        BASIC,
        GOOGLE
    }

    ActivityMainBinding binding;

    String comprobacionEmail, comprobacionProvider;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        recibirDatos();
        fragments();

    }

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
        //-----------------------------AUTH FIREBASE RECIBE DATOS DE LOG IN--------------------------------------------------
        //recibir datos de autenticacion
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String pT = intent.getStringExtra("provider");
        ProviderType providerType = ProviderType.valueOf(pT);
        //Guardado de datos
        SharedPreferences.Editor sP = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        sP.putString("email", email);
        sP.putString("provider", pT);
        sP.apply();


        //----------------------------------FIN-------------------------------------------------------------------------------
    }

}