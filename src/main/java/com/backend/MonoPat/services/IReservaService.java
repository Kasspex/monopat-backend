package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Reserva;

import java.util.List;
import java.util.Optional;

public interface IReservaService {
    Reserva crearReserva(Long idUsuario, Long idMonopatin) throws Exception;
    Reserva finalizarReserva(Long idReserva) throws Exception;
    Optional<Reserva> findById(Long id);
    List<Reserva> findByUsuarioId(Long idUsuario);
}
