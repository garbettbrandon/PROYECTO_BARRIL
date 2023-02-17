package com.example.barril.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barril.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<ListElement> elements;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        elements = new ArrayList<>();
        //elements.add(new ListElement(R.drawable.botella_mahou,R.drawable.logo_mahou,"mahou","una cerveza muy buena","3","3","3","#FFFFFf"));
        //elements.add(new ListElement(R.drawable.botella_cibeles,R.drawable.logo_cibeles, "Cibeles", "otra cerve","5","6","5","#f4f4f4"));
        elements.add(new ListElement(R.drawable.goose_botella,R.drawable.logo_cibeles, "Goose", "otra cerve","2,82€","0,33cl","5,8%","#C68B3D"));
        elements.add(new ListElement(R.drawable.abadia_botella,R.drawable.logo_cibeles, "Abadia", "otra cerve","2,49€","0,33cl","5,0%","#ED999D"));
        elements.add(new ListElement(R.drawable.birraandblues_botella,R.drawable.logo_cibeles, "Birra & Blues", "otra cerve","2,58€","0,33cl","7,5%","#77BC71"));
        elements.add(new ListElement(R.drawable.la_virgen_botella,R.drawable.la_virgen_logo, "La Virgen", "otra cerve","2,20€","0,33cl","5,2%","#F83B36"));
        elements.add(new ListElement(R.drawable.paquita_brown_botella,R.drawable.paquita_brown_logo, "Paquita Brown", "Curioso nombre para una cerveza especial.","2,49€","0,33cl","5,2%","#845D49"));
        elements.add(new ListElement(R.drawable.kadabra_botella,R.drawable.kadabra, "Kadabra", "La cerveza artesana Golden Ale de Kadabra es una cerveza rubia, dorada y de complejo carácter.","2,49€","0,33cl","4,8%","#0083B4"));
        elements.add(new ListElement(R.drawable.la_socarrada_botella,R.drawable.la_socarrada_logo, "La Socarrada", "Es una cerveza artesana premiun de estilo Winter Honey Ale, elaborada con Romero y Miel de Romero.","2,59€","0,33cl","6,0%","#7D7651"));




        ListAdapter listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }
}