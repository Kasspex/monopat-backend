package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Persona;
import com.backend.MonoPat.repositories.IPersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private IPersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    @Test
    void testFindAll_DevuelveListaDePersonas() {
        // --- ARRANGE ---
        Persona persona1 = new Persona(1L, "Juan", "Perez", 30, "123");
        Persona persona2 = new Persona(2L, "Ana", "Gomez", 25, "456");
        when(personaRepository.findAll()).thenReturn(Arrays.asList(persona1, persona2));

        // --- ACT ---
        List<Persona> personas = personaService.findAll();

        // --- ASSERT ---
        assertNotNull(personas);
        assertEquals(2, personas.size());
        verify(personaRepository).findAll();
    }

    @Test
    void testFindById_DevuelvePersonaExistente() {
        // --- ARRANGE ---
        Persona persona = new Persona(1L, "Juan", "Perez", 30, "123");
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // --- ACT ---
        Optional<Persona> resultado = personaService.findById(1L);

        // --- ASSERT ---
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
        verify(personaRepository).findById(1L);
    }
}