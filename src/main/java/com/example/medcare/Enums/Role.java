package com.example.medcare.enums;

import com.example.medcare.Authorization.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.example.medcare.Authorization.Permission.*;

@RequiredArgsConstructor
public enum Role {
        ADMIN(Set.of(
                        ADMIN_READ,
                        ADMIN_UPDATE,
                        ADMIN_DELETE,
                        ADMIN_CREATE)),
        DOCTOR(Set.of(
                        DOCTOR_READ,
                        DOCTOR_UPDATE,
                        DOCTOR_DELETE,
                        DOCTOR_CREATE)),
        PATIENT(Set.of(
                        PATIENT_READ,
                        PATIENT_UPDATE,
                        PATIENT_DELETE,
                        PATIENT_CREATE)),
                        
        
        
        SUPER_ADMIN(Set.of(
                        SUPER_ADMIN_READ,
                        SUPER_ADMIN_UPDATE,
                        SUPER_ADMIN_DELETE,
                        SUPER_ADMIN_CREATE));

        @Getter
        private final Set<Permission> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                                .stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }
}
