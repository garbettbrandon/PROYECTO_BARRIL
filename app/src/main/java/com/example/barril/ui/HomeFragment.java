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
        elements.add(new ListElement("#775447", "Delirium Brown", "Aqui va la info", "7,8%"));
        elements.add(new ListElement("#C30606", "Delirium Red", "Aqui va la info", "10%%"));
        elements.add(new ListElement("#06C33C", "Crafts", "Aqui va la info", "5,3%"));
        elements.add(new ListElement("#0676C3", "Franzis", "Aqui va la info", "5,5%"));
        elements.add(new ListElement("#C306B8", "Weisehols", "Aqui va la info", "6,6%"));
        elements.add(new ListElement("#C306B8", "Weisehols", "Aqui va la info", "6,6%"));
        elements.add(new ListElement("#C306B8", "Weisehols", "Aqui va la info", "6,6%"));
        elements.add(new ListElement("#C306B8", "Weisehols", "Aqui va la info", "6,6%"));
        ListAdapter listAdapter = new ListAdapter(elements, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }
}