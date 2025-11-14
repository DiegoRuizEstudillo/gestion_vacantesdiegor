package com.example.gestion_vacantesdiegor.dto;

import com.example.gestion_vacantesdiegor.models.EstadoVacante;
import java.time.LocalDateTime;

public class ReporteVacanteDTO {
    private String titulo;
    private LocalDateTime fechaPublicacion;
    private Long numeroAspirantes;
    private EstadoVacante estado;

    public ReporteVacanteDTO(String titulo, LocalDateTime fechaPublicacion,
                             Long numeroAspirantes, EstadoVacante estado) {
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.numeroAspirantes = numeroAspirantes;
        this.estado = estado;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Long getNumeroAspirantes() { return numeroAspirantes; }
    public void setNumeroAspirantes(Long numeroAspirantes) { this.numeroAspirantes = numeroAspirantes; }

    public EstadoVacante getEstado() { return estado; }
    public void setEstado(EstadoVacante estado) { this.estado = estado; }
}