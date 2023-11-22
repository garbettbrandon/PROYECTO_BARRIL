package com.example.barril.ui;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barril.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    List<ListElement> elements;
    private ListAdapter listAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView imagenPrueba;

    CardView cv = new CardView();


   // FirebaseStorage storage = FirebaseStorage.getInstance("gs://barril-e9ba3.appspot.com/botellas/botella_mahou.png");

   // public FirebaseStorage getStorage() {
       // return storage;
   // }

    public HomeFragment() {
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        elements = new ArrayList<>();
        //elements.add(new ListElement(R.drawable.botella_mahou,R.drawable.logo_mahou,"mahou","una cerveza muy buena","3","3","3","#FFFFFf"));
        //elements.add(new ListElement(R.drawable.botella_cibeles,R.drawable.logo_cibeles, "Cibeles", "otra cerve","5","6","5","#f4f4f4"));
        elements.add(new ListElement("as1", R.drawable.goose_botella,R.drawable.goose_logo, "Goose", "Goose Blates es una cerveza intensa y de carácter robusto. Es amarga y con mucho cuerpo, por eso es una Winter Ale rojiza y de persistente espuma.","2,82€","0,33cl","5,8%","#C68B3D"));
        elements.add(new ListElement("as1", R.drawable.abadia_botella,R.drawable.cervezas_abadia_logo, "Abadia", "La Cerveza Artesana Abadía Española está hecha con mimo, con materias primas seleccionadas por expertos malteros a partir de cebada de primavera procedentes de territorio español.","2,49€","0,33cl","5,0%","#ED999D"));
        elements.add(new ListElement("as1",R.drawable.birraandblues_botella,R.drawable.birrablues_logo, "Birra & Blues", "Una doble Red Ale de estilo británico elaborada con cuatro tipos de malta y tres lúpulos empleando en uno de ellos la técnica de dry-hopping.","2,50€","0,33cl","6,4%","#77BC71"));
        elements.add(new ListElement("as1",R.drawable.la_virgen_botella,R.drawable.la_virgen_logo, "La Virgen", "Una cervecera con carácter madrileño. Para elaborar semejantes cervezas mucho tiene que ver la fábrica, la cervecería, el corazón de Cervezas La Virgen.","2,29€","0,33cl","6,5%","#F83B36"));
        elements.add(new ListElement("as1",R.drawable.paquita_brown_botella,R.drawable.paquita_brown_logo, "Paquita Brown", "Curioso nombre para una cerveza especial.","2,49€","0,33cl","5,2%","#845D49"));
        elements.add(new ListElement("as1",R.drawable.kadabra_botella,R.drawable.kadabra, "Kadabra", "La cerveza artesana Golden Ale de Kadabra es una cerveza rubia, dorada y de complejo carácter.","2,49€","0,33cl","4,8%","#0083B4"));
        elements.add(new ListElement("as1",R.drawable.la_socarrada_botella,R.drawable.la_socarrada_logo, "La Socarrada", "Es una cerveza artesana premiun de estilo Winter Honey Ale, elaborada con Romero y Miel de Romero.","2,59€","0,33cl","6,0%","#7D7651"));




        ListAdapter listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imagenPrueba = view.findViewById(R.id.imagenPrueba);
        recogerDatosFirestore();
        initRecyclerView(view);
        //recogerDatosFirestore(); // Llamada al método para obtener datos de Firestore

        return view;
    }

    private void initRecyclerView(View view) {
        elements = new ArrayList<>();
        listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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



                                //////////////////////////////////////////////////////////////////


                                //Drawable miImagen = getResources().getDrawable(R.drawable.mi_imagwn);
                                String url = (String) data.get("url");
                                devolverBotella(url);
                                ////////////////////////////////////////////////////////////////////



                                // Obtener los datos necesarios

                                String marca = (String) data.get("marca");
                                String descripcion = (String) data.get("descripcion");
                                String precio = (String) data.get("precio");
                                String cantidad = (String) data.get("cantidad");
                                String grados = (String) data.get("grados");
                                String color = (String) data.get("color");
                                // Crear un objeto ListElement y agregarlo a la lista
                                ListElement listElement = new ListElement(id,R.drawable.abadia_botella, R.drawable.birrablues_logo, marca, descripcion, precio, cantidad, grados, color, url);

                                elements.add(listElement);
                            }

                            // Actualizar el adaptador después de agregar elementos
                            listAdapter.notifyDataSetChanged();

                        } else {
                            String TAG = "home";
                            Log.w(TAG, "Error getting documents.", task.getException());
                            // Puedes mostrar un Toast o realizar otra acción en caso de error
                            Toast.makeText(getActivity(), "Error obteniendo datos de Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private ImageView devolverBotella(String url) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(url.toString());



        // Recuperar la URL de la imagen
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Aquí tienes la URL de la imagen
            String imageUrl = uri.toString();

            // Cargar la imagen en el ImageView usando Picasso
            Picasso.get().load(imageUrl).into(imagenPrueba);
        }).addOnFailureListener(exception -> {
            // Manejar el caso en que no se puede recuperar la URL de la imagen
            Toast.makeText(requireContext(), "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show();
        });

        return imagenPrueba;
    }
}