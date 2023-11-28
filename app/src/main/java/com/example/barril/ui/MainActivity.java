package com.example.barril.ui;

import androidx.appcompat.app.AlertDialog;
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
import android.view.View;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.databinding.ActivityMainBinding;
import com.example.barril.ui.acountfragment.AccountFragment;
import com.example.barril.ui.cards.CardViewGrande;
import com.example.barril.ui.homefragment.HomeFragment;
import com.example.barril.ui.locationfragment.LocationFragment;
import com.example.barril.ui.searchfragment.SearchFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {


    private static final String ERROR = "No encontrado";
    private static final String MENSAJE_NO_AUTORIZADO = "No hemos encontrado la cerveza que has escaneado";
    private static final String ACEPTAR = "Acceptar";

    public enum ProviderType{
        BASIC,
        GOOGLE
    }

    ActivityMainBinding binding;

    String comprobacionEmail, comprobacionProvider;
    FirebaseFirestore db;
    DocumentReference cervezasRef;


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
        FloatingActionButton cameraBtn = binding.cameraButton;
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
        });
    }



   protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);

        if (result != null){
            if (result.getContents()==null){
                Toast.makeText(this,"Escaneo Cancelado", Toast.LENGTH_SHORT).show();
            }else {
                buscarEnBase(result.getContents().toString());
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void buscarEnBase(String contents) {
        db = FirebaseFirestore.getInstance();
        cervezasRef = db.collection("cervezas").document(contents);

        cervezasRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // El documento con el ID especificado existe
                            // Puedes realizar acciones aquí
                            enviarDocumentoCardGrande(contents.toString());
                        } else {
                            // El documento con el ID especificado no existe
                            noEncontrado();
                        }
                    } else {
                        Toast.makeText(this,"Error al buscar en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void enviarDocumentoCardGrande(String data) {
        Intent i = new Intent(MainActivity.this, CardViewGrande.class);
        i.putExtra("idDocumento", data);
        startActivity(i);
    }

    private void noEncontrado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_NO_AUTORIZADO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    };


    private void fragments() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



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
        replaceFragment(new HomeFragment());
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