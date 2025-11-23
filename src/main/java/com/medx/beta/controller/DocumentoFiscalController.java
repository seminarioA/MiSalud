package com.medx.beta.controller;

import com.medx.beta.dto.DocumentoFiscalRequest;
import com.medx.beta.dto.DocumentoFiscalResponse;
import com.medx.beta.service.DocumentoFiscalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos-fiscales")
@RequiredArgsConstructor
public class DocumentoFiscalController {

    private final DocumentoFiscalService documentoFiscalService;

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<DocumentoFiscalResponse>> listar() {
        return ResponseEntity.ok(documentoFiscalService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<DocumentoFiscalResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(documentoFiscalService.findById(id));
    }

    @GetMapping("/pago/{pagoId}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<DocumentoFiscalResponse> porPago(@PathVariable Long pagoId) {
        return ResponseEntity.ok(documentoFiscalService.findByPago(pagoId));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<DocumentoFiscalResponse> crear(@Valid @RequestBody DocumentoFiscalRequest request) {
        return ResponseEntity.ok(documentoFiscalService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<DocumentoFiscalResponse> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody DocumentoFiscalRequest request) {
        return ResponseEntity.ok(documentoFiscalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoFiscalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

