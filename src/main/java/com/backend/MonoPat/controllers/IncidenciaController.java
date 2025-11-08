package com.backend.MonoPat.controllers;

import com.backend.MonoPat.dto.ReporteIncidenciaDTO;
import com.backend.MonoPat.services.IIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incidencias") // Define la ruta base
public class IncidenciaController {

    @Autowired
    private IIncidenciaService incidenciaService;

    @PostMapping("/reportar")
    public ResponseEntity<?> reportarIncidencia(@RequestBody ReporteIncidenciaDTO reporte) {
        try {
            incidenciaService.reportar(
                    reporte.getIdUsuario(),
                    reporte.getIdMonopatin(),
                    reporte.getTipoProblema()
            );
            return ResponseEntity.ok().body("Incidencia reportada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al reportar: " + e.getMessage());
        }
    }
}