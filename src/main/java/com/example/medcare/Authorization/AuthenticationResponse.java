package com.example.medcare.Authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class AuthenticationResponse {


    @JsonProperty("accessToken")
    private String token;

    @JsonProperty("refreshToken")
    private String refreshToken;


}
