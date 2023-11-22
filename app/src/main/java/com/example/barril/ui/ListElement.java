package com.example.barril.ui;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

public class ListElement {

    public String id;
    public int botella;
    public int logo;
    public String marca;
    public String descripcion;
    public String precio;
    public String cantidad;
    public String grados;
    public String color;
    public String url;
    public ImageView imagen;




    public ListElement(String id, ImageView imagen,  int botella, int logo, String marca, String descripcion, String precio, String cantidad, String grados, String color, String url) {
        this.id = id;
        this.imagen = imagen;
        this.botella = botella;
        this.logo = logo;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.grados = grados;
        this.color = color;
        this.url = url;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public int getBotella() {

        return botella;
    }

    public void setBotella(int botella) {

        this.botella = botella;
    }

    public int getLogo() {

        return logo;
    }

    public void setLogo(int logo) {

        this.logo = logo;
    }

    public String getMarca() {

        return marca;
    }

    public void setMarca(String marca) {

        this.marca = marca;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {

        this.precio = precio;
    }

    public String getCantidad() {

        return cantidad;
    }

    public void setCantidad(String cantidad) {

        this.cantidad = cantidad;
    }

    public String getGrados() {

        return grados;
    }

    public void setGrados(String grados) {
        this.grados = grados;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

