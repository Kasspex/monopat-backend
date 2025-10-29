package com.backend.MonoPat.repositories;

import com.backend.MonoPat.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);

    List<Reserva> findByMonopatin_IdMonopatin(Long idMonopatin);
}
