package com.example.gestion_vacantesdiegor.controllers;

import com.example.gestion_vacantesdiegor.models.Empleador;
import com.example.gestion_vacantesdiegor.services.VacanteServiceI;
import com.example.gestion_vacantesdiegor.repositories.SolicitudRepository;
import com.example.gestion_vacantesdiegor.dto.ReporteVacanteDTO;
import com.example.gestion_vacantesdiegor.dto.AspiranteVacanteReporteDTO;
import com.example.gestion_vacantesdiegor.dto.DesempenioVacanteDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/empleador/reportes")
public class ReporteController {

    private final VacanteServiceI vacanteService;
    private final SolicitudRepository solicitudRepository;

    public ReporteController(VacanteServiceI vacanteService, SolicitudRepository solicitudRepository) {
        this.vacanteService = vacanteService;
        this.solicitudRepository = solicitudRepository;
    }

    // Reporte 1: Vacantes publicadas
    @GetMapping("/vacantes-publicadas")
    public String reporteVacantesPublicadas(HttpSession session, Model model) {
        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        List<ReporteVacanteDTO> reporte = vacanteService.obtenerReporteVacantesPublicadas(empleador.getId());
        model.addAttribute("reporteVacantes", reporte);
        model.addAttribute("empleador", empleador);
        return "empleador/reportes/vacantes-publicadas";
    }

    // Reporte 2: Aspirantes por vacante
    @GetMapping("/aspirantes-por-vacante")
    public String reporteAspirantesPorVacante(@RequestParam Long vacanteId,
                                              HttpSession session, Model model) {
        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        List<AspiranteVacanteReporteDTO> aspirantes = solicitudRepository.findAspirantesByVacanteId(vacanteId);
        model.addAttribute("aspirantes", aspirantes);
        model.addAttribute("empleador", empleador);
        model.addAttribute("vacanteId", vacanteId);
        return "empleador/reportes/aspirantes-por-vacante";
    }

    // Reporte 3: Desempeño de vacantes
    @GetMapping("/desempenio-vacantes")
    public String reporteDesempenioVacantes(HttpSession session, Model model) {
        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        List<DesempenioVacanteDTO> desempenio = vacanteService.obtenerReporteDesempenioVacantes(empleador.getId());
        model.addAttribute("desempenioVacantes", desempenio);
        model.addAttribute("empleador", empleador);
        return "empleador/reportes/desempenio-vacantes";
    }

    // Método auxiliar para obtener empleador de sesión
    private Empleador obtenerEmpleadorDeSesion(HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof Empleador) {
            return (Empleador) usuario;
        }
        return null;
    }
}
