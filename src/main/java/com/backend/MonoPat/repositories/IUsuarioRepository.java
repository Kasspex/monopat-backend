package com.backend.MonoPat.repositories;

import com.backend.MonoPat.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional <Usuario> findByCorreo(String correo);
}
