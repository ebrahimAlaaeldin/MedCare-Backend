package com.example.medcare.dto;


import com.example.medcare.Authorization.AuthenticationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessageDto {
    private String message;
    private boolean success;
    private int statusCode;
    private Object data;
}
