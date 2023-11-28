package com.example.barril.ui.searchfragment;

public class BuscarElement {
    String titulo;
    String descripcion;
    String urlBotella;
    String color;
    String cervezaId;

    public BuscarElement(String titulo, String descripcion, String urlBotella, String color, String cervezaId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlBotella = urlBotella;
        this.color = color;
        this.cervezaId = cervezaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlBotella() {
        return urlBotella;
    }

    public void setUrlBotella(String urlBotella) {
        this.urlBotella = urlBotella;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCervezaId() {
        return cervezaId;
    }

    public void setCervezaId(String cervezaId) {
        this.cervezaId = cervezaId;
    }
}
