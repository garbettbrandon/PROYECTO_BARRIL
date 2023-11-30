package com.example.barril.ui.searchfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barril.R;
import com.example.barril.ui.MainActivity;
import com.example.barril.ui.cards.CardViewGrande;
import com.example.barril.ui.searchfragment.BuscarElement;
import com.example.barril.ui.searchfragment.SearchFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuscarAdapter extends RecyclerView.Adapter<com.example.barril.ui.searchfragment.BuscarAdapter.ViewHolder> {

    private List<BuscarElement> mData;
    private LayoutInflater mInflater;

    private Context context;

    public void semItems(List<BuscarElement> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView idBotellaGuardado;
        TextView idTituloGuardado, idDescripcionGuardado;
        View idColorCabeceraGuardado;

        Button idBotonBorrar, idBotonMas;

        FirebaseStorage storage;
        FirebaseFirestore db;
        DocumentReference cervezasRef;


        StorageReference storageRefBotellaGuardado;

        @SuppressLint("ResourceType")
        ViewHolder(View itemView) {
            super(itemView);
            idTituloGuardado = itemView.findViewById(R.id.idTituloGuardado);
            idBotellaGuardado = itemView.findViewById(R.id.idBotellaGuardado);
            idColorCabeceraGuardado = itemView.findViewById(R.id.idColorCabeceraGuardado);
            idDescripcionGuardado = itemView.findViewById(R.id.idDescripcionGuardado);
            idBotonBorrar = itemView.findViewById(R.id.idBotonBorrar);
            idBotonMas = itemView.findViewById(R.id.idBotonMas);

            idBotonBorrar.setVisibility(View.INVISIBLE);

            idBotonMas.setOnClickListener(view -> {
                String cervezaId = mData.get(getBindingAdapterPosition()).getCervezaId();
                buscarEnBase(cervezaId);
            });


        }

        private void buscarEnBase(String contents) {
            db = FirebaseFirestore.getInstance();
            cervezasRef = db.collection("cervezas").document(contents);
            cervezasRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                enviarDocumentoCardGrande(contents.toString());
                            }
                        }
                    });

        }




        void bindData(final BuscarElement item){
            storage = FirebaseStorage.getInstance();
            storageRefBotellaGuardado = storage.getReference().child(item.getUrlBotella().toString());

            storageRefBotellaGuardado.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                Picasso.get().load(imageUrl).into(idBotellaGuardado);
            });
            //Cargar datos nomral
            idTituloGuardado.setText(item.getTitulo());
            idDescripcionGuardado.setText(item.getDescripcion());
            idColorCabeceraGuardado.setBackgroundColor(Color.parseColor(item.getColor()));
        }
    }



    public BuscarAdapter (List<BuscarElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }
    @NonNull
    @Override
    public com.example.barril.ui.searchfragment.BuscarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //reutilizamos tarjeta para mostrar datos
        View view = mInflater.inflate(R.layout.activity_card_basico,null);
        return new com.example.barril.ui.searchfragment.BuscarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.barril.ui.searchfragment.BuscarAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    ////////////////////////////////////////metodos///////////////////////////////////////////////////
    private void enviarDocumentoCardGrande(String data) {
        Intent i = new Intent(context, CardViewGrande.class);
        i.putExtra("idDocumento", data);
        context.startActivity(i);
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
