package com.backend.MonoPat.controllers;


import com.backend.MonoPat.entities.Reserva;
import com.backend.MonoPat.services.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> crearReserva(@RequestBody Map<String, Long> request) {
        try {
            Long idUsuario = request.get("idUsuario");
            Long idMonopatin = request.get("idMonopatin");
            if (idUsuario == null || idMonopatin == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan los IDs de usuario y/o monopatín.");
            }
            Reserva nuevaReserva = reservaService.crearReserva(idUsuario, idMonopatin);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (Exception e) {
            // Captura errores de negocio como "Monopatín no disponible"
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

}
