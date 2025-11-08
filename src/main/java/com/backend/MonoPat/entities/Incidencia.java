package com.backend.MonoPat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;

    @Column(nullable = false)
    private String tipoProblema; // ej: "freno_defectuoso", "llanta_pinchada"

    @Column(nullable = false)
    private String estado; // ej: "ABIERTA", "EN_REVISION", "RESUELTA"

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    // Relación: Muchas incidencias pueden pertenecer a UN monopatín
    @ManyToOne
    @JoinColumn(name = "id_monopatin", nullable = false)
    private Monopatin monopatin;

    // Relación: Muchas incidencias pueden ser creadas por UN usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Incidencia(String tipoProblema, String estado, LocalDateTime fechaReporte, Monopatin monopatin, Usuario usuario) {
        this.tipoProblema = tipoProblema;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
        this.monopatin = monopatin;
        this.usuario = usuario;
    }
}