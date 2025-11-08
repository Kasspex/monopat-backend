package com.backend.MonoPat.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    private Long idUsuario;
    private Long idMonopatin;
    private Integer duracionEnMinutos; // El cliente debe enviar esto
}