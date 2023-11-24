package com.example.barril.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String BIRRA_AGREGADA = "Birra agregada a tu jarra";
    private static final String ERROR_ALAGREGAR_CERVEZA = "Error al agregar la cerveza a favoritos";
    private static final String YA_TIENES_ESTA_BIRRA = "La birra ya está en tu jarra";
    private static final String FAVORITOS= "favoritos";
    private static final String USUARIOS= "usuarios";


    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public void setItems(List<ListElement> items ){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView botella, logo, corazon;
        TextView marca, descripcion, precio, cantidad, grados, idTipo;
        View color;
        FirebaseStorage storage;
        StorageReference storageRefBotella, storageRefLogo;


        @SuppressLint("ResourceType")
        ViewHolder(View itemView){
            super(itemView);
            corazon = itemView.findViewById(R.id.idMeGustaMinii);
            botella = itemView.findViewById(R.id.botellaCerveza );
            logo = itemView.findViewById(R.id.idLogoCervezaMini);
            marca = itemView.findViewById(R.id.idTituloCervezaMini);
            descripcion = itemView.findViewById(R.id.idDescripcionCervezaMini);
            precio = itemView.findViewById(R.id.idPrecioCervezaMini);
            cantidad = itemView.findViewById(R.id.idTamanioCervezaMini);
            grados = itemView.findViewById(R.id.idPorcentajeCervezaMini);
            color = itemView.findViewById(R.id.idColorCabeceraMini);
            idTipo = itemView.findViewById(R.id.idTipo);



            //agregarFavoritos
            corazon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idCerveza  = mData.get(getBindingAdapterPosition()).getId();
                    corazon.setImageResource(R.drawable.jarra2);
                    cervezaFavorita(idCerveza);
                }
            });
        }




        void bindData(final ListElement item){
            // Cargar desde una URL usando Picasso
            storage = FirebaseStorage.getInstance();
            storageRefBotella = storage.getReference().child(item.getUrlLogo().toString());
            storageRefLogo = storage.getReference().child(item.getUrlBotella().toString());

            // Recuperar la URL de la imagen
            storageRefBotella.getDownloadUrl().addOnSuccessListener(uri -> {
                // Aquí tienes la URL de la imagen
                String imageUrl = uri.toString();

                // Cargar la imagen en el ImageView usando Picasso
                Picasso.get().load(imageUrl).into(logo);
            });
            storageRefLogo.getDownloadUrl().addOnSuccessListener(uri -> {
                // Aquí tienes la URL de la imagen
                String imageUrl = uri.toString();

                // Cargar la imagen en el ImageView usando Picasso
                Picasso.get().load(imageUrl).into(botella);
            });

            //Cargar datos nomral
            marca.setText(item.getMarca());
            descripcion.setText(item.getDescripcion());
            precio.setText(item.getPrecio());
            cantidad.setText(item.getCantidad());
            grados.setText(item.getGrados());
            color.setBackgroundColor(Color.parseColor(item.getColor()));
            idTipo.setText(item.getIdTipo());
        }
    }

    public ListAdapter (List<ListElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_card_view,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

        //comprobar que le cerfveza esta y poner la jarra de color
        String idCerveza = mData.get(position).getId();
        isCervezaEnFavoritos(idCerveza, new Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                if (result) {
                    holder.corazon.setImageResource(R.drawable.jarra2);
                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //////////////////////////////////////METODOSSSSS///////////////////////////////////////////////////////////////////
    private void cervezaFavorita(String idCerveza) {

        // Aquí deberías agregar la lógica para guardar 'idCerveza' en la lista de favoritos del usuario en Firebase
        // Puedes hacer esto actualizando el documento del usuario en la colección 'usuarios'
        // y agregando 'idCerveza' a la matriz 'favoritos'.

        // Ejemplo simplificado (no olvides manejar los casos de error y las tareas asincrónicas):
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection(USUARIOS).document(userId);

            // Obtener la lista actual de favoritos del usuario
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                Set<String> favoritos = new HashSet<>((List<String>) documentSnapshot.get(FAVORITOS));

                // Asegurarse de que la lista exista
                if (favoritos == null) {
                    favoritos = new HashSet<>();
                }

                // Verificar si la cerveza ya está en la lista de favoritos
                if (!favoritos.contains(idCerveza)) {
                    // Agregar la nueva cerveza a la lista de favoritos
                    favoritos.add(idCerveza);

                    // Actualizar la lista de favoritos del usuario en Firebase
                    userRef.update("favoritos", new ArrayList<>(favoritos))
                            .addOnSuccessListener(aVoid -> {
                                // Éxito al agregar la cerveza a la lista de favoritos
                                showToast(BIRRA_AGREGADA);
                            })
                            .addOnFailureListener(e -> {
                                // Manejar errores al actualizar la lista de favoritos
                                showToast(ERROR_ALAGREGAR_CERVEZA);
                            });
                } else {
                    showToast(YA_TIENES_ESTA_BIRRA);
                }
            });
        }

    }

    private void isCervezaEnFavoritos(String idCerveza, Callback<Boolean> callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Referencia al documento del usuario en la colección 'usuarios'
            DocumentReference userRef = FirebaseFirestore.getInstance().collection(USUARIOS).document(userId);

            // Obtener la lista actual de favoritos del usuario
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                Set<String> favoritos = new HashSet<>((List<String>) documentSnapshot.get(FAVORITOS));

                // Verificar si la cerveza ya está en la lista de favoritos
                boolean isFavorita = favoritos != null && favoritos.contains(idCerveza);
                callback.onResult(isFavorita);
            });
        } else {
            callback.onResult(false);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    //////////////////////////////////////////interfaz para manejar la asincronia//////////////////////////////////////////
    interface Callback<T> {
        void onResult(T result);
    }
}
