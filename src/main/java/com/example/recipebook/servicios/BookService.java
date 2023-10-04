package com.example.recipebook.servicios;

import com.example.recipebook.entidades.Receta;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class BookService {
    private List<Receta> repositorioRecetas = new ArrayList<>();

    @Value("classpath:/recetas.txt")
    private Resource sourceResource;

    @Value("file:src/main/resources/recetas.txt")
    private Resource targetResource;

    private Resource resource;
    private final ResourceLoader resourceLoader;
    private boolean recetasCargadas=false;
    private FileWriter fileWriter;

    public BookService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.resource = resourceLoader.getResource("classpath:recetas.txt"); // Inicializa resource aquí
        try {
            Resource resource= resourceLoader.getResource("classpath:recetas.txt");
            File file=resource.getFile();
            fileWriter=new FileWriter(file,true);
        }catch (IOException e) {
            log.error("Ocurrio un error",e);
        }
    }


    public Receta add(Receta r) {
        log.info("Entrando en la función add");
        // Agrega la receta a la lista
        repositorioRecetas.add(r);

        try {
            // Abre el archivo para escritura
            FileWriter fileWriter = new FileWriter(resource.getFile(), true);

            // Escribe la nueva receta en el archivo
            fileWriter.write(r.getId() + " | " + r.getNombre() + " | " + r.getIngredientes() + " | " + r.getPreparacion() + "\n");

            // Cierra el archivo
            fileWriter.close();

            log.info("Se ha agregado una receta al archivo de texto");

        } catch (IOException e) {
            log.error("Ocurrió un error al escribir en el archivo", e);
            throw new RuntimeException(e);
        }

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
    public List<Receta> cargarRecetasDesdeArchivo() {
        if (!recetasCargadas || repositorioRecetas.isEmpty()) {
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
                recetasCargadas = true;

            } catch (IOException e) {
                log.error("Ocurrió un error de IO", e);
            }

        }
        return repositorioRecetas;
    }

    public List<Receta> obtenerListaRecetas() {
        log.info("Entrando en la función add");
        return repositorioRecetas;
    }



    public Receta edit(Receta r) {
        Receta receta = findById(r.getId());
        if (receta != null) {
            receta.setNombre(r.getNombre());
            receta.setIngredientes(r.getIngredientes());
            receta.setPreparacion(r.getPreparacion());
            guardarRecetasEnArchivo();
            return receta;
        } else {
          return null;
        }
    }
    public void guardarRecetasEnArchivo() {
        try {
            FileWriter fileWriter = new FileWriter(targetResource.getFile(), false);

            for (Receta receta : repositorioRecetas) {
                fileWriter.write(receta.getId() + " | " + receta.getNombre() + " | " + receta.getIngredientes() + " | " + receta.getPreparacion() + "\n");
            }

            fileWriter.close();
            log.info("Se ha guardado la lista de recetas en el archivo de texto");
        } catch (IOException e) {
            log.error("Ocurrió un error al escribir en el archivo", e);
            throw new RuntimeException(e);
        }
    }

    public void copyFile() throws IOException {
        if (sourceResource.exists()) {
            try (InputStream inputStream = sourceResource.getInputStream()) {
                Path targetPath = targetResource.getFile().toPath();
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    public boolean borrarRecetaById(int id){
        List<Receta> recetasNoBorradas = new ArrayList<>();
        for (Receta receta : repositorioRecetas) {
            if (receta.getId() != id) {
                recetasNoBorradas.add(receta);
            }
        }
        repositorioRecetas = recetasNoBorradas;
        guardarRecetasEnArchivo();
        return true;
    }

    }
