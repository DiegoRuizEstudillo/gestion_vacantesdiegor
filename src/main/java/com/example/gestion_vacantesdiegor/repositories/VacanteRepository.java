package com.example.gestion_vacantesdiegor.repositories;

import com.example.gestion_vacantesdiegor.models.Empleador;
import com.example.gestion_vacantesdiegor.models.EstadoVacante;
import com.example.gestion_vacantesdiegor.models.Vacante;
import com.example.gestion_vacantesdiegor.dto.ReporteVacanteDTO;
import com.example.gestion_vacantesdiegor.dto.DesempenioVacanteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VacanteRepository extends JpaRepository<Vacante, Long> {

    List<Vacante> findByEmpleador(Empleador empleador);
    List<Vacante> findByEmpleadorAndEstado(Empleador empleador, EstadoVacante estado);
    List<Vacante> findByFechaCierreBeforeAndEstado(LocalDateTime fecha, EstadoVacante estado);
    List<Vacante> findByEstado(EstadoVacante estado);

    @Query("SELECT v FROM Vacante v WHERE v.fechaCierre < :now AND v.estado = 'PUBLICADA'")
    List<Vacante> findVacantesExpiradas(@Param("now") LocalDateTime now);

    // CONSULTA SIMPLIFICADA: Reporte de vacantes publicadas
    @Query("SELECT new com.example.gestion_vacantesdiegor.dto.ReporteVacanteDTO(" +
            "v.titulo, v.fechaPublicacion, " +
            "(SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = v.id), " +
            "v.estado) " +
            "FROM Vacante v " +
            "WHERE v.empleador.id = :empleadorId " +
            "ORDER BY v.fechaPublicacion DESC")
    List<ReporteVacanteDTO> findReporteVacantesByEmpleador(@Param("empleadorId") Long empleadorId);

    // CONSULTA SIMPLIFICADA: Reporte de desempeño de vacantes
    @Query("SELECT new com.example.gestion_vacantesdiegor.dto.DesempenioVacanteDTO(" +
            "v.titulo, " +
            "(SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = v.id), " +
            "(SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = v.id AND s.estado = 'CONTRATADO')) " +
            "FROM Vacante v " +
            "WHERE v.empleador.id = :empleadorId")
    List<DesempenioVacanteDTO> findDesempenioVacantesByEmpleador(@Param("empleadorId") Long empleadorId);
}