package com.example.gestion_vacantesdiegor.controllers;

import com.example.gestion_vacantesdiegor.models.*;
import com.example.gestion_vacantesdiegor.repositories.SolicitudRepository;
import com.example.gestion_vacantesdiegor.repositories.VacanteRepository;
import com.example.gestion_vacantesdiegor.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/empleador")
public class EmpleadorSolicitudController {

    private final SolicitudRepository solicitudRepository;
    private final VacanteRepository vacanteRepository;
    private final EmailService emailService;

    public EmpleadorSolicitudController(SolicitudRepository solicitudRepository,
                                        VacanteRepository vacanteRepository,
                                        EmailService emailService) {
        this.solicitudRepository = solicitudRepository;
        this.vacanteRepository = vacanteRepository;
        this.emailService = emailService;
    }

    // VER SOLICITUDES RECIBIDAS
    @GetMapping("/solicitudes")
    public String verSolicitudesRecibidas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.EMPLEADOR) {
            return "redirect:/auth/login";
        }

        Empleador empleador = (Empleador) usuario;

        // Obtener todas las solicitudes para este empleador
        List<Solicitud> solicitudes = solicitudRepository.findByEmpleadorId(empleador.getId());

        // DEBUG DETALLADO
        System.out.println("=== DEBUG DETALLADO SOLICITUDES ===");
        for (Solicitud solicitud : solicitudes) {
            System.out.println("Solicitud ID: " + solicitud.getId());
            System.out.println("  - Estado: " + solicitud.getEstado());
            System.out.println("  - Estado == PENDIENTE: " + (solicitud.getEstado() == EstadoSolicitud.PENDIENTE));
            System.out.println("  - Estado.name(): " + solicitud.getEstado().name());
            System.out.println("  - Estado.toString(): " + solicitud.getEstado().toString());
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("empleador", empleador);

        return "empleador/solicitudes";
    }

    // APROBAR SOLICITUD
    @PostMapping("/solicitudes/aprobar")
    public String aprobarSolicitud(@RequestParam Long solicitudId,
                                   HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.EMPLEADOR) {
            return "redirect:/auth/login";
        }

        // Buscar la solicitud
        var solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            Solicitud solicitud = solicitudOpt.get();

            // Verificar que la solicitud pertenece a este empleador
            if (solicitud.getEmpleador().getId().equals(usuario.getId())) {
                solicitud.setEstado(EstadoSolicitud.APROBADA);
                solicitudRepository.save(solicitud);

                // ✅ ENVIAR CORREO DE APROBACIÓN
                try {
                    emailService.enviarAprobacionAspirante(solicitud);
                } catch (Exception e) {
                    System.err.println("Error enviando correo: " + e.getMessage());
                    // No interrumpir el flujo si falla el correo
                }
            }
        }

        return "redirect:/empleador/solicitudes?success=Solicitud aprobada y notificación enviada";
    }

    // RECHAZAR SOLICITUD
    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@RequestParam Long solicitudId,
                                    HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.EMPLEADOR) {
            return "redirect:/auth/login";
        }

        // Buscar la solicitud
        var solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            Solicitud solicitud = solicitudOpt.get();

            // Verificar que la solicitud pertenece a este empleador
            if (solicitud.getEmpleador().getId().equals(usuario.getId())) {
                solicitud.setEstado(EstadoSolicitud.RECHAZADA);
                solicitudRepository.save(solicitud);

                // ✅ ENVIAR CORREO DE RECHAZO
                try {
                    emailService.enviarRechazoAspirante(solicitud);
                } catch (Exception e) {
                    System.err.println("Error enviando correo: " + e.getMessage());
                }
            }
        }

        return "redirect:/empleador/solicitudes?success=Solicitud rechazada y notificación enviada";
    }
}