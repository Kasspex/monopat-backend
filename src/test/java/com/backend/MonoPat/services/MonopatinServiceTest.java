package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonopatinServiceTest {

    @Mock
    private IMonopatinRepository monopatinRepository;

    @InjectMocks
    private MonopatinService monopatinService;

    @Test
    void testSave_EstableceEstadoDisponibleParaNuevosMonopatines() {
        // --- ARRANGE ---
        // 1. Crea un monopatín de prueba tal como vendría del frontend (sin estado y sin ID).
        Monopatin monopatinNuevo = new Monopatin();
        monopatinNuevo.setIdMonopatin(null); // Es nuevo
        monopatinNuevo.setLatitud(4.43);
        monopatinNuevo.setLongitud(-75.23);

        // 2. Preparamos un "captor" para espiar qué objeto se manda a guardar.
        ArgumentCaptor<Monopatin> monopatinCaptor = ArgumentCaptor.forClass(Monopatin.class);

        // --- ACT ---
        // 3. Llamamos al método a probar.
        monopatinService.save(monopatinNuevo);

        // --- ASSERT ---
        // 4. Capturamos el objeto que se pasó al método 'save' del repositorio.
        verify(monopatinRepository).save(monopatinCaptor.capture());
        Monopatin monopatinGuardado = monopatinCaptor.getValue();

        // 5. Verificamos que el estado fue añadido correctamente por el servicio.
        assertNotNull(monopatinGuardado);
        assertEquals("disponible", monopatinGuardado.getEstado());
    }
}