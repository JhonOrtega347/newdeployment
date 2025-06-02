package com.example.p_backendsigmaorder.pago.controller;

import com.example.p_backendsigmaorder.pago.event.CheckoutSessionCompletedEvent;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook")
public class StripeWebhookController {

    private final ApplicationEventPublisher publisher;

    public StripeWebhookController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<Void> handleEvent(@RequestBody Event stripeEvent) {
        if ("checkout.session.completed".equals(stripeEvent.getType())) {
            // devuelve un StripeObject genérico: lo casteamos a Session
            StripeObject obj = stripeEvent
                    .getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("No se pudo deserializar el objeto de datos"));

            if (!(obj instanceof Session)) {
                throw new RuntimeException("Unexpected event object type: " + obj.getClass());
            }
            Session session = (Session) obj;

            String jwt = session.getMetadata().get("jwt");
            publisher.publishEvent(
                    new CheckoutSessionCompletedEvent(this, session.getId(), jwt)
            );
        }
        return ResponseEntity.ok().build();
    }
}