package com.example.medcare.entities;
import com.example.medcare.embedded.Address;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Doctor extends User {




    @ManyToMany
    @JoinTable(
            name = "doctor_clinic",
            joinColumns = {
                    @JoinColumn(name = "doctor_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "clinic_id")
            }
    )
    private List<Clinic>  clinics;







}
