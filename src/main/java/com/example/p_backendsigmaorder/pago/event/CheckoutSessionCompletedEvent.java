package com.example.p_backendsigmaorder.pago.event;

import org.springframework.context.ApplicationEvent;

public class CheckoutSessionCompletedEvent extends ApplicationEvent {
    private final String sessionId;
    private final String jwt;

    public CheckoutSessionCompletedEvent(Object source, String sessionId, String jwt) {
        super(source);
        this.sessionId = sessionId;
        this.jwt       = jwt;
    }

    public String getSessionId() { return sessionId; }
    public String getJwt()       { return jwt; }
}