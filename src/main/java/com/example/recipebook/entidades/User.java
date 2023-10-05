package com.example.recipebook.entidades;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotBlank (message = "El campo Nombre no puede estar vacío")
    private String username;
    @NotBlank (message = "El campo Nombre no puede estar vacío")
    private String password;
}
