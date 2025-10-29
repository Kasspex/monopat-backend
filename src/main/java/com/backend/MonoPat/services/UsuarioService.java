package com.backend.MonoPat.services;


import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.repositories.IPersonaRepository;
import com.backend.MonoPat.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo.toLowerCase());
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        // Lógica de negocio antes de guardar:
        if (usuario.getPersona() == null) {
            throw new IllegalArgumentException("El usuario debe tener una persona asociada.");
        }

        // ¡Ya no necesitamos guardar la persona manualmente!
        // personaRepository.save(usuario.getPersona()); <--- BORRA ESTA LÍNEA

        // Simplemente guardamos el usuario. JPA en cascada guardará la persona.
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Actualizamos los campos del usuario
        usuario.setCorreo(usuarioDetails.getCorreo());
        // ¡OJO! La contraseña se debe manejar con un proceso especial de encriptación,
        // nunca se debe actualizar directamente de esta forma en una app real.
        usuario.setContrasena(usuarioDetails.getContrasena());

        // También podrías querer actualizar los datos de la persona asociada
        if (usuario.getPersona() != null && usuarioDetails.getPersona() != null) {
            usuario.getPersona().setNombre(usuarioDetails.getPersona().getNombre());
            usuario.getPersona().setApellido(usuarioDetails.getPersona().getApellido());
            usuario.getPersona().setEdad(usuarioDetails.getPersona().getEdad());
            usuario.getPersona().setTelefono(usuarioDetails.getPersona().getTelefono());
            // ... otros campos de persona
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // La lógica aquí podría ser compleja. ¿Borrar el usuario borra también a la persona?
        // Depende de las reglas de tu negocio.
        // Por ahora, asumimos que solo borra al usuario.
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede borrar, usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
