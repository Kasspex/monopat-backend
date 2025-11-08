package com.backend.MonoPat.services;

import com.backend.MonoPat.dto.EstadoViajeDTO;
import com.backend.MonoPat.dto.ReservaRequestDTO;
import com.backend.MonoPat.entities.Reserva;

import java.util.List;
import java.util.Optional;

public interface IReservaService {
    Reserva crearReserva(ReservaRequestDTO request) throws Exception;
    Reserva finalizarReserva(Long idReserva) throws Exception;
    Optional<Reserva> findById(Long id);
    List<Reserva> findByUsuarioId(Long idUsuario);
    Optional<Reserva> findReservaActivaByUsuario(Long idUsuario);
    EstadoViajeDTO calcularEstadoActual(Reserva reservaActiva);
}
