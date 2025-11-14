package com.example.gestion_vacantesdiegor.dto;

public class DesempenioVacanteDTO {
    private String tituloVacante;
    private Long totalPostulaciones;
    private Long candidatosContratados;
    private Double porcentajeContratacion;
    private Double tiempoPromedioDias;

    public DesempenioVacanteDTO(String tituloVacante, Long totalPostulaciones,
                                Long candidatosContratados) {
        this.tituloVacante = tituloVacante;
        this.totalPostulaciones = totalPostulaciones;
        this.candidatosContratados = candidatosContratados;
        this.porcentajeContratacion = totalPostulaciones > 0 ?
                (candidatosContratados * 100.0) / totalPostulaciones : 0.0;
    }

    public DesempenioVacanteDTO(String tituloVacante, Long totalPostulaciones,
                                Long candidatosContratados, Double tiempoPromedioDias) {
        this(tituloVacante, totalPostulaciones, candidatosContratados);
        this.tiempoPromedioDias = tiempoPromedioDias;
    }

    // Getters y Setters
    public String getTituloVacante() { return tituloVacante; }
    public void setTituloVacante(String tituloVacante) { this.tituloVacante = tituloVacante; }

    public Long getTotalPostulaciones() { return totalPostulaciones; }
    public void setTotalPostulaciones(Long totalPostulaciones) { this.totalPostulaciones = totalPostulaciones; }

    public Long getCandidatosContratados() { return candidatosContratados; }
    public void setCandidatosContratados(Long candidatosContratados) { this.candidatosContratados = candidatosContratados; }

    public Double getPorcentajeContratacion() { return porcentajeContratacion; }
    public void setPorcentajeContratacion(Double porcentajeContratacion) { this.porcentajeContratacion = porcentajeContratacion; }

    public Double getTiempoPromedioDias() { return tiempoPromedioDias; }
    public void setTiempoPromedioDias(Double tiempoPromedioDias) { this.tiempoPromedioDias = tiempoPromedioDias; }
}