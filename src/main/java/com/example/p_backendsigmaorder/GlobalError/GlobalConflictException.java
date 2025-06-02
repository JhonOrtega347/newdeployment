package com.example.p_backendsigmaorder.GlobalError;

public class GlobalConflictException extends RuntimeException {
    public GlobalConflictException(String message){
        super(message);
    }
}
