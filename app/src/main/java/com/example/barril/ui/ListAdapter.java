package com.example.barril.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barril.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public void setItems(List<ListElement> items ){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView botella, logo, corazon;
        TextView marca, descripcion, precio, cantidad, grados;
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

            corazon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Cambiar el color del corazón a rojo

                    //corazon.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                    corazon.setImageResource(R.drawable.jarra2);

                    // Puedes realizar otras acciones aquí, por ejemplo, agregar a favoritos, etc.
                    showToast("Corazón clickeado en posición: " + getBindingAdapterPosition());
                }
            });
        }
        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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


    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


}
