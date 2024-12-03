package com.example.medcare.Authorization;



public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:create"),
    DOCTOR_READ("doctor:read"),
    DOCTOR_UPDATE("doctor:update"),
    DOCTOR_DELETE("doctor:delete"),
    DOCTOR_CREATE("doctor:create"),
    PATIENT_READ("patient:read"),
    PATIENT_UPDATE("patient:update"),
    PATIENT_DELETE("patient:delete"),
    PATIENT_CREATE("patient:create");



    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
