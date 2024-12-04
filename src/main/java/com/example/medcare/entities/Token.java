package com.example.medcare.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token") // Ensure the table name is specified
public class Token {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY) // Ensure the ID generation strategy is specified
    private Integer id;

    @Column(unique = true,nullable = false) // Ensure the column is unique and not null
    private String token;

    private boolean revoked;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false) // Ensure the join column is not null
    private User user;


}
