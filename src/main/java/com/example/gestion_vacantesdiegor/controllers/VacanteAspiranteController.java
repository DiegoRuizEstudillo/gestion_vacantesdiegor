package com.example.gestion_vacantesdiegor.controllers;

import com.example.gestion_vacantesdiegor.models.*;
import com.example.gestion_vacantesdiegor.repositories.VacanteRepository;
import com.example.gestion_vacantesdiegor.repositories.SolicitudRepository;
import com.example.gestion_vacantesdiegor.repositories.AspiranteRepository;
import com.example.gestion_vacantesdiegor.repositories.EmpleadorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/aspirante") // <- AGREGAR ESTO
public class VacanteAspiranteController {

    private final VacanteRepository vacanteRepository;
    private final SolicitudRepository solicitudRepository;
    private final AspiranteRepository aspiranteRepository;
    private final EmpleadorRepository empleadorRepository;

    public VacanteAspiranteController(VacanteRepository vacanteRepository,
                                      SolicitudRepository solicitudRepository,
                                      AspiranteRepository aspiranteRepository,
                                      EmpleadorRepository empleadorRepository) {
        this.vacanteRepository = vacanteRepository;
        this.solicitudRepository = solicitudRepository;
        this.aspiranteRepository = aspiranteRepository;
        this.empleadorRepository = empleadorRepository;
    }

    // Ver vacantes disponibles para aspirantes
    @GetMapping("/vacantes") // Ahora la ruta completa es /aspirante/vacantes
    public String verVacantesDisponibles(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.ASPIRANTE) {
            return "redirect:/auth/login";
        }

        List<Vacante> vacantes = vacanteRepository.findByEstado(EstadoVacante.PUBLICADA);
        model.addAttribute("vacantes", vacantes);
        model.addAttribute("usuario", usuario);

        return "aspirante/vacantes";
    }

    // Ver mis solicitudes
    @GetMapping("/solicitudes") // Ahora la ruta completa es /aspirante/solicitudes
    public String verMisSolicitudes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.ASPIRANTE) {
            return "redirect:/auth/login";
        }

        List<Solicitud> solicitudes = solicitudRepository.findByAspiranteId(usuario.getId());
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("usuario", usuario);

        return "aspirante/solicitudes";
    }

    // Ver mi perfil
    @GetMapping("/perfil") // Ahora la ruta completa es /aspirante/perfil
    public String verMiPerfil(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.ASPIRANTE) {
            return "redirect:/auth/login";
        }

        Optional<Aspirante> aspiranteOpt = aspiranteRepository.findById(usuario.getId());

        if (aspiranteOpt.isPresent()) {
            model.addAttribute("aspirante", aspiranteOpt.get());
            return "aspirante/perfil";
        } else {
            return "redirect:/auth/login";
        }
    }

    // POSTULARSE A VACANTE
    // POSTULARSE A VACANTE
    @PostMapping("/postularse")
    public String postularseAVacante(@RequestParam Long vacanteId,
                                     HttpSession session,
                                     Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.ASPIRANTE) {
            return "redirect:/auth/login";
        }

        // Obtener el aspirante
        Optional<Aspirante> aspiranteOpt = aspiranteRepository.findById(usuario.getId());
        if (!aspiranteOpt.isPresent()) {
            return "redirect:/aspirante/vacantes?error=Aspirante no encontrado";
        }

        Aspirante aspirante = aspiranteOpt.get();

        // Obtener la vacante
        Optional<Vacante> vacanteOpt = vacanteRepository.findById(vacanteId);
        if (!vacanteOpt.isPresent()) {
            return "redirect:/aspirante/vacantes?error=Vacante no encontrada";
        }

        Vacante vacante = vacanteOpt.get();

        // Verificar si ya se postuló a esta vacante
        if (solicitudRepository.existsByAspiranteIdAndVacanteId(aspirante.getId(), vacanteId)) {
            return "redirect:/aspirante/vacantes?error=Ya te has postulado a esta vacante";
        }

        // **IMPORTANTE: Crear la solicitud con estado PENDIENTE**
        Solicitud solicitud = new Solicitud();
        solicitud.setAspirante(aspirante);
        solicitud.setVacante(vacante);
        solicitud.setEmpleador(vacante.getEmpleador());
        solicitud.setHabilidadesUsadas(aspirante.getHabilidades());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE); // ← Asegurar que sea PENDIENTE
        solicitud.setFechaSolicitud(java.time.LocalDateTime.now());

        // Guardar la solicitud
        solicitudRepository.save(solicitud);

        return "redirect:/aspirante/solicitudes?success=Te has postulado exitosamente a: " + vacante.getTitulo();
    }
}