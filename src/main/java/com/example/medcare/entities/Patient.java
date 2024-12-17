package com.example.medcare.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@SuperBuilder
public class Patient extends User {


    //    private String insurance;
    private String insuranceId;
    //    private String insuranceGroup;
//
//
//   private String emergencyContactName;
    private String emergencyContactPhone;
//    private String emergencyContactRelation;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true) //orphanRemoval = true means that if the patient is deleted, all of his appointments will be deleted as well
    private List<Appointment> appointments;

}









