package com.backend.MonoPat.controllers;

import com.backend.MonoPat.entities.Persona;
import com.backend.MonoPat.services.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @GetMapping
    public List<Persona> findAll(){
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Persona> personaOptional = personaService.findById(id);

        if (personaOptional.isPresent()) {
            Persona personaEncontrada = personaOptional.get();
            return ResponseEntity.ok(personaEncontrada); // Devuelve ResponseEntity<Persona>
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada."); // Devuelve ResponseEntity<String>
        }
    }
    @PostMapping
    public ResponseEntity<Persona> save(@RequestBody Persona persona) {
        // @RequestBody convierte el JSON del cuerpo de la petición a un objeto Persona.
        Persona nuevaPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona); // Devuelve 201 Created.
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Persona personaDetails) {
        try {
            Persona updatedPersona = personaService.update(id, personaDetails);
            return ResponseEntity.ok(updatedPersona);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            // Verificamos si existe antes de borrar para dar una respuesta más clara.
            if (!personaService.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada con id: " + id);
            }
            personaService.deleteById(id);
            return ResponseEntity.ok("Persona eliminada correctamente."); // Devuelve 200 OK con un mensaje.
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la persona.");
        }
    }
}
