package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.service.EmailServiceImple;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImpleTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImple emailService;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testEnviarCorreoRecuperacion_Success() throws Exception {
        // Arrange
        String destinatario = "test@example.com";
        String nombreUsuario = "usuarioPrueba";
        String urlRecuperacion = "http://recuperacion.com";
        when(templateEngine.process(eq("email-recuperacion"), any(Context.class)))
                .thenReturn("<html>Contenido</html>");

        // Act
        emailService.enviarCorreoRecuperacion(destinatario, nombreUsuario, urlRecuperacion);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1))
                .process(eq("email-recuperacion"), any(Context.class));
    }

    @Test
    void testEnviarCorreoRecuperacion_Failure_MailException() {
        // Arrange
        String destinatario = "test@example.com";
        when(templateEngine.process(eq("email-recuperacion"), any(Context.class)))
                .thenReturn("<html>Contenido</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Lanzamos un MailException en lugar de RuntimeException
        doThrow(new MailSendException("Fallo SMTP"))
                .when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        MessagingException exception = assertThrows(
                MessagingException.class,
                () -> emailService.enviarCorreoRecuperacion(destinatario, "usuario", "http://url.com")
        );

        assertTrue(exception.getMessage().contains("Error al enviar el correo de recuperación"));
    }
}
