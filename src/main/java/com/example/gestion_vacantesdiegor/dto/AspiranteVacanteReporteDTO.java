package com.example.gestion_vacantesdiegor.dto;

import com.example.gestion_vacantesdiegor.models.EstadoSolicitud;
import java.time.LocalDateTime;

public class AspiranteVacanteReporteDTO {
    private String nombreAspirante;
    private String email;
    private LocalDateTime fechaPostulacion;
    private EstadoSolicitud estado;

    public AspiranteVacanteReporteDTO(String nombreAspirante, String email,
                                      LocalDateTime fechaPostulacion, EstadoSolicitud estado) {
        this.nombreAspirante = nombreAspirante;
        this.email = email;
        this.fechaPostulacion = fechaPostulacion;
        this.estado = estado;
    }

    // Getters y Setters
    public String getNombreAspirante() { return nombreAspirante; }
    public void setNombreAspirante(String nombreAspirante) { this.nombreAspirante = nombreAspirante; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDateTime fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
}
