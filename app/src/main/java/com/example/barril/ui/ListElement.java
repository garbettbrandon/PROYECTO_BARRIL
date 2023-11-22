package com.example.barril.ui;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

public class ListElement {

    public String id;
    public String marca;
    public String descripcion;
    public String precio;
    public String cantidad;
    public String grados;
    public String color;
    public String url;
    public String urlBotella;
    public String urlLogo;
    public ImageView imagen;




    public ListElement(String id,String urlBotella, String urlLogo, String marca, String descripcion, String precio, String cantidad, String grados, String color) {
        this.id = id;
        this.urlBotella = urlBotella;
        this.urlLogo = urlLogo;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.grados = grados;
        this.color = color;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlBotella() {
        return urlBotella;
    }

    public void setUrlBotella(String urlBotella) {
        this.urlBotella = urlBotella;
    }



    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
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

