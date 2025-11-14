package com.example.gestion_vacantesdiegor.controllers;

import com.example.gestion_vacantesdiegor.models.Empleador;
import com.example.gestion_vacantesdiegor.models.EstadoVacante;
import com.example.gestion_vacantesdiegor.models.Vacante;
import com.example.gestion_vacantesdiegor.services.VacanteServiceI;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/empleador/vacantes")
public class VacanteController {

    private final VacanteServiceI vacanteService;

    public VacanteController(VacanteServiceI vacanteService) {
        this.vacanteService = vacanteService;
    }

    // LISTAR VACANTES DEL EMPLEADOR
    @GetMapping
    public String listarVacantes(HttpSession session, Model model) {
        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("vacantes", vacanteService.obtenerPorEmpleador(empleador));
        model.addAttribute("empleador", empleador);
        return "empleador/vacantes/listar";
    }

    // FORMULARIO NUEVA VACANTE
    @GetMapping("/nueva")
    public String formularioNuevaVacante(HttpSession session, Model model) {
        if (obtenerEmpleadorDeSesion(session) == null) {
            return "redirect:/auth/login";
        }

        Vacante vacante = new Vacante();
        model.addAttribute("vacante", vacante);
        model.addAttribute("tiposTrabajo", com.example.gestion_vacantesdiegor.models.TipoTrabajo.values());
        return "empleador/vacantes/formulario";
    }

    // GUARDAR NUEVA VACANTE - CORREGIDO
    @PostMapping
    public String guardarVacante(@Valid @ModelAttribute Vacante vacante,
                                 BindingResult result,
                                 HttpSession session,
                                 Model model) {

        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("tiposTrabajo", com.example.gestion_vacantesdiegor.models.TipoTrabajo.values());
            return "empleador/vacantes/formulario";
        }

        // Lógica automática para fechas y estado
        if (vacante.getId() == null) {
            // NUEVA VACANTE
            vacante.setFechaPublicacion(LocalDateTime.now());
            vacante.setEstado(EstadoVacante.PUBLICADA);
        } else {
            // EDITAR VACANTE EXISTENTE - CORREGIDO
            Vacante vacanteExistente = vacanteService.obtenerPorId(vacante.getId());
            if (vacanteExistente != null) {
                vacante.setFechaPublicacion(vacanteExistente.getFechaPublicacion());
                // MANTENER EL ESTADO ORIGINAL - LÍNEA CLAVE AGREGADA
                vacante.setEstado(vacanteExistente.getEstado());
            }
        }

        // Verificar si la fecha de cierre ya pasó
        if (vacante.getFechaCierre() != null && LocalDateTime.now().isAfter(vacante.getFechaCierre())) {
            vacante.setEstado(EstadoVacante.CERRADA);
        }

        vacante.setEmpleador(empleador);
        vacanteService.guardar(vacante);
        return "redirect:/empleador/vacantes";
    }

    // FORMULARIO EDITAR VACANTE
    @GetMapping("/editar/{id}")
    public String formularioEditarVacante(@PathVariable Long id,
                                          HttpSession session,
                                          Model model) {

        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        Vacante vacante = vacanteService.obtenerPorId(id);
        if (vacante == null || !vacante.getEmpleador().getId().equals(empleador.getId())) {
            return "redirect:/empleador/vacantes";
        }

        model.addAttribute("vacante", vacante);
        model.addAttribute("tiposTrabajo", com.example.gestion_vacantesdiegor.models.TipoTrabajo.values());
        return "empleador/vacantes/formulario";
    }

    // ELIMINAR VACANTE
    @GetMapping("/eliminar/{id}")
    public String eliminarVacante(@PathVariable Long id, HttpSession session) {
        Empleador empleador = obtenerEmpleadorDeSesion(session);
        if (empleador == null) {
            return "redirect:/auth/login";
        }

        Vacante vacante = vacanteService.obtenerPorId(id);
        if (vacante != null && vacante.getEmpleador().getId().equals(empleador.getId())) {
            vacanteService.eliminar(id);
        }

        return "redirect:/empleador/vacantes";
    }

    // METODO PRIVADO PARA OBTENER EMPLEADOR DE LA SESIÓN
    private Empleador obtenerEmpleadorDeSesion(HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof Empleador) {
            return (Empleador) usuario;
        }
        return null;
    }
}