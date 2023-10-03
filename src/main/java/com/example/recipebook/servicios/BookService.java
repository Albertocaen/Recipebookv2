package com.example.recipebook.servicios;

import com.example.recipebook.entidades.Receta;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookService {
    private List<Receta> repositorioRecetas = new ArrayList<>();
    private final ResourceLoader resourceLoader;
    private boolean recetasCargadas=false;

    public BookService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //public List<Receta> findAll() {
    //  return repositorioRecetas;
    //}

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


    @PostConstruct
    public List<Receta> cargarrecetas() {
        if (!recetasCargadas){
            try {
                Resource resource = resourceLoader.getResource("classpath:recetas.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));

                String linea;

                while ((linea = br.readLine()) != null) {
                    String[] columnas = linea.split("\\|"); // Dividir la línea en columnas usando el carácter '|'
                    if (columnas.length == 4) {
                        int id = Integer.parseInt(columnas[0].trim()); // Utiliza trim() para eliminar espacios en blanco
                        String nombre = columnas[1];
                        String ingredientes = columnas[2];
                        String preparacion = columnas[3];

                        Receta receta = new Receta(id, nombre, ingredientes, preparacion);
                        repositorioRecetas.add(receta);
                    }
                }
                recetasCargadas=true;

            } catch (IOException e) {
                log.error("Ocurrió un error de IO", e);
            }
        }
        return repositorioRecetas;
    }

    public Receta edit(Receta r) {
        log.info("Se esta editando la receta {}", r);
        Receta receta = findById(r.getId());
        log.info("Receta encontrada {}", receta);

        if (receta != null) {
            repositorioRecetas.set(repositorioRecetas.indexOf(receta), r);
        } else {
            repositorioRecetas.add(r);
        }

        return r;

    }

}