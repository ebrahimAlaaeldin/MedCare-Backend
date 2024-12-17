package com.example.medcare.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ResponseMessageDto {
    private String message;
    private boolean success;
    private Integer statusCode;
    private Object data;

}
