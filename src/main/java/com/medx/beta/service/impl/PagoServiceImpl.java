package com.medx.beta.service.impl;

import com.medx.beta.dto.PagoRequest;
import com.medx.beta.dto.PagoResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Cita;
import com.medx.beta.model.Pago;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.PagoRepository;
import com.medx.beta.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> findAll() {
        return pagoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse findById(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        return toResponse(pago);
    }

    @Override
    public PagoResponse create(PagoRequest request) {
        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

        // 1. Validate if trying to pay more than needed
        BigDecimal totalPaid = pagoRepository.findByCitaId(cita.getId()).stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nuevoTotal = totalPaid.add(request.monto());
        BigDecimal costoNeto = cita.getCostoNetoCita();

        // Allow 10% tolerance or exact check? Let's be strict for now, but allow exact
        // match.
        // If cost is 0 (fully covered), maybe allow 0 payments?
        // For simplicity: If cost > 0 and new total > cost * 1.1 -> Error
        if (costoNeto != null && costoNeto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal maxAllowed = costoNeto.multiply(new BigDecimal("1.1")); // 10% buffer
            if (nuevoTotal.compareTo(maxAllowed) > 0) {
                throw new IllegalArgumentException("El monto excede el costo neto de la cita (" + costoNeto + ")");
            }
        }

        Pago pago = new Pago();
        applyRequest(pago, request, cita);
        pago = pagoRepository.save(pago);

        // 2. Auto-Confirm Logic
        if (costoNeto != null && nuevoTotal.compareTo(costoNeto) >= 0) {
            if (cita.getEstado() == Cita.EstadoCita.PENDIENTE) {
                cita.setEstado(Cita.EstadoCita.CONFIRMADA);
                citaRepository.save(cita);
            }
        }

        return toResponse(pago);
    }

    @Override
    public PagoResponse update(Long id, PagoRequest request) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        applyRequest(pago, request, cita);
        return toResponse(pagoRepository.save(pago));
    }

    @Override
    public void delete(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        pagoRepository.delete(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> findByCita(Long citaId) {
        return pagoRepository.findByCitaId(citaId).stream()
                .map(this::toResponse)
                .toList();
    }

    private void applyRequest(Pago pago, PagoRequest request, Cita cita) {
        pago.setCita(cita);
        pago.setMonto(request.monto());
        pago.setTipoTransaccion(request.tipoTransaccion());
        pago.setEstadoPago(request.estadoPago());
        pago.setMetodoPago(request.metodoPago());
        pago.setCodigoOperacion(request.codigoOperacion());
        pago.setFechaPago(request.fechaPago());
    }

    private PagoResponse toResponse(Pago pago) {
        return new PagoResponse(
                pago.getId(),
                pago.getCita().getId(),
                pago.getMonto(),
                pago.getTipoTransaccion(),
                pago.getEstadoPago(),
                pago.getMetodoPago(),
                pago.getCodigoOperacion(),
                pago.getFechaPago());
    }
}
