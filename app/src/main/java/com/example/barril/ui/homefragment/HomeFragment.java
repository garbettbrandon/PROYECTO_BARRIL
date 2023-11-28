package com.example.barril.ui.homefragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.barril.R;
import com.example.barril.ui.admin.AgregarCervezasAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    public static final String URL_BOTELLA = "urlBotella";
    public static final String URL_LOGO = "urlLogo";
    public static final String MARCA = "marca";
    public static final String DESCRIPCION = "descripcion";
    public static final String PRECIO = "precio";
    public static final String CANTIDAD = "cantidad";
    public static final String GRADOS = "grados";
    public static final String COLOR = "color";
    public static final String TIPO = "tipo";
    public static final String MENSAJE_NO_AUTORIZADO = "Error obteniendo datos de Firestore";
    private static String ERROR = "Error";
    private static String ACEPTAR = "Aceptar";

    List<ListElement> elements;
    private ListAdapter listAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String urlBotella;
    String urlLogo;
    String marca;
    String descripcion;
    String precio;
    String cantidad;
    String grados;
    String color;
    String tipo;

    ImageView masCervezasAdmin;

    ListElement listElement;

    FirebaseAuth auth;
    FirebaseUser user;

    Configuration configuration;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initRecyclerView(view);

        masCervezasAdmin = view.findViewById(R.id.masCervezasAdmin);
        masCervezasAdmin.setVisibility(View.INVISIBLE);
        comprobarAdmin(recogerUserId());
        masCervezasAdmin.setOnClickListener(view1 -> {
            agregarCervezasActivity();

        });
        recogerDatosFirestore();
        return view;
    }

    private void initRecyclerView(View view) {
        elements = new ArrayList<>();
        listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }

    private void recogerDatosFirestore() {
        db.collection("cervezas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Recoger datos
                                String id = document.getId();
                                Map<String, Object> data = document.getData();

                                 urlBotella = (String) data.get(URL_BOTELLA);
                                 urlLogo = (String) data.get(URL_LOGO);
                                 marca = (String) data.get(MARCA);
                                 descripcion = (String) data.get(DESCRIPCION);
                                 precio = (String) data.get(PRECIO);
                                 cantidad = (String) data.get(CANTIDAD);
                                 grados = (String) data.get(GRADOS);
                                 color = (String) data.get(COLOR);
                                 tipo = (String) data.get(TIPO);
                                 listElement = new ListElement(id,urlBotella, urlLogo, marca, descripcion, precio, cantidad, grados, color, tipo);

                                elements.add(listElement);
                            }
                            // Actualizar el adaptador después de agregar elementos
                            listAdapter.notifyDataSetChanged();
                        } else {
                            showAlert();
                        }
                    }

                });

    }

    public  String recogerUserId(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid();
        return userId;
    }
    private void comprobarAdmin(String userId){
        db.collection("usuarios").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // El documento existe, ahora comprobar el campo "admin"
                        boolean admin = document.contains("admin") ? document.getBoolean("admin") : false;
                        ocultarBoton(admin);
                    }
                }
            }
        });
    }
    private void agregarCervezasActivity(){
        Intent intent = new Intent(getContext(), AgregarCervezasAdmin.class);
        startActivity(intent);
    }

    private void ocultarBoton(boolean admin){
        if(admin == true){
            masCervezasAdmin.setVisibility(View.VISIBLE);
        }
    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_NO_AUTORIZADO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}