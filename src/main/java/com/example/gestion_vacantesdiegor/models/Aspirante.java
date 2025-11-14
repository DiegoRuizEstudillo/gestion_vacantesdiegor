package com.example.gestion_vacantesdiegor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "aspirantes")
public class Aspirante extends Usuario {

    @NotBlank(message = "Las habilidades son obligatorias")
    @Column(columnDefinition = "TEXT")
    private String habilidades;

    // Constructores
    public Aspirante() {}

    public Aspirante(String nombre, String correo, String password, String habilidades) {
        this.setNombre(nombre);
        this.setCorreo(correo);
        this.setPassword(password);
        this.setRol(Role.ASPIRANTE);
        this.habilidades = habilidades;
    }

    // Getters y Setters
    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }
}