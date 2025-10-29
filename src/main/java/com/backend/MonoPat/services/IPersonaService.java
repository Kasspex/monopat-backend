package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Persona;

import java.util.List;
import java.util.Optional;

public interface IPersonaService {
    List<Persona> findAll();
    Optional<Persona> findById(Long id);
    Persona save(Persona persona);
    void deleteById(Long id);
    Persona update(Long id, Persona personaDetails);
}
