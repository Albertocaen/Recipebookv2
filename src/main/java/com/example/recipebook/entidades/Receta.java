package com.example.recipebook.entidades;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL)
    private List<Ingrediente> ingredientes;

    public String getIngredientesAsString() {
        if (ingredientes != null && !ingredientes.isEmpty()) {
            return ingredientes.stream()
                    .map(Ingrediente::getNombre)
                    .collect(Collectors.joining("\n"));
        } else {
            return "";
        }
    }

    public List<Ingrediente> getIngredientesList() {
        return this.ingredientes;
    }
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "El campo Preparación no puede estar vacío")
    private String preparacion;
    private String foto;
    private Integer visitas;



}