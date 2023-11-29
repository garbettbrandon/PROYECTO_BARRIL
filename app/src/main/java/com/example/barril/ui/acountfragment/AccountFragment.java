package com.example.barril.ui.acountfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barril.R;
import com.example.barril.ui.auth.LogIn;
import com.example.barril.ui.cards.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {

    private static final String EL_BARRIL_DE = "El barril de ";
    private static final int REQUEST_IMAGE_PICK = 1;
    TextView id_tipo_inicio, idNombreBarril, idNombre;
    Button id_cerrar_sesion;
    ImageView imagenUsuario;
    CardView cardNombre, cardTipo;

    List<GuardadoElement> elements;
    private GuardadoAdapter guardadoAdapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference userRef;
    StorageReference storageRef;
    StorageReference imageRef;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //declarar e inicializar un view para posteriormente poder utilizarlos
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initRecyclerView(view);

        imagenUsuario = view.findViewById(R.id.imagenUsuario);
        id_cerrar_sesion = view.findViewById(R.id.id_cerrar_sesion);
        id_tipo_inicio = view.findViewById(R.id.id_tipo_inicio_acount);
        idNombreBarril = view.findViewById(R.id.idNombreBarril);
        idNombre = view.findViewById(R.id.idNombre);

        ImageView imagenUsuario = view.findViewById(R.id.imagenUsuario);

        mostrarNombreInicio();
        mostrarCervezasGuardadas();

        id_tipo_inicio.setVisibility(View.INVISIBLE);
        idNombre.setVisibility(View.INVISIBLE);

        id_cerrar_sesion.setOnClickListener(viewLambda -> {
            borradoDatosAutentificacion();
            FirebaseAuth.getInstance().signOut();
            volverInicio();
        });

        imagenUsuario.setOnClickListener(v -> {
            cambiarFotoPerfil();
        });
        obtenerUrlFotoPerfil();


        return view;

    }
    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            subirNuevaFotoPerfil(selectedImageUri);
                        }
                    }
                }
            }
    );

    private void subirNuevaFotoPerfil(Uri imageUri) {
        //Crear una referencia al lugar donde se almacenará la imagen en Firebase Storage
        storageRef = FirebaseStorage.getInstance().getReference();
        imageRef = storageRef.child("fotos_perfil/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

        //Subir la imagen a Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    //Obtener la URL de descarga de la imagen
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        //Actualizar la URL de la foto de perfil del usuario en Firestore
                        actualizarFotoPerfilUsuario(uri);
                    });
                })
                .addOnFailureListener(e -> {
                    //Manejar errores durante la subida de la imagen
                    Toast.makeText(requireContext(), "Error al subir la nueva foto de perfil", Toast.LENGTH_SHORT).show();
                });
    }
    private void actualizarFotoPerfilUsuario(Uri photoUri) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            //Obtener el ID del usuario
            String userId = user.getUid();

            //Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            //actualizar el campo 'fotoPerfil' con la nueva URL
            userRef.update("fotoPerfil", photoUri.toString())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Foto de perfil actualizada con éxito", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void obtenerUrlFotoPerfil() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            // Obtener el ID del usuario
            String userId = user.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            // Obtener datos del usuario desde Firestore
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // El documento existe, comprobar si existe el campo "fotoPerfil"
                    if (documentSnapshot.contains("fotoPerfil")) {
                        // Obtener la URL de la foto de perfil
                        String fotoPerfilUrl = documentSnapshot.getString("fotoPerfil");

                        // Guardar la URL en un String (puedes almacenarla en una variable de clase)
                        if (fotoPerfilUrl != null && !fotoPerfilUrl.isEmpty()) {
                            // Aquí puedes utilizar la variable "fotoPerfilUrl" según tus necesidades
                            // Por ejemplo, cargar la imagen con Glide
                            cargarImagenUsuario(fotoPerfilUrl);
                        }
                    } else {
                        // El campo "fotoPerfil" no existe en el documento
                        // Puedes manejar esta situación según tus necesidades
                    }
                } else {
                    // El documento no existe
                    // Puedes manejar esta situación según tus necesidades
                }
            }).addOnFailureListener(e -> {
                // Manejar errores al obtener datos del usuario desde Firestore
                Toast.makeText(requireContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            });
        }
    }




    private void cargarImagenUsuario(String fotoPerfilUrl) {

        Glide.with(requireContext())
                .load(fotoPerfilUrl)
                .into(imagenUsuario);
    }


    //Método para cambiar la foto de perfil
    public void cambiarFotoPerfil() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            // Obtener el ID del usuario
            String userId = user.getUid();

            //Referencia al documento del usuario en la colección 'usuarios'
            userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            //Obtener datos del usuario desde Firestore
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    //El documento existe, obtener el nombre del usuario
                    String nombreUsuario = documentSnapshot.getString("nombre");

                    // Actualizar la interfaz de usuario con el nombre del usuario
                    idNombreBarril.setText(EL_BARRIL_DE + nombreUsuario);
                }
            }).addOnFailureListener(e -> {
                //Manejar errores al obtener datos del usuario desde Firestore
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            //Obtener el ID del usuario
            String userId = user.getUid();

            //Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            //Obtener el array 'favoritos' del usuario
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    List<String> favoritos = (List<String>) documentSnapshot.get("favoritos");
                    if (favoritos != null && !favoritos.isEmpty()) {
                        elements.clear(); // Limpiar la lista antes de agregar las nuevas cervezas

                        //Obtener la información completa de cada cerveza favorita
                        for (String cervezaId : favoritos) {
                            obtenerInfoCerveza(cervezaId);
                        }
                    } else {
                        //El usuario no tiene cervezas favoritas
                        Toast.makeText(requireContext(), "No tienes cervezas favoritas", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                //Manejar errores al obtener el array 'favoritos' del usuario
                Toast.makeText(requireContext(), "Error al obtener cervezas favoritas", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void obtenerInfoCerveza(String cervezaId) {
        //Referencia al documento de la cerveza en la colección 'cervezas'
        DocumentReference cervezaRef = FirebaseFirestore.getInstance().collection("cervezas").document(cervezaId);

        cervezaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Recoger datos de la cerveza
                String nombre = (String) task.getResult().get("modelo");
                String descripcion = (String) task.getResult().get("descripcion");
                String urlBotella = (String) task.getResult().get("urlBotella");
                String color = (String) task.getResult().get("color");

                //Crear un objeto GuardadoList y agregarlo a la lista
                GuardadoElement guardadoList = new GuardadoElement(nombre, descripcion, urlBotella, color, cervezaId);
                elements.add(guardadoList);

                //Notificar al adaptador sobre el cambio en los datos
                guardadoAdapter.notifyDataSetChanged();
            } else {
                //Manejar errores al obtener la información de la cerveza
                Toast.makeText(requireContext(), "Error al obtener información de la cerveza", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

