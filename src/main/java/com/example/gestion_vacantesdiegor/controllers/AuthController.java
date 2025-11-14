package com.example.gestion_vacantesdiegor.controllers;

import com.example.gestion_vacantesdiegor.models.*;
import com.example.gestion_vacantesdiegor.repositories.AspiranteRepository;
import com.example.gestion_vacantesdiegor.repositories.EmpleadorRepository;
import com.example.gestion_vacantesdiegor.services.AuthService;
import com.example.gestion_vacantesdiegor.services.VacanteServiceI;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AspiranteRepository aspiranteRepository;
    private final EmpleadorRepository empleadorRepository;
    private final VacanteServiceI vacanteService;

    public AuthController(AuthService authService,
                          AspiranteRepository aspiranteRepository,
                          EmpleadorRepository empleadorRepository,
                          VacanteServiceI vacanteService) {
        this.authService = authService;
        this.aspiranteRepository = aspiranteRepository;
        this.empleadorRepository = empleadorRepository;
        this.vacanteService = vacanteService;
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("correo", "");
        model.addAttribute("password", "");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Usuario usuario = authService.autenticar(correo, password);

        if (usuario == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "auth/login";
        }

        if (!usuario.isActive()) {
            model.addAttribute("error", "El usuario está inactivo");
            return "auth/login";
        }

        session.setAttribute("usuario", usuario);

        // Redirigir según rol
        if (usuario.getRol() == Role.ASPIRANTE) {
            return "redirect:/auth/aspirante/bienvenida";
        } else if (usuario.getRol() == Role.EMPLEADOR) {
            return "redirect:/auth/bienvenida/empleador";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    // ================= REGISTRO ASPIRANTE =================
    @GetMapping("/registro/aspirante")
    public String mostrarRegistroAspirante(Model model) {
        model.addAttribute("aspirante", new Aspirante());
        return "auth/registro-aspirante";
    }

    @PostMapping("/registro/aspirante")
    public String registrarAspirante(@Valid @ModelAttribute Aspirante aspirante,
                                     BindingResult result,
                                     Model model,
                                     HttpSession session) { // ✅ AGREGADO: HttpSession

        if (result.hasErrors()) return "auth/registro-aspirante";

        if (existeCorreo(aspirante.getCorreo())) {
            model.addAttribute("error", "El correo ya está registrado");
            return "auth/registro-aspirante";
        }

        aspirante.setPassword(authService.hashPassword(aspirante.getPassword()));
        aspirante.setRol(Role.ASPIRANTE);
        Aspirante nuevoAspirante = aspiranteRepository.save(aspirante);

        // ✅ NUEVO: Iniciar sesión automáticamente después del registro
        session.setAttribute("usuario", nuevoAspirante);

        // ✅ CAMBIADO: Redirigir directamente a la bienvenida en lugar del login
        return "redirect:/auth/aspirante/bienvenida";
    }

    // ================= REGISTRO EMPLEADOR =================
    @GetMapping("/registro/empleador")
    public String mostrarRegistroEmpleador(Model model) {
        model.addAttribute("empleador", new Empleador());
        return "auth/registro-empleador";
    }

    @PostMapping("/registro/empleador")
    public String registrarEmpleador(@Valid @ModelAttribute Empleador empleador,
                                     BindingResult result,
                                     Model model,
                                     HttpSession session) { // ✅ AGREGADO: HttpSession

        if (result.hasErrors()) return "auth/registro-empleador";

        if (existeCorreo(empleador.getCorreo())) {
            model.addAttribute("error", "El correo ya está registrado");
            return "auth/registro-empleador";
        }

        empleador.setPassword(authService.hashPassword(empleador.getPassword()));
        empleador.setRol(Role.EMPLEADOR);
        Empleador nuevoEmpleador = empleadorRepository.save(empleador);

        // ✅ NUEVO: Iniciar sesión automáticamente después del registro
        session.setAttribute("usuario", nuevoEmpleador);

        // ✅ CAMBIADO: Redirigir directamente a la bienvenida en lugar del login
        return "redirect:/auth/bienvenida/empleador";
    }

    // ================= BIENVENIDA ASPIRANTE =================
    @GetMapping("/aspirante/bienvenida")
    public String bienvenidaAspirante(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Role.ASPIRANTE) {
            return "redirect:/auth/login";
        }

        Optional<Aspirante> aspiranteOpt = aspiranteRepository.findById(usuario.getId());

        if (aspiranteOpt.isPresent()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("aspirante", aspiranteOpt.get());
            return "aspirante/bienvenida";
        } else {
            return "redirect:/auth/login";
        }
    }

    // ================= BIENVENIDA EMPLEADOR =================
    @GetMapping("/bienvenida/empleador")
    public String bienvenidaEmpleador(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Role.EMPLEADOR) {
            return "redirect:/auth/login";
        }

        Empleador empleador = (Empleador) usuario;

        List<Vacante> vacantesRecientes = vacanteService.obtenerPorEmpleador(empleador)
                .stream()
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("usuario", empleador);
        model.addAttribute("vacantesRecientes", vacantesRecientes);
        return "empleador/bienvenida";
    }

    // ================= REDIRECCIÓN AL DASHBOARD =================
    @GetMapping("/empleador/bienvenida")
    public String redirectToBienvenida(HttpSession session) {
        return "redirect:/auth/bienvenida/empleador";
    }

    // ================= METODO PRIVADO PARA VERIFICAR CORREO =================
    private boolean existeCorreo(String correo) {
        return aspiranteRepository.findByCorreo(correo).isPresent() ||
                empleadorRepository.findByCorreo(correo).isPresent();
    }
}