package com.example.medcare.embedded;


import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ClinicPermit {


    private Integer permitID;

    private LocalDate issuingDate;

    private LocalDate expiryDate;
    
}
