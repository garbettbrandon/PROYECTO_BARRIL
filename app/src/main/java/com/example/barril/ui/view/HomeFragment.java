package com.example.barril.ui.view;

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

        elements.add(new ListElement(R.drawable.goose_botella, R.drawable.goose_logo, "Goose", "Goose Blates es una cerveza intensa y de carácter robusto. Es amarga y con mucho cuerpo, por eso es una Winter Ale rojiza y de persistente espuma.", "2,82€", "0,33cl", "5,8%", "#C68B3D"));
        elements.add(new ListElement(R.drawable.abadia_botella, R.drawable.cervezas_abadia_logo, "Abadia", "La Cerveza Artesana Abadía Española está hecha con mimo, con materias primas seleccionadas por expertos malteros a partir de cebada de primavera procedentes de territorio español.", "2,49€", "0,33cl", "5,0%", "#ED999D"));
        elements.add(new ListElement(R.drawable.birraandblues_botella, R.drawable.birrablues_logo, "Birra & Blues", "Una doble Red Ale de estilo británico elaborada con cuatro tipos de malta y tres lúpulos empleando en uno de ellos la técnica de dry-hopping.", "2,50€", "0,33cl", "6,4%", "#77BC71"));
        elements.add(new ListElement(R.drawable.la_virgen_botella, R.drawable.la_virgen_logo, "La Virgen", "Una cervecera con carácter madrileño. Para elaborar semejantes cervezas mucho tiene que ver la fábrica, la cervecería, el corazón de Cervezas La Virgen.", "2,29€", "0,33cl", "6,5%", "#F83B36"));
        elements.add(new ListElement(R.drawable.paquita_brown_botella, R.drawable.paquita_brown_logo, "Paquita Brown", "Curioso nombre para una cerveza especial.", "2,49€", "0,33cl", "5,2%", "#845D49"));
        elements.add(new ListElement(R.drawable.kadabra_botella, R.drawable.kadabra, "Kadabra", "La cerveza artesana Golden Ale de Kadabra es una cerveza rubia, dorada y de complejo carácter.", "2,49€", "0,33cl", "4,8%", "#0083B4"));
        elements.add(new ListElement(R.drawable.la_socarrada_botella, R.drawable.la_socarrada_logo, "La Socarrada", "Es una cerveza artesana premiun de estilo Winter Honey Ale, elaborada con Romero y Miel de Romero.", "2,59€", "0,33cl", "6,0%", "#7D7651"));

        ListAdapter listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }
}