package com.example.artwise_as.ui.slideshow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwise_as.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView tituloView,descripcionView;

    public MyViewHolder(@NonNull  View itemView){
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);
        tituloView=itemView.findViewById(R.id.titulo);
        descripcionView=itemView.findViewById(R.id.descripción);
    }
}
