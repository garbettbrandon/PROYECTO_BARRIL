package com.example.barril.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GuardadoAdapter extends RecyclerView.Adapter<GuardadoAdapter.ViewHolder> {

    private List<GuardadoList> mData;
    private LayoutInflater mInflater;
    private Context context;

    public void semItems(List<GuardadoList> items) {
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

            idBotonBorrar.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeCerveza(position);
                }
            });

        }



        void bindData(final GuardadoList item){
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
    public GuardadoAdapter (List<GuardadoList> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }
    @NonNull
    @Override
    public GuardadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_card_guardadas,null);
        return new GuardadoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuardadoAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    ////////////////////////////////////////metodos///////////////////////////////////////////////////
    public void removeCerveza(int position) {
        String idCerveza = mData.get(position).getCervezaId();
        // Aquí deberías llamar a un método para eliminar la cerveza en Firebase
        // por ejemplo, algo como removeCervezaEnFirebase(idCerveza);

        // Luego, puedes eliminar el elemento de la lista localmente
        //mData.remove(position);
        //notifyItemRemoved(position);

        removeCervezaEnFirebase(idCerveza, position);
    }
    private void removeCervezaEnFirebase(String idCerveza, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId);

            // Obtener la lista actual de favoritos del usuario
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                List<String> favoritos = (List<String>) documentSnapshot.get("favoritos");

                // Asegurarse de que la lista exista
                if (favoritos == null) {
                    favoritos = new ArrayList<>();
                }

                // Verificar si la cerveza está en la lista de favoritos
                if (favoritos.contains(idCerveza)) {
                    // Eliminar la cerveza de la lista de favoritos
                    favoritos.remove(idCerveza);

                    // Actualizar la lista de favoritos del usuario en Firebase
                    userRef.update("favoritos", favoritos)
                            .addOnSuccessListener(aVoid -> {
                                // Éxito al eliminar la cerveza de la lista de favoritos en Firebase
                                showToast("CERVEZA_ELIMINADA_EXITOSAMENTE");
                                // Eliminar el elemento de la lista localmente
                                mData.remove(position);
                                notifyItemRemoved(position);
                            })
                            .addOnFailureListener(e -> {
                                // Manejar errores al actualizar la lista de favoritos
                                showToast("ERROR_AL_ELIMINAR_CERVEZA");
                            });
                } else {
                    showToast("CERVEZA_NO_ENCONTRADA");
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
