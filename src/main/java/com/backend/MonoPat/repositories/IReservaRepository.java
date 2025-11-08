package com.backend.MonoPat.repositories;

import com.backend.MonoPat.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    // Métodos existentes
    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);

    List<Reserva> findByMonopatin_IdMonopatin(Long idMonopatin);

    /**
     * SOLUCIÓN FINAL:
     *
     * Encuentra solo la reserva MÁS RECIENTE (mayor id_reserva) de cada monopatín
     * que cumple estas condiciones:
     * 1. El tiempo pagado (fechaFin) ya expiró (fechaFin < ahora)
     * 2. El monopatín está en estado "en_uso"
     *
     * Esto evita que se procesen múltiples reservas históricas del mismo monopatín.
     */
    @Query("SELECT r FROM Reserva r " +
            "JOIN r.monopatin m " +
            "WHERE r.fechaFin < :ahora " +
            "AND m.estado = 'en_uso' " +
            "AND r.idReserva = (" +
            "    SELECT MAX(r2.idReserva) " +
            "    FROM Reserva r2 " +
            "    WHERE r2.monopatin.idMonopatin = r.monopatin.idMonopatin" +
            ")")
    List<Reserva> findReservasExpiradasYActivas(@Param("ahora") LocalDateTime ahora);
}