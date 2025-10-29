package com.backend.MonoPat.controllers;


import com.backend.MonoPat.dto.UbicacionRequestDTO;
import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.services.IMonopatinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

    @Autowired
    private IMonopatinService monopatinService;

    @GetMapping
    public List<Monopatin> findAll() {
        return monopatinService.findAll();
    }

    // Endpoint para OBTENER un monopatín por su ID
    // Petición: GET http://localhost:8080/api/monopatines/1
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Monopatin> monopatinOptional = monopatinService.findById(id);
        if (monopatinOptional.isPresent()) {
            return ResponseEntity.ok(monopatinOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monopatín no encontrado con id: " + id);
    }

    // Endpoint para BUSCAR monopatines por estado
    // Petición: GET http://localhost:8080/api/monopatines/estado/disponible
    @GetMapping("/estado/{estado}")
    public List<Monopatin> findByEstado(@PathVariable String estado) {
        return monopatinService.findByEstado(estado);
    }

    // Endpoint para CREAR un nuevo monopatín
    // Petición: POST http://localhost:8080/api/monopatines
    /* Body (JSON):
       {
           "latitud": 4.438889,
           "longitud": -75.232222
       }
       Nota: No necesitamos enviar el estado, el servicio lo pondrá en "disponible" por defecto.
    */
    @PostMapping
    public ResponseEntity<Monopatin> save(@RequestBody Monopatin monopatin) {
        Monopatin nuevoMonopatin = monopatinService.save(monopatin);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMonopatin);
    }

    // Endpoint para ACTUALIZAR un monopatín
    // Petición: PUT http://localhost:8080/api/monopatines/1
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Monopatin monopatinDetails) {
        try {
            Monopatin updatedMonopatin = monopatinService.update(id, monopatinDetails);
            return ResponseEntity.ok(updatedMonopatin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para BORRAR un monopatín
    // Petición: DELETE http://localhost:8080/api/monopatines/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            monopatinService.deleteById(id);
            return ResponseEntity.ok("Monopatín eliminado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    //N8N requests

    @PostMapping("/cercanos")
    public ResponseEntity<List<Monopatin>> buscarMonopatinesCercanos(@RequestBody UbicacionRequestDTO ubicacion) {

        // Aquí llamas a la lógica de tu servicio (que tendrás que implementar)
        // para que busque en la base de datos.
        List<Monopatin> monopatinesCercanos = monopatinService.encontrarCercanos(ubicacion);

        // Devuelves la lista de monopatines encontrados con un código de estado "200 OK".
        return ResponseEntity.ok(monopatinesCercanos);
    }
}
