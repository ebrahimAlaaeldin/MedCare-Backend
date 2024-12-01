package com.example.medcare.entities;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "patient")
@Entity
@Data
public class patient {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "DOB", nullable = false)
    private String DOB;


}
