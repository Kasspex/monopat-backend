package com.backend.MonoPat.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReserva")
    private Long idReserva;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private double costo;

    @ManyToOne // Muchas reservas pueden ser hechas por un mismo usuario.
    @JoinColumn(name = "idUsuario", nullable = false) // Columna FK en la tabla Reserva.
    private Usuario usuario;

    @ManyToOne // Muchas reservas pueden involucrar al mismo monopatín (en diferentes momentos).
    @JoinColumn(name = "idMonopatin", nullable = false) // Columna FK en la tabla Reserva.
    private Monopatin monopatin;
}