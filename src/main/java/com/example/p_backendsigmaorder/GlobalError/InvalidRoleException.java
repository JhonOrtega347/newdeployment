package com.example.p_backendsigmaorder.GlobalError;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRoleException extends ResponseStatusException {
    public InvalidRoleException() {
        super(HttpStatus.FORBIDDEN, "El usuario no tiene el rol requerido");
    }
}
