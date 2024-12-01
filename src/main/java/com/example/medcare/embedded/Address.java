package com.example.medcare.embedded;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Embeddable

public class Address {

    private String street;
    private String city;
    private String zipCode;
    private String country;

}
