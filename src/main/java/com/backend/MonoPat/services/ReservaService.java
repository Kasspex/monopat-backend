package com.backend.MonoPat.services;

import com.backend.MonoPat.entities.Monopatin;
import com.backend.MonoPat.entities.Reserva;
import com.backend.MonoPat.entities.Usuario;
import com.backend.MonoPat.repositories.IMonopatinRepository;
import com.backend.MonoPat.repositories.IReservaRepository;
import com.backend.MonoPat.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService implements IReservaService{

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IMonopatinRepository monopatinRepository;

    @Value("${monopat.reserva.tarifa-por-minuto}") // <-- Spring inyecta el valor desde application.properties
    private double tarifaPorMinuto;

    @Override
    @Transactional
    public Reserva crearReserva(Long idUsuario, Long idMonopatin) throws Exception {
        // 1. Buscar las entidades necesarias
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + idUsuario));

        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new Exception("Monopatín no encontrado con id: " + idMonopatin));

        // 2. Aplicar la REGLA DE NEGOCIO más importante
        if (!"disponible".equalsIgnoreCase(monopatin.getEstado())) {
            throw new Exception("El monopatín con id " + idMonopatin + " no está disponible.");
        }

        // 3. Modificar el estado del monopatín
        monopatin.setEstado("en_uso");
        monopatinRepository.save(monopatin);

        // 4. Crear y configurar la nueva reserva
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setMonopatin(monopatin);
        nuevaReserva.setFechaInicio(LocalDateTime.now());
        // fechaFin y costo se dejan nulos hasta que finalice.

        return reservaRepository.save(nuevaReserva);
    }
    @Override
    @Transactional
    public Reserva finalizarReserva(Long idReserva) throws Exception {
        // 1. Buscar la reserva activa
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new Exception("Reserva no encontrada con id: " + idReserva));

        // 2. REGLA DE NEGOCIO: No se puede finalizar una reserva que ya terminó.
        if (reserva.getFechaFin() != null) {
            throw new Exception("La reserva con id " + idReserva + " ya ha sido finalizada.");
        }

        // 3. Actualizar la reserva
        reserva.setFechaFin(LocalDateTime.now());

        // 4. Calcular el costo
        long minutos = ChronoUnit.MINUTES.between(reserva.getFechaInicio(), reserva.getFechaFin());
        reserva.setCosto(minutos * tarifaPorMinuto);

        // 5. Liberar el monopatín
        Monopatin monopatin = reserva.getMonopatin();
        monopatin.setEstado("disponible");
        monopatinRepository.save(monopatin);

        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findByUsuarioId(Long idUsuario) {
        return reservaRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
