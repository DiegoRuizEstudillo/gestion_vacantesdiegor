package com.example.gestion_vacantesdiegor.services;

import com.example.gestion_vacantesdiegor.models.Solicitud;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarCorreoSimple(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("tu_correo@gmail.com"); // <- Usa el mismo correo configurado en spring.mail.username
            mailSender.send(message);
            System.out.println("✅ Correo enviado a: " + to);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }

    public void enviarAprobacionAspirante(Solicitud solicitud) {
        String asunto = "¡Felicidades! Tu solicitud ha sido aprobada";
        String contenido = crearContenidoAprobacion(solicitud);
        enviarCorreoSimple(solicitud.getAspirante().getCorreo(), asunto, contenido);
    }

    public void enviarRechazoAspirante(Solicitud solicitud) {
        String asunto = "Actualización sobre tu solicitud de empleo";
        String contenido = crearContenidoRechazo(solicitud);
        enviarCorreoSimple(solicitud.getAspirante().getCorreo(), asunto, contenido);
    }

    private String crearContenidoAprobacion(Solicitud solicitud) {
        return String.format(
                "Hola %s,\n\n" +
                        "¡Felicidades! Tu solicitud para la vacante \"%s\" en %s ha sido APROBADA.\n\n" +
                        "Detalles:\nPuesto: %s\nEmpresa: %s\nSalario: $%s\nUbicación: %s\n\n" +
                        "Saludos,\nEquipo de Gestión de Vacantes",
                solicitud.getAspirante().getNombre(),
                solicitud.getVacante().getTitulo(),
                solicitud.getVacante().getEmpleador().getEmpresa(),
                solicitud.getVacante().getTitulo(),
                solicitud.getVacante().getEmpleador().getEmpresa(),
                solicitud.getVacante().getSalario(),
                solicitud.getVacante().getUbicacionId()
        );
    }

    private String crearContenidoRechazo(Solicitud solicitud) {
        return String.format(
                "Hola %s,\n\n" +
                        "Gracias por tu interés en la vacante \"%s\" en %s.\n" +
                        "En esta ocasión hemos decidido continuar con otros candidatos.\n\n" +
                        "Te animamos a seguir postulando a más vacantes.\n\n" +
                        "Saludos,\nEquipo de %s",
                solicitud.getAspirante().getNombre(),
                solicitud.getVacante().getTitulo(),
                solicitud.getVacante().getEmpleador().getEmpresa(),
                solicitud.getVacante().getEmpleador().getEmpresa()
        );
    }
}
