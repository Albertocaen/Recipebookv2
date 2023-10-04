package com.example.recipebook.entidades;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {
    @NotNull(message = "El campo 'ID' no puede estar vacío")
    @Min(value = 0, message = "El campo 'ID' debe ser un número mayor o igual a cero")
    private int id;
    @NotBlank (message = "El campo Nombre no puede estar vacío")
    private String nombre;
    @NotBlank (message = "El campo Ingredientes no puede estar vacío")
    private String Ingredientes;
    @NotBlank (message = "El campo Preparación no puede estar vacío")
    private String preparacion;
}
