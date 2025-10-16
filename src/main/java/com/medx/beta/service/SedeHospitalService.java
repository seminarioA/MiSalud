package com.medx.beta.service;

import com.medx.beta.dto.SedeHospitalRequest;
import com.medx.beta.dto.SedeHospitalResponse;
import java.util.List;

public interface SedeHospitalService {
    List<SedeHospitalResponse> getAll();
    SedeHospitalResponse getById(Integer id);
    SedeHospitalResponse create(SedeHospitalRequest sedeHospitalRequest);
    SedeHospitalResponse update(Integer id, SedeHospitalRequest sedeHospitalRequest);
    void delete(Integer id);
    List<SedeHospitalResponse> getByHospital(Integer hospitalId);
}
