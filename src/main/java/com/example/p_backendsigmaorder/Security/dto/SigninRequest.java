package com.example.p_backendsigmaorder.Security.dto;

import lombok.Data;

@Data
public class SigninRequest {
    private String email;
    private String password;
}
