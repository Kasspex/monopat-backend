package com.backend.MonoPat.controllers;

import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    // Endpoint para OBTENER un usuario por su ID
    // Petición: GET http://localhost:8080/api/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
        }
    }

    // Endpoint para OBTENER un usuario por su CORREO
    // Petición: GET http://localhost:8080/api/usuarios/correo/juan@example.com
    @GetMapping("/correo/{correo}")
    public ResponseEntity<?> findByCorreo(@PathVariable String correo) {
        Optional<Usuario> usuarioOptional = usuarioService.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con correo: " + correo);
        }
    }

    // Endpoint para CREAR un nuevo usuario
    // Petición: POST http://localhost:8080/api/usuarios
    /* Body (JSON):
       {
           "correo": "ana@example.com",
           "contrasena": "secreto123",
           "persona": {
               "nombre": "Ana",
               "apellido": "Gomez",
               "edad": 25,
               "telefono": "987654321"
           }
       }
    */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            // Este error viene de nuestra validación en el servicio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
