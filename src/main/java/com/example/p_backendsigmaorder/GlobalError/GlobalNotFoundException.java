package com.example.p_backendsigmaorder.GlobalError;

public class GlobalNotFoundException extends RuntimeException {
    public GlobalNotFoundException(String message){
        super(message);
    }
}
