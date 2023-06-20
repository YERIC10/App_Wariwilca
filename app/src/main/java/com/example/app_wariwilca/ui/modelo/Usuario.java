package com.example.app_wariwilca.ui.modelo;

public class Usuario {

    public Usuario(String nombre, String comentario) {
        this.nombre = nombre;
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentaio() {
        return comentario;
    }

    public void setComentaio(String comentaio) {
        this.comentario = comentario;
    }

    String nombre;
    String comentario;
}
