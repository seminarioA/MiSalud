package com.medx.beta.service;

import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;

import java.util.List;

public interface PersonService {

    List<PersonaResponse> findAll();

    PersonaResponse findById(Long id);

    PersonaResponse create(PersonaRequest request);

    PersonaResponse update(Long id, PersonaRequest request);

    void delete(Long id);
}

