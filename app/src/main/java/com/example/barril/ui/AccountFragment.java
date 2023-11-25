package com.example.barril.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barril.R;
import com.example.barril.ui.login.LogIn;
import com.example.barril.ui.login.Registrarse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {

    private static final String EL_BARRIL_DE = "El barril de ";
    TextView id_tipo_inicio, idNombreBarril;
    Button id_cerrar_sesion;

    List<GuardadoList> elements;
    private GuardadoAdapter guardadoAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //declarar e inicializar un view para posteriormente poder utilizarlos
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initRecyclerView(view);

        id_cerrar_sesion = view.findViewById(R.id.id_cerrar_sesion);
        id_tipo_inicio = view.findViewById(R.id.id_tipo_inicio_acount);
        idNombreBarril = view.findViewById(R.id.idNombreBarril);
        ImageView imagenUsuario = view.findViewById(R.id.imagenUsuario);

        mostrarNombreInicio();
        mostrarCervezasGuardadas();

        id_cerrar_sesion.setOnClickListener(viewLambda -> {
            borradoDatosAutentificacion();
            FirebaseAuth.getInstance().signOut();
            volverInicio();
        });
        return view;

    }
    private void initRecyclerView(View view) {
        elements = new ArrayList<>();
        guardadoAdapter = new GuardadoAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.idRecyclerGuardado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(guardadoAdapter);
    }

    private void mostrarNombreInicio() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Obtener el ID del usuario
            String userId = user.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            // Obtener datos del usuario desde Firestore
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // El documento existe, obtener el nombre del usuario
                    String nombreUsuario = documentSnapshot.getString("nombre");

                    // Actualizar la interfaz de usuario con el nombre del usuario
                    idNombreBarril.setText(EL_BARRIL_DE + nombreUsuario);
                }
            }).addOnFailureListener(e -> {
                // Manejar errores al obtener datos del usuario desde Firestore
                Toast.makeText(requireContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            });
        }
    }


    public void volverInicio(){
        Intent i = new Intent(requireActivity(), LogIn.class);
        startActivity(i);
       requireActivity().finish();
    }

    public void borradoDatosAutentificacion() {
        SharedPreferences sP = getActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.clear();
        editor.apply();

    }

    private void mostrarCervezasGuardadas() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Obtener el ID del usuario
            String userId = user.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            // Obtener el array 'favoritos' del usuario
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    List<String> favoritos = (List<String>) documentSnapshot.get("favoritos");
                    if (favoritos != null && !favoritos.isEmpty()) {
                        elements.clear(); // Limpiar la lista antes de agregar las nuevas cervezas

                        // Obtener la información completa de cada cerveza favorita
                        for (String cervezaId : favoritos) {
                            obtenerInfoCerveza(cervezaId);
                        }
                    } else {
                        // El usuario no tiene cervezas favoritas
                        Toast.makeText(requireContext(), "No tienes cervezas favoritas", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                // Manejar errores al obtener el array 'favoritos' del usuario
                Toast.makeText(requireContext(), "Error al obtener cervezas favoritas", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void obtenerInfoCerveza(String cervezaId) {
        // Referencia al documento de la cerveza en la colección 'cervezas'
        DocumentReference cervezaRef = FirebaseFirestore.getInstance().collection("cervezas").document(cervezaId);

        cervezaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Recoger datos de la cerveza
                String nombre = (String) task.getResult().get("modelo");
                String descripcion = (String) task.getResult().get("descripcion");
                String urlBotella = (String) task.getResult().get("urlBotella");
                String color = (String) task.getResult().get("color");

                // Crear un objeto GuardadoList y agregarlo a la lista
                GuardadoList guardadoList = new GuardadoList(nombre, descripcion, urlBotella, color, cervezaId);
                elements.add(guardadoList);

                // Notificar al adaptador sobre el cambio en los datos
                guardadoAdapter.notifyDataSetChanged();
            } else {
                // Manejar errores al obtener la información de la cerveza
                Toast.makeText(requireContext(), "Error al obtener información de la cerveza", Toast.LENGTH_SHORT).show();
            }
        });
    }

}