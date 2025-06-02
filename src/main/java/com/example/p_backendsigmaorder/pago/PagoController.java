package com.example.p_backendsigmaorder.pago;

import com.example.p_backendsigmaorder.Carrito.domain.CarritoService;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pago")
public class PagoController {

    @Autowired
    private CarritoService carritoService;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @Value("${stripe.currency}")
    private String currency;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout() {
        // 1) Obtener DTO del carrito (incluye precioTotal ya calculado)
        CarritoResponseDTO carritoDTO = carritoService.obtenerCarritoDTO();

        double total = carritoDTO.getPrecioTotal();
        if (total <= 0) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "El carrito está vacío"));
        }

        // 2) Convertir a centavos
        long montoCentavos = Math.round(total * 100);

        // 3) Construir línea de item única
        SessionCreateParams.LineItem item = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(montoCentavos)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Pago total de carrito")
                                                .build()
                                )
                                .build()
                )
                .build();

        // 4) Crear parámetros de sesión
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(item)
                .build();

        // 5) Invocar a Stripe
        try {
            Session session = Session.create(params);
            return ResponseEntity.ok(Map.of(
                    "sessionId", session.getId(),
                    "url", session.getUrl()
            ));
        } catch (StripeException e) {
            return ResponseEntity
                    .status(502)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
