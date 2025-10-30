package com.backend.MonoPat.services;



import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.entities.Reserva;
import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import com.backend.MonoPat.repositories.IReservaRepository;
import com.backend.MonoPat.repositories.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    // 2. Crea un "mock" (un doble de acción) para cada dependencia
    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private IUsuarioRepository usuarioRepository;
    @Mock
    private IMonopatinRepository monopatinRepository;

    // 3. Crea una instancia real de la clase que queremos probar e inyecta los mocks en ella
    @InjectMocks
    private ReservaService reservaService;

    // 4. Define un método de prueba con la anotación @Test
    @Test
    void testCrearReserva_Exitosamente() throws Exception {
        // --- ARRANGE (Preparar el escenario) ---

        // a. Crea los objetos de prueba que necesitaremos
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        Monopatin monopatinDisponible = new Monopatin();
        monopatinDisponible.setIdMonopatin(10L);
        monopatinDisponible.setEstado("disponible");

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setIdReserva(100L);
        reservaGuardada.setUsuario(usuario);
        reservaGuardada.setMonopatin(monopatinDisponible);
        reservaGuardada.setFechaInicio(LocalDateTime.now());

        // b. Define el comportamiento de los mocks (les damos su guion)
        // "Cuando alguien llame a usuarioRepository.findById con el ID 1, devuelve nuestro usuario de prueba"
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // "Cuando alguien llame a monopatinRepository.findById con el ID 10, devuelve nuestro monopatín de prueba"
        when(monopatinRepository.findById(10L)).thenReturn(Optional.of(monopatinDisponible));

        // "Cuando alguien llame a reservaRepository.save con CUALQUIER objeto Reserva, devuelve nuestra reserva de prueba"
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);


        // --- ACT (Ejecutar la acción que queremos probar) ---
        Reserva resultado = reservaService.crearReserva(1L, 10L);


        // --- ASSERT (Verificar que los resultados son los esperados) ---

        // a. Verifica que el resultado no es nulo
        assertNotNull(resultado);
        // b. Verifica que el ID de la reserva es el que esperamos
        assertEquals(100L, resultado.getIdReserva());
        // c. Verifica que el estado del monopatín se actualizó a "en_uso"
        assertEquals("en_uso", monopatinDisponible.getEstado());

        // d. (Opcional pero muy útil) Verifica que los métodos de los mocks fueron llamados
        verify(usuarioRepository, times(1)).findById(1L); // Se llamó a findById 1 vez
        verify(monopatinRepository, times(1)).findById(10L);
        verify(monopatinRepository, times(1)).save(monopatinDisponible); // Se guardó el monopatín actualizado
        verify(reservaRepository, times(1)).save(any(Reserva.class)); // Se guardó la nueva reserva
    }

    @Test
    void testCrearReserva_FallaSiMonopatinNoDisponible() {
        // --- ARRANGE ---
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        Monopatin monopatinOcupado = new Monopatin();
        monopatinOcupado.setIdMonopatin(10L);
        monopatinOcupado.setEstado("en_uso");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(monopatinRepository.findById(10L)).thenReturn(Optional.of(monopatinOcupado));

        // --- ACT & ASSERT ---
        // Verifica que al llamar al método, se lanza una excepción de tipo 'Exception'
        Exception exception = assertThrows(Exception.class, () -> {
            reservaService.crearReserva(1L, 10L);
        });

        // Verifica que el mensaje de la excepción es el correcto
        assertTrue(exception.getMessage().contains("no está disponible"));

        // Verifica que el método 'save' NUNCA fue llamado
        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}
