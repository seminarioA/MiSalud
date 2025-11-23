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
        Pago pago = new Pago();
        applyRequest(pago, request, cita);
        return toResponse(pagoRepository.save(pago));
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
        return pagoRepository.findAll().stream()
                .filter(p -> p.getCita().getId().equals(citaId))
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
            pago.getFechaPago()
        );
    }
}

