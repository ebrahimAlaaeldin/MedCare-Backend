package com.example.medcare.embedded;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder

public class Address {

    private String street;
    private String city;
    private String zipCode;
    private String country;

}
