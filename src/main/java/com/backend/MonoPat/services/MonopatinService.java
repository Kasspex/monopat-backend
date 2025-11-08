package com.backend.MonoPat.services;

import com.backend.MonoPat.dto.UbicacionRequestDTO;
import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import com.backend.MonoPat.utils.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MonopatinService implements IMonopatinService{

    @Autowired
    private IMonopatinRepository monopatinRepository;


    @Override
    @Transactional
    public Monopatin save(Monopatin monopatin) {
        // Lógica de negocio antes de guardar:
        // Por ejemplo, asegurar que el estado inicial siempre sea "disponible".
        if (monopatin.getIdMonopatin() == null) { // Solo si es un monopatín nuevo
            monopatin.setEstado("disponible");
        }
        return monopatinRepository.save(monopatin);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Monopatin> findById(Long id) {
        return monopatinRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Monopatin> findAll() {
        return monopatinRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!monopatinRepository.existsById(id)) {
            throw new RuntimeException("No se puede borrar, monopatín no encontrado con id: " + id);
        }
        monopatinRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Monopatin update(Long id, Monopatin monopatinDetails) {
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id: " + id));

        // Actualizamos los campos que pueden cambiar
        monopatin.setEstado(monopatinDetails.getEstado());
        monopatin.setLatitud(monopatinDetails.getLatitud());
        monopatin.setLongitud(monopatinDetails.getLongitud());

        return monopatinRepository.save(monopatin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Monopatin> findByEstado(String estado) {
        // Antes de buscar, podríamos validar que el estado sea uno de los permitidos
        // ("disponible", "en_uso", "mantenimiento") para evitar búsquedas inválidas.
        return monopatinRepository.findByEstado(estado);
    }

//N8N

    @Override
    public List<Monopatin> encontrarCercanos(UbicacionRequestDTO ubicacion) {
        // 1. Definimos un radio de búsqueda
        final double RADIO_DE_BUSQUEDA_KM = 1.0;

        // 2. Obtenemos TODOS los monopatines que estén disponibles.
        List<Monopatin> todosLosDisponibles = monopatinRepository.findByEstado("disponible");
        List<Monopatin> monopatinesCercanos = new ArrayList<>();

        // 3. Recorremos y filtramos los que están dentro del radio
        for (Monopatin monopatin : todosLosDisponibles) {
            double distancia = GeoUtils.calcularDistancia(
                    ubicacion.getLatitud(),
                    ubicacion.getLongitud(),
                    monopatin.getLatitud(),
                    monopatin.getLongitud()
            );

            if (distancia <= RADIO_DE_BUSQUEDA_KM) {
                monopatinesCercanos.add(monopatin);
            }
        }

        // --- ¡AQUÍ ESTÁ LA NUEVA LÓGICA! ---
        // 4. Ordenamos la lista de resultados por distancia (de menor a mayor)
        monopatinesCercanos.sort(Comparator.comparingDouble(m ->
                GeoUtils.calcularDistancia(
                        ubicacion.getLatitud(),
                        ubicacion.getLongitud(),
                        m.getLatitud(),
                        m.getLongitud()
                )
        ));
        // --- FIN DE LA NUEVA LÓGICA ---

        // 5. Devolvemos la lista, ahora ordenada por cercanía.
        return monopatinesCercanos;
    }
}
