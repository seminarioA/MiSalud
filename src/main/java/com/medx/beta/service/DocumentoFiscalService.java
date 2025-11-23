package com.medx.beta.service;

import com.medx.beta.dto.DocumentoFiscalRequest;
import com.medx.beta.dto.DocumentoFiscalResponse;

import java.util.List;

public interface DocumentoFiscalService {

    List<DocumentoFiscalResponse> findAll();

    DocumentoFiscalResponse findById(Long id);

    DocumentoFiscalResponse create(DocumentoFiscalRequest request);

    DocumentoFiscalResponse update(Long id, DocumentoFiscalRequest request);

    void delete(Long id);

    DocumentoFiscalResponse findByPago(Long pagoId);
}

