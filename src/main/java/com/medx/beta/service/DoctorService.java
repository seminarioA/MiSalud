package com.medx.beta.service;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;

import java.util.List;

public interface DoctorService {

    List<DoctorResponse> findAll();

    DoctorResponse findById(Long id);

    DoctorResponse create(DoctorRequest request);

    DoctorResponse update(Long id, DoctorRequest request);

    void delete(Long id);
}

