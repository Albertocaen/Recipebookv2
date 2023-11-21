package com.example.recipebook.servicios;

import com.example.recipebook.entidades.Receta;
import com.example.recipebook.entidades.User;
import com.example.recipebook.repositorio.RecetaRepositorio;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Service
public class BookService {

    @Autowired
    private RecetaRepositorio recetaRepository;
    private List<Receta> repositorioRecetas = new ArrayList<>();


    private boolean recetasCargadas=false;




    public Receta add(Receta r) {
        repositorioRecetas.add(r);
        return r;
    }

    public Receta findById(int id) {
        Receta receta = repositorioRecetas.stream()
                .filter(r -> id == r.getId())
                .findAny()
                .orElse(null);
        return receta;
    }




    public List<Receta> obtenerListaRecetas() {
        return repositorioRecetas;
    }


    public Receta edit(Receta r) {

      Receta receta= findById(r.getId());

        if (receta != null) {
            repositorioRecetas.set(repositorioRecetas.indexOf(receta), r);
        } else {
            repositorioRecetas.add(r);
        }

        return r;

    }



    public boolean borrarRecetaById(int id){
        List<Receta> recetasNoBorradas = new ArrayList<>();
        for (Receta receta : repositorioRecetas) {
            if (receta.getId() != id) {
                recetasNoBorradas.add(receta);
            }
        }
        repositorioRecetas = recetasNoBorradas;

        return true;
    }
    private Map<Integer, Integer> visitasPorReceta = new HashMap<>();
    public Receta obtenerRecetaYActualizarVisitas(int id) {
        Receta receta = findById(id);
        if (receta != null) {
            int visitas = visitasPorReceta.getOrDefault(id, 0);
            visitasPorReceta.put(id, visitas + 1);
        }
        return receta;
    }

    public List<Receta> obtenerRecetasOrdenadasPorVisitas() {
        // Cargar o asegurarse de que las recetas estén cargadas


        // Ordenar la lista de recetas por el número de visitas (de mayor a menor)
        repositorioRecetas.sort(Comparator.comparingInt((Receta r) -> visitasPorReceta.getOrDefault(r.getId(), 0)).reversed());

        return repositorioRecetas;
    }
    @PostConstruct
    public void init() {
        repositorioRecetas.addAll(
                Arrays.asList(
                        Receta.builder()
                                .id(1)
                                .nombre("pan")
                                .Ingredientes("harina,agua, sal")
                                .preparacion("Se mezcla bien hasta formar una masa y hornear")
                                .foto(null)
                                .build(),

                        new Receta (2, "arroz Blanco ",
                                "arroz , agua , sal",
                                "se hierbe 10min y dejar reposar 5min",null
                        ),
                        new Receta (3, "huevo frito",
                                "huevo, sal",
                                "Se frie en aceite y se le hecha sal ",null
                        ),
                        new Receta (4, "huevo duro",
                                                 "huevo, agua",
                                        "Se hierve 7min , se saca, se pela y listo ",null
                        )
                ));


    }
}
