package com.example.medcare.dto;


import lombok.Builder;

@Builder
public record MailBody (String to, String subject, String body ) {
}
