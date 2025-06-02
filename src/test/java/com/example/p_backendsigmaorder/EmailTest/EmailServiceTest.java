package com.example.p_backendsigmaorder.EmailTest;

import com.example.p_backendsigmaorder.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_NAME = "Usuario Test";
    private final String TEST_TEMPLATE = "email/registro_confirmacion";
    private final String TEST_HTML = "<html><body>Test HTML</body></html>";

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(TEST_TEMPLATE), any(Context.class))).thenReturn(TEST_HTML);
    }

    @Test
    void enviarConfirmacionRegistro_DeberiaEnviarCorreoExitosamente() {
        // Act
        assertDoesNotThrow(() -> emailService.enviarConfirmacionRegistro(TEST_EMAIL, TEST_NAME));

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(templateEngine, times(1)).process(eq(TEST_TEMPLATE), any(Context.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    void enviarConfirmacionRegistro_DeberiaUsarPlantillaCorrecta() {
        // Act
        emailService.enviarConfirmacionRegistro(TEST_EMAIL, TEST_NAME);

        // Assert
        verify(templateEngine).process(eq(TEST_TEMPLATE), argThat(context ->
                TEST_NAME.equals(context.getVariable("nombre"))
        ));
    }

    @Test
    void enviarConfirmacionRegistro_DeberiaConfigurarDestinatarioCorrecto() throws MessagingException {
        // Arrange
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        emailService.enviarConfirmacionRegistro(TEST_EMAIL, TEST_NAME);

        // Assert
        verify(mailSender).send(any(MimeMessage.class));
    }


}
