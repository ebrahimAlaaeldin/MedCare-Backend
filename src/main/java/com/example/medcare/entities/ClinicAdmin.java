package com.example.medcare.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClinicAdmin extends User{

    @OneToOne
    @JoinColumn(name = "clinicId")
    private Clinic clinic;

}
