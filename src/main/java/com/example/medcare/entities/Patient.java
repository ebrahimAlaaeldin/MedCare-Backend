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


<<<<<<< HEAD
//    private String insurance;
       private String insuranceId;
//    private String insuranceGroup;
=======
    //    private String insurance;
    private String insuranceId;
    //    private String insuranceGroup;
>>>>>>> 5d5a516 (rebasing process)
//
//
//   private String emergencyContactName;
    private String emergencyContactPhone;
//    private String emergencyContactRelation;

}
<<<<<<< HEAD
=======









>>>>>>> 5d5a516 (rebasing process)
