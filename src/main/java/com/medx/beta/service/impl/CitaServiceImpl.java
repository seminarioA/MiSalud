package com.medx.beta.service.impl;

import com.medx.beta.dto.CitaRequest;
import com.medx.beta.dto.CitaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Cita;
import com.medx.beta.model.Consultorio;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.model.Seguro;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.ConsultorioRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.repository.SeguroRepository;
import com.medx.beta.service.CitaService;
import lombok.RequiredArgsConstructor;
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

                boolean ocupado = citaRepository.existsByDoctorIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                doctor.getId(),
                                request.fechaCita(),
                                request.horaCita(),
                                Cita.EstadoCita.CANCELADA);

                if (ocupado) {
                        throw new IllegalArgumentException("El doctor ya tiene una cita agendada en ese horario.");
                }

                Cita cita = new Cita();
                applyRequest(cita, request, paciente, doctor, consultorio);
                return toResponse(citaRepository.save(cita));
        }

        @Override
        public CitaResponse update(Long id, CitaRequest request) {
                Cita cita = citaRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
                Paciente paciente = pacienteRepository.findById(request.pacienteId())
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

                // Validar Disponibilidad
                boolean ocupado = citaRepository.existsByDoctorIdAndFechaCitaAndHoraCitaAndEstadoNot(
                                cita.getDoctor().getId(),
                                nuevaFecha,
                                nuevaHora,
                                Cita.EstadoCita.CANCELADA);

                if (ocupado) {
                        throw new IllegalArgumentException("El doctor ya tiene una cita agendada en ese horario.");
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
                cita.setFechaImpresion(request.fechaImpresion());
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
                        // Calculate Copay
                        BigDecimal copago;
                        if (seguro.getCopagoFijo() != null && seguro.getCopagoFijo().compareTo(BigDecimal.ZERO) > 0) {
                                copago = seguro.getCopagoFijo();
                        } else {
                                BigDecimal coverage = seguro.getCoberturaPorcentaje();
                                BigDecimal patientShare = BigDecimal.ONE.subtract(coverage);
                                copago = request.precioBase().multiply(patientShare);
                        }
                        cita.setCopagoEstimado(copago);
                        cita.setCostoNetoCita(copago); // Update net cost to be the patient's responsibility
                } else {
                        cita.setSeguro(null);
                        cita.setCopagoEstimado(BigDecimal.ZERO);
                        // Default logic if no insurance
                        if (request.costoNetoCita() != null) {
                                cita.setCostoNetoCita(request.costoNetoCita());
                        } else {
                                cita.setCostoNetoCita(request.precioBase().subtract(request.montoDescuento()));
                        }
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
                                cita.getFechaImpresion(),
                                cita.getPrecioBase(),
                                cita.getMontoDescuento(),
                                cita.getCostoNetoCita(),
                                cita.getSeguro() != null ? cita.getSeguro().getId() : null,
                                cita.getSeguro() != null ? cita.getSeguro().getNombreAseguradora() : null,
                                cita.getCopagoEstimado());
        }
}
