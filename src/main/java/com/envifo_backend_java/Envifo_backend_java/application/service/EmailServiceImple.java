package com.envifo_backend_java.Envifo_backend_java.application.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImple {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImple(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarCorreoRecuperacion(String destinatario, String nombreUsuario, String urlRecuperacion) throws MessagingException {
        try {
            // Prepara el contexto para Thymeleaf
            Context context = new Context();
            context.setVariable("nombreUsuario", nombreUsuario);
            context.setVariable("urlRecuperacion", urlRecuperacion);

            // Genera el contenido HTML del correo
            String htmlContent = templateEngine.process("email-recuperacion", context);

            // Prepara el mensaje MIME
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject("Recuperación de Contraseña");
            helper.setText(htmlContent, true); // true indica que es HTML

            // Envía el correo
            mailSender.send(mensaje);
        } catch (MailException e) {
            throw new MessagingException("Error al enviar el correo de recuperación: " + e.getMessage(), e);
        }
    }
}
