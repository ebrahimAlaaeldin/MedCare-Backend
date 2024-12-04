package com.example.medcare.entities;
import com.example.medcare.embedded.License;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Doctor extends User {


    @Embedded
    private License license;

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
    private List<Clinic> clinics;

    @Getter
    @Setter
    private Boolean isVerified;
    
    







}
