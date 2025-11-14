package com.example.gestion_vacantesdiegor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vacantes")
public class Vacante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleador_id", nullable = false)
    private Empleador empleador;

    @NotBlank(message = "El título de la vacante es obligatorio")
    @Column(nullable = false, length = 255)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank(message = "Coloca al menos 3 requisitos")
    @Column(columnDefinition = "TEXT")
    private String requisitos;

    @Column(name = "ubicacion_id", length = 100)
    private String ubicacionId;

    @NotNull(message = "Selecciona un tipo de trabajo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_trabajo", nullable = false)
    private TipoTrabajo tipoTrabajo;

    @NotNull(message = "El salario es obligatorio")
    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVacante estado;

    // NUEVA RELACIÓN: Agrega esta línea para conectar con Solicitud
    @OneToMany(mappedBy = "vacante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<com.example.gestion_vacantesdiegor.models.Solicitud> solicitudes;

    // CONSTRUCTOR SIN ARGUMENTOS
    public Vacante() {}

    // GETTERS Y SETTERS MANUALES
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Empleador getEmpleador() { return empleador; }
    public void setEmpleador(Empleador empleador) { this.empleador = empleador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public String getUbicacionId() { return ubicacionId; }
    public void setUbicacionId(String ubicacionId) { this.ubicacionId = ubicacionId; }

    public TipoTrabajo getTipoTrabajo() { return tipoTrabajo; }
    public void setTipoTrabajo(TipoTrabajo tipoTrabajo) { this.tipoTrabajo = tipoTrabajo; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public EstadoVacante getEstado() { return estado; }
    public void setEstado(EstadoVacante estado) { this.estado = estado; }

    // GETTER Y SETTER PARA SOLICITUDES
    public List<com.example.gestion_vacantesdiegor.models.Solicitud> getSolicitudes() { return solicitudes; }
    public void setSolicitudes(List<com.example.gestion_vacantesdiegor.models.Solicitud> solicitudes) { this.solicitudes = solicitudes; }
}