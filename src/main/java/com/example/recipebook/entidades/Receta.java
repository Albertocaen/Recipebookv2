package com.example.recipebook.entidades;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;



@Entity
@Data
@AllArgsConstructor

@Builder
@Table(name = "Receta")
public class Receta {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "El campo Nombre no puede estar vacío")
    private String nombre;

    //CascadeType.ALL: Indica que todas las operaciones de persistencia (como guardar, actualizar y eliminar)
    // en la entidad Receta deben propagarse a las entidades Ingrediente asociadas. Esto significa que si guardas o eliminas una Receta, también se aplicarán esas operaciones a los Ingredientes asociados.
    //orphanRemoval = true:  Indica que si eliminas un ingrediente de la lista de ingredientes de una receta (ingredientes),
    // y ese ingrediente no está asociado con ninguna otra receta, también se eliminará de la base de datos.
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingrediente> ingredientes;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "El campo Preparación no puede estar vacío")
    private String preparacion;
    private String foto;
    private Integer visitas;

    public Receta(/* otros parámetros */) {
        // Otros inicializaciones...
        if (ingredientes == null) {
            ingredientes = new ArrayList<>();
            // Agregar tres elementos predeterminados
            ingredientes.add(new Ingrediente());
        }
    }


}