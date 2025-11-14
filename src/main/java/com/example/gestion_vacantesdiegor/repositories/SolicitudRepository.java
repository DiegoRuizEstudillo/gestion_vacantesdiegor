package com.example.gestion_vacantesdiegor.repositories;

import com.example.gestion_vacantesdiegor.models.Solicitud;
import com.example.gestion_vacantesdiegor.models.EstadoSolicitud;
import com.example.gestion_vacantesdiegor.dto.AspiranteVacanteReporteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Encontrar solicitudes por aspirante
    List<Solicitud> findByAspiranteId(Long aspiranteId);

    // Encontrar solicitudes por empleador
    List<Solicitud> findByEmpleadorId(Long empleadorId);

    // Encontrar solicitudes por vacante
    List<Solicitud> findByVacanteId(Long vacanteId);

    // Verificar si ya existe una solicitud del mismo aspirante para la misma vacante
    boolean existsByAspiranteIdAndVacanteId(Long aspiranteId, Long vacanteId);

    // Encontrar solicitud específica por aspirante y vacante
    Optional<Solicitud> findByAspiranteIdAndVacanteId(Long aspiranteId, Long vacanteId);

    // Contar solicitudes pendientes por empleador
    long countByEmpleadorIdAndEstado(Long empleadorId, EstadoSolicitud estado);

    // Encontrar solicitudes por estado y empleador
    List<Solicitud> findByEmpleadorIdAndEstado(Long empleadorId, EstadoSolicitud estado);

    // CONSULTA CORREGIDA: Usa a.correo en lugar de a.email
    @Query("SELECT new com.example.gestion_vacantesdiegor.dto.AspiranteVacanteReporteDTO(" +
            "a.nombre, a.correo, s.fechaSolicitud, s.estado) " +  // ← CAMBIADO: a.correo
            "FROM Solicitud s, Aspirante a " +
            "WHERE s.aspirante.id = a.id AND s.vacante.id = :vacanteId " +
            "ORDER BY s.fechaSolicitud DESC")
    List<AspiranteVacanteReporteDTO> findAspirantesByVacanteId(@Param("vacanteId") Long vacanteId);

    // CONSULTA SIMPLIFICADA: Contar contratados por vacante
    @Query("SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = :vacanteId AND s.estado = 'CONTRATADO'")
    Long countContratadosByVacanteId(@Param("vacanteId") Long vacanteId);
}