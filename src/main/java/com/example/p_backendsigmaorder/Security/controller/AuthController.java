    package com.example.p_backendsigmaorder.Security.controller;

    import com.example.p_backendsigmaorder.Security.dto.JwtAuthenticationResponse;
    import com.example.p_backendsigmaorder.Security.dto.SigninRequest;
    import com.example.p_backendsigmaorder.Usuario.DTO.ChangePasswordRequest;
    import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioRequestDTO;
    import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;
    import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
    import com.example.p_backendsigmaorder.Security.service.AuthenticationService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        @Autowired
        private AuthenticationService authenticationService;

        // Endpoint para registrar un nuevo usuario
        @PostMapping("/register")
        public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
            JwtAuthenticationResponse tokenResponse = authenticationService.register(requestDTO);
            return ResponseEntity.ok(tokenResponse);
        }
        // Endpoint para hacer login y obtener el token JWT
        @PostMapping("/login")
        public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest signinRequest) {
            JwtAuthenticationResponse response = authenticationService.signin(signinRequest);
            return ResponseEntity.ok(response);
        }



    }
