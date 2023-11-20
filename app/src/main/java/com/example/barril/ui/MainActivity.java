package com.example.barril.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.databinding.ActivityMainBinding;
import com.example.barril.ui.cervezas.Product;
import com.example.barril.ui.login.LogIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    private List<Product> productList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


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

        ////////////////////////////////////////////////////////////////////////////////////////////

        pruebasFire();
        productList = new ArrayList<>();

        db.collection("cervezas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //recogida datos
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                //mostrar por syso
                                /*String id = document.getId();
                                Map<String, Object> data = document.getData();

                                // Imprime la información en la consola del sistema
                                System.out.println("Cerveza ID: " + id);
                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    System.out.println(entry.getKey() + ": " + entry.getValue());
                                }*/

                                //mostrar por toast
                                String id = document.getId();
                                Map<String, Object> data = document.getData();
                                StringBuilder toastMessage = new StringBuilder("Cerveza ID: " + id);
                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    toastMessage.append("\n")
                                            .append(entry.getKey())
                                            .append(": ")
                                            .append(entry.getValue());
                                }

                                // Muestra el Toast
                                Toast.makeText(MainActivity.this, toastMessage.toString(), Toast.LENGTH_SHORT).show();


                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



       /* db.collection("cervezas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/



        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void pruebasFire(){

    }


}