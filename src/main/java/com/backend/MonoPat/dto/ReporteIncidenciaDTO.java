package com.backend.MonoPat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para recibir el reporte. Créalo en el paquete 'dto'
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteIncidenciaDTO {
     private Long idUsuario;
    private Long idMonopatin;
    private String tipoProblema;

}
