package com.example.recipebook.config;

import com.example.recipebook.entidades.Ingrediente;
import com.example.recipebook.entidades.Receta;
import com.example.recipebook.entidades.Usuario;
import com.example.recipebook.repositorio.RecetaRepositorio;
import com.example.recipebook.servicios.UserService;
import com.example.recipebook.storage.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class InitData {
    //@Autowired
    private final StorageService storageService;
    //@Autowired

    private final RecetaRepositorio recetaRepository;
    //@Autowired
 
    private final UserService usuarioService;

    @PostConstruct
    public void initStorage() {
        storageService.deleteAll();
        storageService.init();
    }

    @PostConstruct
    public void initUsuarios() {
        Usuario usuario1 = Usuario.builder()
                .username("user")
                .email("user@mongeil.es")
                .password("user")
                .role("ROLE_USER").build();

        usuario1 = usuarioService.save(usuario1);

        Usuario usuario2 = Usuario.builder()
                .username("admin")
                .email("admin@mongeil.es")
                .password("admin")
                .role("ROLE_ADMIN").build();
        usuario2 = usuarioService.save(usuario2);
    }
    @PostConstruct
    public void init() {
        try {

            Receta pan = Receta.builder()
                    .nombre("pan")
                    .preparacion("Se mezcla bien hasta formar una masa y hornear")
                    .foto("/img/cartaimg.jpg")
                    .build();

            // Crear los ingredientes y establecer la relación bidireccional
            Ingrediente ingrediente = Ingrediente.builder().nombre("Harina,Arroz,Cambur").receta(pan).build();

            // Establecer la lista de ingredientes en la receta
            pan.setIngredientes(Collections.singletonList(ingrediente));

            // Guardar la receta en el repositorio
            recetaRepository.save(pan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
