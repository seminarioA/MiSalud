package com.medx.beta.service.impl;

import com.medx.beta.dto.DocumentoFiscalRequest;
import com.medx.beta.dto.DocumentoFiscalResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.DocumentoFiscal;
import com.medx.beta.model.Pago;
import com.medx.beta.repository.DocumentoFiscalRepository;
import com.medx.beta.repository.PagoRepository;
import com.medx.beta.service.DocumentoFiscalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentoFiscalServiceImpl implements DocumentoFiscalService {

    private final DocumentoFiscalRepository documentoFiscalRepository;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoFiscalResponse> findAll() {
        return documentoFiscalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoFiscalResponse findById(Long id) {
        DocumentoFiscal documento = documentoFiscalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Documento fiscal no encontrado"));
        return toResponse(documento);
    }

    @Override
    public DocumentoFiscalResponse create(DocumentoFiscalRequest request) {
        Pago pago = pagoRepository.findById(request.pagoId())
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        DocumentoFiscal documento = new DocumentoFiscal();
        applyRequest(documento, request, pago);
        return toResponse(documentoFiscalRepository.save(documento));
    }

    @Override
    public DocumentoFiscalResponse update(Long id, DocumentoFiscalRequest request) {
        DocumentoFiscal documento = documentoFiscalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Documento fiscal no encontrado"));
        Pago pago = pagoRepository.findById(request.pagoId())
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        applyRequest(documento, request, pago);
        return toResponse(documentoFiscalRepository.save(documento));
    }

    @Override
    public void delete(Long id) {
        DocumentoFiscal documento = documentoFiscalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Documento fiscal no encontrado"));
        documentoFiscalRepository.delete(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoFiscalResponse findByPago(Long pagoId) {
        return documentoFiscalRepository.findAll().stream()
                .filter(doc -> doc.getPago().getId().equals(pagoId))
                .map(this::toResponse)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Documento fiscal no encontrado para el pago"));
    }

    private void applyRequest(DocumentoFiscal documento, DocumentoFiscalRequest request, Pago pago) {
        documento.setPago(pago);
        documento.setTipoComprobante(request.tipoComprobante());
        documento.setSerie(request.serie());
        documento.setCorrelativo(request.correlativo());
        documento.setRucCliente(request.rucCliente());
        documento.setNombreClienteFiscal(request.nombreClienteFiscal());
    }

    private DocumentoFiscalResponse toResponse(DocumentoFiscal documento) {
        return new DocumentoFiscalResponse(
                documento.getId(),
                documento.getPago().getId(),
                documento.getTipoComprobante(),
                documento.getSerie(),
                documento.getCorrelativo(),
                documento.getRucCliente(),
                documento.getNombreClienteFiscal()
        );
    }
}

