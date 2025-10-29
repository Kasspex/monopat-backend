package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCorreo(String correo);
    Usuario save(Usuario usuario);
    Usuario update(Long id, Usuario usuarioDetails);
    void deleteById(Long id);
}
