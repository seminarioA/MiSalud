package com.medx.beta.service;

import com.medx.beta.model.SedeHospital;
import java.util.List;

public interface SedeHospitalService {
    List<SedeHospital> getAll();
    SedeHospital getById(Integer id);
    SedeHospital create(SedeHospital sedeHospital);
    SedeHospital update(Integer id, SedeHospital sedeHospital);
    void deleteById(Integer id);
    List<SedeHospital> getByHospital(Integer hospitalId);
}
