package com.example.recipebook.entidades;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String role;
}
