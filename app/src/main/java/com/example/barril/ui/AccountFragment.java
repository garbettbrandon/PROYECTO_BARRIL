package com.example.barril.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class AccountFragment extends Fragment {

    TextView id_tipo_inicio, id_mail_acouny;
    Button id_cerrar_sesion;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //declarar e inicializar un view para posteriormente poder utilizarlos
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        id_cerrar_sesion = view.findViewById(R.id.id_cerrar_sesion);
        id_tipo_inicio = view.findViewById(R.id.id_tipo_inicio_acount);

        ImageView imagenUsuario = view.findViewById(R.id.imagenUsuario);

        id_cerrar_sesion.setOnClickListener(viewLambda -> {
            borradoDatosAutentificacion();
            FirebaseAuth.getInstance().signOut();
            volverInicio();
        });

        // Inflate the layout for this fragment
        return view;
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
}