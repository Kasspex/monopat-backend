package com.backend.MonoPat.services;

import com.backend.MonoPat.dto.EstadoViajeDTO;
import com.backend.MonoPat.dto.ReservaRequestDTO;
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

import java.time.Duration;
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

    @Value("${monopat.reserva.tarifa-por-minuto}")
    private double tarifaPorMinuto;

    @Override
    @Transactional
    public Reserva crearReserva(ReservaRequestDTO request) throws Exception {

        // Buscar las entidades
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new Exception("Usuario no encontrado..."));

        Monopatin monopatin = monopatinRepository.findById(request.getIdMonopatin())
                .orElseThrow(() -> new Exception("Monopatín no encontrado..."));

        // Validar disponibilidad
        if (!"disponible".equalsIgnoreCase(monopatin.getEstado())) {
            throw new Exception("El monopatín no está disponible.");
        }

        // Modificar estado del monopatín
        monopatin.setEstado("en_uso");
        monopatinRepository.save(monopatin);

        // Calcular valores
        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaFinPagada = fechaInicio.plusMinutes(request.getDuracionEnMinutos());
        double costo = request.getDuracionEnMinutos() * tarifaPorMinuto;

        // Crear y configurar la nueva reserva
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setMonopatin(monopatin);
        nuevaReserva.setFechaInicio(fechaInicio);
        nuevaReserva.setFechaFin(fechaFinPagada); // Fecha fin ESTIMADA (tiempo pagado)
        nuevaReserva.setCosto(costo);

        return reservaRepository.save(nuevaReserva);
    }

    @Override
    @Transactional
    public Reserva finalizarReserva(Long idReserva) throws Exception {
        // Buscar la reserva activa
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new Exception("Reserva no encontrada con id: " + idReserva));

        // Verificar que el monopatín esté en uso (no finalizada previamente)
        if (!"en_uso".equalsIgnoreCase(reserva.getMonopatin().getEstado())) {
            throw new Exception("La reserva con id " + idReserva + " ya ha sido finalizada.");
        }

        // Actualizar la fecha de finalización REAL
        LocalDateTime fechaFinReal = LocalDateTime.now();

        // Calcular el costo basado en el tiempo REAL usado
        long minutosUsados = ChronoUnit.MINUTES.between(reserva.getFechaInicio(), fechaFinReal);
        double costoFinal = minutosUsados * tarifaPorMinuto;

        // Actualizar la reserva con los valores reales
        reserva.setFechaFin(fechaFinReal); // Sobrescribe con el tiempo REAL
        reserva.setCosto(costoFinal);      // Actualiza el costo según tiempo real

        // Liberar el monopatín
        Monopatin monopatin = reserva.getMonopatin();
        monopatin.setEstado("disponible");
        monopatinRepository.save(monopatin);

        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> findReservaActivaByUsuario(Long idUsuario) {
        List<Reserva> todasLasReservas = findByUsuarioId(idUsuario);
        LocalDateTime ahora = LocalDateTime.now();

        return todasLasReservas.stream()
                .filter(reserva -> {
                    // Condición 1: El monopatín debe estar "en_uso"
                    boolean monopatinEnUso = reserva.getMonopatin() != null &&
                            "en_uso".equalsIgnoreCase(reserva.getMonopatin().getEstado());

                    // Condición 2: La reserva debe tener una fecha de inicio
                    boolean tieneInicio = reserva.getFechaInicio() != null;

                    // CORRECCIÓN CRÍTICA: La reserva NO debe haber expirado
                    boolean noHaExpirado = reserva.getFechaFin() != null &&
                            ahora.isBefore(reserva.getFechaFin());

                    // Solo es activa si cumple las 3 condiciones
                    return monopatinEnUso && tieneInicio && noHaExpirado;
                })
                // IMPORTANTE: Si hay varias, devuelve la más reciente
                .max((r1, r2) -> r1.getIdReserva().compareTo(r2.getIdReserva()));
    }

    @Override
    public EstadoViajeDTO calcularEstadoActual(Reserva reservaActiva) {
        LocalDateTime horaInicio = reservaActiva.getFechaInicio();
        LocalDateTime horaFinPagada = reservaActiva.getFechaFin();
        LocalDateTime ahora = LocalDateTime.now();

        // Tiempo transcurrido desde el inicio
        long tiempoTranscurrido = Duration.between(horaInicio, ahora).toMinutes();

        // Tiempo restante del pago
        long tiempoRestante = Duration.between(ahora, horaFinPagada).toMinutes();

        // Si ya expiró, el tiempo restante es 0
        if (tiempoRestante < 0) {
            tiempoRestante = 0;
        }

        return new EstadoViajeDTO(
                tiempoTranscurrido,
                tiempoRestante,
                reservaActiva.getCosto()
        );
    }

    /**
     * NUEVA FUNCIÓN: Libera monopatines cuyo tiempo pagado ha expirado
     * Esta función debe ser llamada por tu tarea programada (@Scheduled)
     */
    @Transactional
    public int liberarMonopatinesExpirados() {
        LocalDateTime ahora = LocalDateTime.now();
        int liberados = 0;

        // Buscar todas las reservas con monopatines "en_uso"
        List<Reserva> reservasActivas = reservaRepository.findAll().stream()
                .filter(r -> r.getMonopatin() != null &&
                        "en_uso".equalsIgnoreCase(r.getMonopatin().getEstado()))
                .toList();

        for (Reserva reserva : reservasActivas) {
            // Solo liberar si el tiempo PAGADO ha expirado
            if (reserva.getFechaFin() != null && ahora.isAfter(reserva.getFechaFin())) {

                // Actualizar el monopatín a disponible
                Monopatin monopatin = reserva.getMonopatin();
                monopatin.setEstado("disponible");
                monopatinRepository.save(monopatin);

                // NO modificamos la reserva aquí, eso lo hace finalizarReserva
                // cuando el usuario termine manualmente

                liberados++;
            }
        }

        return liberados;
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