package com.backend.MonoPat.controllers;


import com.backend.MonoPat.dto.EstadoViajeDTO;
import com.backend.MonoPat.dto.ReservaRequestDTO;
import com.backend.MonoPat.entities.Reserva;
import com.backend.MonoPat.services.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    // Endpoint para CREAR una nueva reserva (iniciar un viaje)
    // Petición: POST http://localhost:8080/api/reservas/iniciar
    /* Body (JSON):
       {
           "idUsuario": 1,
           "idMonopatin": 3
       }
    */
    @PostMapping("/iniciar")
    // 1. Cambia el @RequestBody a tu nuevo DTO
    public ResponseEntity<?> crearReserva(@RequestBody ReservaRequestDTO request) {
        try {
            // 2. Valida la nueva solicitud
            if (request.getIdUsuario() == null || request.getIdMonopatin() == null || request.getDuracionEnMinutos() == null || request.getDuracionEnMinutos() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan IDs o una duración válida en minutos.");
            }

            // 3. Pasa el objeto DTO completo al servicio
            Reserva nuevaReserva = reservaService.crearReserva(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Endpoint para FINALIZAR una reserva (terminar un viaje)
    // Petición: POST http://localhost:8080/api/reservas/finalizar/5
    @PostMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarReserva(@PathVariable Long id) {
        try {
            Reserva reservaFinalizada = reservaService.finalizarReserva(id);
            return ResponseEntity.ok(reservaFinalizada);
        } catch (Exception e) {
            // Captura errores como "Reserva no encontrada" o "Reserva ya finalizada"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para OBTENER una reserva por su ID
    // Petición: GET http://localhost:8080/api/reservas/5
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Reserva> reservaOptional = reservaService.findById(id);
        if (reservaOptional.isPresent()) {
            return ResponseEntity.ok(reservaOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada con id: " + id);
    }

    // Endpoint para OBTENER todas las reservas de un usuario
    // Petición: GET http://localhost:8080/api/reservas/usuario/1
    @GetMapping("/usuario/{idUsuario}")
    public List<Reserva> findByUsuarioId(@PathVariable Long idUsuario) {
        return reservaService.findByUsuarioId(idUsuario);
    }

    // Endpoint para OBTENER EL ESTADO de un viaje activo
    /**
     * Endpoint para OBTENER EL ESTADO de un viaje activo
     * GET http://localhost:8080/reservas/estado/usuario/1
     *
     * CORRECCIÓN: Ahora devuelve un objeto limpio, no un array
     */
    @GetMapping("/estado/usuario/{idUsuario}")
    public ResponseEntity<?> getEstadoViajeActivo(@PathVariable Long idUsuario) {

        // 1. Busca la reserva activa
        Optional<Reserva> reservaActivaOpt = reservaService.findReservaActivaByUsuario(idUsuario);

        // 2. Si no hay reserva activa
        if (!reservaActivaOpt.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se encontró ningún viaje activo para este usuario.");
            errorResponse.put("tieneViajeActivo", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // 3. Si hay reserva activa, calcula el estado
        Reserva reservaActiva = reservaActivaOpt.get();
        EstadoViajeDTO estado = reservaService.calcularEstadoActual(reservaActiva);

        // 4. Construir respuesta enriquecida
        Map<String, Object> response = new HashMap<>();
        response.put("tieneViajeActivo", true);
        response.put("idReserva", reservaActiva.getIdReserva());
        response.put("idMonopatin", reservaActiva.getMonopatin().getIdMonopatin());
        response.put("tiempoTranscurridoMinutos", estado.getTiempoTranscurridoMinutos());
        response.put("tiempoRestanteMinutos", estado.getTiempoRestanteMinutos());
        response.put("costoTotalPagado", estado.getCostoTotalPagado());
        response.put("fechaInicio", reservaActiva.getFechaInicio());
        response.put("fechaFinEstimada", reservaActiva.getFechaFin());

        // IMPORTANTE: Devuelve el Map directamente, Spring lo convierte a JSON
        return ResponseEntity.ok(response);
    }
}
