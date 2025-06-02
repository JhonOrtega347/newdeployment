package com.example.p_backendsigmaorder.LocalTest;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.repository.LocalRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LocalRepositoryTest {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario encargado;

    @BeforeEach
    void setUp() {
        localRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear y persistir un usuario encargado para las pruebas
        encargado = Usuario.builder()
                .nombre("Juan Encargado")
                .correo("juan@local.com")
                .contrasena("password123")
                .direccion("Av. Principal 123")
                .telefono("+51999888777")
                .fechaRegistro(new Date())
                .rol(Rol.ADMINISTRADOR)
                .build();

        encargado = usuarioRepository.save(encargado); // Guardamos el encargado primero
    }

    @Test
    void deberiaGuardarYRecuperarLocal() {
        // Arrange
        Local local = Local.builder()
                .sede("Sede Central")
                .direccion("Av. Principal 123")
                .telefono("+51999111222")
                .horario("09:00-18:00")
                .encargado(encargado)
                .build();

        // Act
        Local localGuardado = localRepository.save(local);
        Optional<Local> localRecuperado = localRepository.findById(localGuardado.getId());

        // Assert
        assertThat(localRecuperado)
                .isPresent()
                .hasValueSatisfying(l -> {
                    assertThat(l.getSede()).isEqualTo("Sede Central");
                    assertThat(l.getDireccion()).isEqualTo("Av. Principal 123");
                    assertThat(l.getTelefono()).isEqualTo("+51999111222");
                    assertThat(l.getHorario()).isEqualTo("09:00-18:00");
                    assertThat(l.getEncargado().getId()).isEqualTo(encargado.getId());
                    assertThat(l.getEncargado().getNombre()).isEqualTo("Juan Encargado");
                });
    }

    @Test
    void deberiaEliminarLocal() {
        // Arrange
        Local local = Local.builder()
                .sede("Sede Sur")
                .direccion("Av. Sur 456")
                .telefono("+51999333444")
                .horario("10:00-20:00")
                .encargado(encargado)
                .build();

        Local localGuardado = localRepository.save(local);

        // Act
        localRepository.deleteById(localGuardado.getId());
        Optional<Local> resultado = localRepository.findById(localGuardado.getId());

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void deberiaActualizarLocal() {
        // Arrange
        Local local = Local.builder()
                .sede("Sede Este")
                .direccion("Av. Este 789")
                .telefono("+51999555666")
                .horario("08:00-17:00")
                .encargado(encargado)
                .build();

        Local localGuardado = localRepository.save(local);

        // Act
        localGuardado.setSede("Sede Este Modificada");
        localGuardado.setHorario("09:00-18:00");
        Local localActualizado = localRepository.save(localGuardado);

        // Assert
        assertThat(localActualizado.getSede()).isEqualTo("Sede Este Modificada");
        assertThat(localActualizado.getHorario()).isEqualTo("09:00-18:00");
        assertThat(localActualizado.getId()).isEqualTo(localGuardado.getId());
    }

    @Test
    void deberiaListarTodosLosLocales() {
        // Arrange
        Local local1 = Local.builder()
                .sede("Sede 1")
                .direccion("Dirección 1")
                .telefono("+51999111111")
                .horario("09:00-18:00")
                .encargado(encargado)
                .build();

        Local local2 = Local.builder()
                .sede("Sede 2")
                .direccion("Dirección 2")
                .telefono("+51999222222")
                .horario("10:00-19:00")
                .encargado(encargado)
                .build();

        localRepository.save(local1);
        localRepository.save(local2);

        // Act
        List<Local> locales = localRepository.findAll();

        // Assert
        assertThat(locales).hasSize(2);
        assertThat(locales).extracting(Local::getSede).containsExactlyInAnyOrder("Sede 1", "Sede 2");
    }

    @Test
    void deberiaRetornarVacioCuandoNoExisteLocal() {
        // Act
        Optional<Local> resultado = localRepository.findById(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }
}