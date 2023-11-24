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
                    .nombre("Pan")
                    .preparacion("Se mezcla bien hasta formar una masa y hornear")
                    .foto("/img/pan.jpg")
                    .build();
            Ingrediente ingrediente = Ingrediente.builder().nombre("Harina,Arroz,Cambur").receta(pan).build();
            pan.setIngredientes(Collections.singletonList(ingrediente));

            Receta pasta= Receta.builder()
                    .nombre("Pasta")
                    .preparacion("Cocinar la pasta en agua con sal y servir con la salsa de tu elección.")
                    .foto(null)
                    .build();
            Ingrediente ingredientePasta=Ingrediente.builder().nombre("Pasta, Agua, Sal, Salsa").receta(pasta).build();
            pasta.setIngredientes(Collections.singletonList(ingredientePasta));

            Receta ensalada= Receta.builder()
                    .nombre("Ensalada")
                    .preparacion("Mezclar diversos vegetales y aderezar con aceite de oliva y vinagre.")
                    .foto(null)
                    .build();
            Ingrediente ingredienteEnsalada=Ingrediente.builder().nombre("Luchuga, Tomate, Pepino, Aceite de oliva extra virgen, Vinagre").receta(ensalada).build();
            ensalada.setIngredientes(Collections.singletonList(ingredienteEnsalada));

            Receta arroz= Receta.builder()
                    .nombre("Arroz")
                    .preparacion("Lavar el arroz 3 veces, hervir por 10minutos, sal a gusto.")
                    .foto(null)
                    .build();
            Ingrediente ingredienteArroz=Ingrediente.builder().nombre("Arroz, Sal (Opcional), Agua").receta(arroz).build();
            arroz.setIngredientes(Collections.singletonList(ingredienteArroz));

            // Guardar la receta en el repositorio
            recetaRepository.save(pan);
            recetaRepository.save(pasta);
            recetaRepository.save(ensalada);
            recetaRepository.save(arroz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
