package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.entities.Reserva;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import com.backend.MonoPat.repositories.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TareaProgramadaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IMonopatinRepository monopatinRepository;

    /**
     * Esta tarea se ejecuta automáticamente cada minuto.
     * fixedRate = 60000 milisegundos = 1 minuto.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void liberarMonopatinesExpirados() {
        System.out.println("[" + LocalDateTime.now() + "] --- Ejecutando tarea programada: Verificando monopatines... ---");

        LocalDateTime ahora = LocalDateTime.now();
        List<Reserva> reservasExpiradas = reservaRepository.findReservasExpiradasYActivas(ahora);

        if (reservasExpiradas.isEmpty()) {
            System.out.println("--- No hay reservas expiradas en este momento. ---");
            return;
        }

        System.out.println("--- Encontradas " + reservasExpiradas.size() + " reservas expiradas. ---");

        // Usamos un Set para evitar procesar el mismo monopatín dos veces
        Set<Long> monopatinesYaProcesados = new HashSet<>();
        int liberados = 0;

        for (Reserva r : reservasExpiradas) {
            Monopatin m = r.getMonopatin();
            Long idMonopatin = m.getIdMonopatin();

            // Si ya procesamos este monopatín, saltamos
            if (monopatinesYaProcesados.contains(idMonopatin)) {
                System.out.println("  -> Monopatín #" + idMonopatin + " ya fue procesado, omitiendo...");
                continue;
            }

            // Verificar que el monopatín esté en uso
            if ("en_uso".equalsIgnoreCase(m.getEstado())) {
                System.out.println("  -> Liberando monopatín #" + idMonopatin +
                        " (Reserva #" + r.getIdReserva() +
                        ", usuario: " + r.getUsuario().getIdUsuario() +
                        ", expiró: " + r.getFechaFin() + ")");

                m.setEstado("disponible");
                monopatinRepository.save(m);
                monopatinesYaProcesados.add(idMonopatin);
                liberados++;
            } else {
                System.out.println("  -> Monopatín #" + idMonopatin +
                        " ya no está en_uso (estado actual: " + m.getEstado() + "), omitiendo...");
            }
        }

        if (liberados > 0) {
            System.out.println("--- ✓ Se liberaron " + liberados + " monopatines exitosamente. ---");
        } else {
            System.out.println("--- No se liberó ningún monopatín (todos ya estaban disponibles). ---");
        }
    }
}