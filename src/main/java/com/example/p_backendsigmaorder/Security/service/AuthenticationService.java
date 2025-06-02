package com.example.p_backendsigmaorder.Security.service;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoRepository;
import com.example.p_backendsigmaorder.Usuario.DTO.ChangePasswordRequest;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioRequestDTO;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.mapper.UsuarioMapper;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import com.example.p_backendsigmaorder.Security.dto.JwtAuthenticationResponse;
import com.example.p_backendsigmaorder.Security.dto.SigninRequest;
import com.example.p_backendsigmaorder.config.JwtService;
import com.example.p_backendsigmaorder.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CarritoRepository carritoRepository;
    private final EmailService emailService;  // INYECCIÓN del servicio de email

    // Registrar un nuevo usuario y devolver token JWT
    public JwtAuthenticationResponse register(UsuarioRequestDTO requestDTO) {
        if (usuarioRepository.findByCorreo(requestDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("Correo ya registrado");
        }

        Usuario usuario = UsuarioMapper.toEntity(requestDTO);
        usuario.setRol(Rol.CLIENTE);
        usuario.setContrasena(passwordEncoder.encode(requestDTO.getContrasena()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuarioGuardado);
        carritoRepository.save(carrito);

        // Enviar correo de confirmación bonito con plantilla HTML
        emailService.enviarConfirmacionRegistro(usuarioGuardado.getCorreo(), usuarioGuardado.getNombre());

        // Generar token JWT para el usuario recién creado
        String jwt = jwtService.generateToken(usuario.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }

    // Login y obtener token JWT
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByCorreo(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwt = jwtService.generateToken(usuario.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }

    // Obtener usuario autenticado
    public Usuario getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String correo;
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            correo = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en contexto"));
    }

    public void changePassword(String correo, ChangePasswordRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setContrasena(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);
    }

}
