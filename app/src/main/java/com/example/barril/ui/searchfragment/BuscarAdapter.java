package com.example.barril.ui.acountfragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.barril.ui.searchfragment.BuscarElement;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuscarAdapter extends RecyclerView.Adapter<com.example.barril.ui.acountfragment.BuscarAdapter.ViewHolder> {

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

        Button idBotonBorrar;

        FirebaseStorage storage;
        StorageReference storageRefBotellaGuardado, storageRefLogoGuardado;

        @SuppressLint("ResourceType")
        ViewHolder(View itemView) {
            super(itemView);
            idTituloGuardado = itemView.findViewById(R.id.idTituloGuardado);
            idBotellaGuardado = itemView.findViewById(R.id.idBotellaGuardado);
            idColorCabeceraGuardado = itemView.findViewById(R.id.idColorCabeceraGuardado);
            idDescripcionGuardado = itemView.findViewById(R.id.idDescripcionGuardado);
            idBotonBorrar = itemView.findViewById(R.id.idBotonBorrar);

            /*idBotonBorrar.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //borrarCerveza(position);
                }
            });**/

        }



        void bindData(final BuscarElement item){
            // Cargar desde una URL usando Picasso
            storage = FirebaseStorage.getInstance();
            storageRefBotellaGuardado = storage.getReference().child(item.getUrlBotella().toString());


            // Recuperar la URL de la imagen
            storageRefBotellaGuardado.getDownloadUrl().addOnSuccessListener(uri -> {
                // Aquí tienes la URL de la imagen
                String imageUrl = uri.toString();

                // Cargar la imagen en el ImageView usando Picasso
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
    public com.example.barril.ui.acountfragment.BuscarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_card_guardadas,null);
        return new com.example.barril.ui.acountfragment.BuscarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.barril.ui.acountfragment.BuscarAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    ////////////////////////////////////////metodos///////////////////////////////////////////////////



    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
