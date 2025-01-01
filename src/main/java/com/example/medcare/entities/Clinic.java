package com.example.medcare.entities;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clinicId;

    @Column(unique = true)
    private String name;

    @Embedded
    private ClinicPermit permit;

    @Embedded
    private Address address;

    
    private boolean isVerified;

    private boolean isActive;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "clinicId")
    private List<DoctorClinic> doctorClinics;


    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "clinicAdminId")
    private ClinicAdmin clinicAdmin;

    @ManyToMany(mappedBy = "clinics", fetch = FetchType.EAGER)
    private List<Doctor> doctors;


}
