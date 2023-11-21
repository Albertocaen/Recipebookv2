package com.example.recipebook.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    @GeneratedValue
    private int id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;


}
