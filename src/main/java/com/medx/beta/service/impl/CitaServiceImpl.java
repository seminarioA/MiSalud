package com.medx.beta.service.impl;

import com.medx.beta.dto.CitaRequest;
import com.medx.beta.dto.CitaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Cita;
import com.medx.beta.model.Consultorio;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.model.Seguro;
import com.medx.beta.model.UsuarioSistema;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.ConsultorioRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.repository.SeguroRepository;
import com.medx.beta.repository.UsuarioSistemaRepository;
import com.medx.beta.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CitaServiceImpl implements CitaService {

        private final CitaRepository citaRepository;
        private final PacienteRepository pacienteRepository;
        private final DoctorRepository doctorRepository;
        private final ConsultorioRepository consultorioRepository;
        private final SeguroRepository seguroRepository;
        private final com.medx.beta.repository.AusenciaMedicoRepository ausenciaRepository;
        private final UsuarioSistemaRepository usuarioSistemaRepository;

        @Override
        @Transactional(readOnly = true)
        public List<CitaResponse> findAll() {
                return citaRepository.findAll().stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public CitaResponse findById(Long id) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
                return toResponse(cita);
        }

        @Override
        public CitaResponse create(CitaRequest request) {
                Paciente paciente = pacienteRepository.findById(request.pacienteId())
                                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
                Doctor doctor = doctorRepository.findById(request.doctorId())
                                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
                Consultorio consultorio = consultorioRepository.findById(request.consultorioId())
                                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));

                validarAusenciaMedico(doctor.getId(), request.fechaCita(), request.horaCita());

                boolean ocupado = citaRepository.existsByDoctorIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                doctor.getId(),
                                request.fechaCita(),
                                request.horaCita(),
                                Cita.EstadoCita.CANCELADA);

                if (ocupado) {
                        throw new IllegalArgumentException("El doctor ya tiene una cita agendada en ese horario.");
                }

                boolean consultorioOcupado = citaRepository.existsByConsultorioIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                consultorio.getId(),
                                request.fechaCita(),
                                request.horaCita(),
                                Cita.EstadoCita.CANCELADA);

                if (consultorioOcupado) {
                        throw new IllegalArgumentException("El consultorio ya está ocupado en ese horario.");
                }

                Cita cita = new Cita();
                applyRequest(cita, request, paciente, doctor, consultorio);
                return toResponse(citaRepository.save(cita));
        }

        @Override
        public CitaResponse update(Long id, CitaRequest request) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

                // Validar propiedad si el usuario autenticado es PACIENTE
                Long pacienteActualId = getPacienteIdFromCurrentUserIfPaciente();
                if (pacienteActualId != null) {
                        if (!cita.getPaciente().getId().equals(pacienteActualId)) {
                                throw new IllegalStateException("No tiene permisos para editar esta cita");
                        }
                }

                // Resolver paciente: si es PACIENTE autenticado, ignorar cualquier pacienteId del request
                Long pacienteIdParaActualizar = pacienteActualId != null ? pacienteActualId : request.pacienteId();
                Paciente paciente = pacienteRepository.findById(pacienteIdParaActualizar)
                                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
                Doctor doctor = doctorRepository.findById(request.doctorId())
                                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
                Consultorio consultorio = consultorioRepository.findById(request.consultorioId())
                                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
                applyRequest(cita, request, paciente, doctor, consultorio);
                return toResponse(citaRepository.save(cita));
        }

        @Override
        public void delete(Long id) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

                // Validar propiedad si el usuario autenticado es PACIENTE
                Long pacienteActualId = getPacienteIdFromCurrentUserIfPaciente();
                if (pacienteActualId != null && !cita.getPaciente().getId().equals(pacienteActualId)) {
                        throw new IllegalStateException("No tiene permisos para eliminar esta cita");
                }

                citaRepository.delete(cita);
        }

        @Override
        @Transactional(readOnly = true)
        public List<CitaResponse> findByDoctor(Long doctorId, LocalDate fecha) {
                return citaRepository.findByDoctorIdAndFechaCita(doctorId, fecha).stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<CitaResponse> findByPaciente(Long pacienteId) {
                return citaRepository.findByPacienteId(pacienteId).stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Override
        public CitaResponse reprogramar(Long id, java.time.LocalDate nuevaFecha, java.time.LocalTime nuevaHora) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

                // Validar Estado Permitido
                if (cita.getEstado() == Cita.EstadoCita.CANCELADA || cita.getEstado() == Cita.EstadoCita.COMPLETADA
                                || cita.getEstado() == Cita.EstadoCita.NO_ASISTIO) {
                        throw new IllegalStateException(
                                        "No se puede reprogramar una cita que está " + cita.getEstado());
                }

                validarAusenciaMedico(cita.getDoctor().getId(), nuevaFecha, nuevaHora);

                // Validar Disponibilidad
                boolean ocupado = citaRepository.existsByDoctorIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                cita.getDoctor().getId(),
                                nuevaFecha,
                                nuevaHora,
                                Cita.EstadoCita.CANCELADA);

                if (ocupado) {
                        throw new IllegalArgumentException("El doctor ya tiene una cita agendada en ese horario.");
                }

                boolean consultorioOcupado = citaRepository.existsByConsultorioIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                cita.getConsultorio().getId(),
                                nuevaFecha,
                                nuevaHora,
                                Cita.EstadoCita.CANCELADA);

                if (consultorioOcupado) {
                        throw new IllegalArgumentException("El consultorio ya está ocupado en ese horario.");
                }

                cita.setFechaCita(nuevaFecha);
                cita.setHoraCita(nuevaHora);
                return toResponse(citaRepository.save(cita));
        }

        @Override
        public void cambiarEstado(Long id, com.medx.beta.model.Cita.EstadoCita nuevoEstado) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

                Cita.EstadoCita estadoActual = cita.getEstado();

                // Validaciones de Transición
                if (estadoActual == Cita.EstadoCita.CANCELADA || estadoActual == Cita.EstadoCita.COMPLETADA
                                || estadoActual == Cita.EstadoCita.NO_ASISTIO) {
                        throw new IllegalStateException("No se puede cambiar el estado de una cita finalizada ("
                                        + estadoActual + ") a " + nuevoEstado);
                }

                cita.setEstado(nuevoEstado);
                citaRepository.save(cita);
        }

        private void applyRequest(Cita cita, CitaRequest request, Paciente paciente, Doctor doctor,
                        Consultorio consultorio) {
                cita.setPaciente(paciente);
                cita.setDoctor(doctor);
                cita.setConsultorio(consultorio);
                cita.setFechaCita(request.fechaCita());
                cita.setHoraCita(request.horaCita());
                cita.setDuracionMinutos(request.duracionMinutos());
                cita.setEstado(request.estado());
                cita.setTipoAtencion(request.tipoAtencion());
                // Removido: fechaImpresion ya no se gestiona via request/response
                // cita.setFechaImpresion(request.fechaImpresion());
                cita.setPrecioBase(request.precioBase());
                cita.setMontoDescuento(request.montoDescuento());

                // Insurance Logic
                Seguro seguro = null;
                if (request.seguroId() != null) {
                        seguro = seguroRepository.findById(request.seguroId())
                                        .orElseThrow(() -> new NotFoundException("Seguro no encontrado"));
                } else if (paciente.getSeguro() != null) {
                        seguro = paciente.getSeguro();
                }

                if (seguro != null) {
                        cita.setSeguro(seguro);
                        // Calcular copago
                        BigDecimal copago;
                        if (seguro.getCopagoFijo() != null && seguro.getCopagoFijo().compareTo(BigDecimal.ZERO) > 0) {
                                copago = seguro.getCopagoFijo();
                        } else {
                                BigDecimal coverage = seguro.getCoberturaPorcentaje();
                                BigDecimal patientShare = BigDecimal.ONE.subtract(coverage);
                                copago = request.precioBase().multiply(patientShare);
                        }
                        // Normalizar: mínimo 0 y escala 2
                        copago = copago.max(BigDecimal.ZERO).setScale(2, java.math.RoundingMode.HALF_UP);
                        cita.setCopagoEstimado(copago);
                        // El costo neto de la cita para el paciente con seguro es el copago estimado
                        cita.setCostoNetoCita(copago);
                } else {
                        cita.setSeguro(null);
                        cita.setCopagoEstimado(BigDecimal.ZERO);
                        // Sin seguro: costo neto derivado = precioBase - descuento
                        BigDecimal neto = request.precioBase().subtract(request.montoDescuento());
                        neto = neto.max(BigDecimal.ZERO).setScale(2, java.math.RoundingMode.HALF_UP);
                        cita.setCostoNetoCita(neto);
                }
        }

        private CitaResponse toResponse(Cita cita) {
                return new CitaResponse(
                                cita.getId(),
                                cita.getPaciente().getId(),
                                cita.getDoctor().getId(),
                                cita.getConsultorio().getId(),
                                cita.getFechaCita(),
                                cita.getHoraCita(),
                                cita.getDuracionMinutos(),
                                cita.getEstado(),
                                cita.getTipoAtencion(),
                                cita.getPrecioBase(),
                                cita.getMontoDescuento(),
                                cita.getCostoNetoCita(),
                                cita.getSeguro() != null ? cita.getSeguro().getId() : null,
                                cita.getSeguro() != null ? cita.getSeguro().getNombreAseguradora() : null,
                                cita.getCopagoEstimado());
        }

        private void validarAusenciaMedico(Long doctorId, LocalDate fecha, java.time.LocalTime hora) {
                List<com.medx.beta.model.AusenciaMedico> ausencias = ausenciaRepository.findByDoctorAndDate(doctorId,
                                fecha);
                for (com.medx.beta.model.AusenciaMedico a : ausencias) {
                        boolean esDiaCompleto = a.getHoraInicio() == null || a.getHoraFin() == null;
                        if (esDiaCompleto) {
                                throw new IllegalArgumentException(
                                                "El doctor no está disponible por: " + a.getMotivo());
                        }
                        // Check time overlap
                        // If appointment time is within the absence range
                        if (!hora.isBefore(a.getHoraInicio()) && hora.isBefore(a.getHoraFin())) {
                                throw new IllegalArgumentException(
                                                "El doctor no está disponible en ese horario por: " + a.getMotivo());
                        }
                }
        }

        private Long getPacienteIdFromCurrentUserIfPaciente() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                        return null;
                }
                String email = authentication.getName();
                UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(email).orElse(null);
                if (usuario == null || usuario.getRol() != UsuarioSistema.Rol.PACIENTE) {
                        return null;
                }
                Long personaId = usuario.getPersona() != null ? usuario.getPersona().getId() : null;
                if (personaId == null) {
                        throw new IllegalStateException("El usuario PACIENTE no tiene una Persona asociada");
                }
                com.medx.beta.model.Paciente paciente = pacienteRepository.findByPersonaId(personaId)
                                .orElseThrow(() -> new IllegalStateException("No se encontró el Paciente asociado a la Persona del usuario"));
                return paciente.getId();
        }
}
