package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Incidencia;

public interface IIncidenciaService {

    Incidencia reportar(Long idUsuario, Long idMonopatin, String tipoProblema);


}
