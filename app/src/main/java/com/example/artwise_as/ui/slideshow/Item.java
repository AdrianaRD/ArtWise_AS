package com.example.artwise_as.ui.slideshow;

import android.graphics.drawable.Drawable;

public class Item {
    String titulo;
    String información;
    int image;

    public Item(String titulo, String información, int image) {
        this.titulo = titulo;
        this.información = información;
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInformación() {
        return información;
    }

    public void setInformación(String información) {
        this.información = información;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

