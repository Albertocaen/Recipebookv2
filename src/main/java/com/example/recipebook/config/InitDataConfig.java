package com.example.recipebook.config;

import com.example.recipebook.entidades.Ingrediente;
import com.example.recipebook.entidades.Receta;
import com.example.recipebook.servicios.BookService;
import com.example.recipebook.storage.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
@RequiredArgsConstructor
@Configuration
public class InitDataConfig {
    //@Autowired
    private final StorageService storageService;
    //@Autowired
    private final BookService bookService;



    private Ingrediente crearIngrediente(String nombre) {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre(nombre);
        return ingrediente;
    }
}
