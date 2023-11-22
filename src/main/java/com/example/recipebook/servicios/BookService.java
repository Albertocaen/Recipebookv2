package com.example.recipebook.servicios;

import com.example.recipebook.entidades.Ingrediente;
import com.example.recipebook.entidades.Receta;


import com.example.recipebook.repositorio.RecetaRepositorio;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class BookService {

    @Autowired
    private RecetaRepositorio recetaRepository;

    public List<Receta> findAll() {
        return recetaRepository.findAll();
    }

    public Receta add(Receta r) {
        return recetaRepository.save(r);
    }
    public Receta save(Receta r){return recetaRepository.save(r);}

    public void addAll(List<Receta> lista) {
        recetaRepository.saveAll(lista);
    }

    public Optional<Receta> findById(Long id) {
        return recetaRepository.findById(id);
    }

    public Receta edit(Receta r) {
        return recetaRepository.save(r);
    }

    public void borrarRecetaById(Receta r) {
        recetaRepository.delete(r);
    }

    private Map<Integer, Integer> visitasPorReceta = new HashMap<>();

    public Receta obtenerRecetaYActualizarVisitas(Long id) {
        Optional<Receta> optionalReceta = recetaRepository.findById(id);
        if (optionalReceta.isPresent()) {
            Receta receta = optionalReceta.get();
            int visitas = receta.getVisitas() != null ? receta.getVisitas() : 0;
            receta.setVisitas(visitas + 1);
            recetaRepository.save(receta);
            return receta;
        }
        return null;
    }


    public List<Receta> obtenerRecetasOrdenadasPorVisitas() {
        // Obtener las recetas directamente de la base de datos y ordenarlas
        List<Receta> recetas = recetaRepository.findAllByOrderByVisitasDesc();
        return recetas;
    }



}





