package com.example.recipebook.entidades;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {
    private int id;
    private String nombre;
    private String Ingredientes;
    private String preparacion;
}
