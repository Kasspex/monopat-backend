package com.backend.MonoPat.repositories;

import com.backend.MonoPat.entities.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIncidenciaRepository extends JpaRepository<Incidencia, Long> {
    // Aquí puedes añadir métodos de búsqueda en el futuro
    // List<Incidencia> findByEstado(String estado);
}