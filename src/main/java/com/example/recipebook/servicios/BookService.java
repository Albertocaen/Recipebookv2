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

    public void addAll(List<Receta> lista) {
        recetaRepository.saveAll(lista);
    }

    public Optional<Receta> findById(Long id) {
        return recetaRepository.findById(id);
    }

    private List<Receta> repositorioRecetas = new ArrayList<>();


    public List<Receta> obtenerListaRecetas() {
        return repositorioRecetas;
    }


    public Receta edit(Receta r) {
        return recetaRepository.save(r);
    }


    public void borrarRecetaById(Receta r) {recetaRepository.delete(r);
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
        // Cargar o asegurarse de que las recetas estén cargadas


        // Ordenar la lista de recetas por el número de visitas (de mayor a menor)
        repositorioRecetas.sort(Comparator.comparingInt((Receta r) -> visitasPorReceta.getOrDefault(r.getId(), 0)).reversed());

        return repositorioRecetas;
    }
    @PostConstruct
    public void init() {
        Receta pan = Receta.builder()
                .nombre("pan")
                .preparacion("Se mezcla bien hasta formar una masa y hornear")
                .ingredientes(Arrays.asList(
                        new Ingrediente("harina"),
                        new Ingrediente("agua"),
                        new Ingrediente("sal")
                ))
                .foto(null)
                .build();

        recetaRepository.save(pan);


    }


}







