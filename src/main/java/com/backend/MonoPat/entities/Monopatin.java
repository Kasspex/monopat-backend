package com.backend.MonoPat.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMonopatin")
    private Long idMonopatin;

    @Column(nullable = false)
    private String estado; // Ejemplo: "disponible", "en_uso", "mantenimiento"

    @Column(nullable = false)
    private double latitud;

    @Column(nullable = false)
    private double longitud;
}