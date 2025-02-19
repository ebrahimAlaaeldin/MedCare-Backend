package com.example.medcare.embedded;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License {

    private String licenseNumber;
    private String specialty;
    private LocalDate issuingDate;
}
