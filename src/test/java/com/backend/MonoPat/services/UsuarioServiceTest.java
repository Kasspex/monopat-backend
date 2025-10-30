package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Persona;
import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.repositories.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testSave_Exitosamente() {
        // --- ARRANGE ---
        // 1. Crea un usuario de prueba completo, con su persona asociada.
        Persona persona = new Persona();
        persona.setNombre("Ana");

        Usuario usuarioParaGuardar = new Usuario();
        usuarioParaGuardar.setCorreo("ana@example.com");
        usuarioParaGuardar.setPersona(persona);

        // 2. Define el comportamiento del mock del repositorio.
        // "Cuando se llame a save, devuelve el mismo objeto que se le pasó"
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioParaGuardar);

        // --- ACT ---
        // 3. Llama al método que queremos probar.
        Usuario usuarioGuardado = usuarioService.save(usuarioParaGuardar);

        // --- ASSERT ---
        // 4. Verifica que el resultado es el esperado.
        assertNotNull(usuarioGuardado);
        assertEquals("ana@example.com", usuarioGuardado.getCorreo());
        assertNotNull(usuarioGuardado.getPersona());
        verify(usuarioRepository).save(usuarioParaGuardar); // Verifica que el repositorio fue llamado.
    }

    @Test
    void testSave_FallaSiPersonaEsNula() {
        // --- ARRANGE ---
        // 1. Crea un usuario de prueba inválido (sin persona).
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setCorreo("test@example.com");
        usuarioInvalido.setPersona(null); // Explícitamente nulo

        // --- ACT & ASSERT ---
        // 2. Verifica que al llamar al método 'save' con el usuario inválido,
        //    se lanza la excepción que esperamos (IllegalArgumentException).
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.save(usuarioInvalido);
        });

        // 3. Muy importante: verifica que el método 'save' del repositorio NUNCA fue llamado.
        //    Esto nos asegura que la validación detuvo el proceso antes de intentar guardar en la BD.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}