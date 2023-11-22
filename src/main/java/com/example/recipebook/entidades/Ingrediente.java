package com.example.recipebook.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "receta_id")
    private Receta receta;


}
