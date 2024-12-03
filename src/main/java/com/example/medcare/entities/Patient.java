package com.example.medcare.entities;


import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
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

}
