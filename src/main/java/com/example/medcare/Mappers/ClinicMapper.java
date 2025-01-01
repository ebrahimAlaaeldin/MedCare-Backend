package com.example.medcare.Mappers;


import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.entities.Clinic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClinicMapper {


    public List<ClinicDTO> toDTOs(List<Clinic> clinics){
        return clinics.stream().map(this::toDTO).toList();
    }

    public ClinicDTO toDTO(Clinic clinic){
        ClinicDTO clinicDTO = new ClinicDTO();
        clinicDTO.setClinicId(clinic.getClinicId());
        clinicDTO.setClinicName(clinic.getName());
        clinicDTO.setAddress(clinic.getAddress());
        clinicDTO.setPermit(clinic.getPermit());
        return clinicDTO;
    }


    public Clinic toEntity(ClinicDTO clinicDTO){
        Clinic clinic = new Clinic();
        clinic.setClinicId(clinicDTO.getClinicId());
        clinic.setName(clinicDTO.getClinicName());
        clinic.setAddress(clinicDTO.getAddress());
        clinic.setPermit(clinicDTO.getPermit());
        return clinic;
    }
}
