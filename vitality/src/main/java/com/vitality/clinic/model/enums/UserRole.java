package com.vitality.clinic.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_DOCTOR,
    ROLE_PATIENT;

//    public List<SimpleGrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
//    }
}
