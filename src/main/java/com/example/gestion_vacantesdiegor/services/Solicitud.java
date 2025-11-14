package com.example.gestion_vacantesdiegor.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aspirante_id")
    private Aspirante aspirante;

    @ManyToOne
    @JoinColumn(name = "vacante_id")
    private Vacante vacante;

    @ManyToOne
    @JoinColumn(name = "empleador_id")
    private Empleador empleador;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    private String habilidadesUsadas;
    private String cvUrl; // Mantener este campo si existe para URL de CV

    private LocalDateTime fechaSolicitud;

    // CONSTRUCTORES
    public Solicitud() {}

    public Solicitud(Aspirante aspirante, Vacante vacante, Empleador empleador,
                     String habilidadesUsadas, String cvUrl) {
        this.aspirante = aspirante;
        this.vacante = vacante;
        this.empleador = empleador;
        this.habilidadesUsadas = habilidadesUsadas;
        this.cvUrl = cvUrl;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }

    // ✅ GETTERS Y SETTERS MANUALES
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aspirante getAspirante() { return aspirante; }
    public void setAspirante(Aspirante aspirante) { this.aspirante = aspirante; }

    public Vacante getVacante() { return vacante; }
    public void setVacante(Vacante vacante) { this.vacante = vacante; }

    public Empleador getEmpleador() { return empleador; }
    public void setEmpleador(Empleador empleador) { this.empleador = empleador; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public String getHabilidadesUsadas() { return habilidadesUsadas; }
    public void setHabilidadesUsadas(String habilidadesUsadas) { this.habilidadesUsadas = habilidadesUsadas; }

    public String getCvUrl() { return cvUrl; }
    public void setCvUrl(String cvUrl) { this.cvUrl = cvUrl; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    // ELIMINA estas líneas si existen:
    // private CvFile cvFile;
    // public CvFile getCvFile() { return cvFile; }
    // public void setCvFile(CvFile cvFile) { this.cvFile = cvFile; }
}