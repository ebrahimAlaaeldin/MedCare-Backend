package com.example.medcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgetPassEmail {

    private String email;
}
