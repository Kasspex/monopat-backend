package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Incidencia;
import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.repositories.IIncidenciaRepository;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import com.backend.MonoPat.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IncidenciaService implements IIncidenciaService {

    @Autowired
    private IIncidenciaRepository incidenciaRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IMonopatinRepository monopatinRepository;

    @Override
    public Incidencia reportar(Long idUsuario, Long idMonopatin, String tipoProblema) {
        // Buscamos las entidades relacionadas
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Monopatin monopatin = monopatinRepository.findById(idMonopatin).orElse(null);

        if (usuario == null || monopatin == null) {
            throw new RuntimeException("Usuario o Monopatín no encontrado");
        }

        // Creamos la nueva incidencia
        Incidencia nuevaIncidencia = new Incidencia(
                tipoProblema,
                "ABIERTA", // El estado por defecto siempre es "ABIERTA"
                LocalDateTime.now(),
                monopatin,
                usuario
        );

        // Opcional: Cambiamos el estado del monopatín si el problema es grave
        if (tipoProblema.equals("freno_defectuoso")) {
            monopatin.setEstado("en_mantenimiento");
            monopatinRepository.save(monopatin);
        }

        return incidenciaRepository.save(nuevaIncidencia);
    }
}