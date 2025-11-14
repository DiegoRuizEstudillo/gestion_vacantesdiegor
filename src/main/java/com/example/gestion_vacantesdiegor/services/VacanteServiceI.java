package com.example.gestion_vacantesdiegor.services;

import com.example.gestion_vacantesdiegor.models.Empleador;
import com.example.gestion_vacantesdiegor.models.Vacante;
import com.example.gestion_vacantesdiegor.dto.ReporteVacanteDTO;
import com.example.gestion_vacantesdiegor.dto.DesempenioVacanteDTO;
import java.util.List;

public interface VacanteServiceI {
    List<Vacante> obtenerTodas();
    List<Vacante> obtenerPorEmpleador(Empleador empleador);
    Vacante obtenerPorId(Long id);
    Vacante guardar(Vacante vacante);
    void eliminar(Long id);

    // NUEVOS MÉTODOS PARA REPORTES
    List<ReporteVacanteDTO> obtenerReporteVacantesPublicadas(Long empleadorId);
    List<DesempenioVacanteDTO> obtenerReporteDesempenioVacantes(Long empleadorId);
}