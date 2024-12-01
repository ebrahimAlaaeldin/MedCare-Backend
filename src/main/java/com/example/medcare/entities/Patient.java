package com.example.medcare.entities;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Patient extends User {


//    private String insurance;
       private String insuranceId;
//    private String insuranceGroup;
//
//
//   private String emergencyContactName;
    private String emergencyContactPhone;
//    private String emergencyContactRelation;

}
