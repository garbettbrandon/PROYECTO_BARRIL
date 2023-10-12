package com.example.barril.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barril.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

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

    public void setItems(List<ListElement> items ){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView botella, logo;
        TextView marca, descripcion, precio, cantidad, grados;
        View color;


        @SuppressLint("ResourceType")
        ViewHolder(View itemView){
            super(itemView);
            botella = itemView.findViewById(R.id.botellaCerveza );
            logo = itemView.findViewById(R.id.idLogoCervezaMini);
            marca = itemView.findViewById(R.id.idTituloCervezaMini);
            descripcion = itemView.findViewById(R.id.idDescripcionCervezaMini);
            precio = itemView.findViewById(R.id.idPrecioCervezaMini);
            cantidad = itemView.findViewById(R.id.idTamanioCervezaMini);
            grados = itemView.findViewById(R.id.idPorcentajeCervezaMini);
            color = itemView.findViewById(R.id.idColorCabeceraMini);
        }
        void bindData(final ListElement item){
            botella.setImageResource(item.getBotella());
            logo.setImageResource(item.getLogo());
            marca.setText(item.getMarca());
            descripcion.setText(item.getDescripcion());
            precio.setText(item.getPrecio());
            cantidad.setText(item.getCantidad());
            grados.setText(item.getGrados());
            color.setBackgroundColor(Color.parseColor(item.getColor()));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
