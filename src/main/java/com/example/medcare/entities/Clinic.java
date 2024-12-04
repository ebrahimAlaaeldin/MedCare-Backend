package com.example.medcare.entities;
import com.example.medcare.embedded.Address;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clinicId;

    private String name;


    @Embedded
    private Address address;

    private Boolean isVerified;

    private Boolean isActive;

    private LocalDateTime createdAt;





    @OneToOne
    @JoinColumn(name = "clinicAdminId")
    private ClinicAdmin clinicAdmin;

}
