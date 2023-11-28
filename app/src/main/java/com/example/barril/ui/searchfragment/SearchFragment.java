package com.example.barril.ui.searchfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.barril.R;
import com.example.barril.ui.acountfragment.BuscarAdapter;
import com.example.barril.ui.homefragment.ListAdapter;
import com.example.barril.ui.homefragment.ListElement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class SearchFragment extends Fragment {
    public static final String COLOR = "color";
    public static final String MODELO = "modelo";
    public static final String URL_BOTELLA = "urlBotella";
    public static final String DESCRIPCION = "descripcion";

    public static final String MENSAJE_NO_AUTORIZADO = "Error obteniendo datos de Firestore";
    private static String ERROR = "Error";
    private static String ACEPTAR = "Aceptar";


    String urlBotella;
    String modelo;
    String color;
    String descripcion;

    List<BuscarElement> elements;
    BuscarElement buscarElement;
    RecyclerView recyclerView;
    BuscarAdapter buscarAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SearchView searchView;
    private List<BuscarElement> filteredElements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initRecyclerView(view);
        initSearchView(view);
        recogerDatosFirestore();
        return view;
    }
    ////////////////////////////////////////metodos filtrado///////////////////////////////////
    private void initSearchView(View view) {
        searchView = view.findViewById(R.id.idBuscador);
        filteredElements = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterElements(newText);
                return true;
            }
        });
    }

    private void filterElements(String query) {
        filteredElements.clear();

        for (BuscarElement element : elements) {
            if (element.getTitulo().toLowerCase().contains(query.toLowerCase()) ||
                    element.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                filteredElements.add(element);
            }
        }
        buscarAdapter.semItems(filteredElements);
        buscarAdapter.notifyDataSetChanged();
    }
////////////////////////////////////////////////////////iniciar recycler////////////////////////////////
    private void initRecyclerView(View view) {
        elements= new ArrayList<>();
        buscarAdapter = new BuscarAdapter(elements, getActivity());
        recyclerView = view.findViewById(R.id.recyclerViewBuscar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(buscarAdapter);
    }
    ////////////////////////////////////////recoger datos firebase y añadirlas al objeto////////////////////////////////////////
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
                                modelo = (String) data.get(MODELO);
                                descripcion = (String) data.get(DESCRIPCION);
                                color = (String) data.get(COLOR);
                                buscarElement= new BuscarElement(modelo, descripcion,urlBotella,color,id);

                                elements.add(buscarElement);
                            }
                            // Actualizar el adaptador después de agregar elementos
                            buscarAdapter.notifyDataSetChanged();
                        } else {
                            showAlert();
                        }
                    }

                });
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