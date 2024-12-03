package com.example.medcare.embedded;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class License {

    private String licenseNumber;
    private String Specialty;
    private LocalDate issuingDate;

}
