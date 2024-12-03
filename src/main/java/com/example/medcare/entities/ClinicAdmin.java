package com.example.medcare.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class ClinicAdmin extends User{



    @OneToOne
    @JoinColumn(name = "clinicId")
    private Clinic clinic;





}
