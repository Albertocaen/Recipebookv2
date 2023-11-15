package com.example.recipebook.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {

    @Id @GeneratedValue
    private int id;
    @NotBlank (message = "El campo Nombre no puede estar vacío")
    private String nombre;
    @NotBlank (message = "El campo Ingredientes no puede estar vacío")
    private String Ingredientes;
    @Column(columnDefinition = "TEXT")
    @NotBlank (message = "El campo Preparación no puede estar vacío")
    private String preparacion;
    private String foto;
}