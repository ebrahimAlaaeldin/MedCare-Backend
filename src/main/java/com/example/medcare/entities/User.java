package com.example.medcare.entities;

import com.example.medcare.Enums.Gender;
import com.example.medcare.Enums.Role;
import com.example.medcare.embedded.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails{

    public User(){
        this.createdAt = LocalDate.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    //@Column(nullable = false)
    private String pinNumber;

    @Column(unique = true,nullable = false)
    private String email;

    //@Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate createdAt;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private LocalDate birthDate; //example: 1999-12-31

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return role.getAuthorities();
    }

    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

}





