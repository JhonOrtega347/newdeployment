package com.example.p_backendsigmaorder.email;

import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void enviarConfirmacionRegistro(String destinatario, String nombreUsuario) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            // Set data in template
            Context context = new Context();
            context.setVariable("nombre", nombreUsuario);
            String html = templateEngine.process("email/registro_confirmacion", context);

            helper.setTo(destinatario);
            helper.setSubject("Confirmación de Registro");
            helper.setText(html, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        }
    }
    public void enviarConfirmacionPedido(PedidoResponseDTO pedido, String jwt) {
        // Lógica para enviar el correo usando JavaMailSender u otro cliente
    }
}
