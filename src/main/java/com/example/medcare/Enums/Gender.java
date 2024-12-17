package com.example.medcare.Enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }
}
