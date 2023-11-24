package com.example.recipebook.repositorio;

import com.example.recipebook.entidades.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepositorio extends JpaRepository<Receta, Long> {
    public List<Receta> findByNombreStartingWithIgnoreCase(String nombre);

    public List<Receta> findByNombreContainsIgnoreCase(String filtro);

    List<Receta> findAllByOrderByVisitasDesc();

}
