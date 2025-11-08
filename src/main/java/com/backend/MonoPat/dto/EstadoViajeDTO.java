package com.backend.MonoPat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoViajeDTO {

    private long tiempoTranscurridoMinutos;
    private long tiempoRestanteMinutos;
    private double costoTotalPagado;


}