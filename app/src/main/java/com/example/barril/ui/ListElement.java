package com.example.barril.ui;

public class ListElement {
    public String color;
    public String name;
    public String descripcion;
    public String graduacion;

    public ListElement(String color, String name, String descripcion, String graduacion) {
        this.color = color;
        this.name = name;
        this.descripcion = descripcion;
        this.graduacion = graduacion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGraduacion() {
        return graduacion;
    }

    public void setGraduacion(String graduacion) {
        this.graduacion = graduacion;
    }
}
