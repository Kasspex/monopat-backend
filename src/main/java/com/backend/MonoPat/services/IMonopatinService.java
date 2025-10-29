package com.backend.MonoPat.services;

import com.backend.MonoPat.dto.UbicacionRequestDTO;
import com.backend.MonoPat.entities.Monopatin;

import java.util.List;
import java.util.Optional;

public interface IMonopatinService {
    Monopatin save(Monopatin monopatin);
    Optional<Monopatin> findById(Long id);
    List<Monopatin> findAll();
    void deleteById(Long id);
    Monopatin update(Long id, Monopatin monopatinDetails);
    List<Monopatin> findByEstado(String estado);
    List<Monopatin> encontrarCercanos(UbicacionRequestDTO ubicacion);
}
